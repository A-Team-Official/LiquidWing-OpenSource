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
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


@ElementInfo(name = "Session-NOVO")
class NovoSession(x: Double = 10.0, y: Double = 29.0, scale: Float = 1F,
                  side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)
) : Element(x, y, scale, side) {

    private val outline = BoolValue("Outline",false)
    private val linewidth = FloatValue("LineWidth",2f,0f,5f)
    private val drect = BoolValue("Rect",true)
    private val shadowValue = FloatValue("Shadow-Strength", 10f,0f,20f)
    private val colorModeValue = ListValue("Rect-Color", arrayOf("Custom","Gident"), "Gident")
    private val gidentspeed = IntegerValue("GidentSpeed", 100, 1, 1000)
    private val distanceValue = IntegerValue("Line-Distance", 321, 0, 400)
    private val gradientAmountValue = IntegerValue("Gradient-Amount", 20, 1, 50)
    private val radiusValue = FloatValue("Radius", 0f, 0f, 10f)
    private val blur = BoolValue("Blur",false)
    private val bradius = FloatValue("Blur-Strength", 10f,0f,50f)
    private val red = IntegerValue("Rect-R", 0, 0, 255)
    private val green = IntegerValue("Rect-G", 0, 0, 255)
    private val blue = IntegerValue("Rect-B", 0, 0, 255)
    private val bgredValue = IntegerValue("Bg-R", 0, 0, 255)
    private val bggreenValue = IntegerValue("Bg-G", 0, 0, 255)
    private val bgblueValue = IntegerValue("Bg-B", 0, 0, 255)
    private val bgalphaValue = IntegerValue("Bg-Alpha", 105, 0, 255)
    private val fshadow = BoolValue("FontShadow",true)
    val y2 = (Fonts.pop35.fontHeight * 5 + 11f) * 1.1f
    val x2 = 101f * 1.1f
    val floatX = renderX.toFloat()
    val floatY = renderY.toFloat()
    override fun shadow() {
        RenderUtils.drawRoundedRect2(0f, -2.3f, x2,y2 - 0.3f, radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get() + 190).rgb)
    }


    override fun drawElement(): Border {

        val y2 = (Fonts.pop35.fontHeight * 5 + 11f + 3f) * 1.1f
        val x2 = 101f * 1.20f


        val time: String
        time = if (Minecraft.getMinecraft().isSingleplayer) {
            "SinglePlayer"
        } else {
            val durationInMillis: Long = System.currentTimeMillis() - Recorder.startTime
            val second = durationInMillis / 1000 % 60
            val minute = durationInMillis / (1000 * 60) % 60
            val hour = durationInMillis / (1000 * 60 * 60) % 24
            String.format("%02d:%02d:%02d", hour, minute, second)
        }

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
            RenderUtils.drawRoundedRect2(0f, -3f, x2,y2 - 3f , radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get()).rgb)
            GL11.glPopMatrix()

        },{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.drawRoundedRect2(0f, -3f, x2,y2 - 3f , radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get()).rgb)
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)



        RenderUtils.drawRoundedRect2(0f, -3f, x2,y2 - 3f , radiusValue.get(), Color(bgredValue.get(), bggreenValue.get(), bgblueValue.get(), bgalphaValue.get()).rgb)


        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.CustomBlurRoundArea(
                renderX.toFloat(), renderY.toFloat() -3f, x2, y2 - 3f, radiusValue.get(),bradius.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)

        }
        if (outline.get()){
            RenderUtils.drawGidentOutlinedRoundedRect(0.0, -3.0, x2.toDouble(),y2 - 3.0 , radiusValue.get().toDouble(),linewidth.get())
        }
        //RenderUtils.drawShadow(2f, -2f, x2, y2,)
        Fonts.pop40.drawCenteredString("Session Info", x2 / 2f, 4f, Color.WHITE.rgb, fshadow.get())

        Fonts.sicon40.drawString("B",3F,(Fonts.pop35.fontHeight + 13).toFloat(),Color.WHITE.rgb,fshadow.get())
        Fonts.pop35.drawString("Play Time",5F + Fonts.sicon40.getStringWidth("B"),
            (Fonts.pop35.fontHeight + 12).toFloat(), Color.WHITE.rgb,fshadow.get())

        Fonts.pop35.drawString("$time",(x2  - Fonts.pop35.getStringWidth("$time") - 3f),
            Fonts.pop35.fontHeight + 12f,Color.WHITE.rgb,fshadow.get())

        Fonts.sicon40.drawString("F",3F,(Fonts.pop35.fontHeight * 2 + 18).toFloat(),Color.WHITE.rgb,fshadow.get())

        Fonts.pop35.drawString("Kills"  , 5F + Fonts.sicon40.getStringWidth("F"),
            (Fonts.pop35.fontHeight * 2 + 17).toFloat(), Color.WHITE.rgb,fshadow.get())

        Fonts.pop35.drawString(LiquidBounce.combatManager.kills.toString(),(x2 -Fonts.pop35.getStringWidth(
            LiquidBounce.combatManager.kills.toString()
        )-3F),
            Fonts.pop35.fontHeight * 2 + 17f,Color.WHITE.rgb,fshadow.get())

        Fonts.sicon40.drawString("D",3F,(Fonts.pop35.fontHeight * 3 + 22).toFloat(),Color.WHITE.rgb,fshadow.get())
        Fonts.pop35.drawString("Win" , 5F + Fonts.sicon40.getStringWidth("D"),
            (Fonts.pop35.fontHeight * 3 + 21).toFloat(), Color.WHITE.rgb,fshadow.get())
        Fonts.pop35.drawString("${Recorder.win}",(x2 -Fonts.pop35.getStringWidth("${Recorder.win}") -3F) ,
            Fonts.pop35.fontHeight * 3 + 23f,Color.WHITE.rgb,fshadow.get())



        for (i in 0..(gradientAmountValue.get()-1)) {
            val colorMode = colorModeValue.get()
            val x3 = 100 * 1.2f
            val barStart = i.toDouble() / gradientAmountValue.get().toDouble() * x3
            val barEnd = (i + 1).toDouble() / gradientAmountValue.get().toDouble() * x3
            if (drect.get()) {
                RenderUtils.drawGradientSideways(
                    0 + barStart, -3.0, barEnd, -1.6, when {
                        colorMode.equals("Custom", ignoreCase = true) -> Color(red.get(), green.get(), blue.get()).rgb
                        colorMode.equals("Gident", ignoreCase = true) -> RenderUtils.getGradientOffset3(
                            Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get()),
                            Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), i),
                            (Math.abs(
                                System.currentTimeMillis() / gidentspeed.get()
                                    .toDouble() + i * distanceValue.get()
                            ) / 10)
                        ).rgb

                        else -> Color.WHITE.rgb
                    },
                    when {
                        colorMode.equals("Custom", ignoreCase = true) -> Color(red.get(), green.get(), blue.get()).rgb
                        colorMode.equals("Gident", ignoreCase = true) -> RenderUtils.getGradientOffset(
                            Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get()),
                            Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), 1),
                            (Math.abs(
                                System.currentTimeMillis() / gidentspeed.get()
                                    .toDouble() + i * distanceValue.get()
                            ) / 10)
                        ).rgb

                        else -> Color.WHITE.rgb
                    }
                )
            }
        }

        return Border(-2f, -2f, x2, y2,radiusValue.get())
    }
}