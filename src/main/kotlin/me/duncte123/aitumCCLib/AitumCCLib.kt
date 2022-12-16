package me.duncte123.aitumCCLib

import io.javalin.Javalin
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory

object AitumCCLib {

    private val logger = LoggerFactory.getLogger(AitumCCLib::class.java)

    private var hostId: String? = null
    private var hostName: String? = null
    private var apiKey: String? = null
    private var masterBase: String? = null
    private var connected = false

    private val client = OkHttpClient()

    init {
        runBlocking {
            launch {
                startJavalin()
            }
        }
    }

    fun setEnv(hostId: String, hostName: String, apiKey: String) {
        AitumCCLib.hostId = hostId
        AitumCCLib.hostName = hostName
        AitumCCLib.apiKey = apiKey
    }

    suspend fun connect() {
        if (hostId == null) {
            logger.error("Prevented connection to Aitum as there was no host ID set.")
            return
        }

        if (hostName == null) {
            logger.error("Prevented connection to Aitum as there was no host name set.")
            return
        }

        if (apiKey == null) {
            logger.error("Prevented connection to Aitum as there was no API key set.")
            return
        }

        if (connected) {
            logger.error("Prevented connection to Aitum as there is already an active connection.")
            return
        }

        masterBase = AitumApi.findAitum()

        val request = Request.Builder()
            .post(
                """{"id": "$hostId", "name": "$hostName", "actions": [{ "id": "fake-action-id", "name": "Kotlin is cool!", "inputs": { "ktInput": {"label": "Hell from kotlin :)", "type": 2, "validation": {"required": false}}}}]}""".toRequestBody() // TODO Custom JSON
            )
            .url("$masterBase/cc/register")
            .header("Content-Type", "application/json")
            .header("authorization", "Bearer $apiKey")
            .build()

        try {
            val response = client.newCall(request).execute()

            response.body!!.use {
                println(it.string())
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
