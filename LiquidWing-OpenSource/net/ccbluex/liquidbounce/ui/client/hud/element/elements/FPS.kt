package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import java.awt.Color

@ElementInfo(name = "FPS-NEW")
class FPS : Element() {
    val width = 25f + Fonts.newtenacity45.getStringWidth(String.format("" + Minecraft.getDebugFPS() + "FPS"))
    val height = 25f
    override fun drawElement(): Border? {
        RenderUtils.drawRoundedRect(0f,0f,width,height,5f,Color(255 ,247 ,230,170).rgb)
        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glPushMatrix()
        BlurBuffer.CustomBlurRoundArea(renderX.toFloat(), renderY.toFloat()  , width,height,5f, CustomUI.blurValue.get())
        GL11.glPopMatrix()
        GL11.glTranslated(renderX, renderY, 0.0)

        GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
        GL11.glPushMatrix()
        RoundedUtil.drawRoundOutline(renderX.toFloat(),renderY.toFloat(),width,height,5f,0.1f,Color(0,0,0,0),Color(255,255,255,110))
        GL11.glPopMatrix()
        GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)

        Fonts.newtenacity45.drawCenteredString(String.format("" + Minecraft.getDebugFPS() + "FPS"),width/2f,13f - Fonts.newtenacity40.fontHeight / 2f,Color(90,90,90).rgb)

        return Border(0f,0f,width,height,5f)

    }
}