
import me.duncte123.aitumCCLib.AitumCC

fun main() {
    // TODO: remove this when publishing the lib
    val aitumcc = AitumCC.get()

    aitumcc.setEnv(
        "7e423a5c-a447-42b6-89a3-5433bcc3c1e7",
        "Kotlin Lib",
        System.getenv("AITUM_KEY")
    )

    aitumcc.registerAction(DummyAction())

    aitumcc.connect()

    println("Connected to Aitum :D")
}
