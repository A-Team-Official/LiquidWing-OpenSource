/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package ad

import ad.sb.novoline.button.AbstractButtonRenderer
import net.ccbluex.liquidbounce.features.module.modules.client.UIEffects
import net.ccbluex.liquidbounce.utils.render.EaseUtils.easeInOutQuad
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import java.awt.Color

class FLineButtonRenderer(button: GuiButton) : AbstractButtonRenderer(button) {

    private var animation = 0.0
    private var lastUpdate = System.currentTimeMillis()

    override fun render(mouseX: Int, mouseY: Int, mc: Minecraft) {
        val time = System.currentTimeMillis()
        val pct = (time - lastUpdate) / 500.0

        if (button.hovered) {
            if (animation < 1) {
                animation += pct
            }
            if (animation > 1) {
                animation = 1.0
            }
        } else {
            if (animation > 0) {
                animation -= pct
            }
            if (animation < 0) {
                animation = 0.0
            }
        }

        val percent = easeInOutQuad(animation)
        RenderUtils.drawRect(button.x.toFloat(), button.y.toFloat(), (button.x + button.width).toFloat(), (button.y + button.height).toFloat(), Color(11, 11, 11, 150).rgb)
        if (button.enabled) {
            val half = button.width / 2.0
            val center = button.x + half
            RenderUtils.drawRect(center - percent * half, (button.y + button.height - 1).toDouble(), center + percent * half, (button.y + button.height).toDouble(), Color(16 ,96,241,240).rgb)
        }

        lastUpdate = time

        if (UIEffects.buttonShadowValue.equals(true)){
            RenderUtils.drawShadowWithCustomAlpha(button.x.toFloat(), button.y.toFloat(), button.width.toFloat(), button.height.toFloat(), 240f)
        }
    }
}