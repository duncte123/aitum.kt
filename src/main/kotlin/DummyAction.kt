import me.duncte123.aitumCCLib.ICustomCode
import me.duncte123.aitumCCLib.ReceivedInput
import me.duncte123.aitumCCLib.inputs.*

class DummyAction : ICustomCode() {

    override val name = "Dummy Action"

    override val inputs = mapOf(
        "testStringInput" to StringInput(
            "What is your name?", StringInputValidation(
                required = false
            )
        ),
        "testBooleanInput" to BooleanInput(
            "Are you a fun person?", SimpleInputValidation(
                required = false
            )
        ),
        "testIntInput" to IntInput(
            "How old are you?", IntegerInputValidation(
                required = true,
                minValue = 10,
                maxValue = 100
            )
        ),
        "testFloatInput" to FloatInput(
            "Volume level", FloatInputValidation(
                required = false
            )
        ),
        // Does not work yet
        "testListInput" to StringListInput(
            "Favourite Food", SimpleInputValidation(
                required = false
            )
        ),
    )

    override fun method(inputs: Map<String, ReceivedInput>) {
        logger.info("I am a dummy action!!")

        logger.info(inputs.toString())

        // Name is optional so we cannot never null cast it

        val strInput = inputs["testStringInput"]

        if (strInput == null) {
            logger.warn("testStringInput is null :O")
        } else {
            logger.info("Name: ${strInput.asString}")
        }

        logger.info("age ${inputs["testIntInput"]!!.asInt}")
    }
}
