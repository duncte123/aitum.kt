
import me.duncte123.aitumCCLib.AitumCC

fun main() {
    AitumCC.setEnv(
        "7e423a5c-a447-42b6-89a3-5433bcc3c1e7",
        "Kotlin Lib",
        ""
    )

    AitumCC.connect()

    println("Connected to Aitum :D")
}
