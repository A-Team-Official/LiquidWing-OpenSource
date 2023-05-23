package ad.sb.novoline.ui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import java.awt.Color

@ElementInfo(name = "RamRender", disableScale = true, priority = 1)
class Keybinds(x: Double = 5.0, y: Double = 130.0) : Element(x, y) {



    val width = 100F
    val height = 20F
    var y2 = 3
    override fun drawElement(): Border {
        RenderUtils.drawRect(0,0,width.toInt(),height.toInt(), Color(20,20,20,50).rgb)
        Fonts.CsgoIcon.csgoicon_20.csgoicon_20.drawString("a",3,3,-1)
        for (module in LiquidBounce.moduleManager.modules) {
            Fonts.tenacitybold.tenacitybold16.tenacitybold16.drawString(module.name + " (${module.keyBind}",3,y2 + Fonts.CsgoIcon.csgoicon_20.csgoicon_20.height + 2,-1)
            Fonts.tenacitybold.tenacitybold16.tenacitybold16.drawString(if (module.state) "[Toggled]" else "[OFF]",width - Fonts.tenacitybold.tenacitybold16.tenacitybold16.stringWidth(if (module.state) "[ON]" else "[OFF]") - 2,y2 + Fonts.CsgoIcon.csgoicon_20.csgoicon_20.height + 2F,-1)
            y2 += Fonts.tenacitybold.tenacitybold16.tenacitybold16.height + 2
        }
        return Border(0F,0F,width,height,0f)
    }
}