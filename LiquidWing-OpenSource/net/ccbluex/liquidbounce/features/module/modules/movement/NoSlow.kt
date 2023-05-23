/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.api.minecraft.item.IItem
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.C08PacketPlayerBlockPlacement
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.network.play.server.SPacketWindowItems
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos

@ModuleInfo(name = "NoSlow", description = "Cancels slowness effects caused by soulsand and using items.",
    category = ModuleCategory.MOVEMENT, Chinese = "取消减速")
class NoSlow : Module() {

    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val packet = BoolValue("PacketFix", true)
    // Soulsand
    val soulsandValue = BoolValue("Soulsand", true)

    val timer = MSTimer()

    override fun onDisable() {
        timer.reset()
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        if (this.packet.get()) {
            if (event.packet.unwrap() is SPacketWindowItems) {
                if (mc.thePlayer!!.isUsingItem) {
                    event.cancelEvent()
                }
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (!MovementUtils.isMoving)
            return

        if (this.packet.get()) {
            if (event.eventState == EventState.PRE && mc.thePlayer!!.itemInUse?.item != null
                && ((classProvider.isItemBow(mc.thePlayer!!.heldItem?.item) || mc.gameSettings.keyBindUseItem.isKeyDown)
                        && (classProvider.isItemFood(mc.thePlayer!!.heldItem?.item) || mc.gameSettings.keyBindUseItem.isKeyDown)
                        && (classProvider.isItemPotion(mc.thePlayer!!.heldItem?.item) || mc.gameSettings.keyBindUseItem.isKeyDown)
                        && (classProvider.isItemBucketMilk(mc.thePlayer!!.heldItem?.item) || mc.gameSettings.keyBindUseItem.isKeyDown))
                && (classProvider.isItemSword(mc.thePlayer!!.heldItem?.item) || mc.gameSettings.keyBindUseItem.isKeyDown)) {
                val curSlot = mc.thePlayer!!.inventory.currentItem
                val spoof = if (curSlot == 0) 1 else -1
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(curSlot + spoof))
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(curSlot))
            }

            if (event.eventState == EventState.PRE && (classProvider.isItemSword(mc.thePlayer!!.heldItem?.item) || mc.gameSettings.keyBindUseItem.isKeyDown)) {
                mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.getCurrentItemInHand()))
                mc2.connection!!.sendPacket(C08PacketPlayerBlockPlacement(getHytBlockpos(),255,EnumHand.MAIN_HAND,0f, 0f, 0f))
            }
        }
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer!!.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: IItem?, isForward: Boolean): Float {
        return when {
            classProvider.isItemFood(item) || classProvider.isItemPotion(item) || classProvider.isItemBucketMilk(item) -> {
                if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
            }
            classProvider.isItemSword(item) -> {
                if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
            }
            classProvider.isItemBow(item) -> {
                if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
            }
            else -> 0.2F
        }
    }

    fun getHytBlockpos(): BlockPos {
        val random = java.util.Random()
        val dx = WMathHelper.floor_double(random.nextDouble() / 1000 + 2820)
        val jy = WMathHelper.floor_double(random.nextDouble() / 100 * 0.20000000298023224)
        val kz = WMathHelper.floor_double(random.nextDouble() / 1000 + 2820)
        return BlockPos(dx, -jy % 255, kz)
    }

    override val tag: String?
        get() = when {
            packet.get() -> "HytPacket"
            else -> "Vanilla"
        }

}
