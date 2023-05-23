package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.CPacketKeepAlive
import net.minecraft.network.play.client.CPacketPlayer
import java.util.*

@ModuleInfo(name = "NoC03",description = "NoC03", category = ModuleCategory.MISC, Chinese = "取消移动发包1")
class NoC03 : Module() {
    private val c03ingrangeValue:FloatValue = FloatValue("C03Range",6f,0f,8f)
    private val packetBuffer = LinkedList<Packet<INetHandlerPlayServer>>()
    private val keepAlives = arrayListOf<CPacketKeepAlive>()

    private val lagTimer = MSTimer()
    private val msTimer = MSTimer()

    private val transactions = arrayListOf<CPacketPlayer>()


    override fun onEnable() {
        msTimer.reset()
        transactions.clear()
        keepAlives.clear()
        val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        killAura.rangeValue.set(c03ingrangeValue.get())
    }

    override fun onDisable() {
        msTimer.reset()
        transactions.clear()
        keepAlives.clear()
    }
    @EventTarget
    fun onWorld(event : WorldEvent){
        msTimer.reset()
        transactions.clear()
        keepAlives.clear()
    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (packet is CPacketPlayer) {
            event.cancelEvent()
        }
    }
    @EventTarget
    fun onMove(event: MoveEvent?) {
        event?.zero()
    }
    @EventTarget
    fun onUpdate(event : UpdateEvent){

        if(lagTimer.hasTimePassed(5000L) && packetBuffer.size > 4) {
            lagTimer.reset()
            while (packetBuffer.size > 4) {
                PacketUtils.sendPacketNoEvent(packetBuffer.poll())
            }
        }
    }
}