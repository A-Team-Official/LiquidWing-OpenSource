/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.exploit

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.ListValue
import java.util.*

@ModuleInfo(name = "Kick", description = ".bind kick w可以禁用所有反作弊",category = ModuleCategory.EXPLOIT, canEnable = false, Chinese = "踢出自己")
class Kick : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Quit", "InvalidPacket", "SelfHurt", "IllegalChat", "PacketSpam"), "Quit")

    override fun onEnable() {
        if (mc.isIntegratedServerRunning) {
            ClientUtils.displayChatMessage("§c§lError: §aYou can't enable §c§l'Kick' §ain SinglePlayer.")
            return
        }

        val player = mc.thePlayer!!

        when (modeValue.get().toLowerCase()) {
            "quit" -> mc.theWorld!!.sendQuittingDisconnectingPacket()
            "invalidpacket" -> mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, !player.onGround))
            "selfhurt" -> mc.netHandler.addToSendQueue(classProvider.createCPacketUseEntity(player, ICPacketUseEntity.WAction.ATTACK))
            "illegalchat" -> player.sendChatMessage(Random().nextInt().toString() + "§§§" + Random().nextInt())
            "packetspam" -> {
                repeat(9999) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(it.toDouble(), it.toDouble(), it.toDouble(),
                            Random().nextBoolean()))
                }
            }
        }
    }

}