package ad

import com.mojang.realmsclient.gui.ChatFormatting
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder.killCounts
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "NewSession")
class Session(x: Double = 5.0, y: Double = 87.0, scale: Float = 1F) : Element(x, y, scale) {
    val SIR = 0f
    private val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    var aura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura?
    var target = aura!!.target
// This element by 7ad


    override fun drawElement(): Border {
//        RenderUtils.drawShadowWithCustomAlpha(0F, 13F, 150F, 67.2F, 200F)
        RenderUtils.drawRoundedRect(0F, this.SIR * 18F + 12F, 150F, 85F,3f,Color(0, 0, 0, 150).rgb)
        RoundedUtil.drawRound(0F,12.5F,150F,2.3f,2f, ColorUtils.hslRainbow(10, indexOffset = 300))
//        GL11.glTranslated(-renderX, -renderY, 0.0)
//        GL11.glPushMatrix()
//        BlurBuffer.blurRoundArea(renderX.toFloat(), renderY.toFloat()+18f  , 150F, 62F,5)
//        GL11.glPopMatrix()
//        GL11.glTranslated(renderX, renderY, 0.0)
        Fonts.productSans45.drawStringWithShadow("S" + ChatFormatting.WHITE + "ession Info", 3f.toInt(),
            (this.SIR * 18F + 19.5).toInt(), ColorUtils.hslRainbow(10, indexOffset = 300).rgb)
        Fonts.productSans35.drawStringWithShadow("Kills: " +killCounts,
            3f.toInt(),
            (this.SIR * 18F + 48).toInt(), Color(255, 244, 255, 255).rgb)
        Fonts.productSans35.drawStringWithShadow("Health: " + Math.round(mc.thePlayer!!.health), 3f.toInt(),
            (this.SIR * 18F + 60).toInt(), Color(255, 244, 255, 255).rgb)
        Fonts.productSans35.drawStringWithShadow("Played Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}" ,
            3f.toInt(),
            (this.SIR * 18F + 72).toInt(), Color(255, 244, 255, 255).rgb)

        return Border(0F, this.SIR * 18F + 12F, 150F, 80F,0f)
    }

}
