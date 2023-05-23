package ad

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "Session-NEVERLOSE")
class Session2(x: Double = 10.0, y: Double = 30.0, scale: Float = 1F) : Element(x, y, scale) {
    private val GameInfo = ListValue("Mode", arrayOf("Distance"), "Distance")
    private fun BPS(): Double {
        val bps = Math.hypot(
            mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
            mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
        ) * mc.timer.timerSpeed * 20
        return Math.round(bps * 100.0) / 100.0
    }
    override fun drawElement(): Border {
        val fontRenderer = Fonts.sfui35
        val y2 = fontRenderer.fontHeight * 5 + 11.0.toInt()
        if(GameInfo.get().equals("distance" , true)){

            val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
            RoundedUtil.drawRound(-2f, -2f, 150f, y2.toFloat() + 6f, 2F, Color(30, 30, 30, 200))
            RenderUtils.drawRect(2f, 11f,145f,11.8f,Color(80,80,80,210))
            net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.Icons.icons_18.icons_18.drawString("p",
                3.0f, 2.7f,Color(4, 188, 255).rgb)
            fontRenderer.drawString("Session Info", 15f, 2f, Color.WHITE.rgb, false)
            val Date = Date()

            fontRenderer.drawString("FPS: " + Minecraft.getDebugFPS().toString(), 2,
                (fontRenderer.fontHeight + 6f).toInt(), Color.WHITE.rgb)
            fontRenderer.drawString("Speed: " + BPS().toString() + " b/s", 2,
                (fontRenderer.fontHeight * 2 + 8f).toInt(), Color.WHITE.rgb)
            fontRenderer.drawString("Time: " + DATE_FORMAT.format(Date), 2,
                (fontRenderer.fontHeight * 3 + 10f).toInt(), Color.WHITE.rgb)
            fontRenderer.drawString("Health " + Math.round(mc.thePlayer!!.health)+ ".0 / " + Math.round(mc.thePlayer!!.maxHealth) + ".0", 2,
                (fontRenderer.fontHeight * 4 + 12f).toInt(), Color.WHITE.rgb)


        }
        return Border(-2f, -2f, 150f, y2.toFloat() + 6f,2f)
    }
}
