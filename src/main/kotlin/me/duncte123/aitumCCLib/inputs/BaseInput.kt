package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType

sealed class BaseInput(val label: String, val type: InputType, val validation: IValidation) {
    fun toJson() {
        // TODO:
    }
}
