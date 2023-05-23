/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.*
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit.AAC4Hop
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit.AAC4SlowHop
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.*
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.*
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan.SpartanYPort
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreBHop
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreLowHop
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreOnGround
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.server.SPacketPlayerPosLook

@ModuleInfo(name = "Speed", description = "允许你更快移动", category = ModuleCategory.MOVEMENT, Chinese = "加速")
class Speed : Module() {
    private val speedModes = arrayOf( // NCP
        AAC4Hop(),
        AAC4SlowHop(),
        NCPBHop(),
        NCPFHop(),
        SNCPBHop(),
        NCPHop(),
        YPort(),
        YPort2(),
        NCPYPort(),
        Boost(),
        Frame(),
        MiJump(),
        OnGround(),  // AAC
        HYTHop(),
        AACBHop(),
        AAC2BHop(),
        AAC3BHop(),
        AAC4BHop(),
        AAC5BHop(),
        AAC6BHop(),
        AAC7BHop(),
        AACHop3313(),
        AACHop350(),
        AACLowHop(),
        AACLowHop2(),
        AACLowHop3(),
        AACGround(),
        AACGround2(),
        AACYPort(),
        AACYPort2(),
        AACPort(),
        OldAACBHop(),  // Spartan
        SpartanYPort(),  // Spectre
        SpectreLowHop(),
        SpectreBHop(),
        SpectreOnGround(),
        TeleportCubeCraft(),  // Server
        HiveHop(),
        HypixelHop(),
        MineplexGround(),  // Other
        SlowHop(),
        CustomSpeed()
    )

    val modeValue: ListValue = object : ListValue("Mode", modes, "NCPBHop") {
        override fun onChange(oldValue: String, newValue: String) {
            if (state)
                onDisable()
        }

        override fun onChanged(oldValue: String, newValue: String) {
            if (state)
                onEnable()
        }
    }
    val customSpeedValue = FloatValue("CustomSpeed", 1.6f, 0.2f, 2f)
    val customYValue = FloatValue("CustomY", 0f, 0f, 4f)
    val customTimerValue = FloatValue("CustomTimer", 1f, 0.1f, 2f)
    val customStrafeValue = BoolValue("CustomStrafe", true)
    val resetXZValue = BoolValue("CustomResetXZ", false)
    val resetYValue = BoolValue("CustomResetY", false)
    val portMax = FloatValue("AAC-PortLength", 1f, 1f, 20f)
    val aacGroundTimerValue = FloatValue("AACGround-Timer", 3f, 1.1f, 10f)
    val cubecraftPortLengthValue = FloatValue("CubeCraft-PortLength", 1f, 0.1f, 2f)
    val mineplexGroundSpeedValue = FloatValue("MineplexGround-Speed", 0.5f, 0.1f, 1f)

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.sneaking)
            return

        if (MovementUtils.isMoving) {
            thePlayer.sprinting = true
        }

        mode?.onUpdate()
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.sneaking || event.eventState != EventState.PRE)
            return

        if (MovementUtils.isMoving)
            thePlayer.sprinting = true

        mode?.onMotion()
    }

    @EventTarget
    fun onMove(event: MoveEvent?) {
        if (mc.thePlayer!!.sneaking)
            return
        mode?.onMove(event!!)
    }

    @EventTarget
    fun onTick(event: TickEvent?) {
        if (mc.thePlayer!!.sneaking)
            return

        mode?.onTick()
    }

    override fun onEnable() {
        if (mc.thePlayer == null)
            return

        mc.timer.timerSpeed = 1f

        mode?.onEnable()
    }

    override fun onDisable() {
        if (mc.thePlayer == null)
            return

        mc.timer.timerSpeed = 1f

        mode?.onDisable()
    }

    override val tag: String
        get() = modeValue.get()

    private val mode: SpeedMode?
        get() {
            val mode = modeValue.get()

            for (speedMode in speedModes) if (speedMode.modeName.equals(mode, ignoreCase = true))
                return speedMode

            return null
        }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (event.isCancelled && event.packet is SPacketPlayerPosLook){
            onDisable()
            LiquidBounce.hud.addNotification(Notification("Information","Speed was disabled to prevent flags/erros",NotifyType.WARNING))
        }
    }

    private val modes: Array<String>
        get() {
            val list: MutableList<String> = ArrayList()
            for (speedMode in speedModes) list.add(speedMode.modeName)
            return list.toTypedArray()
        }
}