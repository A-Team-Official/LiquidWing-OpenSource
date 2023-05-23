package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketKeepAlive

object PacketUtils : MinecraftInstance() {
    private val packets = ArrayList<Packet<INetHandlerPlayServer>>()

    fun handleSendPacket(packet: Packet<*>): Boolean {
        if (packets.contains(packet)) {
            packets.remove(packet)
            return true
        }
        return false
    }

    fun sendPacketNoEvent(packet: Packet<INetHandlerPlayServer>) {
        packets.add(packet)
        mc2.connection!!.sendPacket(packet)
    }

    fun getPacketType(packet: Packet<*>): PacketType {
        val className = packet.javaClass.simpleName
        if (className.startsWith("C", ignoreCase = true)) {
                return PacketType.CLIENTSIDE
        } else if (className.startsWith("S", ignoreCase = true)) {
                return PacketType.SERVERSIDE
        }
        // idk...
        return PacketType.UNKNOWN
    }

    enum class PacketType {
        SERVERSIDE,
        CLIENTSIDE,
        UNKNOWN
    }
    fun send(cPacketKeepAlive: CPacketKeepAlive) {}

    fun send(cPacketConfirmTransaction: CPacketConfirmTransaction) {}
    fun send(createCPacketHeldItemChange: ICPacketHeldItemChange) {}
    }