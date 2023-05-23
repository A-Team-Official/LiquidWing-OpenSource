
package net.ccbluex.liquidbounce.ui.client.hud.element.elements


import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FontValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.inventory.IInventory
import org.lwjgl.opengl.GL11
import java.awt.Color


@ElementInfo(name = "Inventory")
class Inventory(x: Double = 5.0, y: Double =134.0, scale: Float = 1F) : Element(x, y, scale) {
    private var inventoryRows = 0
    private val lowerInv: IInventory? = null
    private val blur = BoolValue("Blur", true)
    val fontValue = FontValue("Font", Fonts.sfbold40)
    override fun drawElement(): Border {
        val floatX = renderX.toFloat()
        val floatY = renderY.toFloat()
        val x2 = 174f
        val y1 = 26f
        val fontRenderer = fontValue.get()
        RenderUtils.drawShadow(0f, 14f, 176f, 74f)

        if (lowerInv != null) {
            this.inventoryRows = lowerInv.getSizeInventory()
        }
        renderInventory1(mc.thePlayer)
        renderInventory2(mc.thePlayer)
        renderInventory3(mc.thePlayer)
        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.blurArea(floatX, floatY + 14  , 176F, 74F )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        RenderUtils.drawShadow(0f, 14f, 176f, 74f)
        fontRenderer.drawString("Inventory List", 5.0f, 17.0f,Color(255,255,255,255).rgb,true)
        val hud = LiquidBounce.moduleManager.getModule(
            HUD::class.java
        ) as HUD
        RenderUtils.drawGradientSideways(
            2.2,
            y1 + 1.1,
            x2.toDouble() - 2.22,
            y1 + 2.5 + 1.16f - 1,
            Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get()).rgb,
            Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get()).rgb
        )
        if (lowerInv != null) {
            this.inventoryRows = lowerInv.getSizeInventory()
        }
        renderInventory1(mc.thePlayer)
        renderInventory2(mc.thePlayer)
        renderInventory3(mc.thePlayer)



        return   Border(0f, 14f, 176f,90f,0F)



    }
    fun getFadeProgress() = 0f
    fun getColor(color: Color) = ColorUtils.reAlpha(color, color.alpha / 255F * (1F - getFadeProgress()))
    fun getColor(color: Int) = getColor(Color(color))
    private fun renderInventory1(player: IEntityPlayerSP?) {
        var armourStack: IItemStack?
        var renderStack = player!!.inventory.mainInventory
        var xOffset = 8
        renderStack = player.inventory.mainInventory
        for (index in 9..17) {
            armourStack = renderStack[index]
            if (armourStack != null) this.renderItemStack(armourStack, xOffset, 30)
            xOffset += 18
        }
    }
    private fun renderInventory2(player: IEntityPlayerSP?) {
        var armourStack: IItemStack?
        var renderStack = player!!.inventory.mainInventory
        var xOffset = 8
        renderStack = player.inventory.mainInventory
        for (index in 18..26) {
            armourStack = renderStack[index]
            if (armourStack != null) this.renderItemStack(armourStack, xOffset, 48)
            xOffset += 18
        }
    }
    private fun renderInventory3(player: IEntityPlayerSP?) {
        var armourStack: IItemStack?
        var renderStack = player!!.inventory.mainInventory
        var xOffset = 8
        renderStack = player.inventory.mainInventory
        for (index in 27..35) {
            armourStack = renderStack[index]
            if (armourStack != null) this.renderItemStack(armourStack, xOffset, 66)
            xOffset += 18
        }
    }
    private fun renderItemStack(stack: IItemStack, x: Int, y: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderHelper.enableGUIStandardItemLighting()
        mc.renderItem.renderItemAndEffectIntoGUI(stack, x, y)
        mc.renderItem.renderItemOverlays(Fonts.minecraftFont, stack, x, y)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

}