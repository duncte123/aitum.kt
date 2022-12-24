package me.duncte123.aitumCCLib.internal

import me.duncte123.aitumCCLib.AitumCC

data class EnvContainer(
    val hostId: String,
    val hostName: String,
    val apiKey: String
) {
    fun validate() {
        if (hostId.isEmpty()) {
            throw Exception("Prevented connection to Aitum as there was no host ID set.")
        }

        if (hostName.isEmpty()) {
            throw Exception("Prevented connection to Aitum as there was no host name set.")
        }

        if (apiKey.isEmpty()) {
            throw Exception("Prevented connection to Aitum as there was no API key set.")
        }
    }

    companion object {
        var currEnv: EnvContainer? = null
    }
}



