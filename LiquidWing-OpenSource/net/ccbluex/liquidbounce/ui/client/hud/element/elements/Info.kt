package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.InfosUtils.Recorder
import ad.utils.InfosUtils.Recorder.killCounts
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "Info-NEW")
class Info(x: Double = 10.0, y: Double = 30.0, scale: Float = 1F) : Element(x, y, scale) {

    override fun drawElement(): Border {
        val fontRenderer = Fonts.newtenacity60
        val y2 = 74
        val x2 = 155.0.toInt()

        val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glScalef( 1F,  1F,  1F)
        GL11.glPushMatrix()
        ShadowUtils.shadow(6f,{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            RenderUtils.drawRoundedRect(-2f, -2f, x2.toFloat() - 2f, y2.toFloat() -2f, 5F, Color(0,0,0).rgb)
            GL11.glPopMatrix()

        },{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.drawRoundedRect(-2f, -2f, x2.toFloat() - 2f, y2.toFloat() -2f, 5F, Color(0,0,0).rgb)
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)

        RenderUtils.drawRoundedRect(-2f, -2f, x2.toFloat() - 2f, y2.toFloat() -2f, 5F, Color(255 ,247 ,230,170).rgb)

        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glPushMatrix()
        BlurBuffer.CustomBlurRoundArea(renderX.toFloat() - 2f, renderY.toFloat()-2f  , x2.toFloat(),y2.toFloat(), 5f,CustomUI.blurValue.get())
        GL11.glPopMatrix()
        GL11.glTranslated(renderX, renderY, 0.0)

        GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
        GL11.glPushMatrix()
        RoundedUtil.drawRoundOutline(renderX.toFloat() - 2f, renderY.toFloat()-2f  , x2.toFloat(),y2.toFloat(),5f,0.1f,Color(255,255,255,0),Color(255,255,255,110))
        GL11.glPopMatrix()
        GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)

        val ty2 = (fontRenderer.fontHeight) + 13
        val ty3 = (fontRenderer.fontHeight * 2) + 20
        val ty4 = (fontRenderer.fontHeight * 3) + 25
        //drawShadow
        fontRenderer.drawString(
            "Play Time:",
            5,
            6,
            Color(90,90,90).rgb
        )
        fontRenderer.drawString(DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L)),x2- fontRenderer.getStringWidth(DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))) - 10,6,Color(90,90,90).rgb)

        fontRenderer.drawString(
            "Killed:", 5,
            ty2, Color(90,90,90).rgb
        )
        fontRenderer.drawString(killCounts.toString(),x2 - fontRenderer.getStringWidth(killCounts.toString()) - 10, ty2, Color(90,90,90).rgb)

        fontRenderer.drawString(
            "Won:", 5,
            ty3, Color(90,90,90).rgb
        )
        fontRenderer.drawString(Recorder.totalPlayed.toString(),x2- fontRenderer.getStringWidth(Recorder.totalPlayed.toString()) - 10,ty3, Color(90,90,90).rgb)

        fontRenderer.drawString(
            "Banned:", 5,
            ty4, Color(90,90,90).rgb
        )
        fontRenderer.drawString(Recorder.ban.toString(),x2- fontRenderer.getStringWidth(Recorder.ban.toString()) - 10,ty4, Color(90,90,90).rgb)

        return Border(-2f, -2f, x2.toFloat(), y2.toFloat(), 0f)
    }
}
