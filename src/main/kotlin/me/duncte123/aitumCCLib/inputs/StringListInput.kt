package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType

class StringListInput(
    label: String,
    validation: SimpleInputValidation
) : BaseInput(
    label,
    InputType.STRING_LIST,
    validation
)
