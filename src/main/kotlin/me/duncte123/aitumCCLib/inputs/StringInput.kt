package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType

class StringInput(
    label: String,
    validation: StringInputValidation
) : BaseInput(
    label,
    InputType.STRING,
    validation
)
