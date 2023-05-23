package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Main
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.objects.Drag
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.Scroll

enum class ModuleCategory(val displayName: String,val Chinese: String) {
    COMBAT("Combat","战斗"), PLAYER("Player","玩家"), MOVEMENT("Movement","运动"), RENDER("Render","渲染"), WORLD("World","世界"), MISC("Misc","杂类"), EXPLOIT("Exploit","开发"), COLOR("Color","颜色"),HYT(
        "Hyt","花雨庭"), GUI("Gui","界面");

    val posX: Int
    val expanded: Boolean
    val scroll = Scroll()
    val drag: Drag
    var posY = 20

    init {
        posX = 40 + Main.categoryCount * 120
        drag = Drag(posX.toFloat(), posY.toFloat())
        expanded = true
        Main.categoryCount++
    }
}