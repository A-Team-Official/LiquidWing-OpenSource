/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Button", category = ModuleCategory.RENDER, description = "Button", Chinese = "按钮特效")
object UIEffects : Module() {

    val buttonShadowValue = BoolValue("ButtonShadow", true)
    val UiShadowValue = ListValue("Mode", arrayOf("Shadow", "Glow", "None"), "Shadow")

}
