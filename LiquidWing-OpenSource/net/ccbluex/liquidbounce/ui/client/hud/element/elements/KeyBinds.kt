package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


@ElementInfo(name = "KeyBinds-CN")
class KeyBinds : Element() {
    val onlyState = BoolValue("OnlyModuleState", true)
    private var anmitY = 0F
    override fun drawElement(): Border? {
        var y2 = 0
        anmitY = RenderUtils.getAnimationState2(anmitY.toDouble(),(15 + getmoduley()).toFloat().toDouble(), 250.0).toFloat()

        if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影"))
        {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                114f,
                anmitY,
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
                RenderUtils.drawRoundedRect(0f,0f,114f,
                    anmitY, CustomUI.radius.get(), Color(0,0,0).rgb)
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.drawRoundedRect(0f,0f,114f,
                    anmitY,CustomUI.radius.get(), Color(0,0,0).rgb)
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
                renderX.toFloat(), renderY.toFloat(),114f,
                anmitY, CustomUI.radius.get(),CustomUI.blurValue.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }

        if (CustomUI.drawMode.get().equals("圆角矩形")) {
            //draw Background
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                114f,
                anmitY,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
        }
        if (CustomUI.drawMode.get().equals("描边和圆角矩形")){
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                114f,
                anmitY,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat(),
                renderY.toFloat(),
                114f,
                anmitY,
                CustomUI.radius.get(),
                CustomUI.outlinet.get(),
                Color(0,0,0,0),
                Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

        }



        GlStateManager.resetColor()
        if (CustomUI.Chinese.get()) {
            //draw Title
            val fwidth = 10F
            FontLoaders.F16.drawString("按键显示", fwidth, 4.5f, -1, true)

            //draw Module Bind
            for (module in LiquidBounce.moduleManager.modules) {
                if (module.keyBind == 0) continue
                if (onlyState.get()) {
                    if (!module.state) continue
                }
                FontLoaders.F16.drawString(module.Chinese, fwidth, y2 + 19f, -1, true)
                FontLoaders.F16.drawString(
                    if (module.state) "[开启]" else "[关闭]",
                    (108 - FontLoaders.F16.getStringWidth(if (module.state) "[开启]" else "[关闭]")).toFloat(),
                    y2 + 21f,
                    if (module.state) Color(255, 255, 255).rgb else Color(255, 255, 255).rgb,
                    true
                )
                y2 += 10
            }
        }
        else{
            val fwidth = 10F
            Fonts.pop35.drawString("KeyBinds", fwidth, 4.5f, -1, false)

            //draw Module Bind
            for (module in LiquidBounce.moduleManager.modules) {
                if (module.keyBind == 0) continue
                if (onlyState.get()) {
                    if (!module.state) continue
                }
                Fonts.pop32.drawString(module.name, fwidth, y2 + 19f, -1, false)
                Fonts.pop32.drawString(
                    if (module.state) "[Open]" else "[Close]",
                    (108 - FontLoaders.F16.getStringWidth(if (module.state) "[Open]" else "[Close]")).toFloat(),
                    y2 + 21f,
                    if (module.state) Color(255, 255, 255).rgb else Color(255, 255, 255).rgb,
                    false
                )
                y2 += 10
            }
        }
        return Border(0f, 0f, 114f, (17 + getmoduley()).toFloat(),CustomUI.radius.get())
    }

    fun getmoduley(): Int {
        var y = 0
        for (module in LiquidBounce.moduleManager.modules) {
            if (module.keyBind == 0) continue
            if (onlyState.get()) {
                if (!module.state) continue
            }
            y += 12
        }
        return y
    }
}