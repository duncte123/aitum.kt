package me.duncte123.aitumCCLib

import io.javalin.Javalin
import me.duncte123.aitumCCLib.internal.EnvContainer
import me.duncte123.aitumCCLib.internal.Requester
import netscape.javascript.JSObject
import org.json.JSONObject
import org.slf4j.LoggerFactory

class AitumCC {

    val logger = LoggerFactory.getLogger(AitumCC::class.java)

    private var requester = Requester("")
    private var connected = false
    private val registeredActions = mutableListOf<ICustomCode>()

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

        requester.registerSelf(registeredActions)
    }

    private fun invokeAction(id: String, postBody: JSONObject) {
        val foundAction = registeredActions.firstOrNull { it.id == id }

        if (foundAction == null) {
            logger.error("Cannot find action with hash {}", id)
            return
        }

        val resData = mutableMapOf<String, ReceivedInput>()

        // TODO: Remove null items from the map or keep them in?
        postBody.toMap()
            // .filter { (_, v) -> v != null }
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

    companion object {
        private val instance = AitumCC()

        fun get() = instance
    }
}
