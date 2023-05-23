package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketEntityVelocity
@ModuleInfo(name = "NoC0F", description = "c0f", category = ModuleCategory.MISC, Chinese = "取消击退发包")
class NoC0F: Module() {
        var time = -1;

        @EventTarget
        fun onPacket(event: PacketEvent) {
            var packet = event.packet.unwrap();
            if (packet is SPacketEntityVelocity) {
                time = (System.currentTimeMillis() + 100).toInt();
                packet.motionX = 0;
                packet.motionY = 0;
                packet.motionZ = 0;
            }
            if (packet is CPacketConfirmTransaction) {
                if (System.currentTimeMillis() <= time) {
                    event.cancelEvent();
                }
            }
        }
    }