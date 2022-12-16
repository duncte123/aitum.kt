
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.duncte123.aitumCCLib.AitumCCLib

fun main() = runBlocking {
    AitumCCLib.setEnv(
        "7e423a5c-a447-42b6-89a3-5433bcc3c1e7",
        "Kotlin Lib :D",
        ""
    )

    async {
        AitumCCLib.connect()
    }.await()

    println("Connected to Aitum :D")


}
