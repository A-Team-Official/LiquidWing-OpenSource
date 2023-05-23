package net.ccbluex.liquidbounce.ui.client.hud

import ad.Session2
import ad.hud.*
import ad.sb.DisEffects
import ad.sb.TargetHud
import ad.sb.novoline.FuckMixin
import ad.sb.novoline.ui.NovoSession
import ad.sb.novoline.ui.PlayerInfo
import ad.sb.novoline.ui.SessionInfo
import net.ccbluex.liquidbounce.GuiLogin
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.*
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.KeyBinds
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.minecraft.client.Minecraft
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import kotlin.math.max
import kotlin.math.min

open class HUD : MinecraftInstance() {

    val elements = mutableListOf<Element>()
    val notifications = mutableListOf<Notification>()
    val autoplayhuds = mutableListOf<AutoPlayHuds>()
    companion object {

        val elements = arrayOf(

            Logo::class.java,
            NewEffects::class.java,
            BanChecker::class.java,
            NewArmor::class.java,
            PlayerInfo::class.java,
            AutoPlayHud::class.java,
            ChineseArraylist::class.java,
            KeyBinds::class.java,

            Logo2::class.java,
            Info::class.java,
            BPS::class.java,
            FPS::class.java,
            net.ccbluex.liquidbounce.ui.client.hud.element.elements .KeyBinds2::class.java,
            NewArmor2::class.java,

            Radar::class.java,
            Arraylist::class.java,
            Inventory::class.java,
            Armor::class.java,


            Keystrokes::class.java,
            Hotbar::class.java,
            Image::class.java,
            Notifications::class.java,
            TabGUI::class.java,
            Text::class.java,
            ScoreboardElement::class.java,

            NovoSession::class.java,
            SessionInfo::class.java,
            ad.sb.Effects::class.java,

            Session2::class.java,
            DisEffects::class.java,
            ad.hud.KeyBinds::class.java,

            Target::class.java,
            TargetHud::class.java,

            ArmorHud::class.java,
            HealthHud::class.java,
            HurtTimeHud::class.java,
            MemoryHud::class.java
        )

        /**
         * Create default HUD
         */
        @JvmStatic
        fun createDefault() = HUD()
            .addElement(Arraylist())
            .addElement(ScoreboardElement())
            .addElement(Notifications())
            .addElement(Target())
            .addElement(net.ccbluex.liquidbounce.ui.client.hud.element.elements.Hotbar())

    }

    /**
     * Render all elements
     */
    fun rendershadow(designer: Boolean) {
        elements.sortedBy { -it.info.priority }
            .forEach {
                GL11.glPushMatrix()

                if (!it.info.disableScale)
                    GL11.glScalef(it.scale, it.scale, it.scale)

                GL11.glTranslated(it.renderX, it.renderY, 0.0)

                try {
                    it.shadow()
                } catch (ex: Exception) {
                    ClientUtils.getLogger()
                        .error("Something went wrong while drawing ${it.name} element in HUD.", ex)
                }

                GL11.glPopMatrix()
            }
    }
    fun render(designer: Boolean) {
        elements.sortedBy { -it.info.priority }
            .forEach {
                GL11.glPushMatrix()

                if (!it.info.disableScale)
                    GL11.glScalef(it.scale, it.scale, it.scale)

                GL11.glTranslated(it.renderX, it.renderY, 0.0)

                try {
                    it.border = it.drawElement()

                    if (designer)
                        it.border?.draw2()
                } catch (ex: Exception) {
                    ClientUtils.getLogger()
                        .error("Something went wrong while drawing ${it.name} element in HUD.", ex)
                }

                GL11.glPopMatrix()
            }
    }

    fun render(designer: Boolean,mouseX: Int, mouseY: Int) {
        elements.sortedBy { -it.info.priority }
            .forEach {
                GL11.glPushMatrix()

                if (!it.info.disableScale)
                    GL11.glScalef(it.scale, it.scale, it.scale)

                GL11.glTranslated(it.renderX, it.renderY, 0.0)

                try {
                    it.border = it.drawElement()

                    if (designer)
                        it.border?.draw(it.renderX,it.renderY,mouseX, mouseY)
                } catch (ex: Exception) {
                    ClientUtils.getLogger()
                        .error("Something went wrong while drawing ${it.name} element in HUD.", ex)
                }

                GL11.glPopMatrix()
            }
    }

    /**
     * Update all elements
     */
    fun update() {
        FuckMixin.I1lI1l1iIlI1I1ilI11I()
        if (!LiquidBounce.initclient)
        {
            Minecraft.getMinecraft().displayGuiScreen(GuiLogin())
        }
        for (element in elements)
            element.updateElement()
    }

    /**
     * Handle mouse click
     */
    fun handleMouseClick(mouseX: Int, mouseY: Int, button: Int) {
        for (element in elements)
            element.handleMouseClick(
                (mouseX / element.scale) - element.renderX, (mouseY / element.scale)
                        - element.renderY, button
            )

        if (button == 0) {
            for (element in elements.reversed()) {
                if (!element.isInBorder(
                        (mouseX / element.scale) - element.renderX,
                        (mouseY / element.scale) - element.renderY
                    )
                )
                    continue

                element.drag = true
                elements.remove(element)
                elements.add(element)
                break
            }
        }
    }

    /**
     * Handle released mouse key
     */
    fun handleMouseReleased() {
        for (element in elements)
            element.drag = false
    }

    /**
     * Handle mouse move
     */
    fun handleMouseMove(mouseX: Int, mouseY: Int) {

        val scaledResolution = classProvider.createScaledResolution(mc)

        for (element in elements) {
            val scaledX = mouseX / element.scale
            val scaledY = mouseY / element.scale
            val prevMouseX = element.prevMouseX
            val prevMouseY = element.prevMouseY

            element.prevMouseX = scaledX
            element.prevMouseY = scaledY

            if (element.drag) {
                //判断鼠标是否按下 水影自带的太辣鸡啦 什么垃圾API
                if (!Mouse.isButtonDown(0)) {
                    element.drag = false
                }
                val moveX = scaledX - prevMouseX
                val moveY = scaledY - prevMouseY

                if (moveX == 0F && moveY == 0F)
                    continue

                val border = element.border ?: continue

                val minX = min(border.x, border.x2) + 1
                val minY = min(border.y, border.y2) + 1

                val maxX = max(border.x, border.x2) - 1
                val maxY = max(border.y, border.y2) - 1

                val width = scaledResolution.scaledWidth / element.scale
                val height = scaledResolution.scaledHeight / element.scale

                if ((element.renderX + minX + moveX >= 0.0 || moveX > 0) && (element.renderX + maxX + moveX <= width || moveX < 0))
                    element.renderX = moveX.toDouble()
                if ((element.renderY + minY + moveY >= 0.0 || moveY > 0) && (element.renderY + maxY + moveY <= height || moveY < 0))
                    element.renderY = moveY.toDouble()
            }
        }
    }

    /**
     * Handle incoming key
     */
    fun handleKey(c: Char, keyCode: Int) {
        for (element in elements)
            element.handleKey(c, keyCode)
    }

    /**
     * Add [element] to HUD
     */
    fun addElement(element: Element): HUD {
        elements.add(element)
        element.updateElement()
        return this
    }

    /**
     * Remove [element] from HUD
     */
    fun removeElement(element: Element): HUD {
        element.destroyElement()
        elements.remove(element)
        return this
    }

    /**
     * Clear all elements
     */
    fun clearElements() {
        for (element in elements)
            element.destroyElement()

        elements.clear()
    }

    /**
     * Add [notification]
     */
    fun addAutoPlayHud(notification: AutoPlayHuds) =
        elements.any { it is AutoPlayHud } && autoplayhuds.add(notification)
    fun addNotification(notification: Notification) =
        elements.any { it is Notifications } && notifications.add(notification)

    /**
     * Remove [notification]
     */
    fun removeNotification(notification: Notification) = notifications.remove(notification)

}