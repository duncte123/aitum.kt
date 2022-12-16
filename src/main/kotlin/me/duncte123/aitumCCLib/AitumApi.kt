package me.duncte123.aitumCCLib

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

object AitumApi {
    var base: String = ""

    init {
        val jmdns = JmDNS.create(InetAddress.getLocalHost())

        println("Searching for pebble")

        jmdns.addServiceListener("_pebble._tcp.local.", object : ServiceListener {
            override fun serviceAdded(event: ServiceEvent) {
                println("Service added: ${event.info}")
            }

            override fun serviceRemoved(event: ServiceEvent) {
                println("Service removed: ${event.info}")
            }

            override fun serviceResolved(event: ServiceEvent) {
                println("Aitum resolved")
                base = "http://${event.info.inet4Addresses.first()}:7777"
                jmdns.close()
            }
        })
    }

    suspend fun findAitum(): String {
        while (base == "") {
            delay(100)
        }

        return base
    }
}
