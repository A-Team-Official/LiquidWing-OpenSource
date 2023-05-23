package net.ccbluex.liquidbounce.ui.client.newui.element.module

import ad.fonts.Fonts
import ad.utils.Color.modules.CustomUI
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.ui.client.clickgui.lbplus.element.module.value.impl.ListElement
import net.ccbluex.liquidbounce.ui.client.newui.ColorManager
import net.ccbluex.liquidbounce.ui.client.newui.element.components.ToggleSwitch
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.ValueElement
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.impl.BooleanElement
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.impl.FloatElement
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.impl.IntElement
import net.ccbluex.liquidbounce.ui.client.newui.extensions.animSmooth
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.utils.BlendUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.MouseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.Stencil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11.*
import java.awt.Color

class ModuleElement(val module: Module): MinecraftInstance() {

    companion object {
        protected val expandIcon = LiquidBounce.wrapper.classProvider.createResourceLocation("liquidwing/expand.png") }

    private val toggleSwitch = ToggleSwitch()
    private val valueElements = mutableListOf<ValueElement<*>>()

    var animHeight = 0F
    private var fadeKeybind = 0F
    private var animPercent = 0F

    private var listeningToKey = false
    var expanded = false

    init {
        for (value in module.values) {
            if (value is BoolValue)
                valueElements.add(BooleanElement(value))
            if (value is ListValue)
                valueElements.add(ListElement(value))
            if (value is IntegerValue)
                valueElements.add(IntElement(value))
            if (value is FloatValue)
                valueElements.add(FloatElement(value))
        }
    }

    fun drawElement(mouseX: Int, mouseY: Int, x: Float, y: Float, width: Float, height: Float, accentColor: Color): Float {
        animPercent = animPercent.animSmooth(if (expanded) 100F else 0F, 0.5F)
        var expectedHeight = 0F
        for (ve in valueElements)
            if (ve.isDisplayable())
                expectedHeight += ve.valueHeight
        animHeight = animPercent / 100F * (expectedHeight + 10F)

        RenderUtils.drawRoundedRect(
            x + 9.5F,
            y + 4.5F,
            x + width - 9.5F,
            y + height + animHeight - 4.5F,
            4F,
            ColorManager.buttonOutline.rgb
        )
        Stencil.write(true)
        RenderUtils.drawRoundedRect(
            x + 10F,
            y + 5F,
            x + width - 10F,
            y + height + animHeight - 5F,
            4F,
            ColorManager.moduleBackground.rgb
        )
        Stencil.erase(true)
        RenderUtils.drawRect(x + 10F, y + height - 5F, x + width - 10F, y + height - 4.5F, 4281348144L.toInt())
        if (CustomUI.Chinese.get()) {
            FontLoaders.F20.drawStringWithShadow(
                module.Chinese,
                x + 20F,
                y + height / 2F - Fonts.font40.fontHeight + 3F,
                -1
            )
        } else {
            Fonts.font40.drawString(module.name, x + 20F, y + height / 2F - Fonts.font40.fontHeight + 3F, -1)
        }
        //Fonts.font35.drawString(module.description, x + 20F, y + height / 2F + 4F, 10526880L.toInt())

        val keyName = if (listeningToKey) "Listening" else Keyboard.getKeyName(module.keyBind)

        if (CustomUI.Chinese.get()){
        if (MouseUtils.mouseWithinBounds(
                mouseX, mouseY,
                x + 25F +  FontLoaders.F20.getStringWidth(module.Chinese),
                y + height / 2F - FontLoaders.F20.height + 2F,
                x + 35F + FontLoaders.F20.getStringWidth(module.Chinese) +  FontLoaders.F18.getStringWidth(keyName),
                y + height / 2F
            )
        )
            fadeKeybind = (fadeKeybind + 0.1F * RenderUtils.deltaTime * 0.025F).coerceIn(0F, 1F)
        else
            fadeKeybind = (fadeKeybind - 0.1F * RenderUtils.deltaTime * 0.025F).coerceIn(0F, 1F)
    }
        else
        {
            if (MouseUtils.mouseWithinBounds(
                    mouseX, mouseY,
                    x + 25F + Fonts.font40.getStringWidth(module.name),
                    y + height / 2F - Fonts.font40.fontHeight + 2F,
                    x + 35F + Fonts.font40.getStringWidth(module.name) + Fonts.font35.getStringWidth(keyName),
                    y + height / 2F
                )
            )
                fadeKeybind = (fadeKeybind + 0.1F * RenderUtils.deltaTime * 0.025F).coerceIn(0F, 1F)
            else
                fadeKeybind = (fadeKeybind - 0.1F * RenderUtils.deltaTime * 0.025F).coerceIn(0F, 1F)
        }
        if (CustomUI.Chinese.get()){
            RenderUtils.drawRoundedRect(
                x + 25F + FontLoaders.F20.getStringWidth(module.Chinese),
                y + height / 2F - Fonts.font40.fontHeight + 2F,
                x + 35F + FontLoaders.F20.getStringWidth(module.Chinese) + Fonts.font35.getStringWidth(keyName),
                y + height / 2F, 2F, BlendUtils.blend(Color(4282729797L.toInt()), Color(4281677109L.toInt()), fadeKeybind.toDouble()).rgb)
            Fonts.font35.drawString(keyName, x + 30.5F + FontLoaders.F20.getStringWidth(module.Chinese), y + height / 2F - FontLoaders.F20.height / 2f + 5.5F, -1)

            }
        else{
            RenderUtils.drawRoundedRect(
                x + 25F + Fonts.font40.getStringWidth(module.name),
                y + height / 2F - Fonts.font40.fontHeight + 2F,
                x + 35F + Fonts.font40.getStringWidth(module.name) + Fonts.font35.getStringWidth(keyName),
                y + height / 2F, 2F, BlendUtils.blend(Color(4282729797L.toInt()), Color(4281677109L.toInt()), fadeKeybind.toDouble()).rgb)
            Fonts.font35.drawString(keyName, x + 30.5F + Fonts.font40.getStringWidth(module.name), y + height / 2F - Fonts.font40.fontHeight + 5.5F, -1)
        }

        toggleSwitch.state = module.state

        if (module.values.size > 0) {
            RenderUtils.drawRect(x + width - 40F, y + 5F, x + width - 39.5F, y + height - 5F, 4281348144L.toInt())
            GlStateManager.resetColor()
            glPushMatrix()
            glTranslatef(x + width - 25F, y + height / 2F, 0F)
            glPushMatrix()
            glRotatef(180F * (animHeight / (expectedHeight + 10F)), 0F, 0F, 1F)
            glColor4f(1F, 1F, 1F, 1F)
            RenderUtils.drawImage(expandIcon, -4, -4, 8, 8)
            glPopMatrix()
            glPopMatrix()
            toggleSwitch.onDraw(x + width - 70F, y + height / 2F - 5F, 20F, 10F, Color(4280624421L.toInt()), accentColor)
        } else
            toggleSwitch.onDraw(x + width - 40F, y + height / 2F - 5F, 20F, 10F, Color(4280624421L.toInt()), accentColor)

        if (expanded || animHeight > 0F) {
            var startYPos = y + height
            for (ve in valueElements)
                if (ve.isDisplayable())
                    startYPos += ve.drawElement(mouseX, mouseY, x + 10F, startYPos, width - 20F, Color(4280624421L.toInt()), accentColor)
        }
        Stencil.dispose()

        return height + animHeight
    }

    fun handleClick(mouseX: Int, mouseY: Int, x: Float, y: Float, width: Float, height: Float) {
        if (listeningToKey) {
            resetState()
            return
        }
        val keyName = if (listeningToKey) "Listening" else Keyboard.getKeyName(module.keyBind)
        if (CustomUI.Chinese.get()) {
            if (MouseUtils.mouseWithinBounds(
                    mouseX, mouseY,
                    x + 25F + FontLoaders.F20.getStringWidth(module.name),
                    y + height / 2F - FontLoaders.F20.height + 2F,
                    x + 35F + FontLoaders.F18.getStringWidth(module.name) + FontLoaders.F18.getStringWidth(keyName),
                    y + height / 2F
                )
            ) {
                listeningToKey = true
                return
            }
        }
        else{
            if (MouseUtils.mouseWithinBounds(mouseX, mouseY,
                    x + 25F + Fonts.font40.getStringWidth(module.name),
                    y + height / 2F - Fonts.font40.fontHeight + 2F,
                    x + 35F + Fonts.font40.getStringWidth(module.name) + Fonts.font35.getStringWidth(keyName),
                    y + height / 2F)) {
                listeningToKey = true
                return
            }
        }
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, 
                x + width - if (module.values.size > 0) 70F else 40F, y, 
                x + width - if (module.values.size > 0) 50F else 20F, y + height))
            module.toggle()
        if (module.values.size > 0 && MouseUtils.mouseWithinBounds(mouseX, mouseY, x + width - 40F, y, x + width - 10F, y + height))
            expanded = !expanded
        if (expanded) {
            var startY = y + height
            for (ve in valueElements) {
                if (!ve.isDisplayable()) continue
                ve.onClick(mouseX, mouseY, x + 10F, startY, width - 20F)
                startY += ve.valueHeight
            }
        }
    }

    fun handleRelease(mouseX: Int, mouseY: Int, x: Float, y: Float, width: Float, height: Float) {
        if (expanded) {
            var startY = y + height
            for (ve in valueElements) {
                if (!ve.isDisplayable()) continue
                ve.onRelease(mouseX, mouseY, x + 10F, startY, width - 20F)
                startY += ve.valueHeight
            }
        }
    }

    fun handleKeyTyped(typed: Char, code: Int): Boolean {
        if (listeningToKey) {
            if (code == 1) {
                module.keyBind = 0
                listeningToKey = false
            } else {
                module.keyBind = code
                listeningToKey = false
            }
            return true
        }
        if (expanded)
            for (ve in valueElements)
                if (ve.isDisplayable() && ve.onKeyPress(typed, code)) return true
        return false
    }

    fun listeningKeybind(): Boolean = listeningToKey
    fun resetState() {
        listeningToKey = false
    }

}