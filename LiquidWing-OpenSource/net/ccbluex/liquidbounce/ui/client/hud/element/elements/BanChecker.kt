package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

@ElementInfo(name = "BanChecker")
class BanChecker () : Element() {


    override fun drawElement(): Border {
        var width = 105f
        var height = 72f

        if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影"))
        {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                width,
                height,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef( 1F,  1F,  1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(CustomUI.shadowValue.get(),{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect(0f,0f,width,height , CustomUI.radius.get(), Color(0,0,0).rgb)
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.drawRoundedRect(0f,0f,width,height , CustomUI.radius.get(), Color(0,0,0).rgb)
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (CustomUI.drawMode.get().equals("高斯模糊和阴影")){
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.CustomBlurRoundArea(
                renderX.toFloat(), renderY.toFloat(),width,height, CustomUI.radius.get(),CustomUI.blurValue.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (CustomUI.drawMode.get().equals("圆角矩形")) {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                width,
                height,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
        }
        if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                width,
                height,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat() ,
                renderY.toFloat() ,
                width ,
                height ,
                CustomUI.radius.get(),
                CustomUI.outlinet.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), 0),
                Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
            )

            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
        }
        if (CustomUI.Chinese.get())
        {
            FontLoaders.F18.drawStringWithShadow("封禁检测",6f, 5f,Color.WHITE.rgb)
            FontLoaders.F18.drawStringWithShadow("封禁次数: " + Recorder.ban,6f, 7f + FontLoaders.F18.FONT_HEIGHT,Color.WHITE.rgb)
            FontLoaders.F18.drawStringWithShadow("获胜次数: " + Recorder.win,6f, 7f + FontLoaders.F18.FONT_HEIGHT * 2,Color.WHITE.rgb)
            FontLoaders.F18.drawStringWithShadow("击杀次数: " + Recorder.killCounts,6f, 7f + FontLoaders.F18.FONT_HEIGHT *3,Color.WHITE.rgb)
        }
        else{
            Fonts.pop35.drawString("Ban Checker",6, 5,Color.WHITE.rgb)
            Fonts.pop35.drawString("Baned: " + Recorder.ban,6, 7 + FontLoaders.F18.FONT_HEIGHT,Color.WHITE.rgb)
            Fonts.pop35.drawString("Wins: " + Recorder.win,6, 7 + FontLoaders.F18.FONT_HEIGHT * 2,Color.WHITE.rgb)
            Fonts.pop35.drawString("Kills: " + Recorder.killCounts,6, 7 + FontLoaders.F18.FONT_HEIGHT *3,Color.WHITE.rgb)
        }
        return Border(0f,0f,width,height,CustomUI.radius.get())
    }
}