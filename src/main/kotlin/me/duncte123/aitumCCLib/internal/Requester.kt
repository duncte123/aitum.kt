package me.duncte123.aitumCCLib.internal

import me.duncte123.aitumCCLib.AitumCC
import me.duncte123.aitumCCLib.ICustomCode
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture

class Requester(var masterBase: String) {
    private val client = OkHttpClient()

    fun registerSelf(actions: List<ICustomCode>): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        val env = EnvContainer.currEnv!!

        val data = JSONObject()
            .put("id", env.hostId)
            .put("name", env.hostName)
            .put("actions", JSONArray().putAll(
                actions.map(ICustomCode::toJSON)
            ))

        val request = Request.Builder()
            .post(data.toString().toRequestBody())
            .url("${masterBase}/cc/register")
            .header("Content-Type", "application/json")
            .header("authorization", "Bearer ${env.apiKey}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    future.complete(null)
                    return
                }

                val resData = JSONObject(response.body?.string())

                future.completeExceptionally(
                    Exception(resData.getString("error"))
                )
            }
        })

        return future
    }

}
