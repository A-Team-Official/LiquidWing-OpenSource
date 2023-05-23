package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

@ElementInfo(name = "TabGUI")
class TabGUI(x: Double = 5.0, y: Double = 25.0) : Element(x = x, y = y) {

    private val backgroundRedValue = IntegerValue("Background Red", 0, 0, 255)
    private val backgroundGreenValue = IntegerValue("Background Green", 0, 0, 255)
    private val backgroundBlueValue = IntegerValue("Background Blue", 0, 0, 255)
    private val backgroundAlphaValue = IntegerValue("Background Alpha", 150, 0, 255)
    private val textFade = BoolValue("TextFade", true)
    private val textPositionY = FloatValue("TextPosition-Y", 2F, 0F, 5F)
    private val tabHeight = FloatValue("TabHeight", 12F, 10F, 15F)

    private val tabs = mutableListOf<Tab>()

    private var categoryMenu = true
    private var selectedCategory = 0
    private var selectedModule = 0

    private var tabY = 0F
    private var itemY = 0F

    init {
        for (category in ModuleCategory.values()) {
            val tab = Tab(category.displayName)

            LiquidBounce.moduleManager.modules
                    .filter { module: Module -> category == module.category }
                    .forEach { e: Module -> tab.modules.add(e) }

            tabs.add(tab)
        }
    }

    override fun drawElement(): Border? {
        updateAnimation()

        AWTFontRenderer.assumeNonVolatile = true

        val fontRenderer = Fonts.rise35


        val backgroundColor = Color(backgroundRedValue.get(), backgroundGreenValue.get(), backgroundBlueValue.get(),
                backgroundAlphaValue.get())

        // Draw
        val guiHeight = tabs.size * tabHeight.get()

        RoundedUtil.drawRound(1F, 0F, 85f, guiHeight, 3f,backgroundColor)

        // Color
        val rectColor =
            Color(61,123,255)
        RoundedUtil.drawRound(26f, 1 + tabY - 1, 60f, tabHeight.get(),3f, rectColor)
        RoundedUtil.drawRound(1f, 0f,24f,guiHeight,3f,Color(10,10,10,140))
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        var y = 1F
        tabs.forEachIndexed { index, tab ->
            val tabName = tab.tabName

            val textX = if (side.horizontal == Side.Horizontal.RIGHT)
                85f - fontRenderer.getStringWidth(tabName) - tab.textFade - 3
            else
                tab.textFade + 35
            val textY = y + textPositionY.get()

            val textColor = if (selectedCategory == index) 0xffffff else Color(210, 210, 210).rgb

            fontRenderer.drawString(tabName, textX, textY, textColor)

            // String icon = category.icon;
            //绘制图标
            var l = ""
            if (tab.tabName.equals("Combat", ignoreCase = true)) {
                l = "D"
            } else if (tab.tabName.equals("Movement", ignoreCase = true)) {
                l = "A"
            } else if (tab.tabName.equals("Player", ignoreCase = true)) {
                l = "B"
            } else if (tab.tabName.equals("Render", ignoreCase = true)) {
                l = "C"
            } else if (tab.tabName.equals("Exploit", ignoreCase = true)) {
                l = "G"
            } else if (tab.tabName.equals("Misc", ignoreCase = true)) {
                l = "F"
            } else if (tab.tabName.equals("Color", ignoreCase = true)) {
                l = "M"
            } else if (tab.tabName.equals("Hyt", ignoreCase = true)) {
                l = "D"
            } else if (tab.tabName.equals("Gui", ignoreCase = true)) {
                l = "J"
            }
            else if (tab.tabName.equals("World", ignoreCase = true)) {
                l = "L"
            }
            net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.ICONFONT.ICONFONT_20.ICONFONT_20.drawString(l, 8.0,
                textY.toDouble() + 2f, textColor, false)

            y += tabHeight.get()
        }

        AWTFontRenderer.assumeNonVolatile = false

        return Border(1F, 0F, 85f, guiHeight,0F)
    }

    override fun handleKey(c: Char, keyCode: Int) {
        when (keyCode) {
            Keyboard.KEY_UP -> parseAction(Action.UP)
            Keyboard.KEY_DOWN -> parseAction(Action.DOWN)
        }
    }

    private fun updateAnimation() {
        val delta = RenderUtils.deltaTime

        val xPos = tabHeight.get() * selectedCategory
        if (tabY.toInt() != xPos.toInt()) {
            if (xPos > tabY)
                tabY += 0.1F * delta
            else
                tabY -= 0.1F * delta
        } else
            tabY = xPos
        val xPos2 = tabHeight.get() * selectedModule

        if (itemY.toInt() != xPos2.toInt()) {
            if (xPos2 > itemY)
                itemY += 0.1F * delta
            else
                itemY -= 0.1F * delta
        } else
            itemY = xPos2

        if (categoryMenu)
            itemY = 0F

        if (textFade.get()) {
            tabs.forEachIndexed { index, tab ->
                if (index == selectedCategory) {
                    if (tab.textFade < 4)
                        tab.textFade += 0.05F * delta

                    if (tab.textFade > 4)
                        tab.textFade = 4F
                } else {
                    if (tab.textFade > 0)
                        tab.textFade -= 0.05F * delta

                    if (tab.textFade < 0)
                        tab.textFade = 0F
                }
            }
        } else {
            for (tab in tabs) {
                if (tab.textFade > 0)
                    tab.textFade -= 0.05F * delta

                if (tab.textFade < 0)
                    tab.textFade = 0F
            }
        }
    }

    private fun parseAction(action: Action) {
        var toggle = false

        when (action) {
            Action.UP -> if (categoryMenu) {
                --selectedCategory
                if (selectedCategory < 0) {
                    selectedCategory = tabs.size - 1
                    tabY = tabHeight.get() * selectedCategory.toFloat()
                }
            } else {
                --selectedModule
                if (selectedModule < 0) {
                    selectedModule = tabs[selectedCategory].modules.size - 1
                    itemY = tabHeight.get() * selectedModule.toFloat()
                }
            }

            Action.DOWN -> if (categoryMenu) {
                ++selectedCategory
                if (selectedCategory > tabs.size - 1) {
                    selectedCategory = 0
                    tabY = tabHeight.get() * selectedCategory.toFloat()
                }
            } else {
                ++selectedModule
                if (selectedModule > tabs[selectedCategory].modules.size - 1) {
                    selectedModule = 0
                    itemY = tabHeight.get() * selectedModule.toFloat()
                }
            }

        }

        if (toggle) {
            val sel = selectedModule
            tabs[selectedCategory].modules[sel].toggle()
        }
    }

    /**
     * TabGUI Tab
     */
    private inner class Tab(val tabName: String) {

        val modules = mutableListOf<Module>()
        var menuWidth = 0
        var textFade = 0F
    }

    /**
     * TabGUI Action
     */
    enum class Action { UP, DOWN, LEFT, RIGHT, TOGGLE }
}