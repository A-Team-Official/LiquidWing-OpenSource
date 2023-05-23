package ad.sb.novoline.ui

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MSTimer
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

@ElementInfo(name = "PlayerInfo-CN")
class PlayerInfo(x: Double = 10.0, y: Double = 29.0, scale: Float = 1F,
                 side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)
) : Element(x, y, scale, side) {
    val y2 = 80F
    val x2 = 180F
    var health = 0.0
    var BPS = 0.0
    var time = ""
    var time3 = 0F

    var timer2 = 0.0
    private val timer = MSTimer()
    override fun drawElement(): Border? {


        if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影"))
        {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                x2 + 10F,
                y2,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef( 1F,  1F,  1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(CustomUI.shadowValue.get(),{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect(0f, 0f, x2+10F,y2 , CustomUI.radius.get(), Color(0,0,0).rgb)
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.drawRoundedRect(0f, 0f, x2+10F,y2 , CustomUI.radius.get(), Color(0,0,0).rgb)
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (CustomUI.drawMode.get().equals("高斯模糊和阴影")){
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.CustomBlurRoundArea(
                renderX.toFloat(), renderY.toFloat(),x2+10F,y2, CustomUI.radius.get(),CustomUI.blurValue.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (CustomUI.drawMode.get().equals("圆角矩形")) {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                x2 + 10F,
                y2,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
        }
        if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
            RenderUtils.drawRoundedRect(
                0f,
                0f,
                x2 + 10F,
                y2,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )

            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat() , renderY.toFloat() , x2 + 10F, y2,
                CustomUI.radius.get(),
                CustomUI.outlinet.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), 0),
                Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
        }


        BPS = RenderUtils.getAnimationState2(BPS,
            (MovementUtils.getBlockSpeed(mc.thePlayer!!) /10F*(x2-30F)), 130.0
        ).coerceAtMost(((x2-30F).toDouble())).coerceAtLeast(33.0)
        health = RenderUtils.getAnimationState2(health,
            ((mc.thePlayer!!.health/ mc.thePlayer!!.maxHealth)*(x2-30F)).toDouble(), 130.0
        )
        val bps = Math.hypot(
            mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
            mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
        ) * mc.timer.timerSpeed * 20
        val durationInMillis: Long = System.currentTimeMillis()
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24
        time = String.format("%02d:%02d", hour, minute)
        time3 = String.format("%02d", hour).toFloat()


        timer2 = RenderUtils.getAnimationState2(timer2, (time3 /24).toDouble()*(x2-30F),130.0)

        GlStateManager.resetColor()
        if (CustomUI.Chinese.get()){
            FontLoaders.F20.drawString("玩家信息",6F,y2 - 12F,Color.WHITE.rgb)

            FontLoaders.F18.drawStringWithShadow("血量",8F,10F,Color.WHITE.rgb)
            RenderUtils.drawRect(30F,11F,x2-10F,13F,Color.WHITE.rgb)
            RenderUtils.drawRect(30.0, 11.0,
                health,13.0,Color.BLACK.rgb)

            FontLoaders.F18.drawStringWithShadow("速度",8F,30F,Color.WHITE.rgb)
            RenderUtils.drawRect(30F,31F,x2-10F,33F,Color.WHITE.rgb)
            RenderUtils.drawRect(30.0, 31.0,
                BPS,33.0,Color.BLACK.rgb)

            FontLoaders.F18.drawStringWithShadow("时间",8F,50F,Color.WHITE.rgb)
            RenderUtils.drawRect(30F,51F,x2-10F,53F,Color.WHITE.rgb)
            RenderUtils.drawRect(30.0, 51.0,
                timer2,53.0,Color.BLACK.rgb)


        }
        else{
            Fonts.pop40.drawString("PlayerInfo",6F,y2 - 13F,Color.WHITE.rgb)

            Fonts.pop35.drawString("Health",5F,10F,Color.WHITE.rgb)
            RenderUtils.drawRect(35F,12F,x2-10F,14F,Color.WHITE.rgb)
            RenderUtils.drawRect(35.0, 12.0,
                health,14.0,Color.BLACK.rgb)

            Fonts.pop35.drawString("Speed",5F,30F,Color.WHITE.rgb)
            RenderUtils.drawRect(35F,32F,x2-10F,34F,Color.WHITE.rgb)
            RenderUtils.drawRect(35.0, 32.0,
                BPS,34.0,Color.BLACK.rgb)

            Fonts.pop35.drawString("Time",5F,50F,Color.WHITE.rgb)
            RenderUtils.drawRect(35F,52F,x2-10F,54F,Color.WHITE.rgb)
            RenderUtils.drawRect(35.0, 52.0,
                timer2,54.0,Color.BLACK.rgb)
        }
        return Border(-2f, -2f, x2+10F, y2,CustomUI.radius.get())

    }

}