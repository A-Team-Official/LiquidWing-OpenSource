package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.TextValue

@ModuleInfo(name = "AutoL", category = ModuleCategory.WORLD, description = "LiquidWing", Chinese = "自动宣传")
class AutoL : Module() {
    private val L = BoolValue ("L", true)
    private val AutoLmsg = TextValue("AutoLmsg", "@[LiquidWing]")

    // Target
    var target: IEntityLivingBase? = null
    var kill = 0

    @EventTarget
    fun onAttack(event: AttackEvent) {
        target = event.targetEntity as IEntityLivingBase?
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (target!!.health <= 0.1) {
            kill += 1
            if (L.get()) {
                mc.thePlayer!!.sendChatMessage(AutoLmsg.get() + "我已经击杀了" + kill + "人 ,工艺裙 七 二 凌 武 一 三 凌 寺 七")
            }
            target = null
        }
    }

    fun kills() : Int {
        return kill
    }
    override val tag: String?
        get() = "Kill $kill"
}
