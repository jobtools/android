package io.jobtools.android.util

import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.nio.ByteBuffer


/**
 * Created by ian on 2018. 3. 26..
 */
object Ipv4Address {
    fun get(): InetAddress {
        try {
            val en = NetworkInterface.getNetworkInterfaces()

            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress.hostAddress.contains(".")) {
//                        val ip = Formatter.formatIpAddress(inetAddress.hashCode())
//                        return ip

                        return inetAddress
                    }
                }
            }
        } catch (ex: SocketException) {
        }

        return InetAddress.getByName("127.0.0.1")
//        throw RuntimeException("Not found inetAddress")
    }

    fun getWithPostfix(postfix: Int): String {
        return getBaseIp() + postfix
    }

    fun getBaseIp(): String = get()
            .hostAddress
            .split(".")
            .take(3)
            .joinToString(".")

    fun getFrom(ipAddress: Int): String {
        return ByteBuffer.allocate(4).putInt(ipAddress).array()
                .map { it.toInt().and(0x00ff) }
                .map { it.toString() }
                .reversed()
                .reduce { acc, s -> "$acc.$s" }
    }

    fun isValid(ipAddressString: String?): Boolean {
        if (ipAddressString == null) {
            return false
        }

        val split = ipAddressString.split(".")
        return split.size == 4
                && split.all { it.toInt() in (0..255) }
    }

}