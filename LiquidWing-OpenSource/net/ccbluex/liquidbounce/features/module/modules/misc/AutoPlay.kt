package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.AutoPlayHuds
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification.NotifyType
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketClickWindow
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.network.play.server.SPacketChat
import net.minecraft.network.play.server.SPacketOpenWindow
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.schedule

@ModuleInfo(name = "AutoPlay", category = ModuleCategory.PLAYER,description = "e", Chinese = "自动游玩")
class AutoPlay : Module() {
    private var clickState = 0
    private val modeValue = ListValue("Server", arrayOf("RedeSky", "Minemora", "HuaYuTingGG","HuaYuTingSw","HuaYuTing16"), "HuaYuTingGG")
    private val ClientName = BoolValue("Clientname",true)
    private val autodis = BoolValue("Auto-Disable",true)
    val delayValue = IntegerValue("JoinDelay", 3, 0, 7)

    private var clicking = false
    private var queued = false
    override fun onEnable() {
        clickState = 0
        clicking = false
        queued = false
    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()

        when (modeValue.get().toLowerCase()) {
            "redesky" -> {
                if (clicking && (packet is CPacketClickWindow || packet is CPacketPlayerDigging)) {
                    event.cancelEvent()
                    return
                }
                if (clickState == 2 && packet is SPacketOpenWindow) {
                    event.cancelEvent()
                }
            }
            "hypixel" -> {
                if (clickState == 1 && packet is SPacketOpenWindow) {
                    event.cancelEvent()
                }
            }
        }

        val KillAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
        val Velocity = LiquidBounce.moduleManager.getModule(Velocity::class.java) as Velocity
        val ChestStealer = LiquidBounce.moduleManager.getModule(ChestStealer::class.java) as ChestStealer
        val InventoryCleaner = LiquidBounce.moduleManager.getModule(InventoryCleaner::class.java) as InventoryCleaner
        val AutoArmor = LiquidBounce.moduleManager.getModule(AutoArmor::class.java) as AutoArmor
        if (packet is SPacketChat) {
            val text = packet.chatComponent.unformattedText
            when (modeValue.get().toLowerCase()) {
                "minemora" -> {
                    if (text.contains("Has click en alguna de las siguientes opciones", true)) {
                        queueAutoPlay {
                            mc.thePlayer!!.sendChatMessage("/join")
                            Recorder.win++
                        }
                    }
                }
                "huayutinggg" -> {
                    if (text.contains("      喜欢      一般      不喜欢", true)) {
                        LiquidBounce.hud.addNotification(Notification(name,"Send you to next Game", NotifyType.INFO))
                        if (ClientName.get()){mc.thePlayer!!.sendChatMessage("@["+LiquidBounce.CLIENT_NAME+"]GG")}
                        else{mc.thePlayer!!.sendChatMessage("@GG")}
                        if (autodis.get()) {
                            KillAura.state = false
                            Velocity.state = false
                        }
                        Recorder.win++

                        LiquidBounce.hud.addAutoPlayHud(AutoPlayHuds("你赢得了本场游戏胜利 | 胜利数:"+Recorder.win,net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType.INFO))
                        LiquidBounce.hud.addNotification(Notification("Warning","KillAura was disabled, because game has ended", NotifyType.WARNING))

                    }
                }
                "huayutingsw" -> {
                    val matcher = Pattern.compile("你在地图 (.*?)\\(").matcher(packet.chatComponent.unformattedText)
                    if (text.contains("你现在是观察者状态. 按E打开菜单.", true)) {
                        KillAura.state = false
                        Velocity.state = false
                        ChestStealer.state = false
                        InventoryCleaner.state = false
                        AutoArmor.state = false
                        Recorder.win++

                        LiquidBounce.hud.addAutoPlayHud(AutoPlayHuds("你赢得了本场游戏胜利 | 胜利数:"+Recorder.win,net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType.INFO))
                        LiquidBounce.hud.addNotification(Notification("AutoPlay","Game Over", NotifyType.INFO, 1000))
                        if (ClientName.get()){mc.thePlayer!!.sendChatMessage("@["+LiquidBounce.CLIENT_NAME+"]GG")}
                        else{mc.thePlayer!!.sendChatMessage("@GG")}
                    }
                }
                "huayuting16" -> {
                    if (text.contains("[起床战争] Game 结束！感谢您的参与！", true)) {
                        LiquidBounce.hud.addNotification(Notification(name,"Game Over", NotifyType.INFO))
                        LiquidBounce.hud.addNotification(Notification("Warning","KillAura was disabled, because game has ended", NotifyType.WARNING))
                        if (ClientName.get()){
                            mc.thePlayer!!.sendChatMessage("@["+LiquidBounce.CLIENT_NAME+"]GG")}
                        else{mc.thePlayer!!.sendChatMessage("@GG")}
                        Recorder.win++

                        LiquidBounce.hud.addAutoPlayHud(AutoPlayHuds("你赢得了本场游戏胜利 | 胜利数:"+Recorder.win.toString(),net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType.INFO))
                        if (autodis.get()) {
                            KillAura.state = false
                            Velocity.state = false
                        }
                    }
                }
            }
        }
    }

    private fun queueAutoPlay(runnable: () -> Unit) {
        if (queued) {
            return
        }
        queued = true
        if (this.state) {
            Timer().schedule(delayValue.get().toLong() * 1000) {
                if (state) {
                    runnable()
                }
            }

            //play sound when everything done
            LiquidBounce.hud.addNotification(Notification(name,"Sending you to next game in ${delayValue.get()}s...", NotifyType.INFO))
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        clicking = false
        clickState = 0
        queued = false
    }

    override val tag: String
        get() = modeValue.get()

    override fun handleEvents() = true
}
