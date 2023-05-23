package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.features.module.modules.world.Timer

@ModuleInfo(name = "ScaffoldHelper", description = "ScaffoldHelper", category = ModuleCategory.HYT, Chinese = "自动搭路助手")
class ScaffoldHelper : Module() {
    var scaffoldmodule = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
    var timermodule = LiquidBounce.moduleManager.getModule(Timer::class.java) as Timer
    override fun onDisable() {
        scaffoldmodule.state = false
        timermodule.state = false

    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {

        if (!mc.thePlayer!!.sneaking){
            if (mc.thePlayer!!.onGround){
                scaffoldmodule.state = false
                timermodule.state = false
            }else{
                scaffoldmodule.state = true
                timermodule.state = true
            }

        }
    }


}