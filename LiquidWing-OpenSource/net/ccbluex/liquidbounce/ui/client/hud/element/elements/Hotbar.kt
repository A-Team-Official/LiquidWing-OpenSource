package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.utils.hotbarutil
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.util.ITooltipFlag.TooltipFlags
import java.awt.Color

@ElementInfo(name = "Hotbar") class Hotbar(x: Double = 380.0, y: Double = 50.0) : Element(x, y) {
    private val itemrenderY = FloatValue("itemrenderY",-50f, -500f, 500f)
    private val slotlist = mutableListOf<hotbarutil>()

    init {
        for (i in 0..8) {
            val slot = hotbarutil()
            slotlist.add(slot)
        }
    }

    override fun drawElement(): Border {

        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)


        slotlist.forEachIndexed { index, hotbarutil ->

            val hover = index == mc2.player.inventory.currentItem && mc2.player.inventory.mainInventory[index] != null
            val scale = hotbarutil.translate.x
            val positionX = (index * 25 / scale) - 5
            val currentitem = mc2.player.inventory.mainInventory[mc2.player.inventory.currentItem]

            hotbarutil.size = if (hover) 1.5f else 1.0f
            hotbarutil.translate.translate(hotbarutil.size, 0f, 2.0)

            if (hover) {
                GlStateManager.pushMatrix()
                GlStateManager.scale(scale - 0.5f, scale - 0.5f, scale - 0.5f)

                try {
                    val list = currentitem.getTooltip(mc2.player,
                        if (mc2.gameSettings.advancedItemTooltips) TooltipFlags.ADVANCED else TooltipFlags.NORMAL)
                    val infolist : ArrayList<String> = ArrayList()

                    for(i in 0 until list.size) {
                        if (!infolist.contains(list[i]) && list[i].length > 2 ) {
                            infolist.add(list[i])
                        }
                    }
                    val posy = -13f
                    val font = FontLoaders.F18

                    font.drawString(currentitem.displayName, positionX * 1.5f,-(8.5f ) + posy + itemrenderY.get(), -1 , true)
                    // RoundedUtil.drawRound(positionX * 1.5f,posy + 26,20F,2F,1F, Color(20, 23, 22))
                    RoundedUtil.drawRound(positionX * 1.5f,posy +26,20F,1.6F,0.4F, Color.WHITE)
                    infolist.clear()
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                GlStateManager.popMatrix()
            }
            GlStateManager.pushMatrix()
            GlStateManager.scale(scale, scale, scale)
            RenderHelper.enableGUIStandardItemLighting()
            hotbarutil.renderHotbarItem(index, positionX, -10f, mc.timer.renderPartialTicks)
            RenderHelper.disableStandardItemLighting()

            GlStateManager.popMatrix()
        }

        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
        return Border(0f, 0f, 180f, 17f,0f)
    }
}