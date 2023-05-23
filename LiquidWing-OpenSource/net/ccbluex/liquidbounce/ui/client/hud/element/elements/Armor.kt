/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.api.enums.MaterialType
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
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
@ElementInfo(name = "Vape-Armor")
class Armor(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F,
            side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    private val bV = BoolValue("Blur", true)
    private val FShadow = BoolValue("Font-Shadow", true)
    private val alphaValue = IntegerValue("Alpha", 120, 0, 255)

    private val modeValue = ListValue("Ar-Mode", arrayOf("Vape"), "Vape")

    fun shader() {
        if (modeValue.get().equals("Vape", true)) {
            RenderUtils.drawRoundedRect(0F, 0F, 50F, 76F,5f, -1 )
        }
    }
    /**
     * Draw element
     * This element by 7ad QQ:738606342
     */
    override fun drawElement(): Border {
        //  if (mc.playerController.isNotCreative) {
        GL11.glPushMatrix()

        val renderItem = mc.renderItem
        val isInsideWater = mc.thePlayer!!.isInsideOfMaterial(classProvider.getMaterialEnum(MaterialType.WATER))

        var x = 1
        var y = if (isInsideWater) -10 else 0

        val mode = modeValue.get()
                if (bV.get()) {
                    GL11.glTranslated(-renderX, -renderY, 0.0)
                    GL11.glPushMatrix()
                    BlurBuffer.blurRoundArea(renderX.toFloat(), renderY.toFloat()  , 50F, 76F,2)
                    GL11.glPopMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                }
        //Rect
        RenderUtils.drawRoundedRect(0F, 0F, 50F, 76F,2f, Color(11,11,12,this.alphaValue.get()).rgb )
        RenderUtils.drawRoundedRect(x+22f,y+4f,x + 21f,y + 16.5f,0f,Color(100,100,101,this.alphaValue.get()+20).rgb)
        RenderUtils.drawRoundedRect(x+22f,y+23.5f,x + 21f,y + 35.5f,0f,Color(100,100,101,this.alphaValue.get()+20).rgb)
        RenderUtils.drawRoundedRect(x+22f,y+41.5f,x + 21f,y + 53.5f,0f,Color(100,100,101,this.alphaValue.get()+20).rgb)
        RenderUtils.drawRoundedRect(x+22f,y+60.5f,x + 21f,y + 72.5f,0f,Color(100,100,101,this.alphaValue.get()+20).rgb)

        Gui.drawRect(0,0,0,0,-1)
        for (index in 3 downTo 0) {
            val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue

            renderItem.renderItemAndEffectIntoGUI(stack, x + 3, y + 3)
            val itemDamage = stack.maxDamage - stack.itemDamage
            GlStateManager.pushMatrix()
            GlStateManager.scale(0.5F, 0.5F, 0.5F)
            Gui.drawRect(0,0,0,0,-1)
            GlStateManager.popMatrix()
            var ms = Math.round(itemDamage * 1f / stack.maxDamage * 100f).toFloat()
            var s = StringBuilder().append(DecimalFormat().format(java.lang.Float.valueOf(ms))).append("%")
                .toString()
            Fonts.tenacitybold25.drawString(s, (x + 26).toFloat(), (y + 6.7).toFloat(), -1, FShadow.get())
            //Rect Shadow
            RoundedUtil.drawRound(x+25f,y + 13.5f,(itemDamage * 1f / stack.maxDamage * 20f),1.0f,2.5f,Color(6,127,99))
            RoundedUtil.drawRound(x+25f,y + 13.8f,(itemDamage * 1f / stack.maxDamage * 20f),1.1f,2.5f,Color(6,127,99,210))
            RoundedUtil.drawRound(x+25.3f,y + 13.5f,(itemDamage * 1f / stack.maxDamage * 20f),1.1f,2.5f,Color(6,127,99,210))
            if (mode.equals("Vape", true))
                y += 18
        }

        classProvider.getGlStateManager().enableAlpha()
        classProvider.getGlStateManager().disableBlend()
        classProvider.getGlStateManager().disableLighting()
        classProvider.getGlStateManager().disableCull()
        GL11.glPopMatrix()
        // }

        return if (modeValue.get().equals("Vape", true))
            Border(0F, 0F, 50F, 76F,0F)
        else
            Border(0F, 0F, 50F, 76F,0F)
    }
}