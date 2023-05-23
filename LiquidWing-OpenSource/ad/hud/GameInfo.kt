package ad.hud

import ad.utils.Color.modules.CustomUI
import ad.utils.InfosUtils.Recorder
import ad.utils.InfosUtils.Recorder.killCounts
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.ListValue
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "GameInfo")
class GameInfo(x: Double = 10.0, y: Double = 30.0, scale: Float = 1F) : Element(x, y, scale) {
    private val GameInfo = ListValue("Mode", arrayOf("Normal"), "Normal")
    private val shadowValue = ListValue("Normal-Shadow", arrayOf("None", "Basic", "Thick"), "Thick")
    private val lineValue = BoolValue("Line", true)
    private var fontValue = FontValue("Font", Fonts.pop35)

    override fun drawElement(): Border {
        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
        val fontRenderer = fontValue.get()
        val y2 = fontRenderer.fontHeight * 5 + 11.0.toInt()
        val x2 = 140.0.toInt()
        if(GameInfo.get().equals("normal" , true)){

            val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
            RoundedUtil.drawRound(-2f, -2f, x2.toFloat(), y2.toFloat(), 0F, Color(0,0,0,100))
            //drawShadow
            when (shadowValue.get()) {
                "Basic" -> RenderUtils.drawShadow(-2.5f, -2.5f, x2.toFloat()+1, y2.toFloat()+1)
                "Thick" -> {
                    RenderUtils.drawShadow(-2.5f, -2.5f, x2.toFloat()+1, y2.toFloat()+1)
                    RenderUtils.drawShadow(-2.5f, -2.5f, x2.toFloat()+1, y2.toFloat()+1)
                }
            }
            if(lineValue.get()) {
                RenderUtils.drawGradientSideways(
                    2.44,
                    fontRenderer.fontHeight + 2.5 + 0.0,
                    138.0 + -2.44f,
                    Fonts.sfbold40.fontHeight + 2.5 + 1.16f,Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()).rgb, Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get()).rgb)
            }
            Fonts.sfbold40.drawCenteredString("Session Info", x2.toFloat() / 2f, 3f, Color.WHITE.rgb, true)
            fontRenderer.drawStringWithShadow("Play Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}", 2, (fontRenderer.fontHeight + 8f).toInt(), Color.WHITE.rgb)
            fontRenderer.drawStringWithShadow("Players Killed: " + killCounts, 2,
                (fontRenderer.fontHeight * 2 + 8f).toInt(), Color.WHITE.rgb)
            fontRenderer.drawStringWithShadow("Win: " + Recorder.totalPlayed, 2,
                (fontRenderer.fontHeight * 3 + 8f).toInt(), Color.WHITE.rgb)
            fontRenderer.drawStringWithShadow("Total: " + Recorder.totalPlayed , 2,
                (fontRenderer.fontHeight * 4 + 8f).toInt(), Color.WHITE.rgb)
        }
        return Border(-2f, -2f, x2.toFloat(), y2.toFloat(),0f)
    }
}
