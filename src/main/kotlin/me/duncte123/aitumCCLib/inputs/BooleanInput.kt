package me.duncte123.aitumCCLib.inputs

import me.duncte123.aitumCCLib.InputType

class BooleanInput(
    label: String,
    validation: SimpleInputValidation
) : BaseInput(
    label,
    InputType.BOOLEAN,
    validation
)
