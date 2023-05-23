package net.ccbluex.liquidbounce.features.module

import ad.utils.Color.modules.CustomUI
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification.NotifyType
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.Translate
import net.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation
import net.ccbluex.liquidbounce.utils.render.miku.animations.impl.DecelerateAnimation
import net.ccbluex.liquidbounce.value.Value
import org.lwjgl.input.Keyboard

open class Module : MinecraftInstance(), Listenable {
    // Module information
    // TODO: Remove ModuleInfo and change to constructor (#Kotlin)
    var name: String
    var rectHoved = 0F
    var hovered = false
    var expanded: Boolean = false
    val animation: Animation = DecelerateAnimation(250, 1.0)
    var description: String
    var Chinese: String
    var category: ModuleCategory
    var openValues = false
    var keyBind = Keyboard.CHAR_NONE
        set(keyBind) {
            field = keyBind

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        }
    var array = true
        set(array) {
            field = array

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        }
    private val canEnable: Boolean
    var hoverOpacity = 0f
    var slideStep = 0F
    var animation2 = 0F
    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)!!

        name = moduleInfo.name
        description = moduleInfo.description
        Chinese = moduleInfo.Chinese
        category = moduleInfo.category
        keyBind = moduleInfo.keyBind
        array = moduleInfo.array
        canEnable = moduleInfo.canEnable
    }
    val moduleTranslate = Translate(0F, 0F)
    val tab = Translate(0f , 0f)
    // Current state of module
    var state = false
        set(value) {
            if (field == value)
                return

            field = value && canEnable

            // Call toggle
            onToggle(value)

            // Play sound and add notification
            val dmode = Notifications.Companion.drawmode
            if (!LiquidBounce.isStarting) {
                when (LiquidBounce.moduleManager.toggleSoundMode) {
                    1 -> (if (value) mc.soundHandler.playSound("minecraft:block.anvil.break", 0.1F))
                    2 -> (if (value) LiquidBounce.tipSoundManager.enableSound else LiquidBounce.tipSoundManager.disableSound).asyncPlay()
                }
//                if (dmode.equals("Tenacity")){
                if (dmode.get().equals("Novoline2")) {
                    if (value) {
                        LiquidBounce.hud.addNotification(Notification("", "Feature " + "'"+name+"'"+" was enabled", NotifyType.SUCCESS))
                    } else {

                        LiquidBounce.hud.addNotification(Notification("", "Feature " + "'"+name+"'"+" was disabled", NotifyType.ERROR))
                    }
                }
                if (dmode.get().equals("Windows11")) {
                    if (value) {
                        LiquidBounce.hud.addNotification(Notification("Enabled", name, NotifyType.SUCCESS))
                    } else {
                        LiquidBounce.hud.addNotification(Notification("Disabled", name, NotifyType.ERROR))
                    }
                }
                if (dmode.get().equals("NewRound")) {
                    if (value) {
                        LiquidBounce.hud.addNotification(Notification("Module toggled",
                            "$name was \u00A7aenabled\r", NotifyType.SUCCESS))
                    } else {
                        LiquidBounce.hud.addNotification(Notification("Module toggled", name + " was " + "\u00A7cdisabled\r", NotifyType.ERROR))
                    }
                }
                if (dmode.get().equals("LiQuidWIng")) {
                    if (CustomUI.Chinese.get()) {
                        if (value) {
                            LiquidBounce.hud.addNotification(
                                Notification(
                                    "通知",
                                    "开启功能 $Chinese",
                                    NotifyType.SUCCESS
                                )
                            )
                        } else {
                            LiquidBounce.hud.addNotification(
                                Notification(
                                    "通知",
                                    "关闭功能 $Chinese",
                                    NotifyType.ERROR
                                )
                            )
                        }
                    } else {
                        if (value) {
                            LiquidBounce.hud.addNotification(
                                Notification(
                                    "Notifications",
                                    "Open $name",
                                    NotifyType.SUCCESS
                                )
                            )
                        } else {
                            LiquidBounce.hud.addNotification(
                                Notification(
                                    "Notifications",
                                    "Close $name",
                                    NotifyType.ERROR
                                )
                            )
                        }
                    }

                }
                else
                if (value) {
                    LiquidBounce.hud.addNotification(Notification("Module toggled", "$name${" was Enabled"}", NotifyType.SUCCESS))
                } else
                    LiquidBounce.hud.addNotification(Notification("Module toggled", "$name${" was Disabled"}", NotifyType.ERROR))

                // Call on enabled or disabled
                if (value) {
                    onEnable()
                } else {
                    onDisable()
                }
                // Save module state
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
            }
        }

    @JvmField
    var showSettings = false
    var hovvv=false
    // HUD
    val hue = Math.random().toFloat()
    var slide = 0F
    var nameBreak: Boolean = false
    var higt = 0F

    // Tag
    open val tag: String?
        get() = null

    val ChineseTagName: String
        get() = "$Chinese${if (tag == null) "" else " §7$tag"}"
    val tagName: String
        get() = "$name${if (tag == null) "" else " §7$tag"}"

    val colorlessTagName: String
        get() = "$name${if (tag == null) "" else " " + stripColor(tag)}"

    /**
     * Toggle module
     */
    fun toggle() {
        state = !state
    }

    /**
     * Called when module toggled
     */
    open fun onToggle(state: Boolean) {}

    /**
     * Called when module enabled
     */
    open fun onEnable() {}

    /**
     * Called when module disabled
     */
    open fun onDisable() {}

    /**
     * Get module by [valueName]
     */
    open fun getValue(valueName: String) = values.find { it.name.equals(valueName, ignoreCase = true) }

    /**
     * Get all values of module
     */
    open val values: List<Value<*>>
        get() = javaClass.declaredFields.map { valueField ->
            valueField.isAccessible = true
            valueField[this]
        }.filterIsInstance<Value<*>>()

    /**
     * Events should be handled when module is enabled
     */
    override fun handleEvents() = state
}