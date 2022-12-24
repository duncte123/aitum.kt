package me.duncte123.aitumCCLib

import me.duncte123.aitumCCLib.inputs.BaseInput
import org.json.JSONObject
import java.security.MessageDigest

abstract class ICustomCode {
    private var computedId: String? = null
    val id: String
        get() {
            if (computedId == null) {
                computedId = MessageDigest.getInstance("SHA-1")
                    .digest(name.toByteArray())
                    .joinToString("") { "%02x".format(it) }
            }

            return computedId!!
        }
    abstract val name: String
    abstract val inputs: List<BaseInput>

    abstract fun method(inputs: Map<String, ReceivedInput>)

    fun toJSON(): JSONObject {
        return JSONObject()
            .put("id", this.id)
            .put("name", this.name)
            .put("inputs", JSONObject()) // TODO: add actions
    }
}
