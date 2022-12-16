package me.duncte123.aitumCCLib

import me.duncte123.aitumCCLib.inputs.BaseInput

interface ICustomCode {
    val name: String
    val inputs: List<BaseInput>

    fun method(inputs: Map<String, ReceivedInput>)
}
