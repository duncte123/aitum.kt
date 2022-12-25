package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType
import org.json.JSONObject

sealed class BaseInput(
    private val label: String,
    private val type: InputType,
    private val validation: IValidation
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("label", label)
            .put("type", type.id)
            .put("validation", validation.toJSON())
    }
}
