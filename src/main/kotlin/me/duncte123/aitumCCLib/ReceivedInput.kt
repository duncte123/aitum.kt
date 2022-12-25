package me.duncte123.aitumCCLib

import org.json.JSONArray

class ReceivedInput(private val value: Any) {

    val asString: String
        get () {
            if (value !is String) {
                throw Error("This input is not of string type. Detected type: ${value.javaClass}")
            }

            return value
        }

    val asStringList: List<String>
        get () {
            if (value !is JSONArray) {
                throw Error("This input is not of string[] type. Detected type: ${value.javaClass}")
            }

            return value.toList().map { it.toString() }
        }

    val asBoolean: Boolean
        get () {
            if (value !is Boolean) {
                throw Error("This input is not of boolean type. Detected type: ${value.javaClass}")
            }

            return value
        }

    val asInt: Int
        get () {
            if (value !is Int) {
                throw Error("This input is not of int type. Detected type: ${value.javaClass}")
            }

            return value
        }

    val asFloat: Float
        get () {
            if (value !is Float) {
                throw Error("This input is not of int type. Detected type: ${value.javaClass}")
            }

            return value
        }

    override fun toString(): String {
        return "Input($value)"
    }
}
