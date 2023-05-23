package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "AuraKeepSprint", description = "You Aura KeepSprint", category = ModuleCategory.COMBAT, Chinese = "保持开启杀戮时疾跑")
class AuraKeepSprint : Module() {
    private val KeepMode = ListValue("KeepMode", arrayOf("CritHit","CritHit2"),"CritHit")
    private var attackEntity: IEntityLivingBase? = null

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(attackEntity != null && attackEntity!!.isDead && attackEntity!!.health == 0.0F){
            attackEntity = null
        }
    }

    @EventTarget
    fun onAttack(event: AttackEvent){
        if(classProvider.isEntityLivingBase(event.targetEntity)){
            attackEntity = event.targetEntity!!.asEntityLivingBase()
        }
        if(KeepMode.get() == "CritHit")
            mc.thePlayer!!.onCriticalHit(attackEntity!!.asEntityLivingBase())
        if(KeepMode.get() == "CritHit2")
            mc.thePlayer!!.onEnchantmentCritical(attackEntity!!.asEntityLivingBase())
    }
}