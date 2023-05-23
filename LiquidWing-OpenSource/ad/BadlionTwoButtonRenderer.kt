package net.ccbluex.liquidbounce.features.module.modules.client.button

import ad.sb.novoline.button.AbstractButtonRenderer
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.ResourceLocation

class BadlionTwoButtonRenderer(button: GuiButton) : AbstractButtonRenderer(button) {
    override fun render(mouseX: Int, mouseY: Int, mc: Minecraft) {
        val hoveredimg = ResourceLocation("liquidwing/7ad/buttons/bhover.png")
        val elseimg = ResourceLocation("liquidwing/7ad/buttons/bbutton.png")
        if(button.hovered) { RenderUtils.drawImage(hoveredimg, button.x, button.y, button.width, button.height) } else { RenderUtils.drawImage(elseimg, button.x, button.y, button.width, button.height) }
    }
}