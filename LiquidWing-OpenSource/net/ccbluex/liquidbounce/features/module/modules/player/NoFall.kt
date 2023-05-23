/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.enums.BlockType
import net.ccbluex.liquidbounce.api.enums.EnumFacingType
import net.ccbluex.liquidbounce.api.enums.ItemType
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.VecRotation
import net.ccbluex.liquidbounce.utils.block.BlockUtils.collideBlock
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TickTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.ceil
import kotlin.math.sqrt

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.COMBAT, Chinese = "取消摔落伤害")
class NoFall : Module() {
    @JvmField
    val modeValue = ListValue("Mode", arrayOf("SpoofGround","HuaYuTing", "AAC4", "MLG","Test","HuaYuTingfix2"), "SpoofGround")
    private val minFallDistance = FloatValue("MinMLGHeight", 5f, 2f, 50f)
    private val mlgTimer = TickTimer()
    private val mlgTimer2 = MSTimer()
    private val mlgTimer3 = MSTimer()

    private var jumped = false
    private var currentMlgRotation: VecRotation? = null
    private var currentMlgItemIndex = 0
    private var currentMlgBlock: WBlockPos? = null
    private var test = FloatValue("TEST",1000F,0F,20000F)
    private var test2 = FloatValue("TEST2",1000F,0F,20000F)

    private var aac4Fakelag = false
    private var packetModify = false
    private val aac4Packets = mutableListOf<ICPacketPlayer>()
    private val packets = LinkedBlockingQueue<IPacket>()
    private var  fallrange = FloatValue("Fall",0f,0f,1.5F)

    private var fall = false

    @EventTarget(ignoreCondition = true)
    fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer!!.onGround)
            jumped = false

        if (mc.thePlayer!!.motionY > 0)
            jumped = true

        if (collideBlock(mc.thePlayer!!.entityBoundingBox, classProvider::isBlockLiquid) ||
            collideBlock(classProvider.createAxisAlignedBB(mc.thePlayer!!.entityBoundingBox.maxX, mc.thePlayer!!.entityBoundingBox.maxY, mc.thePlayer!!.entityBoundingBox.maxZ, mc.thePlayer!!.entityBoundingBox.minX, mc.thePlayer!!.entityBoundingBox.minY - 0.01, mc.thePlayer!!.entityBoundingBox.minZ), classProvider::isBlockLiquid))
            return

        when (modeValue.get().toLowerCase()) {
            "packet" -> {
                if (mc.thePlayer!!.fallDistance > 2f) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayer(true))
                }
            }
        }
    }
    override fun onEnable() {
        aac4Packets.clear()
        packetModify =false
        aac4Fakelag = false
        fall = false
    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        val mode = modeValue.get()
        if (classProvider.isCPacketPlayer(packet)) {
            val playerPacket = packet.asCPacketPlayer()
            if (mode.equals("SpoofGround", ignoreCase = true)) playerPacket.onGround = true
        }

        when(modeValue.get().toLowerCase()){
            "AAC4"->{
                if(classProvider.isCPacketPlayer(event.packet) && aac4Fakelag) {
                    event.cancelEvent()
                    if (packetModify) {
                        event.packet.asCPacketPlayer().onGround = true
                        packetModify = false
                    }
                    aac4Packets.add(event.packet.asCPacketPlayer())
                }
            }
            "test" -> {
                if (mc.thePlayer!!.fallDistance >2.0 && mlgTimer2.hasTimePassed(test.get().toLong())){
                    if (classProvider.isCPacketPlayer(packet)) // Cancel all movement stuff
                        event.cancelEvent()
                    if (classProvider.isCPacketPlayerPosition(packet) || classProvider.isCPacketPlayerPosLook(packet) ||
                        classProvider.isCPacketPlayerBlockPlacement(packet) ||
                        classProvider.isCPacketAnimation(packet) ||
                        classProvider.isCPacketEntityAction(packet) || classProvider.isCPacketUseEntity(packet) || classProvider.isCPacketConfirmTransaction(packet)) {
                        event.cancelEvent()
                        packets.add(packet)
                    }
                    fall = true
                    mlgTimer2.reset()
                }
                if (fall  &&  mlgTimer3.hasTimePassed(test2.get().toLong())){
                    while (!packets.isEmpty()) {
                        mc.netHandler.networkManager.sendPacket(packets.take())
                    }
                    packets.clear()
                    mlgTimer3.reset()
                    fall =false
                }
            }
            "huayutingfix2" -> {
                if (mc.thePlayer!!.fallDistance > 2.0 && mlgTimer2.hasTimePassed(test.get().toLong())) {
                    LiquidBounce.moduleManager[Blink::class.java].state = true
                    fall = true
                    mlgTimer2.reset()

                }
                if (fall && mlgTimer3.hasTimePassed(test2.get().toLong())) {
                    LiquidBounce.moduleManager[Blink::class.java].state = false
                    mlgTimer3.reset()
                }
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        when(modeValue.get().toLowerCase()){
            "AAC4" -> {
                if (event.eventState == EventState.PRE) {
                    if (!inVoid()) {
                        if (aac4Fakelag) {
                            aac4Fakelag = false
                            if (aac4Packets.size > 0) {
                                for (packet in aac4Packets) {
                                    mc.thePlayer!!.sendQueue.addToSendQueue(packet)
                                }
                                aac4Packets.clear()
                            }
                        }
                        return
                    }
                    if (mc.thePlayer!!.onGround && aac4Fakelag) {
                        aac4Fakelag = false
                        if (aac4Packets.size > 0) {
                            for (packet in aac4Packets) {
                                mc.thePlayer!!.sendQueue.addToSendQueue(packet)
                            }
                            aac4Packets.clear()
                        }
                        return
                    }
                    if (mc.thePlayer!!.fallDistance > 2.5 && aac4Fakelag) {
                        packetModify = true
                        mc.thePlayer!!.fallDistance = 0f
                    }
                    if (inAir(4.0, 1.0)) {
                        return
                    }
                    if (!aac4Fakelag) {
                        aac4Fakelag = true
                    }
                }
            }

            "huayuting" -> if (event.eventState == EventState.PRE){
                if (mc.thePlayer!!.fallDistance > 2f && mlgTimer.hasTimePassed(test.get().toInt())) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayer(true))
                    mlgTimer.reset()
                }
            }
        }
    }
    private fun inVoid(): Boolean {
        if (mc.thePlayer!!.posY < 0) {
            return false
        }
        var off = 0
        while (off < mc.thePlayer!!.posY + 2) {
            val bb = classProvider.createAxisAlignedBB(
                mc.thePlayer!!.posX,
                mc.thePlayer!!.posY,
                mc.thePlayer!!.posZ,
                mc.thePlayer!!.posX,
                off.toDouble(),
                mc.thePlayer!!.posZ
            )
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, bb).isNotEmpty()) {
                return true
            }
            off += 2
        }
        return false
    }
    private fun inAir(height: Double, plus: Double): Boolean {
        if (mc.thePlayer!!.posY < 0) return false
        var off = 0
        while (off < height) {
            val bb = classProvider.createAxisAlignedBB(
                mc.thePlayer!!.posX,
                mc.thePlayer!!.posY,
                mc.thePlayer!!.posZ,
                mc.thePlayer!!.posX,
                mc!!.thePlayer!!.posY - off,
                mc.thePlayer!!.posZ
            )
            if (!mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, bb).isEmpty()) {
                return true
            }
            off += plus.toInt()
        }
        return false
    }

    @EventTarget
    private fun onMotionUpdate(event: MotionEvent) {
        if (!modeValue.get().equals("MLG", ignoreCase = true))
            return

        if (event.eventState == EventState.PRE) {
            currentMlgRotation = null

            mlgTimer.update()

            if (!mlgTimer.hasTimePassed(10))
                return

            if (mc.thePlayer!!.fallDistance > minFallDistance.get()) {
                val fallingPlayer = FallingPlayer(
                    mc.thePlayer!!.posX,
                    mc.thePlayer!!.posY,
                    mc.thePlayer!!.posZ,
                    mc.thePlayer!!.motionX,
                    mc.thePlayer!!.motionY,
                    mc.thePlayer!!.motionZ,
                    mc.thePlayer!!.rotationYaw,
                    mc.thePlayer!!.moveStrafing,
                    mc.thePlayer!!.moveForward
                )

                val maxDist: Double = mc.playerController.blockReachDistance + 1.5

                val collision = fallingPlayer.findCollision(ceil(1.0 / mc.thePlayer!!.motionY * -maxDist).toInt())
                    ?: return

                var ok: Boolean = WVec3(mc.thePlayer!!.posX, mc.thePlayer!!.posY + mc.thePlayer!!.eyeHeight, mc.thePlayer!!.posZ).distanceTo(WVec3(collision.pos).addVector(0.5, 0.5, 0.5)) < mc.playerController.blockReachDistance + sqrt(0.75)

                if (mc.thePlayer!!.motionY < collision.pos.y + 1 - mc.thePlayer!!.posY) {
                    ok = true
                }

                if (!ok)
                    return

                var index = -1

                for (i in 36..44) {
                    val itemStack = mc.thePlayer!!.inventoryContainer.getSlot(i).stack

                    if (itemStack != null && (itemStack.item == classProvider.getItemEnum(ItemType.WATER_BUCKET) || classProvider.isItemBlock(itemStack.item) && (itemStack.item?.asItemBlock())?.block == classProvider.getBlockEnum(BlockType.WEB))) {
                        index = i - 36

                        if (mc.thePlayer!!.inventory.currentItem == index)
                            break
                    }
                }
                if (index == -1)
                    return

                currentMlgItemIndex = index
                currentMlgBlock = collision.pos

                if (mc.thePlayer!!.inventory.currentItem != index) {
                    mc.thePlayer!!.sendQueue.addToSendQueue(classProvider.createCPacketHeldItemChange(index))
                }

                currentMlgRotation = RotationUtils.faceBlock(collision.pos)
                currentMlgRotation!!.rotation.toPlayer(mc.thePlayer!!)
            }
        } else if (currentMlgRotation != null) {
            val stack = mc.thePlayer!!.inventory.getStackInSlot(currentMlgItemIndex + 36)

            if (classProvider.isItemBucket(stack!!.item)) {
                mc.playerController.sendUseItem(mc.thePlayer!!, mc.theWorld!!, stack)
            } else {
                val dirVec: WVec3i = classProvider.getEnumFacing(EnumFacingType.UP).directionVec

                if (mc.playerController.sendUseItem(mc.thePlayer!!, mc.theWorld!!, stack)) {
                    mlgTimer.reset()
                }
            }
            if (mc.thePlayer!!.inventory.currentItem != currentMlgItemIndex)
                mc.thePlayer!!.sendQueue.addToSendQueue(classProvider.createCPacketHeldItemChange(mc.thePlayer!!.inventory.currentItem))
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onJump(event: JumpEvent?) {
        jumped = true
    }

    override val tag: String
        get() = modeValue.get()
}