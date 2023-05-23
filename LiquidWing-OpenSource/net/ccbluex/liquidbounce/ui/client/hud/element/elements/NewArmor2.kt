/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.api.enums.MaterialType
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat


/**
 * CustomHUD Armor element
 *
 * This element by 7ad QQ:738606342
 */
@ElementInfo(name = "Armor-New")
class NewArmor2(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F,
                side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    private var antimY = 0.0

    var y2 = 0F

    /**
     * Draw element
     * This element by 7ad QQ:738606342
     */
    override fun drawElement(): Border {
        //  if (mc.playerController.isNotCreative) {
        GL11.glPushMatrix()
        var x = 0
        var y = 0
        val renderItem = mc.renderItem
        antimY = RenderUtils.getAnimationState2(antimY, y2.toDouble(), 250.0)

        if (mc.thePlayer!!.totalArmorValue != 0) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef(1F, 1F, 1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(10F, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect(
                    0F, 0F, 65F, antimY.toFloat(), 2F, Color(0, 0, 0, 255).rgb

                )
                GL11.glPopMatrix()

            }, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.fastRoundedRect(
                    0F, 0F, 65F, antimY.toFloat(), 5F
                )
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
            RenderUtils.drawRoundedRect(
                0F,
                0F,
                65F,
                antimY.toFloat(),
                5F,
                Color(255, 247, 230, 170).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.blurRoundArea(renderX.toFloat(), renderY.toFloat(), 65F, antimY.toFloat(), 5)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat(),
                renderY.toFloat(),
                65.toFloat(),
                antimY.toFloat(),
                5f,
                0.1f,
                Color(255, 255, 255, 0),
                Color(255, 255, 255, 110)
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
        }
        if (classProvider.isGuiHudDesigner(mc.currentScreen) && mc.thePlayer!!.totalArmorValue == 0) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef(1F, 1F, 1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(10F, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect(
                    0F, 0F, 65F, 30.toFloat(), 2F, Color(0, 0, 0, 255).rgb

                )
                GL11.glPopMatrix()

            }, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.fastRoundedRect(
                    0F, 0F, 65F, 30.toFloat(), 5F
                )
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
            RenderUtils.drawRoundedRect(
                0F,
                0F,
                65F,
                30.toFloat(),
                5F,
                Color(255, 247, 230, 170).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.blurRoundArea(renderX.toFloat(), renderY.toFloat(), 65F, 30.toFloat(), 5)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat(),
                renderY.toFloat(),
                65.toFloat(),
                30.toFloat(),
                5f,
                0.1f,
                Color(255, 255, 255, 0),
                Color(255, 255, 255, 110)
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
            Fonts.never900_40.drawCenteredString("Armor",30F,15F,Color.WHITE.rgb)

        }
        if (classProvider.isGuiChat(mc.currentScreen)&& mc.thePlayer!!.totalArmorValue == 0) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef(1F, 1F, 1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(10F, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect(
                    0F, 0F, 65F, 30.toFloat(), 2F, Color(0, 0, 0, 255).rgb

                )
                GL11.glPopMatrix()

            }, {
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.fastRoundedRect(
                    0F, 0F, 65F, 30.toFloat(), 5F
                )
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
            RenderUtils.drawRoundedRect(
                0F,
                0F,
                65F,
                30.toFloat(),
                5F,
                Color(255, 247, 230, 170).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.blurRoundArea(renderX.toFloat(), renderY.toFloat(), 65F, 30.toFloat(), 5)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat(),
                renderY.toFloat(),
                65.toFloat(),
                30.toFloat(),
                5f,
                0.1f,
                Color(255, 255, 255, 0),
                Color(255, 255, 255, 110)
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
            Fonts.never900_40.drawString("Armor",10F,15F,Color.WHITE.rgb)

        }
        Gui.drawRect(0,0,0,0,-1)
        for (index in 3 downTo 0) {
            val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue
            val armorValue2 = (((stack.maxDamage - stack.itemDamage).toFloat() / stack.maxDamage))
            val itemDamage = stack.maxDamage - stack.itemDamage
            GlStateManager.pushMatrix()
            GlStateManager.scale(0.5F, 0.5F, 0.5F)
            Gui.drawRect(0,0,0,0,-1)
            GlStateManager.popMatrix()
            var ms = Math.round(itemDamage * 1f / stack.maxDamage * 100f).toFloat()
            var s = StringBuilder().append(DecimalFormat().format(java.lang.Float.valueOf(ms))).append("%")
                .toString()
            if (s != "0%") {
                GL11.glTranslated(-renderX, -renderY, 0.0)
                GL11.glScalef(1F, 1F, 1F)
                GL11.glPushMatrix()
                ShadowUtils.shadow(10F, {
                    GL11.glPushMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                    GL11.glScalef(scale, scale, scale)
                    RenderUtils.renderItemStack(stack, x + 3, (y + 3))
                    GL11.glPopMatrix()

                }, {
                    GL11.glPushMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                    GL11.glScalef(scale, scale, scale)
                    GlStateManager.enableBlend()
                    GlStateManager.disableTexture2D()
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                    RenderUtils.renderItemStack(stack, x + 3, (y + 3))

                    GlStateManager.enableTexture2D()
                    GlStateManager.disableBlend()
                    GL11.glPopMatrix()
                })
                GL11.glPopMatrix()
                GL11.glScalef(scale, scale, scale)
                GL11.glTranslated(renderX, renderY, 0.0)
                Fonts.newtenacity35.drawString(s, 25F, (y + 9).toFloat(), Color(90,90,90).rgb )

            }
            renderItem.renderItemAndEffectIntoGUI(stack, x + 3, (y + 3))

            if (s != "0%") {
                y += 25
                y2 = y.toFloat()
            }
        }

        classProvider.getGlStateManager().enableAlpha()
        classProvider.getGlStateManager().disableBlend()
        classProvider.getGlStateManager().disableLighting()
        classProvider.getGlStateManager().disableCull()
        GL11.glPopMatrix()
        // }

        return  Border(0F, 0F, 50F, antimY.toFloat(),5F)

    }
}