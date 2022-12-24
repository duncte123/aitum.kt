package me.duncte123.aitumCCLib

import io.javalin.Javalin
import me.duncte123.aitumCCLib.internal.EnvContainer
import me.duncte123.aitumCCLib.internal.Requester
import org.slf4j.LoggerFactory

object AitumCC {

    private val logger = LoggerFactory.getLogger(AitumCC::class.java)

    private var requester = Requester("")
    private var connected = false

    init {
        startJavalin()
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

        AitumApi.findAitum().thenAccept {
            requester.masterBase = it

            requester.registerSelf(listOf())
        }
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
                println("TODO: Run rule with id ${ctx.pathParam("ruleId")}")

                ctx.result("OK")
            }
            .start(7252)
    }
}
