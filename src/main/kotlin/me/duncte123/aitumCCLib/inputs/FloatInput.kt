package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType

class FloatInput (
    label: String,
    validation: FloatInputValidation
) : BaseInput(
    label,
    InputType.FLOAT,
    validation
)
