package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "Strafe", description = "Allows you to freely move in mid air.", category = ModuleCategory.MOVEMENT, Chinese = "灵活运动")
class Strafe : Module() {
    private val hurt = BoolValue("Hurt", true)
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.POST)
            return

        if (hurt.get()){
            if (mc.thePlayer!!.hurtTime>=9)
                MovementUtils.strafe()
        }
        else MovementUtils.strafe()
    }
}
