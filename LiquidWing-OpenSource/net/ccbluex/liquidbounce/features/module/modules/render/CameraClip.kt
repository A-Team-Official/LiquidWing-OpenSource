package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "CameraClip", description = "Allows you to see through walls in third person view.", category = ModuleCategory.RENDER, Chinese = "透视玩家动作")
class CameraClip : Module()