package me.duncte123.aitumCCLib

import io.javalin.Javalin
import me.duncte123.aitumCCLib.internal.EnvContainer
import me.duncte123.aitumCCLib.internal.Requester
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class AitumCC {

    val logger = LoggerFactory.getLogger(AitumCC::class.java)

    private var requester = Requester("")
    private var connected = false
    private val registeredActions = mutableListOf<ICustomCode>()
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private var heartbeatTask: ScheduledFuture<*>? = null

    init {
        startJavalin()
    }

    fun registerAction(action: ICustomCode) {
        // TODO: Check name is greater than 1 length

        // TODO: Check for duplicates

        registeredActions.add(action)

        logger.info("Registered action: {} ({})", action.name, action.id)
    }

    fun setEnv(hostId: String, hostName: String, apiKey: String) {
        EnvContainer.currEnv = EnvContainer(hostId, hostName, apiKey)
    }

    // TODO: return future
    fun connect() {
        if (EnvContainer.currEnv == null) {
            logger.error("Prevented connection to Aitum as the env was not configured properly.")
            return
        }

        EnvContainer.currEnv?.validate()

        if (connected) {
            logger.error("Prevented connection to Aitum as there is already an active connection.")
            return
        }

        val aitumIP = AitumApi.findAitum().get()

        requester.masterBase = aitumIP

        // TODO: Error handling
        requester.registerSelf(registeredActions).get()

        connected = true

        startHeartbeat()
    }

    private fun invokeAction(id: String, postBody: JSONObject) {
        val foundAction = registeredActions.firstOrNull { it.id == id }

        if (foundAction == null) {
            logger.error("Cannot find action with hash {}", id)
            return
        }

        val resData = mutableMapOf<String, ReceivedInput>()

        postBody.toMap()
            .filter { (_, v) -> v != null }
            .forEach { (k, v) ->
                resData[k] = ReceivedInput(v)
            }

        logger.info("Running {}", foundAction.name)

        foundAction.method(resData)
    }

    private fun startJavalin() {
        val app = Javalin.create { config ->
            config.plugins.enableCors { cors ->
                cors.add {
                    it.reflectClientOrigin = true
                    it.allowCredentials = true
                }
            }
        }
            .get("/hc") { ctx -> ctx.result("OK") }
            .post("/rules/{ruleId}") { ctx ->
                val postBody = JSONObject(ctx.body())
                // println(postBody)
                invokeAction(ctx.pathParam("ruleId"), postBody)

                ctx.result("OK")
            }
            .start(7252)
    }

    private fun startHeartbeat() {
        heartbeatTask = scheduler.scheduleAtFixedRate(
            {
                this.heartbeatLogic()
            },
            5L,
            5L,
            TimeUnit.SECONDS
        )
    }

    private fun stopHeartbeat() {
        heartbeatTask?.cancel(true)
        heartbeatTask = null
    }

    private fun heartbeatLogic() {
        logger.debug("Running heartbeat")

        if (!connected) {
            logger.debug("Not connected, stopping heartbeat")
            stopHeartbeat()
            return
        }

        requester.masterHealthCheck()
            .thenAccept { running ->
                if (!running) {
                    stopHeartbeat()
                    logger.error("Aitum instance disconnected. Attempting to reconnect.")
                    Thread.sleep(250)
                    connected = false
                    connect()
                }
            }
            .exceptionally { ex ->
                logger.error("Aitum instance disconnected. Attempting to reconnect.", ex)
                Thread.sleep(250)
                connected = false
                connect()
                return@exceptionally null
            }
    }

    companion object {
        private val instance = AitumCC()

        fun get() = instance
    }
}
