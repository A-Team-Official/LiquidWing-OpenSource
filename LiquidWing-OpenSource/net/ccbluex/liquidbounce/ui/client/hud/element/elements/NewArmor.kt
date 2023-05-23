package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.api.enums.MaterialType
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Armor-CN")
class NewArmor(x: Double = 0.0, y: Double = 0.0, scale: Float = 1F,
               side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    private val modeValue = ListValue("Alignment", arrayOf("Horizontal"), "Horizontal")

    /**
     * Draw element
     */
    override fun drawElement(): Border {
//        val arrayList: ArrayList<ArmorUtils> = ArrayList<liying.utils.armorUtils>()

            GL11.glPushMatrix()

            val renderItem = mc.renderItem
            val isInsideWater = mc.thePlayer!!.isInsideOfMaterial(classProvider.getMaterialEnum(MaterialType.WATER))
            var x = 1
            var y = if (isInsideWater) -10 else 0

            val mode = modeValue.get()
            // RenderUtils.drawRoundedRect(x-4f,-12f,75f,30f,4f, Color(0,0,0,50).rgb)

            if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影")) {
                RenderUtils.drawRoundedRect(
                    x - 2f,
                    -12f,
                    75f + x - 2f,
                    40f - 12f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
                GL11.glTranslated(-renderX, -renderY, 0.0)
                GL11.glScalef(1F, 1F, 1F)
                GL11.glPushMatrix()
                ShadowUtils.shadow(CustomUI.shadowValue.get(), {
                    GL11.glPushMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                    GL11.glScalef(scale, scale, scale)
                    RenderUtils.drawRoundedRect(
                        x - 2f,
                        -12f,
                        75f + x - 2f,
                        40f - 12f, CustomUI.radius.get(), Color(0, 0, 0).rgb
                    )
                    GL11.glPopMatrix()

                }, {
                    GL11.glPushMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                    GL11.glScalef(scale, scale, scale)
                    GlStateManager.enableBlend()
                    GlStateManager.disableTexture2D()
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                    RenderUtils.drawRoundedRect(
                        x - 2f,
                        -12f,
                        75f + x - 2f,
                        40f - 12f,CustomUI.radius.get(), Color(0, 0, 0).rgb
                    )
                    GlStateManager.enableTexture2D()
                    GlStateManager.disableBlend()
                    GL11.glPopMatrix()
                })
                GL11.glPopMatrix()
                GL11.glScalef(scale, scale, scale)
                GL11.glTranslated(renderX, renderY, 0.0)
            }
            if (CustomUI.drawMode.get().equals("高斯模糊和阴影")) {
                GL11.glTranslated(-renderX, -renderY, 0.0)
                GL11.glPushMatrix()
                BlurBuffer.CustomBlurRoundArea(
                    renderX.toFloat() - x, renderY.toFloat() - 12f, 75f + x - 1f,
                    40f, CustomUI.radius.get(), CustomUI.blurValue.get()
                )
                GL11.glPopMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
            }
            if (CustomUI.drawMode.get().equals("圆角矩形")) {
                RenderUtils.drawRoundedRect(
                    x - 2f,
                    -12f,
                    75f + x - 2f,
                    40f - 12f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
            }
            if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
                RenderUtils.drawRoundedRect(
                    x - 2f,
                    -12f,
                    75f + x - 2f,
                    40f - 12f,
                    CustomUI.radius.get(),
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
                )
                GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
                GL11.glPushMatrix()
                RoundedUtil.drawRoundOutline(
                    renderX.toFloat() - x, renderY.toFloat() - 12f, 75f + x - 1f,
                    40f,
                    CustomUI.radius.get(),
                    CustomUI.outlinet.get(),
                    Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get(), 0),
                    Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get(),CustomUI.a.get())
                )
                GL11.glPopMatrix()
                GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
            }
            GlStateManager.resetColor()
            if (CustomUI.Chinese.get()) {

                FontLoaders.F18.drawString("盔甲显示", x.toFloat() + 1f, -8f, Color.WHITE.rgb)
                for (index in 3 downTo 0) {
                    val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue
                    val stack2 = mc2.player.inventory.armorInventory[index]

                    renderItem.renderItemIntoGUI(stack, x, y)
                    renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
                    GlStateManager.pushMatrix();
                    FontLoaders.F15.drawString(
                        (stack2.maxDamage - stack.itemDamage).toString(), x.toFloat() + 3f,
                        y.toFloat() + 18f, Color.WHITE.rgb
                    )
                    GlStateManager.popMatrix();
                    if (mode.equals("Horizontal", true))
                        x += 18
                    else if (mode.equals("Vertical", true))
                        y += 18
                }

                classProvider.getGlStateManager().enableAlpha()
                classProvider.getGlStateManager().disableBlend()
                classProvider.getGlStateManager().disableLighting()
                classProvider.getGlStateManager().disableCull()
                GL11.glPopMatrix()
            }
            else{
                Fonts.pop35.drawString("Armor", x.toFloat(), -8f, Color.WHITE.rgb)
                for (index in 3 downTo 0) {
                    val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue
                    val stack2 = mc2.player.inventory.armorInventory[index]

                    renderItem.renderItemIntoGUI(stack, x, y)
                    renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
                    GlStateManager.pushMatrix();
                    Fonts.pop30.drawString(
                        (stack2.maxDamage - stack.itemDamage).toString(), x.toFloat() + 2f,
                        y.toFloat() + 18f, Color.WHITE.rgb
                    )
                    GlStateManager.popMatrix();
                    if (mode.equals("Horizontal", true))
                        x += 18
                    else if (mode.equals("Vertical", true))
                        y += 18
                }

                classProvider.getGlStateManager().enableAlpha()
                classProvider.getGlStateManager().disableBlend()
                classProvider.getGlStateManager().disableLighting()
                classProvider.getGlStateManager().disableCull()
                GL11.glPopMatrix()
            }

        return if (modeValue.get().equals("Horizontal", true))
            Border(
                (x - 2f).toFloat(),
                -12f,
                (75f + x - 2f).toFloat(),
                40f - 12f,
                CustomUI.radius.get())
        else
            Border(0F, 0F, 18F, 72F,CustomUI.radius.get())
    }
}