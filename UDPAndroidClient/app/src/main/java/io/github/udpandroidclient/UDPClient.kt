package io.github.udpandroidclient

import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

fun scan(port: Int) {
    val datagramChannel = DatagramChannel.open();
    val en = NetworkInterface.getNetworkInterfaces()
    val broadcastIpList = mutableListOf<InetAddress>()
    while (en.hasMoreElements()) {
        val ni: NetworkInterface = en.nextElement()
        Log.i("INFO", " Display Name = " + ni.displayName)
        val list = ni.interfaceAddresses
        val it: Iterator<InterfaceAddress> = list.iterator()
        while (it.hasNext()) {
            val ia = it.next()
            Log.i("info", "Broadcast = " + ia.broadcast)
            if (ia.broadcast != null) {
                broadcastIpList.add(ia.broadcast)
            }
        }
    }
    datagramChannel.setOption(StandardSocketOptions.SO_BROADCAST, true)

    try {

        runBlocking(Dispatchers.IO) {
            broadcastIpList.map {
                datagramChannel.send(
                    ByteBuffer.wrap("test".toByteArray()),
                    InetSocketAddress(it, port)
                )
            }
            while (true) {
                val rt = async(Dispatchers.IO) {
                    val byteBuffer = ByteBuffer.allocate(1024)
                    val remoteSocketAddress = datagramChannel.receive(byteBuffer);
                    remoteSocketAddress
                }

                launch {
                    // Delay 3 seconds
                    delay(3000)
                    rt.cancel()
                }

                val remoteSocketAddress = rt.await()
                Log.i("UDP Scan", "Remote Address: $remoteSocketAddress")
                if (rt.isCancelled) {
                    Log.i("UDP Scan", "Waiting timeout")
                    break;
                }
            }
        }
    } catch (e: IOException) {
        Log.e("Scan Error", e.toString())
    }
}

open class UDPClient(ipStr: String, portStr: String) {

    private val socketAddress = InetSocketAddress(InetAddress.getByName(ipStr), portStr.toInt())
    private val datagramChannel = DatagramChannel.open();


    fun send(data: String) {
        try {
            datagramChannel.setOption(StandardSocketOptions.SO_BROADCAST, false)
            datagramChannel.send(ByteBuffer.wrap(data.toByteArray()), socketAddress)
        } catch (e: Exception) {
            Log.e("Send Failed", e.toString())
        }
    }

    private fun response(): SocketAddress {
        val byteBuffer = ByteBuffer.allocate(1024)
        val remoteSocketAddress = datagramChannel.receive(byteBuffer);
        return remoteSocketAddress
    }

    fun receive(): String {
        val byteBuffer = ByteBuffer.allocate(1024)
        val remoteSocketAddress = datagramChannel.receive(byteBuffer);
        Log.i("UDP Client", "Remote address is $remoteSocketAddress")
        return String(byteBuffer.array(), Charsets.UTF_8);
    }
}