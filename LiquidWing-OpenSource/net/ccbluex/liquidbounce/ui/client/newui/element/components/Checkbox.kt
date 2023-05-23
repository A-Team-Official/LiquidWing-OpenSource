package net.ccbluex.liquidbounce.ui.client.newui.element.components

import net.ccbluex.liquidbounce.ui.client.newui.extensions.animLinear
import net.ccbluex.liquidbounce.utils.BlendUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color

    class Checkbox {
    private var smooth = 0F
    var state = false

    fun onDraw(x: Float, y: Float, width: Float, height: Float, bgColor: Color, accentColor: Color) {
        smooth = smooth.animLinear((if (state) 0.2F else -0.2F) * RenderUtils.deltaTime * 0.045F, 0F, 1F)
        val borderColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(Color(160, 160, 160), accentColor), smooth)
        val mainColor = BlendUtils.blendColors(floatArrayOf(0F, 1F), arrayOf(bgColor, accentColor), smooth)

        RenderUtils.drawRoundedRect(x - 0.5F, y - 0.5F, x + width + 0.5F, y + width + 0.5F, 3F, borderColor.rgb)
        RenderUtils.drawRoundedRect(x, y, x + width, y + width, 3F, mainColor.rgb)
        GL11.glColor4f(bgColor.red / 255F, bgColor.green / 255F, bgColor.blue / 255F, 1F)
        RenderUtils.drawLine(x + width / 4.0, y + width / 2.0, x + width / 2.15, y + width / 4.0 * 3.0, 2F)
        RenderUtils.drawLine(x + width / 2.15, y + width / 4.0 * 3.0, x + width / 3.95* 3.0, y + width / 3.0, 2F)
        GL11.glColor4f(1F, 1F, 1F, 1F)
    }
}