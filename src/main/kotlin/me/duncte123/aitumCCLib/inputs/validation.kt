package me.duncte123.aitumCCLib.inputs

import org.json.JSONObject

sealed interface IValidation {
    fun toJSON(): JSONObject {
        return JSONObject(toMap())
    }

    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf()
    }
}

open class SimpleInputValidation(private val required: Boolean) : IValidation {
    override fun toMap(): MutableMap<String, Any> {
        val baseMap = super.toMap()

        baseMap["required"] = required

        return baseMap
    }
}

class StringInputValidation(
    required: Boolean,
    private val minLength: Int? = null,
    private val maxLength: Int? = null
) : SimpleInputValidation(required) {
    override fun toMap(): MutableMap<String, Any> {
        val baseMap = super.toMap()

        if (this.minLength!= null) baseMap["minLength"] = this.minLength
        if (this.maxLength!= null) baseMap["maxLength"] = this.maxLength

        return baseMap
    }
}

sealed class NumericInputValidation<N>(
    required: Boolean,
    private val minValue: N? = null,
    private val maxValue: N? = null
) : SimpleInputValidation(required) {
    override fun toMap(): MutableMap<String, Any> {
        val baseMap = super.toMap()

        if (this.minValue!= null) baseMap["minValue"] = this.minValue
        if (this.maxValue!= null) baseMap["maxValue"] = this.maxValue

        return baseMap
    }
}

class IntegerInputValidation(
    required: Boolean,
    minValue: Int? = null,
    maxValue: Int? = null
) : NumericInputValidation<Int>(required, minValue, maxValue)

class FloatInputValidation(
    required: Boolean,
    minValue: Float? = null,
    maxValue: Float? = null
) : NumericInputValidation<Float>(required, minValue, maxValue)
