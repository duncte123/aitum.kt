package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType

class IntInput(
    label: String,
    validation: IntegerInputValidation
) : BaseInput(
    label,
    InputType.INT,
    validation
)
