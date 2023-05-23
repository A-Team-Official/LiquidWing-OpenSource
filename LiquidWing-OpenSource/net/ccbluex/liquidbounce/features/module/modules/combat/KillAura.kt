/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import ad.utils.AnimationUtils
import ad.utils.Color.modules.CustomUI
import ad.utils.Color.modules.Rainbow
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.LiquidBounce.moduleManager
import net.ccbluex.liquidbounce.api.enums.EnumFacingType
import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.hyt.CancelC03
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams
import net.ccbluex.liquidbounce.features.module.modules.player.Blink
import net.ccbluex.liquidbounce.features.module.modules.render.Animations
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.injection.backend.Backend
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.extensions.isAnimal
import net.ccbluex.liquidbounce.utils.extensions.isClientFriend
import net.ccbluex.liquidbounce.utils.extensions.isMob
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TimeUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.Block
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.init.Blocks
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.MathHelper
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.Cylinder
import java.awt.Color
import java.awt.Robot
import java.awt.event.InputEvent
import java.util.*
import kotlin.math.*

// 弱智killaura by 7ad
@ModuleInfo(name = "Killaura", description = "Automatically attacks targets around you.",
    category = ModuleCategory.COMBAT, Chinese = "杀戮光环", keyBind = Keyboard.KEY_R)
class KillAura : Module() {

    /**
     * OPTIONS
     */

    // CPS - Attack speed
    private val maxCPS: IntegerValue = object : IntegerValue("MaxCPS", 8, 1, 20) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = minCPS.get()
            if (i > newValue) set(i)

            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), this.get())
        }
    }

    private val minCPS: IntegerValue = object : IntegerValue("MinCPS", 5, 1, 20) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = maxCPS.get()
            if (i < newValue) set(i)

            attackDelay = TimeUtils.randomClickDelay(this.get(), maxCPS.get())
        }
    }

    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val cooldownValue = FloatValue("Cooldown", 1f, 0f, 1f)
    private val switchDelayValue = IntegerValue("SwitchDelay", 700, 0, 2000)

    // Range
    val rangeValue = FloatValue("Range", 3.7f, 1f, 8f)
    private val rinrf = FloatValue("Air-Range", 3.0f, 1f, 8f)
    private val c03r = FloatValue("C03-Range", 5.0f, 1f, 8f)
    private val grinrf = FloatValue("Ground-Range", 3.0f, 1f, 8f)
    private val rangefix = BoolValue("RangeFix", true)
    private val atc03 = BoolValue("AutoC03", false)
    private val rsend = BoolValue("RangeFix-Debug",false)

    //    private val nc03rg = FloatValue("NoC03-Ka-Range", 3.7f, 1f, 8f)
//    private val nc03debug = BoolValue("NoC03-Debug", false)
    private val throughWallsRangeValue = FloatValue("ThroughWallsRange", 3f, 0f, 8f)
    private val rangeSprintReducementValue = FloatValue("RangeSprintReducement", 0f, 0f, 0.4f)

    // Modes
    private val priorityValue = ListValue("Priority", arrayOf("Health", "Distance", "Direction", "LivingTime"), "Distance")
    private val targetModeValue = ListValue("TargetMode", arrayOf("Single", "Switch", "Multi"), "Switch")

    // Bypass
    private val swingValue = BoolValue("Swing", true)
    public val keepSprintValue = BoolValue("KeepSprint", true)
    private val stopSprintValue = BoolValue("StopSprintOnAir", true)
    val jump = BoolValue("Jump-Velocity",true)



    // AutoBlock
    private val delayedBlockValue = BoolValue("DelayedBlock", true)
    private val blockModeValue = ListValue("BlockMode", arrayOf("None","LiquidWing","LiquidWingPacket", "C07C08", "C07", "Packet", "Fake", "Mouse", "GameSettings", "UseItem","Real" ), "Packet")
    private val blockRate = IntegerValue("BlockRate", 100, 1, 100)

    // Raycast
    private val raycastValue = BoolValue("RayCast", true)
    private val raycastIgnoredValue = BoolValue("RayCastIgnored", false)
    private val livingRaycastValue = BoolValue("LivingRayCast", true)
    // Bypass
//    private val atnc03 = BoolValue("AutoNoc03", false)

    private val aacValue = BoolValue("AAC", false)

    // Turn Speed
    private val maxTurnSpeed: FloatValue = object : FloatValue("MaxTurnSpeed", 180f, 0f, 180f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minTurnSpeed.get()
            if (v > newValue) set(v)
        }
    }

    private val minTurnSpeed: FloatValue = object : FloatValue("MinTurnSpeed", 180f, 0f, 180f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxTurnSpeed.get()
            if (v < newValue) set(v)
        }
    }

    // Modes
    private val rotations = ListValue("RotationMode", arrayOf("Vanilla","BackTrack", "LiquidSensePlus","Test","Test2", "Test3","Test4","LockView","HytRotation"), "Test")

    private val silentRotationValue = BoolValue("SilentRotation", true)
     val rotationStrafeValue = ListValue("Strafe", arrayOf("Off", "Strict", "Silent","HYT"), "Off")
    private val randomCenterValue = BoolValue("RandomCenter", true)
    val silentFixVaule = BoolValue("Silent", true)
    private val outborderValue = BoolValue("Outborder", false)
    private val fovValue = FloatValue("FOV", 180f, 0f, 180f)

    // Predict
    private val predictValue = BoolValue("Predict", true)

    private val maxPredictSize: FloatValue = object : FloatValue("MaxPredictSize", 1f, 0.1f, 5f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minPredictSize.get()
            if (v > newValue) set(v)
        }
    }

    private val minPredictSize: FloatValue = object : FloatValue("MinPredictSize", 1f, 0.1f, 5f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxPredictSize.get()
            if (v < newValue) set(v)
        }
    }

    // Lighting
    private val lightingValue = BoolValue("Lighting", false)
    private val lightingModeValue = ListValue("Lighting-Mode", arrayOf("Dead", "Attack"), "Dead")
    private val lightingSoundValue = BoolValue("Lighting-Sound", true)
    // Bypass
    private val failRateValue = FloatValue("FailRate", 0f, 0f, 100f)
    private val fakeSwingValue = BoolValue("FakeSwing", true)
    private val noInventoryAttackValue = BoolValue("NoInvAttack", false)
    private val noInventoryDelayValue = IntegerValue("NoInvDelay", 200, 0, 500)
    private val limitedMultiTargetsValue = IntegerValue("LimitedMultiTargets", 0, 0, 50)

    // Visuals
    private val hiteffect = ListValue("HitEffect", arrayOf("Criticals","Blood","Fire","Water","Smoke","Flame","Heart","None"),"None")
    private val markValue = ListValue("Mark", arrayOf("Liquid","Box","Rise","FDP","Block","Jello", "Plat", "Red", "Sims", "None"),"Rise")
    private val markValue2 = ListValue("Mark2", arrayOf("None","Block","Rise","Box"),"None")
    private val colorModeValue =
        ListValue("JelloColor", arrayOf("Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Health", "Gident"), "Gident")
    private val colorRedValue = IntegerValue("JelloRed", 255, 0, 255)
    private val colorGreenValue = IntegerValue("JelloGreen", 255, 0, 255)
    private val colorBlueValue = IntegerValue("JelloBlue", 255, 0, 255)

    private val colorAlphaValue = IntegerValue("JelloAlpha", 255, 0, 255)
    private val saturationValue = FloatValue("Saturation", 1f, 0f, 1f)
    private val brightnessValue = FloatValue("Brightness", 1f, 0f, 1f)

    private val colorTeam = BoolValue("JelloTeam", false)

    private val jelloAlphaValue =
        FloatValue("JelloEndAlphaPercent", 0.4f, 0f, 1f)
    private val jelloWidthValue =
        FloatValue("JelloCircleWidth", 3f, 0.01f, 5f)
    private val jelloGradientHeightValue =
        FloatValue("JelloGradientHeight", 8f, 1f, 12f)
    private val jelloFadeSpeedValue =
        FloatValue("JelloFadeSpeed", 0.1f, 0.01f, 0.5f)
    private val fakeSharpValue = BoolValue("FakeSharp", true)
    private val circleValue=BoolValue("Circle",true)
    private val circletargetValue = BoolValue("CircleTarget",true)
    private val circleRed = IntegerValue("CircleRed", 255, 0, 255)
    private val circleGreen = IntegerValue("CircleGreen", 255, 0, 255)
    private val circleBlue = IntegerValue("CircleBlue", 255, 0, 255)
    private val circleAlpha = IntegerValue("CircleAlpha", 255, 0, 255)
    private val circleAccuracy = IntegerValue("CircleAccuracy", 15, 0, 60)
    private val circleThicknessValue = FloatValue("CircleSize", 2F, 1F, 5F)


    /**
     * MODULE
     */

    // Target
    private var lastTarget: IEntityLivingBase? = null
    private var direction = 1.0
    private var direction2 = true;
    private var lastDeltaMS = 0L
    private var al = 0f
    var target: IEntityLivingBase? = null
     var currentTarget: IEntityLivingBase? = null
    private var hitable = false
    private val prevTargetEntities = mutableListOf<Int>()
    var syncEntity: IEntityLivingBase? = null

    // Attack delay
    private var bb: IAxisAlignedBB? = null
    private var entity: IEntityLivingBase? = null
    private var yPos2 = 0F
    private var yPos = 0.0
    private  var progress:kotlin.Double = 0.0
    private var lastMS = System.currentTimeMillis()
    private val attackTimer = MSTimer()
    private val switchTimer = MSTimer()
    private var attackDelay = 0L
    private var clicks = 0
    var killCounts = 0
    // Container Delay
    private var containerOpen = -1L

    // Fake block status
    var blockingStatus = false
    var blocking = false
    var silentFix = false
    var doFix = false
    var isOverwrited = false
    /**
     * Enable kill aura module
     */
    override fun onEnable() {
        if (atc03.get())
        {
            val c03 = moduleManager.getModule(
                CancelC03::class.java
            ) as CancelC03
            c03.state = true
            val velocity = moduleManager[Velocity::class.java] as Velocity
            velocity.modeValue.set("Simple")
        }
        mc.thePlayer ?: return
        mc.theWorld ?: return
//        if (atnc03.get()){
//        val nc03 = moduleManager.getModule(
//            NoC03::class.java
//        ) as NoC03
//        nc03.state = true}
        updateTarget()
    }
    /**
     * Disable kill aura module
     */
    override fun onDisable() {
        if (atc03.get())
        {
            val c03 = moduleManager.getModule(
                CancelC03::class.java
            ) as CancelC03
            c03.state = false
            val velocity = moduleManager[Velocity::class.java] as Velocity
            velocity.modeValue.set("LiQuidWIng")
        }
        lastTarget = null
        target = null
        currentTarget = null
        hitable = false
        prevTargetEntities.clear()
        attackTimer.reset()
        clicks = 0
        doFix = false

//        if (atnc03.get()){
//            val nc03 = moduleManager.getModule(
//                NoC03::class.java
//            ) as NoC03
//            nc03.state = false}
        stopBlocking()
    }

    /**
     * Motion event
     */
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (this.stopSprintValue.get()) {
            if (mc.thePlayer!!.onGround) {
                this.keepSprintValue.set(true)
            } else {
                this.keepSprintValue.set(false)
            }
        }
        if (event.eventState == EventState.POST) {
            target ?: return
            currentTarget ?: return

            if (blockModeValue.get().equals("LiquidWingPacket",true) && canBlock && currentTarget!= null)  {
                mc.gameSettings.keyBindUseItem.pressed = true
                blocking = true
            }else{
                blocking = false

            }
            // Update hitable
            updateHitable()

            // AutoBlock
            if (!blockModeValue.get().equals("None", true)  && canBlock)
                startBlocking(currentTarget!!)

            return
        }
        if (event.eventState == EventState.PRE) {
            if (blockModeValue.get().equals("LiquidWing",true) && canBlock && currentTarget!= null) {
                mc.gameSettings.keyBindUseItem.pressed = true
                blocking = true
            }else{
                blocking = false
            }
        }
        if (rotationStrafeValue.get().equals("Off", true))
            update()

    }

    fun update() {
        if (cancelRun || (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
                    System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())))
            return

        // Update target
        updateTarget()

        if (target == null) {
            stopBlocking()
            return
        }

        // Target
        currentTarget = target

        if (!targetModeValue.get().equals("Switch", ignoreCase = true) && isEnemy(currentTarget))
            target = currentTarget
    }

    /**
     * Update event
     */
    var sbsend = false
    var sbsend2 = false
    var sbsend3 = false
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!isOverwrited) {
            silentFix = silentFixVaule.get()
            doFix = true
        }
        if (mc.thePlayer!!.isAirBorne && rangefix.get() && !atc03.get()) {
            rangeValue.set(rinrf.get())
            sbsend = false
            if(rsend.get()&&rangeValue!=rinrf&&mc.thePlayer!!.isAirBorne&&sbsend==false){
                ClientUtils.displayChatMessage("[ Rangefix ] Your range is set( "+rinrf.value.toString() + " )")
                sbsend = true
            }
        }
        if (mc.thePlayer!!.onGround && atc03.get()) {
            rangeValue.set(c03r.get())
        }
        if(mc.thePlayer!!.onGround && rangefix.get() && !atc03.get()){
            rangeValue.set(grinrf.get())
            sbsend2 = false
            if(rsend.get()&& rangeValue!=grinrf&&mc.thePlayer!!.onGround&&sbsend2==false){
                ClientUtils.displayChatMessage("[ Rangefix ] Your range is set( "+grinrf.value.toString() + " )")}
            sbsend2 = true
        }
        when(rotationStrafeValue.get().toLowerCase()){
            "hyt"->{
                applyForceStrafe(true, true)
            }
        }
//        if (atnc03.get()) {
//            rangeValue.set(nc03rg.get())
//            sbsend3 = false
//            if(nc03debug.get()&& rangeValue!=nc03rg&&sbsend3==false){
//                ClientUtils.displayChatMessage("[ NoC03-Ka-Range ] Your range is set( "+nc03rg.value.toString() + " )")}
//            sbsend3 = true
//        }

        if (lightingValue.get()) {
            when (lightingModeValue.get().toLowerCase()) {
                "dead" -> {
                    if (target != null) {
                        lastTarget = if (lastTarget == null) {
                            target
                        } else {
                            if (lastTarget!!.health <= 0) {
                                mc.netHandler2.handleSpawnGlobalEntity(
                                    SPacketSpawnGlobalEntity(
                                        EntityLightningBolt(mc2.world,
                                            lastTarget!!.posX, lastTarget!!.posY, lastTarget!!.posZ, true)
                                    )
                                )
                                if (lightingSoundValue.get()) mc.soundHandler.playSound("entity.lightning.impact", 0.5f)
                            } //ambient.weather.thunder
                            target
                        }
                    } else {
                        if (lastTarget != null && lastTarget!!.health <= 0) {
                            mc.netHandler2.handleSpawnGlobalEntity(
                                SPacketSpawnGlobalEntity(
                                    EntityLightningBolt(mc2.world,
                                        lastTarget!!.posX, lastTarget!!.posY, lastTarget!!.posZ, true)
                                )
                            )
                            if (lightingSoundValue.get()) mc.soundHandler.playSound("entity.lightning.impact", 0.5f)
                            lastTarget = target
                        }
                    }
                }

                "attack" -> {
                    mc.netHandler2.handleSpawnGlobalEntity(
                        SPacketSpawnGlobalEntity(
                            EntityLightningBolt(mc2.world,
                                target!!.posX, target!!.posY, target!!.posZ, true)
                        )
                    )
                    if (lightingSoundValue.get()) mc.soundHandler.playSound("entity.lightning.impact", 0.5f)
                }
            }
        }

        if (syncEntity != null && syncEntity!!.isDead) {
            ++killCounts
            syncEntity = null
        }
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            return
        }
        if (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
                    System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
            target = null
            currentTarget = null
            hitable = false
            if (classProvider.isGuiContainer(mc.currentScreen)) containerOpen = System.currentTimeMillis()
            return
        }
        if (target != null && currentTarget != null && (Backend.MINECRAFT_VERSION_MINOR == 8 || mc.thePlayer!!.getCooledAttackStrength(0.0F) >= cooldownValue.get())) {
            while (clicks > 0) {
                runAttack()
                clicks--
            }
        }
    }
    fun applyForceStrafe(isSilent: Boolean, runStrafeFix: Boolean) {
        silentFix = isSilent
        doFix = runStrafeFix
        isOverwrited = true
    }
    /**
     * Strafe event
     */
    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if (rotationStrafeValue.get().equals("Off", true))
            return
        update()
        if (currentTarget != null && RotationUtils.targetRotation != null) {
            when (rotationStrafeValue.get().toLowerCase()) {
                "strict" -> {
                    val (yaw) = RotationUtils.targetRotation ?: return
                    var strafe = event.strafe
                    var forward = event.forward
                    val friction = event.friction

                    var f = strafe * strafe + forward * forward

                    if (f >= 1.0E-4F) {
                        f = sqrt(f)

                        if (f < 1.0F)
                            f = 1.0F

                        f = friction / f
                        strafe *= f
                        forward *= f

                        val yawSin = sin((yaw * Math.PI / 180F).toFloat())
                        val yawCos = cos((yaw * Math.PI / 180F).toFloat())

                        val player = mc.thePlayer!!

                        player.motionX += strafe * yawCos - forward * yawSin
                        player.motionZ += forward * yawCos + strafe * yawSin
                    }
                    event.cancelEvent()
                }
                "silent" -> {
                    update()

                    RotationUtils.targetRotation.applyStrafeToPlayer(event)
                    event.cancelEvent()
                }

            }
        }
    }
    fun updateOverwrite() {
        isOverwrited = false
        doFix = state
        silentFix = silentFixVaule.get()
    }
    fun runStrafeFixLoop(isSilent: Boolean, event: StrafeEvent) {
        if (event.isCancelled) {
            return
        }
        val (yaw) = RotationUtils.targetRotation ?: return
        var strafe = event.strafe
        var forward = event.forward
        var friction = event.friction
        var factor = strafe * strafe + forward * forward

        var angleDiff = ((MathHelper.wrapDegrees(mc.thePlayer!!.rotationYaw - yaw - 22.5f - 135.0f) + 180.0).toDouble() / (45.0).toDouble()).toInt()
        //alert("Diff: " + angleDiff + " friction: " + friction + " factor: " + factor);
        var calcYaw = if(isSilent) { yaw + 45.0f * angleDiff.toFloat() } else yaw

        var calcMoveDir = Math.max(Math.abs(strafe), Math.abs(forward)).toFloat()
        calcMoveDir = calcMoveDir * calcMoveDir
        var calcMultiplier = MathHelper.sqrt(calcMoveDir / Math.min(1.0f, calcMoveDir * 2.0f))

        if (isSilent) {
            when (angleDiff) {
                1, 3, 5, 7, 9 -> {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier
                    }
                }
            }
        }
        if (factor >= 1.0E-4F) {
            factor = MathHelper.sqrt(factor)

            if (factor < 1.0F) {
                factor = 1.0F
            }

            factor = friction / factor
            strafe *= factor
            forward *= factor

            val yawSin = MathHelper.sin((calcYaw * Math.PI / 180F).toFloat())
            val yawCos = MathHelper.cos((calcYaw * Math.PI / 180F).toFloat())

            mc.thePlayer!!.motionX += strafe * yawCos - forward * yawSin
            mc.thePlayer!!.motionZ += forward * yawCos + strafe * yawSin
        }
        event.cancelEvent()
    }
    /**
     * Render event
     */
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        fun post3D() {
            GL11.glDepthMask(true)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
            GL11.glColor4f(1f, 1f, 1f, 1f)
        }
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            return
        }
        fun drawCircle2(entity: IEntity, partialTicks: Float, pos: Float) {
            GL11.glPushMatrix()
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glShadeModel(7425)
            GL11.glLineWidth(1f)
            val x =
                entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks.toDouble() - mc.renderManager.viewerPosX
            val y =
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks.toDouble() - mc.renderManager.viewerPosY + pos
            val z =
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks.toDouble() - mc.renderManager.viewerPosZ
            GL11.glBegin(GL11.GL_LINE_STRIP)
            for (i in 0..180) {
                val c1 = i * Math.PI * 2 / 180
                GlStateManager.color(2f, 1f, 1f, 1f)
                GL11.glVertex3d(x + 0.5 * Math.cos(c1), y, z + 0.5 * Math.sin(c1))
            }
            GL11.glEnd()
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glShadeModel(7424)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
        }
        fun drawCircle(
            x: Double,
            y: Double,
            z: Double,
            width: Float,
            radius: Double,
            red: Float,
            green: Float,
            blue: Float,
            alp: Float
        ) {
            GL11.glLineWidth(width)
            GL11.glBegin(GL11.GL_LINE_LOOP)
            GL11.glColor4f(red, green, blue, alp)
            var i = 0
            while (i <= 360) {
                val posX = x - Math.sin(i * Math.PI / 180) * radius
                val posZ = z + Math.cos(i * Math.PI / 180) * radius
                GL11.glVertex3d(posX, y, posZ)
                i += 1
            }
            GL11.glEnd()
        }

        fun pre3D() {
            GL11.glPushMatrix()
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glShadeModel(GL11.GL_SMOOTH)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LIGHTING)
            GL11.glDepthMask(false)
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
            GL11.glDisable(2884)
        }

        fun getColor(ent: IEntityLivingBase?): Color? {
            val counter = intArrayOf(0)
            if (ent is EntityLivingBase) {
                val entityLivingBase = ent

                if (colorModeValue.get().equals("Health", ignoreCase = true)) return BlendUtils.getHealthColor(
                    entityLivingBase.health,
                    entityLivingBase.maxHealth
                )
                if (colorTeam.get()) {

                    val chars = entityLivingBase.displayName!!.formattedText.toCharArray()
                    var color = Int.MAX_VALUE
                    for (i in chars.indices) {
                        if (chars[i] != '§' || i + 1 >= chars.size) continue
                        val index = GameFontRenderer.getColorIndex(chars[i + 1])
                        if (index < 0 || index > 15) continue
                        color = ColorUtils.hexColors[index]
                        break
                    }
                    return Color(color)
                }
            }
            val hud = LiquidBounce.moduleManager.getModule(
                HUD::class.java
            ) as HUD
            return when (colorModeValue.get()) {
                "Gident" -> RenderUtils.getGradientOffset(Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get()),
                    Color (CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get()),   (Math.abs(
                        System.currentTimeMillis() / hud.gradientSpeed.get().toDouble()
                    )))

                "Custom" -> Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
                "Rainbow" -> ColorUtils.hslRainbow(counter[0] * 100 + 1, indexOffset = 100 * Rainbow.rainbowSpeed.get())

                "Sky" -> RenderUtils.skyRainbow(0, saturationValue.get(), brightnessValue.get())
                "LiquidSlowly" -> ColorUtils.LiquidSlowly(
                    System.nanoTime(),
                    0,
                    saturationValue.get(),
                    brightnessValue.get()
                )
                else -> ColorUtils.fade(Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), 0, 100)
            }
        }
        if (circletargetValue.get()) {
            if (LiquidBounce.combatManager.target != null) {
                GL11.glPushMatrix()
                GL11.glTranslated(
                    LiquidBounce.combatManager.target!!.lastTickPosX + (LiquidBounce.combatManager.target!!.posX - LiquidBounce.combatManager.target!!.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    LiquidBounce.combatManager.target!!.lastTickPosY + (LiquidBounce.combatManager.target!!.posY - LiquidBounce.combatManager.target!!.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY,
                    LiquidBounce.combatManager.target!!.lastTickPosZ + (LiquidBounce.combatManager.target!!.posZ - LiquidBounce.combatManager.target!!.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(circleThicknessValue.get())
                GL11.glColor4f(
                    circleRed.get().toFloat() / 255.0F,
                    circleGreen.get().toFloat() / 255.0F,
                    circleBlue.get().toFloat() / 255.0F,
                    circleAlpha.get().toFloat() / 255.0F
                )
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 61 - circleAccuracy.get()) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(
                        cos(i * Math.PI / 180.0).toFloat() * 2,
                        (sin(i * Math.PI / 180.0).toFloat() * 2)
                    )
                }
                GL11.glVertex2f(
                    cos(360 * Math.PI / 180.0).toFloat() * 2,
                    (sin(360 * Math.PI / 180.0).toFloat() * 2)
                )

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
            }
        }
        if (circleValue.get()) {
            GL11.glPushMatrix()
            GL11.glTranslated(
                mc.thePlayer!!.lastTickPosX + (mc.thePlayer!!.posX - mc.thePlayer!!.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                mc.thePlayer!!.lastTickPosY + (mc.thePlayer!!.posY - mc.thePlayer!!.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY,
                mc.thePlayer!!.lastTickPosZ + (mc.thePlayer!!.posZ - mc.thePlayer!!.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
            )
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            GL11.glLineWidth(circleThicknessValue.get())
            GL11.glColor4f(
                circleRed.get().toFloat() / 255.0F,
                circleGreen.get().toFloat() / 255.0F,
                circleBlue.get().toFloat() / 255.0F,
                circleAlpha.get().toFloat() / 255.0F
            )
            GL11.glRotatef(90F, 1F, 0F, 0F)
            GL11.glBegin(GL11.GL_LINE_STRIP)

            for (i in 0..360 step 61 - circleAccuracy.get()) { // You can change circle accuracy  (60 - accuracy)
                GL11.glVertex2f(
                    cos(i * Math.PI / 180.0).toFloat() * rangeValue.get(),
                    (sin(i * Math.PI / 180.0).toFloat() * rangeValue.get())
                )
            }
            GL11.glVertex2f(
                cos(360 * Math.PI / 180.0).toFloat() * rangeValue.get(),
                (sin(360 * Math.PI / 180.0).toFloat() * rangeValue.get())
            )

            GL11.glEnd()

            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)

            GL11.glPopMatrix()
        }
        if (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
                    System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())
        ) {
            target = null
            currentTarget = null
            hitable = false
            if (classProvider.isGuiContainer(mc.currentScreen)) containerOpen = System.currentTimeMillis()
            return
        }

        target ?: return

        if (this.markValue.get().toLowerCase().equals("box") && !targetModeValue.get()
                .equals("Multi", ignoreCase = true)
        )
            RenderUtils.drawPlatform(
                target,
                if (target!!.hurtTime > 3) Color(255, 55, 55, 70) else Color(255, 0, 0, 70)
            )
        if (this.markValue.get().toLowerCase().equals("box") && !targetModeValue.get()
                .equals("Single", ignoreCase = true)
        )
            RenderUtils.drawPlatform(
                target,
                if (target!!.hurtTime > 3) Color(255, 55, 55, 70) else Color(255, 0, 0, 70)
            )
        if (this.markValue.get().toLowerCase().equals("box") && !targetModeValue.get()
                .equals("Switch", ignoreCase = true)
        )
            RenderUtils.drawPlatform(
                target,
                if (target!!.hurtTime > 3) Color(255, 55, 55, 70) else Color(255, 0, 0, 70)
            )
        if (this.markValue2.get().toLowerCase().equals("box") && !targetModeValue.get()
                .equals("Multi", ignoreCase = true)
        )
            RenderUtils.drawPlatform(
                target,
                if (target!!.hurtTime > 3) Color(255, 55, 55, 70) else Color(255, 0, 0, 70)
            )
        if (this.markValue2.get().toLowerCase().equals("box") && !targetModeValue.get()
                .equals("Single", ignoreCase = true)
        )
            RenderUtils.drawPlatform(
                target,
                if (target!!.hurtTime > 3) Color(255, 55, 55, 70) else Color(255, 0, 0, 70)
            )
        if (this.markValue2.get().toLowerCase().equals("box") && !targetModeValue.get()
                .equals("Switch", ignoreCase = true)
        )
            RenderUtils.drawPlatform(
                target,
                if (target!!.hurtTime > 3) Color(255, 55, 55, 70) else Color(255, 0, 0, 70)
            )
        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
        if (this.markValue.get().toLowerCase().equals("rise") && targetModeValue.get()
                .equals("Switch", ignoreCase = true)
        ) {
            RenderUtils.drawCircle(target, 0.67, Color(159, 24, 242).rgb, true)
        }
        if (this.markValue.get().toLowerCase().equals("rise") && targetModeValue.get()
                .equals("Single", ignoreCase = true)
        ) {
            RenderUtils.drawCircle(target, 0.67, Color(159, 24, 242).rgb, true)
        }
        if (this.markValue2.get().toLowerCase().equals("rise") && targetModeValue.get()
                .equals("Switch", ignoreCase = true)
        ) {
            RenderUtils.drawCircle(target, 0.67, Color(159, 24, 242).rgb, true)
        }
        if (this.markValue2.get().toLowerCase().equals("rise") && targetModeValue.get()
                .equals("Single", ignoreCase = true)
        ) {
            RenderUtils.drawCircle(target, 0.67, Color(159, 24, 242).rgb, true)
        }
        if (currentTarget != null && attackTimer.hasTimePassed(attackDelay) &&
            currentTarget!!.hurtTime <= hurtTimeValue.get()
        ) {
            clicks++
            attackTimer.reset()
            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), maxCPS.get())
        }
        if (noInventoryAttackValue.get() && (classProvider.isGuiContainer(mc.currentScreen) ||
                    System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
            target = null
            currentTarget = null
            hitable = false
            if (classProvider.isGuiContainer(mc.currentScreen)) containerOpen = System.currentTimeMillis()
            return
        }

        target ?: return

        val ent = classProvider.createEntityLightningBolt(mc.theWorld!!, target!!.posX, target!!.posY, target!!.posZ, false)

        when(hiteffect.get().toLowerCase()){
            "flame" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.FLAME)
            "smoke" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.SMOKE_LARGE)
            "heart" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.HEART)
            "fire" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.LAVA)
            "water" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.WATER_DROP)
            "criticals" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.CRIT)
            "blood" ->{
                repeat(10) {
                    mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.particleID, target!!.posX, target!!.posY + target!!.height / 2, target!!.posZ,
                        target!!.motionX + RandomUtils.nextFloat(-0.5f, 0.5f), target!!.motionY + RandomUtils.nextFloat(-0.5f, 0.5f), target!!.motionZ + RandomUtils.nextFloat(-0.5f, 0.5f), Block.getStateId(Blocks.REDSTONE_BLOCK.defaultState))
                }
            }
        }
        when (markValue2.get().toLowerCase()) {
            "block" -> {
                val bb = target!!.entityBoundingBox
                target!!.entityBoundingBox = bb.expand(0.2, 0.2, 0.2)
                RenderUtils.drawEntityBox(target!!, if (target!!.hurtTime <= 0) Color.GREEN else Color.RED, false)
                target!!.entityBoundingBox = bb
            }
        }
        when (markValue.get().toLowerCase()) {
            "liquid" -> {
                RenderUtils.drawPlatform(target!!, if (target!!.hurtTime <= 0) Color(37, 126, 255, 170) else Color(255, 0, 0, 170))
            }
            "plat" -> RenderUtils.drawPlatform(
                target!!,
                if (hitable) Color(37, 126, 255, 70) else Color(255, 0, 0, 70)
            )
            "block" -> {
                val bb = target!!.entityBoundingBox
                target!!.entityBoundingBox = bb.expand(0.2, 0.2, 0.2)
                RenderUtils.drawEntityBox(target!!, if (target!!.hurtTime <= 0) Color.GREEN else Color.RED, false)
                target!!.entityBoundingBox = bb
            }
            "red" -> {
                RenderUtils.drawPlatform(target!!, if (target!!.hurtTime <= 0) Color(255, 255, 255, 255) else Color(124, 215, 255, 255))
            }
            "LiquidWing" -> {
                drawShadow(target!!, event.partialTicks, yPos2, direction2)
                drawCircle2(target!!, event.partialTicks, yPos2)
            }
            "sims" -> {
                val radius = 0.15f
                val side = 4
                GL11.glPushMatrix()
                GL11.glTranslated(
                    target!!.lastTickPosX + (target!!.posX - target!!.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX,
                    (target!!.lastTickPosY + (target!!.posY - target!!.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + target!!.height * 1.1,
                    target!!.lastTickPosZ + (target!!.posZ - target!!.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                )
                GL11.glRotatef(-target!!.width, 0.0f, 1.0f, 0.0f)
                GL11.glRotatef((mc.thePlayer!!.ticksExisted + mc.timer.renderPartialTicks) * 5, 0f, 1f, 0f)
                RenderUtils.glColor(if (target!!.hurtTime <= 0) Color(80, 255, 80) else Color(255, 0, 0))
                RenderUtils.enableSmoothLine(1.5F)
                val c = Cylinder()
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f)
                c.draw(0F, radius, 0.3f, side, 1)
                c.drawStyle = 100012
                GL11.glTranslated(0.0, 0.0, 0.3)
                c.draw(radius, 0f, 0.3f, side, 1)
                GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f)
                GL11.glTranslated(0.0, 0.0, -0.3)
                c.draw(0F, radius, 0.3f, side, 1)
                GL11.glTranslated(0.0, 0.0, 0.3)
                c.draw(radius, 0F, 0.3f, side, 1)
                RenderUtils.disableSmoothLine()
                GL11.glPopMatrix()
            }
            "fdp" -> {
                val drawTime = (System.currentTimeMillis() % 1500).toInt()
                val drawMode = drawTime > 750
                var drawPercent = drawTime / 750.0
                //true when goes up
                if (!drawMode) {
                    drawPercent = 1 - drawPercent
                } else {
                    drawPercent -= 1
                }
                drawPercent = EaseUtils.easeInOutQuad(drawPercent)
                GL11.glPushMatrix()
                GL11.glDisable(3553)
                GL11.glEnable(2848)
                GL11.glEnable(2881)
                GL11.glEnable(2832)
                GL11.glEnable(3042)
                GL11.glBlendFunc(770, 771)
                GL11.glHint(3154, 4354)
                GL11.glHint(3155, 4354)
                GL11.glHint(3153, 4354)
                GL11.glDisable(2929)
                GL11.glDepthMask(false)

                val bb = target!!.entityBoundingBox
                val radius = (bb.maxX - bb.minX) + 0.3
                val height = bb.maxY - bb.minY
                val x = target!!.lastTickPosX + (target!!.posX - target!!.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                val y = (target!!.lastTickPosY + (target!!.posY - target!!.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + height * drawPercent
                val z = target!!.lastTickPosZ + (target!!.posZ - target!!.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                GL11.glLineWidth((radius * 5f).toFloat())
                GL11.glBegin(3)
                for (i in 0..360) {
                    val rainbow = Color(Color.HSBtoRGB((mc.thePlayer!!.ticksExisted / 70.0 + sin(i / 50.0 * 1.75)).toFloat() % 1.0f, 0.7f, 1.0f))
                    GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                    GL11.glVertex3d(x + radius * cos(i * 6.283185307179586 / 45.0), y, z + radius * sin(i * 6.283185307179586 / 45.0))
                }
                GL11.glEnd()

                GL11.glDepthMask(true)
                GL11.glEnable(2929)
                GL11.glDisable(2848)
                GL11.glDisable(2881)
                GL11.glEnable(2832)
                GL11.glEnable(3553)
                GL11.glPopMatrix()
            }
            "jello" -> {
                val lastY: Double = yPos
                fun easeInOutQuart(x: Double): Double {
                    return if (x < 0.5) 8 * x * x * x * x else 1 - Math.pow(-2 * x + 2, 4.0) / 2
                }
                if (al > 0f) {
                    if (System.currentTimeMillis() - lastMS >= 1000L) {
                        direction = -direction
                        lastMS = System.currentTimeMillis()
                    }
                    val weird: Long =
                        if (direction > 0) System.currentTimeMillis() - lastMS else 1000L - (System.currentTimeMillis() - lastMS)
                    progress = weird.toDouble() / 1000.0
                    lastDeltaMS = System.currentTimeMillis() - lastMS
                } else { // keep the progress
                    lastMS = System.currentTimeMillis() - lastDeltaMS
                }

                if (target != null) {
                    entity = target
                    bb = entity!!.entityBoundingBox
                }

                if (bb == null || entity == null) return

                val radius: Double = bb!!.maxX - bb!!.minX
                val height: Double = bb!!.maxY - bb!!.minY
                val posX: Double =
                    entity!!.lastTickPosX + (entity!!.posX - entity!!.lastTickPosX) * mc.timer.renderPartialTicks
                val posY: Double =
                    entity!!.lastTickPosY + (entity!!.posY - entity!!.lastTickPosY) * mc.timer.renderPartialTicks
                val posZ: Double =
                    entity!!.lastTickPosZ + (entity!!.posZ - entity!!.lastTickPosZ) * mc.timer.renderPartialTicks

                yPos = easeInOutQuart(progress) * height

                val deltaY: Double =
                    (if (direction > 0) yPos - lastY else lastY - yPos) * -direction * jelloGradientHeightValue.get()

                if (al <= 0 && entity != null) {
                    entity = null
                    return
                }

                val colour: Color? = getColor(entity)
                val r = colour!!.red / 255.0f
                val g = colour!!.green / 255.0f
                val b = colour!!.blue / 255.0f

                pre3D()
                //post circles
                //post circles
                GL11.glTranslated(
                    -mc.renderManager.viewerPosX,
                    -mc.renderManager.viewerPosY,
                    -mc.renderManager.viewerPosZ
                )

                GL11.glBegin(GL11.GL_QUAD_STRIP)

                for (i in 0..360) {
                    val calc = i * Math.PI / 180
                    val posX2 = posX - Math.sin(calc) * radius
                    val posZ2 = posZ + Math.cos(calc) * radius
                    GL11.glColor4f(r, g, b, 0f)
                    GL11.glVertex3d(posX2, posY + yPos + deltaY, posZ2)
                    GL11.glColor4f(r, g, b, al * jelloAlphaValue.get())
                    GL11.glVertex3d(posX2, posY + yPos, posZ2)
                }

                GL11.glEnd()

                drawCircle(posX, posY + yPos, posZ, jelloWidthValue.get(), radius, r, g, b, al)

                post3D()
//                val drawTime = (System.currentTimeMillis() % 2000).toInt()
//                val drawMode = drawTime > 1000
//                var drawPercent = drawTime / 1000.0
//
//                //true when goes up
//                if (!drawMode) {
//                    drawPercent = 1 - drawPercent
//                } else {
//                    drawPercent -= 1
//                }
//                drawPercent = EaseUtils.easeInOutQuad(drawPercent)
//                val points = mutableListOf<WVec3>()
//                val bb = target!!.entityBoundingBox
//                val radius = bb.maxX - bb.minX
//                val height = bb.maxY - bb.minY
//                val posX = target!!.lastTickPosX + (target!!.posX - target!!.lastTickPosX) * mc.timer.renderPartialTicks
//                var posY = target!!.lastTickPosY + (target!!.posY - target!!.lastTickPosY) * mc.timer.renderPartialTicks
//
//                if (drawMode) {
//                    posY -= 0.5
//                } else {
//                    posY += 0.5
//                }
//                val posZ = target!!.lastTickPosZ + (target!!.posZ - target!!.lastTickPosZ) * mc.timer.renderPartialTicks
//                for (i in 0..360 step 7) {
//                    points.add(WVec3(posX - sin(i * Math.PI / 180F) * radius, posY + height * drawPercent, posZ + cos(i * Math.PI / 180F) * radius))
//                }
//                points.add(points[0])
//                //draw
//                mc.entityRenderer.disableLightmap()
//                GL11.glPushMatrix()
//                GL11.glDisable(GL11.GL_TEXTURE_2D)
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
//                GL11.glEnable(GL11.GL_LINE_SMOOTH)
//                GL11.glEnable(GL11.GL_BLEND)
//                GL11.glDisable(GL11.GL_DEPTH_TEST)
//                GL11.glBegin(GL11.GL_LINE_STRIP)
//                val baseMove = (if (drawPercent > 0.5) {
//                    1 - drawPercent
//                } else {
//                    drawPercent
//                }) * 2
//                val min = (height / 60) * 20 * (1 - baseMove) * (if (drawMode) {
//                    -1
//                } else {
//                    1
//                })
//                for (i in 0..20) {
//                    var moveFace = (height / 60F) * i * baseMove
//                    if (drawMode) {
//                        moveFace = -moveFace
//                    }
//                    val firstPoint = points[0]
//                    GL11.glVertex3d(firstPoint.xCoord - mc.renderManager.viewerPosX, firstPoint.yCoord - moveFace - min - mc.renderManager.viewerPosY, firstPoint.zCoord - mc.renderManager.viewerPosZ)
//                    GL11.glColor4f(1F, 1F, 1F, 0.7F * (i / 20F))
//                    for (vec3 in points) {
//                        GL11.glVertex3d(
//                            vec3.xCoord - mc.renderManager.viewerPosX, vec3.yCoord - moveFace - min - mc.renderManager.viewerPosY,
//                            vec3.zCoord - mc.renderManager.viewerPosZ
//                        )
//                    }
//                    GL11.glColor4f(0F, 0F, 0F, 0F)
//                }
//                GL11.glEnd()
//                GL11.glEnable(GL11.GL_DEPTH_TEST)
//                GL11.glDisable(GL11.GL_LINE_SMOOTH)
//                GL11.glDisable(GL11.GL_BLEND)
//                GL11.glEnable(GL11.GL_TEXTURE_2D)
//                GL11.glPopMatrix()
            }
        }

        if (currentTarget != null && attackTimer.hasTimePassed(attackDelay) &&
            currentTarget!!.hurtTime <= hurtTimeValue.get()) {
            clicks++
            attackTimer.reset()
            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), maxCPS.get())
        }
    }

    /**
     * Handle entity move
     */
    @EventTarget
    fun onTick(event: TickEvent?) {
        if (markValue.get().equals("jello", ignoreCase = true)
        ) al = AnimationUtils.changer(
            al,
            if (target != null) jelloFadeSpeedValue.get() else -jelloFadeSpeedValue.get(),
            0f,
            colorAlphaValue.get() / 255.0f
        )
    }
    @EventTarget
    fun onEntityMove(event: EntityMovementEvent) {
        val movedEntity = event.movedEntity

        if (target == null || movedEntity != currentTarget)
            return

        updateHitable()
    }

    /**
     * Attack enemy
     */
    private fun drawShadow(entity: IEntity, partialTicks: Float, pos: Float, direction: Boolean) {
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glShadeModel(7425)
        val x =
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks.toDouble() - mc.renderManager.viewerPosX
        val y =
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks.toDouble() - mc.renderManager.viewerPosY + pos
        val z =
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks.toDouble() - mc.renderManager.viewerPosZ
        GL11.glBegin(GL11.GL_QUAD_STRIP)
        for (i in 0..180) {
            val c1 = i * Math.PI * 2 / 180
            val c2 = (i + 1) * Math.PI * 2 / 180
            //GlStateManager.color(1f, 1f, 1f, 90f)
            GL11.glColor4f(1f,1f,1f,0.2f)
            GL11.glVertex3d(x + 0.5 * cos(c1), y, z + 0.5 * sin(c1))
            GL11.glVertex3d(x + 0.5 * cos(c2), y, z + 0.5 * sin(c2))
            //GlStateManager.color(1f, 1f, 1f, 0f)
            GL11.glColor4f(1f,1f,1f,0f)
            GL11.glVertex3d(x + 0.5 * cos(c1), y + if (direction) -0.2 else 0.2, z + 0.5 * sin(c1))
            GL11.glVertex3d(x + 0.5 * cos(c2), y + if (direction) -0.2 else 0.2, z + 0.5 * sin(c2))
        }
        GL11.glEnd()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glShadeModel(7424)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopMatrix()
    }
    private fun runAttack() {
        target ?: return
        currentTarget ?: return
        val thePlayer = mc.thePlayer ?: return
        val theWorld = mc.theWorld ?: return

        // Settings
        val failRate = failRateValue.get()
        val swing = swingValue.get()
        val multi = targetModeValue.get().equals("Multi", ignoreCase = true)
        val openInventory = aacValue.get() && classProvider.isGuiContainer(mc.currentScreen)
        val failHit = failRate > 0 && Random().nextInt(100) <= failRate


        // Close inventory when open
        if (openInventory)
            mc.netHandler.addToSendQueue(classProvider.createCPacketCloseWindow())

        // Check is not hitable or check failrate

        if (!hitable || failHit) {
            if (swing && (fakeSwingValue.get() || failHit))
                thePlayer.swingItem()
        } else {
            // Attack
            if (!multi) {
                attackEntity(currentTarget!!)
            } else {
                var targets = 0

                for (entity in theWorld.loadedEntityList) {
                    val distance = thePlayer.getDistanceToEntityBox(entity)

                    if (classProvider.isEntityLivingBase(entity) && isEnemy(entity) && distance <= getRange(entity)) {
                        attackEntity(entity.asEntityLivingBase())

                        targets += 1

                        if (limitedMultiTargetsValue.get() != 0 && limitedMultiTargetsValue.get() <= targets)
                            break
                    }
                }
            }

            if(switchTimer.hasTimePassed(switchDelayValue.get().toLong()) || targetModeValue.get() != "Switch") {
                prevTargetEntities.add(if (aacValue.get()) target!!.entityId else currentTarget!!.entityId)
                switchTimer.reset()
            }

            prevTargetEntities.add(if (aacValue.get()) target!!.entityId else currentTarget!!.entityId)

            if (target == currentTarget)
                target = null


        }

        // Open inventory
        if (openInventory)
            mc.netHandler.addToSendQueue(createOpenInventoryPacket())
    }

    /**
     * Update current target
     */
    private fun updateTarget() {
        // Reset fixed target to null
        target = null

        // Settings
        val hurtTime = hurtTimeValue.get()
        val fov = fovValue.get()
        val switchMode = targetModeValue.get().equals("Switch", ignoreCase = true)

        // Find possible targets
        val targets = mutableListOf<IEntityLivingBase>()

        val theWorld = mc.theWorld!!
        val thePlayer = mc.thePlayer!!

        for (entity in theWorld.loadedEntityList) {
            if (!classProvider.isEntityLivingBase(entity) || !isEnemy(entity) || (switchMode && prevTargetEntities.contains(entity.entityId)))
                continue

            val distance = thePlayer.getDistanceToEntityBox(entity)
            val entityFov = RotationUtils.getRotationDifference(entity)

            if (distance <= maxRange && (fov == 180F || entityFov <= fov) && entity.asEntityLivingBase().hurtTime <= hurtTime)
                targets.add(entity.asEntityLivingBase())
        }

        // Sort targets by priority
        when (priorityValue.get().toLowerCase()) {
            "distance" -> targets.sortBy { thePlayer.getDistanceToEntityBox(it) } // Sort by distance
            "health" -> targets.sortBy { it.health } // Sort by health
            "direction" -> targets.sortBy { RotationUtils.getRotationDifference(it) } // Sort by FOV
            "livingtime" -> targets.sortBy { -it.ticksExisted } // Sort by existence
        }

        // Find best target
        for (entity in targets) {
            // Update rotations to current target
            if (!updateRotations(entity)) // when failed then try another target
                continue

            // Set target to current entity
            target = entity
            return
        }

        // Cleanup last targets when no target found and try again
        if (prevTargetEntities.isNotEmpty()) {
            prevTargetEntities.clear()
            updateTarget()
        }
    }

    /**
     * Check if [entity] is selected as enemy with current target options and other modules
     */
    private fun isEnemy(entity: IEntity?): Boolean {
        if (classProvider.isEntityLivingBase(entity) && entity != null && (EntityUtils.targetDead || isAlive(entity.asEntityLivingBase())) && entity != mc.thePlayer) {
            if (!EntityUtils.targetInvisible && entity.invisible)
                return false

            if (EntityUtils.targetPlayer && classProvider.isEntityPlayer(entity)) {
                val player = entity.asEntityPlayer()

                if (player.spectator || AntiBot.isBot(player))
                    return false

                if (player.isClientFriend() && !LiquidBounce.moduleManager[NoFriends::class.java].state)
                    return false

                val teams = LiquidBounce.moduleManager[Teams::class.java] as Teams

                return !teams.state || !teams.isInYourTeam(entity.asEntityLivingBase())
            }

            return EntityUtils.targetMobs && entity.isMob() || EntityUtils.targetAnimals && entity.isAnimal()
        }

        return false
    }

    /**
     * Attack [entity]
     */
    private fun attackEntity(entity: IEntityLivingBase) {
        // Stop blocking
        val thePlayer = mc.thePlayer!!

        if (thePlayer.isBlocking || blockingStatus)
            stopBlocking()

        // Call attack event
        LiquidBounce.eventManager.callEvent(AttackEvent(entity))

        // Attack target
        if (swingValue.get() && Backend.MINECRAFT_VERSION_MINOR == 8)
            thePlayer.swingItem()

        mc.netHandler.addToSendQueue(classProvider.createCPacketUseEntity(entity, ICPacketUseEntity.WAction.ATTACK))

        if (swingValue.get() && Backend.MINECRAFT_VERSION_MINOR != 8)
            thePlayer.swingItem()

        if (keepSprintValue.get()) {
            // Critical Effect
            if (thePlayer.fallDistance > 0F && !thePlayer.onGround && !thePlayer.isOnLadder &&
                !thePlayer.isInWater && !thePlayer.isPotionActive(classProvider.getPotionEnum(PotionType.BLINDNESS)) && !thePlayer.isRiding)
                thePlayer.onCriticalHit(entity)

            // Enchant Effect
            if (functions.getModifierForCreature(thePlayer.heldItem, entity.creatureAttribute) > 0F)
                thePlayer.onEnchantmentCritical(entity)
        } else {
            if (mc.playerController.currentGameType != IWorldSettings.WGameType.SPECTATOR)
                thePlayer.attackTargetEntityWithCurrentItem(entity)
        }

        // Extra critical effects
        val criticals = LiquidBounce.moduleManager[Criticals::class.java] as Criticals

        for (i in 0..2) {
//            // Critical Effect
//            if (thePlayer.fallDistance > 0F && !thePlayer.onGround && !thePlayer.isOnLadder && !thePlayer.isInWater && !thePlayer.isPotionActive(classProvider.getPotionEnum(PotionType.BLINDNESS)) && thePlayer.ridingEntity == null || criticals.state && criticals.msTimer.hasTimePassed(criticals.delayValue.get().toLong()) && !thePlayer.isInWater && !thePlayer.isInLava && !thePlayer.isInWeb)
//                thePlayer.onCriticalHit(target!!)
//
            // Enchant Effect
            if (functions.getModifierForCreature(thePlayer.heldItem, target!!.creatureAttribute) > 0.0f || fakeSharpValue.get())
                thePlayer.onEnchantmentCritical(target!!)
        }

        // Start blocking after attack
        if (thePlayer.isBlocking || (!blockModeValue.get().equals("None") && canBlock)) {
            if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
                return

            if (delayedBlockValue.get())
                return

            startBlocking(entity)
        }

        @Suppress("ConstantConditionIf")
        if (Backend.MINECRAFT_VERSION_MINOR != 8) {
            thePlayer.resetCooldown()
        }
    }

    /**
     * Update killaura rotations to enemy
     */
    private fun updateRotations(entity: IEntity): Boolean {
        var boundingBox = entity.entityBoundingBox
        if (rotations.get().equals("Vanilla", ignoreCase = true)){
            if (maxTurnSpeed.get() <= 0F)
                return true

            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )

            val (vec, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }/*
        if (rotations.get().equals("BackTrack", ignoreCase = true)) {
            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )
            val (_, rotation) = RotationUtils.searchCenter(
                    boundingBox,
                    outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                    randomCenterValue.get(),
                    predictValue.get(),
                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                    maxRange
            ) ?: return false
            //debug
            // ClientUtils.displayChatMessage((mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get()).toString())
            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
                rotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get()) {
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            }else {
                limitedRotation.toPlayer(mc.thePlayer!!)
                return true
            }
        }*/
        if (rotations.get().equals("BackTrack", ignoreCase = true)) {
            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )

            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
                RotationUtils.OtherRotation(boundingBox,RotationUtils.getCenter(entity.entityBoundingBox), predictValue.get(),
                    mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),maxRange), (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get()) {
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            }else {
                limitedRotation.toPlayer(mc.thePlayer!!)
                return true
            }
        }
        if (rotations.get().equals("HytRotation", ignoreCase = true)) {
            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )
            val (_, rotation) = RotationUtils.lockView(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
                rotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }
        if (rotations.get().equals("Test", ignoreCase = true)) {
            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )
            val (_, rotation) = RotationUtils.lockView(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false
            //debug
            // ClientUtils.displayChatMessage((mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get()).toString())
            val limitedRotation = RotationUtils.limitAngleChange(
                RotationUtils.serverRotation,
                rotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat()
            )

            if (silentRotationValue.get()) {
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            } else {
                limitedRotation.toPlayer(mc.thePlayer!!)
                return true
            }
        }
        if (rotations.get().equals("Test2", ignoreCase = true)){
            //用这个test2看看
            if (maxTurnSpeed.get() <= 0F)
                return true

            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )

            val (vec, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation =   RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox),false),(Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }
        if (rotations.get().equals("Test3", ignoreCase = true)) {
            if (maxTurnSpeed.get() <= 0F)
                return true

            var boundingBox = entity.entityBoundingBox

            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX - (mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY - (mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ - (mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )

            val (_, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }
        if (rotations.get().equals("Test4", ignoreCase = true)){
            if (maxTurnSpeed.get() <= 0F)
                return true

            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )

            val (vec, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation =  RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.getNewRotations(RotationUtils.getCenter(entity.entityBoundingBox),false),(Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }
        if (rotations.get().equals("LockView", ignoreCase = true)){
            val (vec, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
                (180.0).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }
        if (rotations.get().equals("LiquidSensePlus", ignoreCase = true)) {
            if (maxTurnSpeed.get() <= 0F)
                return true

            var boundingBox = entity.entityBoundingBox

            if (predictValue.get())
                boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX - (mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY - (mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ - (mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ)) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
                )

            val (_, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer!!.getDistanceToEntityBox(entity) < throughWallsRangeValue.get(),
                maxRange
            ) ?: return false

            val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

            if (silentRotationValue.get())
                RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) 15 else 0)
            else
                limitedRotation.toPlayer(mc.thePlayer!!)

            return true
        }
        return true
    }

    /**
     * Check if enemy is hitable with current rotations
     */
    private fun updateHitable() {
        // Disable hitable check if turn speed is zero
        if (maxTurnSpeed.get() <= 0F) {
            hitable = true
            return
        }

        val reach = min(maxRange.toDouble(), mc.thePlayer!!.getDistanceToEntityBox(target!!)) + 1

        if (raycastValue.get()) {
            val raycastedEntity = RaycastUtils.raycastEntity(reach, object : RaycastUtils.EntityFilter {
                override fun canRaycast(entity: IEntity?): Boolean {
                    return (!livingRaycastValue.get() || (classProvider.isEntityLivingBase(entity) && !classProvider.isEntityArmorStand(entity))) &&
                            (isEnemy(entity) || raycastIgnoredValue.get() || aacValue.get() && mc.theWorld!!.getEntitiesWithinAABBExcludingEntity(entity, entity!!.entityBoundingBox).isNotEmpty())
                }

            })

            if (raycastValue.get() && raycastedEntity != null && classProvider.isEntityLivingBase(raycastedEntity)
                && (LiquidBounce.moduleManager[NoFriends::class.java].state || !(classProvider.isEntityPlayer(raycastedEntity) && raycastedEntity.asEntityPlayer().isClientFriend())))
                currentTarget = raycastedEntity.asEntityLivingBase()

            hitable = if (maxTurnSpeed.get() > 0F) currentTarget == raycastedEntity else true
        } else
            hitable = RotationUtils.isFaced(currentTarget, reach)
    }
    /**
     * Start blocking
     */
    private fun startBlocking(interactEntity: IEntity, interact: Boolean) {
        if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
            return

        if (interact) {
            val positionEye = mc.renderViewEntity?.getPositionEyes(1F)

            val expandSize = interactEntity.collisionBorderSize.toDouble()
            val boundingBox = interactEntity.entityBoundingBox.expand(expandSize, expandSize, expandSize)

            val (yaw, pitch) = RotationUtils.targetRotation ?: Rotation(
                mc.thePlayer!!.rotationYaw,
                mc.thePlayer!!.rotationPitch
            )
            val yawCos = cos(-yaw * 0.017453292F - Math.PI.toFloat())
            val yawSin = sin(-yaw * 0.017453292F - Math.PI.toFloat())
            val pitchCos = -cos(-pitch * 0.017453292F)
            val pitchSin = sin(-pitch * 0.017453292F)
            val range = min(maxRange.toDouble(), mc.thePlayer!!.getDistanceToEntityBox(interactEntity)) + 1
            val lookAt = positionEye!!.addVector(yawSin * pitchCos * range, pitchSin * range, yawCos * pitchCos * range)

            val movingObject = boundingBox.calculateIntercept(positionEye, lookAt) ?: return
            val hitVec = movingObject.hitVec

            mc.netHandler.addToSendQueue(
                classProvider.createCPacketUseEntity(
                    interactEntity, WVec3(
                        hitVec.xCoord - interactEntity.posX,
                        hitVec.yCoord - interactEntity.posY,
                        hitVec.zCoord - interactEntity.posZ
                    )
                )
            )
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketUseEntity(
                    interactEntity,
                    ICPacketUseEntity.WAction.INTERACT
                )
            )
        }
        if (LiquidBounce.moduleManager[Animations::class.java].state) {
            mc.netHandler.addToSendQueue(
                createUseItemPacket(
                    mc.thePlayer!!.inventory.getCurrentItemInHand(),
                    WEnumHand.OFF_HAND
                )
            )
        } else {
            mc.netHandler.addToSendQueue(
                createUseItemPacket(
                    mc.thePlayer!!.inventory.getCurrentItemInHand(),
                    WEnumHand.MAIN_HAND
                )
            )
            mc.netHandler.addToSendQueue(
                createUseItemPacket(
                    mc.thePlayer!!.inventory.getCurrentItemInHand(),
                    WEnumHand.OFF_HAND
                )
            )
        }
        blockingStatus = true
    }

    /**
     * Start blocking
     */
    private fun startBlocking(interactEntity: IEntity) {
        if(blockModeValue.get().equals("Real",true)) {
            mc.netHandler.addToSendQueue(classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND))
            blockingStatus = true
        }
        if(blockModeValue.get().equals("UseItem", true)) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
        }
        if(blockModeValue.get().equals("GameSettings", true)) {
            mc.gameSettings.keyBindUseItem.pressed = true
        }
        if(blockModeValue.get().equals("Mouse", true)) {
            Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
        }
        if(blockModeValue.get().equals("Packet", true)) {
            mc.netHandler.addToSendQueue(createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.MAIN_HAND))
            mc.netHandler.addToSendQueue(createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.OFF_HAND))
        }

        blockingStatus = true
    }

    /**
     * Stop blocking
     */
    private fun stopBlocking() {
        if (blocking){
            mc.gameSettings.keyBindUseItem.pressed = false
            blocking = false
        }
        if (blockingStatus) {
            if(blockModeValue.get().equals("c07c08", true)){
                mc.netHandler.addToSendQueue(
                    classProvider.createCPacketPlayerDigging(
                        ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                        WBlockPos.ORIGIN,
                        classProvider.getEnumFacing(EnumFacingType.DOWN)
                    )
                )
            }
            if(blockModeValue.get().equals("Real",true)) {
                mc.netHandler.addToSendQueue(
                    classProvider.createCPacketPlayerDigging(
                        ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                        WBlockPos.ORIGIN,
                        classProvider.getEnumFacing(EnumFacingType.DOWN)
                    )
                )
            }
            if(blockModeValue.get().equals("UseItem", true)) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, false)
            }
            if (blockModeValue.get().equals("c08", true)) {
                mc.gameSettings.keyBindUseItem.pressed = false
                mc.thePlayer!!.itemInUseCount = 0
            }
            if(blockModeValue.get().equals("GameSettings", true)) {
                mc.gameSettings.keyBindUseItem.pressed = false
            }
            if(blockModeValue.get().equals("Mouse", true)) {
                Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
            }
            if(blockModeValue.get().equals("Packet", true)) {
                mc.netHandler.addToSendQueue(
                    classProvider.createCPacketPlayerDigging(
                        ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                        WBlockPos.ORIGIN,
                        classProvider.getEnumFacing(EnumFacingType.DOWN)
                    )
                )
            }

            blockingStatus = false
        }
    }


    /**
     * Check if run should be cancelled
     */
    private val cancelRun: Boolean
        inline get() = mc.thePlayer!!.spectator || !isAlive(mc.thePlayer!!)
                || LiquidBounce.moduleManager[Blink::class.java].state


    /**
     * Check if [entity] is alive
     */
    private fun isAlive(entity: IEntityLivingBase) = entity.entityAlive && entity.health > 0 ||
            aacValue.get() && entity.hurtTime > 5

    private val canBlock: Boolean
        inline get() = mc.thePlayer!!.heldItem != null && classProvider.isItemSword(mc.thePlayer!!.heldItem!!.item)

    /**
     * Range
     */
    private val maxRange: Float
        get() = max(rangeValue.get(), throughWallsRangeValue.get())



    private fun getRange(entity: IEntity) =
        (if (mc.thePlayer!!.getDistanceToEntityBox(entity) >= throughWallsRangeValue.get()) rangeValue.get() else throughWallsRangeValue.get()) - if (mc.thePlayer!!.sprinting) rangeSprintReducementValue.get() else 0F

    /**
     * HUD Tag
     */
    override val tag: String?
        get() = targetModeValue.get()

    val isBlockingChestAura: Boolean
        get() = state && target != null

    companion object {
        lateinit var target: Any
    }
}