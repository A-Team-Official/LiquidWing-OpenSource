package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import com.sun.org.apache.xpath.internal.operations.Bool
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.render.tenacity.GradientUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font

@ElementInfo(name = "Logo2-NEW")

class Logo2 : Element(){
    private val mode = ListValue("Mode", arrayOf("Logo1","Logo2"), "Logo2")
    override fun drawElement(): Border? {

        val width = Fonts.newtenacity60.getStringWidth("LIQUIDWING") + 15f
        val height = Fonts.newtenacity60.fontHeight + 11f

        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD

        if (mode.get().equals("Logo1")) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef(1F, 1F, 1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(10f, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GradientUtil.applyGradientHorizontal(
                    0f,
                    0f,
                    Fonts.tenacityboldLogo.getStringWidth("LIQUIDWING").toFloat(),
                    Fonts.tenacityboldLogo.fontHeight.toFloat(),
                    1f,
                    hud.clientColors[0],
                    hud.clientColors[1]
                ) {
                    Fonts.tenacityboldLogo.drawString("LIQUIDWING", 0f, 0f, -1)
                }
                GL11.glPopMatrix()

            }, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                GradientUtil.applyGradientHorizontal(
                    0f,
                    0f,
                    Fonts.tenacityboldLogo.getStringWidth("LIQUIDWING").toFloat(),
                    Fonts.tenacityboldLogo.fontHeight.toFloat(),
                    1f,
                    hud.clientColors[0],
                    hud.clientColors[1]
                ) {
                    Fonts.tenacityboldLogo.drawString("LIQUIDWING", 0f, 0f, -1)
                }
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
            GradientUtil.applyGradientHorizontal(
                0f,
                0f,
                Fonts.tenacityboldLogo.getStringWidth("LIQUIDWING").toFloat(),
                Fonts.tenacityboldLogo.fontHeight.toFloat(),
                1f,
                hud.clientColors[0],
                hud.clientColors[1]
            ) {
                Fonts.tenacityboldLogo.drawString("LIQUIDWING", 0f, 0f, -1)
            }
        }
        else{
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef( 1F,  1F,  1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(6f,{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect(0f,0f,width,height,5f, Color(0,0,0).rgb)
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.drawRoundedRect(0f,0f,width,height,5f, Color(255 ,247 ,230,130).rgb)
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)

            RenderUtils.drawRoundedRect(0f,0f,width,height,5f, Color(255 ,247 ,230,130).rgb)
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.CustomBlurRoundArea(renderX.toFloat(), renderY.toFloat()  , width,height,5f, CustomUI.blurValue.get())
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(renderX.toFloat(),renderY.toFloat(),width,height,5f,0.1f,
                Color(255,255,255,0),
                Color(255,255,255,110)
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)

            Fonts.newtenacity60.drawString("LIQUIDWING",6,(height/2).toInt() - 6,
                Color(0,0,0).rgb)

        }
        if (mode.get().equals("Logo1"))
            return Border(-2f,-2f,Fonts.tenacityboldLogo.getStringWidth("LIQUIDWING").toFloat(),Fonts.tenacityboldLogo.fontHeight.toFloat() * 2,0f)
        else
            return Border(0f,0f,width,height,5f)
    }
}