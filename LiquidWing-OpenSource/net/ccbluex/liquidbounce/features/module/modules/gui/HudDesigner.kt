package net.ccbluex.liquidbounce.features.module.modules.gui

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner


/**
 *
 * Skid by Paimon.
 * @Date 2022/10/6
 */
@ModuleInfo(name ="HudDesigner", description = "SB", category = ModuleCategory.GUI, canEnable = false, Chinese = "界面设置")
class HudDesigner : Module(){
    override fun onEnable() {
        mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiHudDesigner()))
    }
}