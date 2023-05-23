package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "PotionSaver", description = "Freezes all potion effects while you are standing still.", category = ModuleCategory.PLAYER, Chinese = "站着时保留药水效果")
class PotionSaver : Module() {

    @EventTarget
    fun onPacket(e: PacketEvent) {
        val packet = e.packet

        if (classProvider.isCPacketPlayer(packet) && !classProvider.isCPacketPlayerPosition(packet) && !classProvider.isCPacketPlayerPosLook(packet) &&
                !classProvider.isCPacketPlayerPosLook(packet) && mc.thePlayer != null && !mc.thePlayer!!.isUsingItem)
            e.cancelEvent()
    }

}