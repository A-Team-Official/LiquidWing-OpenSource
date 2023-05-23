package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "AutoPlayHud-CN", single = true)
class AutoPlayHud(x: Double = 502.0, y: Double = 543.0, scale: Float = 1.0F, side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {
    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = AutoPlayHuds("This is an example AutoPlayInfo",  NotifyType.INFO)

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        val notifications = mutableListOf<AutoPlayHuds>()
        //FUCK YOU java.util.ConcurrentModificationException
        for ((index, notify) in LiquidBounce.hud.autoplayhuds.withIndex()) {
            GL11.glPushMatrix()

            if (notify.drawNotification(index)) {
                notifications.add(notify)
            }

            GL11.glPopMatrix()
        }
        for (notify in notifications) {
            LiquidBounce.hud.autoplayhuds.remove(notify)
        }

        if (classProvider.isGuiHudDesigner(mc.currentScreen)) {
            if (!LiquidBounce.hud.autoplayhuds.contains(exampleNotification))
                LiquidBounce.hud.addAutoPlayHud(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
            //            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-exampleNotification.width.toFloat() + 79, exampleNotification.height.toFloat()-2.5f, 80F, 55F,8F)
        }
        if (classProvider.isGuiChat(mc.currentScreen)) {
            if (!LiquidBounce.hud.autoplayhuds.contains(exampleNotification))
                LiquidBounce.hud.addAutoPlayHud(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
            //            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-exampleNotification.width.toFloat() + 79, exampleNotification.height.toFloat()-2.5f, 80F, 55F,8F)
        }

        return null
    }

}

class AutoPlayHuds( val content: String, val type: NotifyType, val time: Int = 2000, val animeTime: Int = 500) {
    val height = 20
    var fadeState = FadeState.IN
    var nowY = -height
    var displayTime = System.currentTimeMillis()
    var animeXTime = System.currentTimeMillis()
    var animeYTime = System.currentTimeMillis()
    val width = Fonts.rise35.getStringWidth(content) + 45

    fun drawCircle(x: Float, y: Float, radius: Float, start: Int, end: Int) {
        val hud = LiquidBounce.moduleManager[net.ccbluex.liquidbounce.features.module.modules.render.HUD::class.java] as net.ccbluex.liquidbounce.features.module.modules.render.HUD
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(2f)
        GL11.glBegin(GL11.GL_LINE_STRIP)
        var i = end.toFloat()
        while (i >= start) {
            var c = RenderUtils.getGradientOffset(Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(),CustomUI.a.get()), Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), 1), (Math.abs(System.currentTimeMillis() / 360.0 + (i* 34 / 360) * 56 / 100) / 10)).rgb
            val f2 = (c shr 24 and 255).toFloat() / 255.0f
            val f22 = (c shr 16 and 255).toFloat() / 255.0f
            val f3 = (c shr 8 and 255).toFloat() / 255.0f
            val f4 = (c and 255).toFloat() / 255.0f
            GlStateManager.color(f22, f3, f4, f2)
            GL11.glVertex2f(
                (x + Math.cos(i * Math.PI / 180) * (radius * 1.001f)).toFloat(),
                (y + Math.sin(i * Math.PI / 180) * (radius * 1.001f)).toFloat()
            )
            i -= 360f / 90.0f
        }
        GL11.glEnd()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }
    /**
     * Draw notification
     */
    fun drawNotification(index: Int): Boolean {
        val realY = (index + 1) * (height + 10)
        val nowTime = System.currentTimeMillis()
        //Y-Axis Animation
        if (nowY != realY) {
            var pct = (nowTime - animeYTime) / animeTime.toDouble()
            if (pct > 1) {
                nowY = realY
                pct = 1.0
            } else {
                pct = EaseUtils.easeOutBack(pct)
            }
            GL11.glTranslated(0.0, (realY - nowY) * pct, 0.0)
        } else {
            animeYTime = nowTime
        }
        GL11.glTranslated(0.0, nowY.toDouble(), 0.0)

        //X-Axis Animation
        var pct = (nowTime - animeXTime) / animeTime.toDouble()
        when (fadeState) {
            FadeState.IN -> {
                if (pct > 1) {
                    fadeState = FadeState.STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = EaseUtils.easeOutBack(pct)
            }

            FadeState.STAY -> {
                pct = 1.0
                if ((nowTime - animeXTime) > time) {
                    fadeState = FadeState.OUT
                    animeXTime = nowTime
                }
            }

            FadeState.OUT -> {
                if (pct > 1) {
                    fadeState = FadeState.END
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = 1 - EaseUtils.easeInBack(pct)
            }

            FadeState.END -> {
                return true
            }
        }

        GL11.glScaled(pct,pct,pct)
        GL11.glTranslatef(-width.toFloat()/2 , -height.toFloat()/2, 0F)
        if (type.toString() == "WARNING") {
            if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
                RoundedUtil.drawRound(
                    0F - CustomUI.outlinet.get() * 1.15F,
                    0F - CustomUI.outlinet.get() * 1.15F,
                    width.toFloat() + (CustomUI.outlinet.get() * 2 * 1.15F),
                    height.toFloat() + (CustomUI.outlinet.get() * 2 * 1.15F),
                    CustomUI.radius.get(),
                    Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
                )
            }
                RoundedUtil.drawRound(0F, 0F, width.toFloat(), height.toFloat(),CustomUI.radius.get(), Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(),CustomUI.a.get()))

            FontLoaders.F16.drawCenteredString("$content",
                (width/2).toDouble() + 3.0, 6.0, Color.WHITE.rgb)
            // Fonts.NotiFicationIcon.drawString(string, 4, 8, Color.WHITE.rgb)
            //drawCircle(12.9f,15.0f,8.8f, 0,360)
            GlStateManager.resetColor()
        }
        if (type.toString() == "INFO") {
            if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
                RoundedUtil.drawRound(
                    0F - CustomUI.outlinet.get() * 1.15F,
                    0F - CustomUI.outlinet.get() * 1.15F,
                    width.toFloat() + (CustomUI.outlinet.get() * 2 * 1.15F),
                    height.toFloat() + (CustomUI.outlinet.get() * 2 * 1.15F),
                    CustomUI.radius.get(),
                    Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
                )
            }
            RoundedUtil.drawRound(0F, 0F, width.toFloat(), height.toFloat(),CustomUI.radius.get(), Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(),CustomUI.a.get()))

            FontLoaders.F16.drawCenteredString("$content",
                (width/2).toDouble() + 3.0, 6.0, Color.WHITE.rgb)
            //Fonts.NotiFicationIcon.drawString(string, 4, 8, Color.WHITE.rgb)
            //drawCircle(12.9f,15.0f,8.8f, 0,360)
            GlStateManager.resetColor()
        }
        //RenderUtils.drawGradientSideways(0.0, height - 1.7,
        //(width * ((nowTime - displayTime) / (animeTime * 2F + time))).toDouble(), height.toDouble(), Color(ColorMixer.col1RedValue.get(),ColorMixer.col1GreenValue.get(),ColorMixer.col1BlueValue.get()).rgb, Color(ColorMixer.col2RedValue.get(),ColorMixer.col2GreenValue.get(),ColorMixer.col2BlueValue.get()).rgb)
        return false
    }
}

enum class NotifyType(var icon: String) {
    SUCCESS("check-circle"),
    ERROR("close-circle"),
    WARNING("warning"),
    INFO("information");
}


enum class FadeState { IN, STAY, OUT, END }


