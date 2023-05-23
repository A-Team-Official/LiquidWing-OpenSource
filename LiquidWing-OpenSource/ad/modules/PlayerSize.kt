package ad.modules

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(
    name = "PlayerSize",
    description = "Edit the player's size",
    category = ModuleCategory.RENDER, Chinese = "玩家大小"
)
class PlayerSize : Module() {
    val playerSizeValue = FloatValue("PlayerSize", 0.5F, 0.01F, 5F)
}