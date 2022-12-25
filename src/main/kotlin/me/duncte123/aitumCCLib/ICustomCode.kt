package me.duncte123.aitumCCLib

import me.duncte123.aitumCCLib.inputs.BaseInput
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.security.MessageDigest

abstract class ICustomCode {
    protected val logger = LoggerFactory.getLogger(this.javaClass)

    private var computedId: String? = null
    val id: String
        get() {
            if (computedId == null) {
                println("Computing ID for $name")
                computedId = MessageDigest.getInstance("SHA-1")
                    .digest(name.toByteArray())
                    .joinToString("") { "%02x".format(it) }
            }

            return computedId!!
        }
    abstract val name: String
    open val inputs: Map<String, BaseInput> = mapOf()

    abstract fun method(inputs: Map<String, ReceivedInput>)

    fun toJSON(): JSONObject {
        val inputsObj = JSONObject()

        inputs.forEach { input ->
            inputsObj.put(input.key, input.value.toJson())
        }

        return JSONObject()
            .put("id", this.id)
            .put("name", this.name)
            .put("inputs", inputsObj)
    }
}
