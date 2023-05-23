package ad.sb.novoline.ui

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.roundToInt


@ElementInfo(name = "Session4")
class Session(x: Double = 10.0, y: Double = 29.0, scale: Float = 1F,
              side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)
) : Element(x, y, scale, side) {

    private val shadowValue = FloatValue("Shadow-Strength", 10f,0f,20f)
    private val radiusValue = FloatValue("Radius", 0f, 0f, 10f)
    private val blur = BoolValue("Blur",false)
    private val bradius = FloatValue("Blur-Strength", 10f,0f,50f)
    private val bgredValue = IntegerValue("Bg-R", 0, 0, 255)
    private val bggreenValue = IntegerValue("Bg-G", 0, 0, 255)
    private val bgblueValue = IntegerValue("Bg-B", 0, 0, 255)
    private val bgalphaValue = IntegerValue("Bg-Alpha", 105, 0, 255)
    val y2 = 60 * 1.05
    val x2 = 120* 1.05
    val floatX = renderX.toFloat()
    val floatY = renderY.toFloat()
    override fun shadow() {
        RenderUtils.drawRoundedRect2(0f, 0f, x2.toFloat()
            ,y2.toFloat(), radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get() + 190).rgb)
    }


    override fun drawElement(): Border? {
        val x2:Float = x2.toFloat()
        val y2:Float = y2.toFloat()



        val time: String

        val durationInMillis: Long = System.currentTimeMillis() - Recorder.startTime
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24
        time = String.format("%02d:%02d:%02d", hour, minute, second)

        val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD


        //val barLength = (fontRenderer.getSgetStringWidth(displayText) + 4F).toDouble()

        // RoundedUtil.drawRound(0f, 0f, x2,y2, radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get()))
        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glScalef( 1F,  1F,  1F)
        GL11.glPushMatrix()
        ShadowUtils.shadow(shadowValue.get(),{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            RenderUtils.drawRoundedRect2(0f, 0f, x2,y2  , radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get()).rgb)
            GL11.glPopMatrix()

        },{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.drawRoundedRect2(0f, 0f, x2,y2 , radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get()).rgb)
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)



        RoundedUtil.drawGradientHorizontal(0f, 0f, x2,y2 , radiusValue.get(), Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get(),180
        ),Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get(),180))
        RenderUtils.drawRect(-1f* 1.05F, 40F* 1.05F,   39F* 1.05F,40F+21F* 1.05F , Color.WHITE)
        RenderUtils.drawRect(40F* 1.05F, 40F* 1.05F, 40F* 1.05F+ 39F* 1.05F,40F+21F* 1.05F ,Color.WHITE)
        RenderUtils.drawRect(80F* 1.05F, 40F* 1.05F, 80F* 1.05F + 41F* 1.05F,40F+21F * 1.05F, Color.WHITE)
        Fonts.pop35.drawString("SessionInfo",5* 1.05F,5* 1.05F,Color.WHITE.rgb)
        Fonts.pop25.drawCenteredString(LiquidBounce.combatManager.kills.toString(),19F* 1.05F,45F* 1.05F,Color.BLACK.rgb)
        Fonts.pop25.drawCenteredString("Kills",19F* 1.05F,45F+Fonts.pop35.fontHeight* 1.05F,Color(138,138,138).rgb)
        Fonts.pop25.drawCenteredString(time,59F* 1.05F,45F* 1.05F,Color.BLACK.rgb)
        Fonts.pop25.drawCenteredString("Played Time",59F* 1.05F,45F+Fonts.pop35.fontHeight* 1.05F,Color(138,138,138).rgb)
        Fonts.pop25.drawCenteredString(mc.thePlayer!!.health.roundToInt().toString()+"/"+ mc.thePlayer!!.maxHealth.toInt(),99F* 1.05F,45F* 1.05F,Color.BLACK.rgb)
        Fonts.pop25.drawCenteredString("Health",99F* 1.05F,45F+Fonts.pop35.fontHeight* 1.05F,Color(138,138,138).rgb)



        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.CustomBlurRoundArea(
                renderX.toFloat(), renderY.toFloat() , x2, y2 - 3f, radiusValue.get(),bradius.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

        }

        //RenderUtils.drawShadow(2f, -2f, x2, y2,)




        return Border(-2f, -2f, x2, y2,radiusValue.get())
    }
}