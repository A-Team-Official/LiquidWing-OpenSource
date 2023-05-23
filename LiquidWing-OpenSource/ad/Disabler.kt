/*
 * LiquidBounce Hacked Client
 * Thank you FDP
 * Thank you for writing the code separately
 * FUCK!
 */
package net.ccbluex.liquidbounce.features.module.modules.exploit

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketKeepAlive
import net.minecraft.network.play.client.CPacketPlayer
import org.lwjgl.Sys.alert
import java.util.*

@ModuleInfo(name = "Disabler", description = "禁用(不是完全)反作弊' checks.", category = ModuleCategory.EXPLOIT, Chinese = "禁用器")
class Disabler : Module() {
    private val debugValue = BoolValue("Debug", false)
    fun debugMessage(str: String) {
        if (debugValue.get()) {
            alert("Disabler","§7[§c§lDisabler§7] §b$str")
        }
    }
    val modeValue = ListValue("Mode",
        arrayOf(
            "VulcanCombat",
            "SpartanCombat",
            "SB",
            "WuDi",
            "WatchDogeFull",
            "AAC5"
        ), "VulcanCombat")
    private var currentTrans = 0
    private var vulTickCounterUID = 0
    private val packetBuffer = LinkedList<Packet<INetHandlerPlayServer>>()

    private val transactions = arrayListOf<CPacketConfirmTransaction>()
    private val packetBufferN = LinkedList<Packet<*>>()
    private val lagTimer = MSTimer()
    private val msTimer = MSTimer()
    private val keepAlives = arrayListOf<CPacketKeepAlive>()
    override fun onEnable() {
        transactions.clear()
        keepAlives.clear()
        msTimer.reset()
        vulTickCounterUID = -25767
        debugMessage("VulcanCombat Disabler §c§lONLY §r§awork when you rejoined the server!")
    }
    override fun onDisable() {
        keepAlives.forEach {
            PacketUtils.sendPacketNoEvent(it)
        }
        transactions.forEach {
            PacketUtils.sendPacketNoEvent(it)
        }
        keepAlives.clear()
        transactions.clear()
        msTimer.reset()
    }
    fun onUpdate(event: UpdateEvent) {
        when (modeValue.get().toLowerCase()) {
            "spartancombat" -> {
                if (msTimer.hasTimePassed(3000L) && keepAlives.size > 0 && transactions.size > 0) {
                    PacketUtils.sendPacketNoEvent(keepAlives[keepAlives.size - 1])
                    PacketUtils.sendPacketNoEvent(transactions[transactions.size - 1])

                    keepAlives.clear()
                    transactions.clear()
                    msTimer.reset()
                }
            }
            "vulcancombat"->{
                if(lagTimer.hasTimePassed(5000L) && packetBuffer.size > 4) {
                    lagTimer.reset()
                    while (packetBuffer.size > 4) {
                        PacketUtils.sendPacketNoEvent(packetBuffer.poll())
                    }
                }
            }
        }

    }
    fun onWorld(event: WorldEvent) {
        currentTrans = 0
        packetBuffer.clear()
        lagTimer.reset()
        transactions.clear()
        keepAlives.clear()
        msTimer.reset()
        vulTickCounterUID = -25767
    }
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        when (modeValue.get().toLowerCase()) {
            "vulcancombat" -> {
                if (packet is CPacketConfirmTransaction) {
                    if (Math.abs(
                            (Math.abs((packet.uid).toInt()).toInt() - Math.abs(vulTickCounterUID.toInt())
                                .toInt()).toInt()
                        ) <= 4
                    ) {
                        vulTickCounterUID = (packet.uid).toInt()
                        packetBufferN.add(packet)
                        event.cancelEvent()
                        debugMessage("C0F-PingTickCounter IN ${packetBuffer.size}")
                    } else if (Math.abs((Math.abs((packet.uid).toInt()).toInt() - 25767).toInt()) <= 4) {
                        vulTickCounterUID = (packet.uid).toInt()
                        debugMessage("C0F-PingTickCounter RESETED")
                    }
                }
            }
            "spartancombat" -> {
                if (packet is CPacketKeepAlive && (keepAlives.size <= 0 || packet != keepAlives[keepAlives.size - 1])) {
                    keepAlives.add(packet)
                    event.cancelEvent()
                }
                if (packet is CPacketConfirmTransaction && (transactions.size <= 0 || packet != transactions[transactions.size - 1])) {
                    transactions.add(packet)
                    event.cancelEvent()
                }
            }
            "sb"->{
                if(packet is CPacketPlayer){
                    event.cancelEvent()
                }
            }
            "watchdogefull"->{
                event.cancelEvent()
                mc.thePlayer!!.motionY  = 114.514
                mc.thePlayer!!.motionX  = 1919.810
                mc.thePlayer!!.motionZ  = 415.411
                LiquidBounce.hud.addNotification(Notification("WuYuTong","Watchdog Disabled!", Notification.NotifyType.INFO))
                LiquidBounce.hud.addNotification(Notification("WuYuTong","ENJOY THE BAN LMAO", Notification.NotifyType.INFO))
                repeat(114514) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(it.toDouble(), it.toDouble(), it.toDouble(),
                        Random().nextBoolean()))
                }

            }


        }
    }
}
