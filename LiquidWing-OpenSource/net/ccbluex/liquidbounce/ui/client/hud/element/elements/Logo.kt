/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ServerUtils
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD effects element
 *
 * Shows a list of active potion effects
 */
@ElementInfo(name = "Logo-CN")
class Logo (x: Double = 5.0, y: Double = 10.0) : Element() {


    /**
     * Draw element
     */

    override fun drawElement(): Border {
        var eh_text = "LiquidWing | " + LiquidBounce.USERNAME + " | FPS:" + Minecraft.getDebugFPS()
            .toString() + " | IP:" + ServerUtils.getRemoteIp()
        var cn_text = "液体翅膀 | " + LiquidBounce.USERNAME + " | 帧数:" + Minecraft.getDebugFPS()
            .toString() + " | 地址:液体翅膀.cc"

        if (CustomUI.Chinese.get()) {

            if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影"))
            {
                RenderUtils.drawRoundedRect(
                    0f,
                    0f,
                    (FontLoaders.F20.getStringWidth(cn_text) + 12f),
                    15f,
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
                    RenderUtils.drawRoundedRect(0f,0f,  (FontLoaders.F20.getStringWidth(cn_text) + 12f),
                        15f, CustomUI.radius.get(), Color(0,0,0).rgb)
                    GL11.glPopMatrix()

                },{
                    GL11.glPushMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                    GL11.glScalef(scale, scale, scale)
                    GlStateManager.enableBlend()
                    GlStateManager.disableTexture2D()
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                    RenderUtils.drawRoundedRect(0f,0f,  (FontLoaders.F20.getStringWidth(cn_text) + 12f),
                        15f, CustomUI.radius.get(), Color(0,0,0).rgb)
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
                    renderX.toFloat(), renderY.toFloat(),  (FontLoaders.F20.getStringWidth(cn_text) + 12f),
                    15f,CustomUI.radius.get(),CustomUI.blurValue.get()
                )
                GL11.glPopMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
            }
            if (CustomUI.drawMode.get().equals("圆角矩形")) {
                RenderUtils.drawRoundedRect(
                    0f,
                    0f,
                    (FontLoaders.F20.getStringWidth(cn_text) + 12f),
                    15f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
            }
            if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
                RenderUtils.drawRoundedRect(
                    0f,
                    0f,
                    (FontLoaders.F20.getStringWidth(cn_text) + 12f),
                    15f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
                GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
                GL11.glPushMatrix()
                RoundedUtil.drawRoundOutline(
                    renderX.toFloat() - 0.7f,
                    renderY.toFloat() - 0.7f,
                    (FontLoaders.F20.getStringWidth(cn_text) + 13.2f),
                    16f,
                    CustomUI.radius.get(),
                    CustomUI.outlinet.get(),
                    Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get(), 0),
                    Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get(),CustomUI.a.get())
                )
                GL11.glPopMatrix()
                GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
            }
            GlStateManager.resetColor()
            FontLoaders.F20.drawString(cn_text, 5f, 4f, Color.WHITE.rgb)
        } else {

            if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影"))
            {
                RenderUtils.drawRoundedRect(
                    0f,
                    0f,
                    (Fonts.pop40.getStringWidth(eh_text) + 12f),
                    15f,
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
                    RenderUtils.drawRoundedRect(0f,0f,   (Fonts.pop40.getStringWidth(eh_text) + 12f),
                        15f,CustomUI.radius.get(), Color(0,0,0).rgb)
                    GL11.glPopMatrix()

                },{
                    GL11.glPushMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                    GL11.glScalef(scale, scale, scale)
                    GlStateManager.enableBlend()
                    GlStateManager.disableTexture2D()
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                    RenderUtils.drawRoundedRect(0f,0f, (Fonts.pop40.getStringWidth(eh_text) + 12f),
                        15f,CustomUI.radius.get(), Color(0,0,0).rgb)
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
                    renderX.toFloat(), renderY.toFloat(),  (FontLoaders.F20.getStringWidth(eh_text) + 14f),
                    15f, CustomUI.radius.get(),CustomUI.blurValue.get()
                )
                GL11.glPopMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
            }
            if (CustomUI.drawMode.get().equals("圆角矩形")) {
                RenderUtils.drawRoundedRect(
                    0f,
                    0f,
                    (Fonts.pop40.getStringWidth(eh_text) + 12f),
                    15f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
            }
            if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
                RenderUtils.drawRoundedRect(
                    0f,
                    0f,
                    (Fonts.pop40.getStringWidth(eh_text) + 12f),
                    15f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
                GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
                GL11.glPushMatrix()
                RoundedUtil.drawRoundOutline(
                    renderX.toFloat(),
                    renderY.toFloat(),
                    (Fonts.pop40.getStringWidth(eh_text) + 12f),
                    16f,
                    CustomUI.radius.get(),
                    CustomUI.outlinet.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), 0),
                    Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
                )
                GL11.glPopMatrix()
                GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
            }

            GlStateManager.resetColor()
            Fonts.pop40.drawString(eh_text, 5, 4, Color.WHITE.rgb)

        }

        return Border(0f, 0f, (Fonts.pop40.getStringWidth(eh_text) + 12f), 15.7f, CustomUI.radius.get())
    }
}