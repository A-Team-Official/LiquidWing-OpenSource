package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils

@ModuleInfo(name = "HYTSpeedMine", description = "SpeedMine", category = ModuleCategory.HYT, Chinese = "更快的挖掘2")
class BWSpeedMine : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.playerController.blockHitDelay = 0
        if(mc.playerController.curBlockDamageMP == 0.1f)
        {
            mc.playerController.curBlockDamageMP += 0.05f
            ClientUtils.displayChatMessage("§7[BWSpeedMine] §bSend Packet §c10%")
        }
        if(mc.playerController.curBlockDamageMP == 0.4f)
        {
            mc.playerController.curBlockDamageMP += 0.05f
            ClientUtils.displayChatMessage("§7[BWSpeedMine] §bSend Packet §c40%")
        }
        if(mc.playerController.curBlockDamageMP == 0.7f)
        {
            mc.playerController.curBlockDamageMP += 0.05f
            ClientUtils.displayChatMessage("§7[BWSpeedMine] §bSend Packet §c50%")
        }
    }
}