
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.ShadowUtils
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.alpha
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.blur
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.drawmode
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.hAnimModeValue
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.vAnimModeValue
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.textShadow
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications.Companion.animationSpeed
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.BlurUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.render.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt


/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 0.0, y: Double = 0.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    /**
     * Example notification for CustomHUD designer
     */
    companion object {
        val drawmode = ListValue("Mode", arrayOf("Tenacity", "Tenacity2","Novoline","NarleBone","Zork","Windows11","New","LiQuidWIng","LiQuidWIng-New"), "LiQuidWIng")
        val textShadow = BoolValue("TextShadow", true)

        val hAnimModeValue = ListValue("H-Animation", arrayOf("LiquidBounce", "Smooth"), "LiquidBounce")
        val vAnimModeValue = ListValue("V-Animation", arrayOf("None", "Smooth"), "Smooth")
        val animationSpeed = FloatValue("Speed", 0.5F, 0.01F, 1F)

        val blur = BoolValue("Narle-Blur", false)
        val shadowValue = FloatValue("New-ShadowStrength", 10f,0f,20f)
        val radius = FloatValue("LW-Radius", 15f,0f,20f)
        val alpha = IntegerValue("Novo-Alpha", 90,0,255)
        var Xvalue = 0.0
        var Yvalue = 0.0
    }
    private val exampleNotification = Notification("Notification", "This is example", Notification.NotifyType.INFO)
    private val exampleNotification2 = Notification("通知", "这是一个例子", Notification.NotifyType.INFO)


    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        val notifications = mutableListOf<Notification>()


        //FUCzwK YOU java.util.ConcurrentModificationException
        for ((index, notify) in LiquidBounce.hud.notifications.withIndex()) {
            GL11.glPushMatrix()

            if (notify.drawNotification(index, this.renderX.toFloat(), this.renderY.toFloat(), scale, this)) {
                notifications.add(notify)
            }

            GL11.glPopMatrix()
        }
        for (notify in notifications) {
            LiquidBounce.hud.notifications.remove(notify)
        }

        if (classProvider.isGuiHudDesigner(mc.currentScreen)) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification) && !LiquidBounce.hud.notifications.contains(
                    exampleNotification2
                )
            ) {
                if (CustomUI.Chinese.get()) {
                    LiquidBounce.hud.addNotification(exampleNotification2)
                } else {
                    LiquidBounce.hud.addNotification(exampleNotification)

                }
            }

            if (CustomUI.Chinese.get()) {
                exampleNotification2.fadeState = Notification.FadeState.STAY
                exampleNotification2.displayTime = System.currentTimeMillis()
                return Border(
                    -exampleNotification2.width.toFloat(),
                    -exampleNotification2.height.toFloat(),
                    0F,
                    0F,
                    0F
                )

            } else {
                exampleNotification.fadeState = Notification.FadeState.STAY
                exampleNotification.displayTime = System.currentTimeMillis()
                return Border(
                    -exampleNotification.width.toFloat(),
                    -exampleNotification.height.toFloat(),
                    0F,
                    0F,
                    0F
                )
            }

//            exampleNotification.x = exampleNotification.textLength + 8F
        }
        if (classProvider.isGuiChat(mc.currentScreen)) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification) && !LiquidBounce.hud.notifications.contains(
                    exampleNotification2
                )
            ) {
                if (CustomUI.Chinese.get()) {
                    LiquidBounce.hud.addNotification(exampleNotification2)
                } else {
                    LiquidBounce.hud.addNotification(exampleNotification)

                }
            }
            if (CustomUI.Chinese.get()) {
                exampleNotification2.fadeState = Notification.FadeState.STAY
                exampleNotification2.displayTime = System.currentTimeMillis()
                return Border(
                    -exampleNotification2.width.toFloat(),
                    -exampleNotification2.height.toFloat(),
                    0F,
                    0F,
                    0F
                )

            } else {
                exampleNotification.fadeState = Notification.FadeState.STAY
                exampleNotification.displayTime = System.currentTimeMillis()
                return Border(
                    -exampleNotification.width.toFloat(),
                    -exampleNotification.height.toFloat(),
                    0F,
                    0F,
                    0F
                )

            }
        }



        return null

    }
}
fun drawBoarderBlur(blurRadius: Float) {}


fun easeInBack(x: Double): Double {
    val c1 = 1.70158
    val c3 = c1 + 1

    return c3 * x * x * x - c1 * x * x
}

fun easeOutBack(x: Double): Double {
    val c1 = 1.70158
    val c3 = c1 + 1

    return 1 + c3 * (x - 1).pow(3) + c1 * (x - 1).pow(2)
}
fun drawCircle(x: Float, y: Float, radius: Float, start: Int, end: Int) {
    GlStateManager.enableBlend()
    GlStateManager.disableTexture2D()
    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
    GL11.glEnable(GL11.GL_LINE_SMOOTH)
    GL11.glLineWidth(2f)
    GL11.glBegin(GL11.GL_LINE_STRIP)
    var i = end.toFloat()
    val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
    while (i >= start) {
        var c = RenderUtils.getGradientOffset(Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()), Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get(), 1), (Math.abs(System.currentTimeMillis() / 360.0 + (i* 34 / 360) * 56 / 100) / 10)).rgb
        val f2 = (c shr 24 and 255).toFloat() / 255.0f
        val f22 = (c shr 16 and 255).toFloat() / 255.0f
        val f3 = (c shr 8 and 255).toFloat() / 255.0f
        val f4 = (c and 255).toFloat() / 255.0f
        GlStateManager.color(f22, f3, f4, f2)
        GL11.glVertex2f(
            (x + Math.cos(i * Math.PI / 180) * (radius * 1.001f)).toFloat(),
            (y + Math.sin(i * Math.PI / 180) * (radius * 1.001f)).toFloat()
        )
        i -= 360f / 90.0f
    }
    GL11.glEnd()
    GL11.glDisable(GL11.GL_LINE_SMOOTH)
    GlStateManager.enableTexture2D()
    GlStateManager.disableBlend()
}
class Notification(val title: String, val content: String, val type: NotifyType, val time: Int=1000, val animeTime: Int=350) {
    private var s: String? = null

    var n2: Int = FontLoaders.T20.getStringWidth(content)
    var textLength = Math.max(n2 as Int, 0 as Int)
    var width = this.textLength.toFloat() + 80.0f
    val height = 30
    var fadeState = FadeState.IN
    var nowY = -height
    var nowY2 = -40
    var displayTime = System.currentTimeMillis()
    var animeXTime = System.currentTimeMillis()
    var x = 0F
    var stayTimer = MSTimer()
    var notifHeight = 0F
    private var stay = 0F
    var animeYTime = System.currentTimeMillis()
    var color:Int = Color(83,78,86).rgb
    private var fadeStep = 0F
    private var firstY = 0f
    val delta = RenderUtils.deltaTime

    /**
     * Draw notification
     */
    fun drawNotification(index: Int, blurRadius: Float, y: Float, scale: Float,notifications: Notifications): Boolean {
        val renderX:Double = notifications.renderX;
        val renderY:Double = notifications.renderY;
        var string1 = ""
        var string = ""
        var string2 = ""
        val hAnimMode = hAnimModeValue.get()
        val vAnimMode = vAnimModeValue.get()
        val animSpeed = animationSpeed.get()

        var realY : Int
        var realY2 : Int


//        if (drawmode.get().equals("Novoline2")){
//            realY = 2 * 30
//        }
        if (type == NotifyType.SUCCESS) {
            s = "SUCCESS";
            string = "a"
            string1 = "o"
            string2 = "a"
        } else if (type == NotifyType.ERROR) {
            s = "ERROR";
            string = "B"
            string1 = "p"
            string2 = "b"

        } else if (type == NotifyType.WARNING) {
            s = "WARNING";
            string = "D"
            string1 = "r"
            string2 = "d"

        } else if (type == NotifyType.INFO) {
            s = "INFO";
            string = "C"
            string1 = "m"
            string2 = "c"

        }
        if (firstY == 19190.0F)
            firstY = ((index-1) * (height)).toFloat()
        else
            firstY = AnimationUtils.animate((index-1) * (height).toDouble(), firstY.toDouble(), (0.02F * delta).toDouble()).toFloat()
        var y = firstY
        if (drawmode.get().equals("LiQuidWIng-New")) {
            val width = (Fonts.newtenacity35.getStringWidth(this.content)) + 30.coerceAtLeast(60)

            GlStateManager.resetColor()

            GL11.glTranslated((-renderX).toDouble(), (-renderY).toDouble(), 0.0)
            GL11.glScalef(1F, 1F, 1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(10F, {

                RenderUtils.drawRoundedRect(
                    renderX.toFloat()-x,
                    renderY.toFloat()-y,
                    renderX.toFloat()-x+width,
                    renderY.toFloat()-y+height,
                    5F,
                    Color(0, 0, 0, 255).rgb
                )
            }, {

                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.fastRoundedRect(
                    renderX.toFloat()-x,
                    renderY.toFloat()-y,
                    renderX.toFloat()-x+width,
                    renderY.toFloat()-y+height,
                    5F
                )
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX.toDouble(), renderY.toDouble(), 0.0)

            GL11.glTranslated((-renderX).toDouble(), (-renderY).toDouble(), 0.0)
            GL11.glPushMatrix()
            RenderUtils.drawRoundedRect(
                renderX.toFloat()-x,
                renderY.toFloat()-y,
                renderX.toFloat()-x+width,
                renderY.toFloat()-y+height,
                5F,
                Color(255, 247, 230, 130).rgb
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX.toDouble(), renderY.toDouble(), 0.0)

            GL11.glTranslated((-renderX).toDouble(), (-renderY).toDouble(), 0.0)
            GL11.glPushMatrix()
            BlurUtils.blurAreaRounded(
                renderX.toFloat()-x,
                renderY.toFloat()-y,
                renderX.toFloat()-x+width,
                renderY.toFloat()-y+height,
                5F,
                CustomUI.blurValue.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX.toDouble(), renderY.toDouble(), 0.0)

            GL11.glTranslated((-renderX).toDouble(), (-renderY).toDouble(), 0.0)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat()-x,
                renderY.toFloat()-y,
                width.toFloat(),
                height.toFloat(),
                5F, 0.1f,
                Color(255, 255, 255, 0),
                Color(255, 255, 255, 110)
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX.toDouble(), renderY.toDouble(), 0.0)
            GlStateManager.resetColor()
            GL11.glTranslated((-renderX).toDouble(), (-renderY).toDouble(), 0.0)
            GL11.glPushMatrix()
            Fonts.notificationIcon80.drawString(string, renderX.toFloat()-x+3, renderY.toFloat()-y+8, Color(148, 143, 149).rgb)
            Fonts.newtenacity40.drawString(
                title,
                renderX.toFloat()-x + 3 + Fonts.notificationIcon80.getStringWidth(string) + 3,
                renderY.toFloat() -y+8,
                color
            )
            Fonts.newtenacity35.drawString(
                content,
                renderX.toFloat()-x+3 + Fonts.notificationIcon80.getStringWidth(string) + 3,
                renderY.toFloat()-y+8 + Fonts.newtenacity40.fontHeight + 3,
                type.renderColor.rgb
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX.toDouble(), renderY.toDouble(), 0.0)

        }
        if (drawmode.get().equals("Zork")){
            realY = -(index + 1) * (height + 9)
        }
        else if(drawmode.get().equals("Windows11")){
            realY = -(index + 1) * (height + 13)
        }else if(drawmode.get().equals("LiQuidWIng")){
            realY = -(index + 1) * (height + 15)
        } else {
            realY = -(index + 1) * (height + 2)
        }
        val nowTime = System.currentTimeMillis()


        var transY = nowY.toDouble()
        //Y-Axis Animation
        if (nowY != realY) {
            var pct = (nowTime - animeYTime) / animeTime.toDouble()
            if (pct > 1) {
                nowY = realY
                pct = 1.0
            } else {
                pct = EaseUtils.easeOutQuart(pct)
            }
            GL11.glTranslated(0.0, (realY - nowY) * pct, 0.0)
        } else {
            animeYTime = nowTime

        }
        GL11.glTranslated(1.0, nowY.toDouble(), 0.0)

        //X-Axis Animation
        var pct = (nowTime - animeXTime) / animeTime.toDouble()
        when (fadeState) {
            FadeState.IN -> {
                if (pct > 1) {
                    fadeState = FadeState.STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = EaseUtils.easeOutQuart(pct)
                transY += (realY - nowY) * pct
            }

            FadeState.STAY -> {
                pct = 1.0
                if ((nowTime - animeXTime) > time) {
                    fadeState = FadeState.OUT
                    animeXTime = nowTime
                }
            }

            FadeState.OUT -> {
                if (pct > 1) {
                    fadeState = FadeState.END
                    animeXTime = nowTime
                    pct = 2.0
                }
                pct = 1 - EaseUtils.easeInQuart(pct)
            }

            FadeState.END -> {
                return true
            }
        }
        val lwHeight = 25
        val mode = drawmode.get()
        val textShadow = textShadow.get()
        val blur = blur.get()


        GL11.glTranslated(width - (width * pct), 0.0, 0.0)
        GL11.glTranslatef(-width.toFloat(), 0F, 0F)

        val hud = LiquidBounce.moduleManager.getModule(
            HUD::class.java
        ) as HUD

        if (mode.equals("Tenacity")) {
            if (s == "INFO") {
                RoundedUtil.drawRound(38F, 0F, width - 50F, 28F, 4.5F, Color(192, 192, 192, 140))
                FontLoaders.Tcheck.drawString("m", 42f, 5F, Color(192, 192, 192, 255).rgb)
                Fonts.tenacitybold50.drawString(title, 60F, 3f, Color.white.rgb)
                Fonts.newtenacity40.drawString(content, 60f, 16f, Color.white.rgb)
            }

            if (s == "WARNING") {
                RoundedUtil.drawRound(38F, 0F, width - 50F, 28F, 4.5F, Color(224, 194, 30, 140))
                FontLoaders.Tcheck.drawString("r", 42f, 5F, Color(224, 194, 30, 255).rgb)
                Fonts.tenacitybold50.drawString(title, 60F, 3f, Color.white.rgb)
                Fonts.newtenacity40.drawString(content, 60f, 16f, Color.white.rgb)
            }

            if (s == "SUCCESS") {
                RoundedUtil.drawRound(38F, 0F, width - 50F, 28F, 4.5F, Color(90, 239, 87, 140))
                FontLoaders.Tcheck.drawString("o", 42f, 5F, Color(90, 239, 87, 240).rgb)
                Fonts.tenacitybold50.drawString(title, 60f, 3f, Color.white.rgb)
                Fonts.newtenacity40.drawString(content, 60f, 16f, Color.white.rgb)
            }
            if (s == "ERROR") {
                RoundedUtil.drawRound(38F, 0F, width - 50F, 28F, 4.5F, Color(206, 33, 33, 140))
                FontLoaders.Tcheck.drawString("p", 42f, 5F, Color(206, 33, 33, 240).rgb)
                Fonts.tenacitybold50.drawString(title, 60F, 3f, Color.white.rgb)
                Fonts.newtenacity40.drawString(content, 60f, 16f, Color.white.rgb)
            }

            return false
        }

        if (mode.equals("LiQuidWIng")){
            val width = (FontLoaders.F16.getStringWidth(this.content))+30.coerceAtLeast(70)
            val hight = 32

            if (CustomUI.drawMode.get().equals("描边和圆角矩形")){
                RoundedUtil.drawRound(0F-CustomUI.outlinet.get()*1.15F,0F-CustomUI.outlinet.get()*1.15F,width.toFloat() + (CustomUI.outlinet.get()*2*1.15F),hight.toFloat()+ (CustomUI.outlinet.get()*2*1.15F),CustomUI.radius.get(), Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get(),CustomUI.a.get()))
            }
            RoundedUtil.drawRound(0F,0F,width.toFloat(),hight.toFloat(),CustomUI.radius.get(), Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get(),CustomUI.a.get()))
            FontLoaders.F18.drawString(title,5,5,Color.WHITE.rgb)
            FontLoaders.F18.drawString(content,5,5 + FontLoaders.F18.FONT_HEIGHT - 2,Color.WHITE.rgb)
            Fonts.notiicon60.drawString(string2,width-5-Fonts.notiicon60.getStringWidth(string2),1 + FontLoaders.F18.FONT_HEIGHT,Color.WHITE.rgb)

        }
        if(mode.equals("Windows11")){
            var height2 = 40
            var width2 = 100.coerceAtLeast(Fonts.newtenacity45.getStringWidth(content) + 22)
            GL11.glTranslated(width2 - (width2 * pct), 0.0, 0.0)
            GL11.glTranslatef(-width2.toFloat(), 0F, 0F)
            var s1 = ""
            when (type) {
                NotifyType.ERROR -> {
                    s1 = "liquidwing/notification/error.png"
                }

                NotifyType.INFO -> {
                    s1 = "liquidwing/notification/warning.png"
                }

                NotifyType.SUCCESS -> {
                    s1 = "liquidwing/notification/cross.png"

                }

                NotifyType.WARNING -> {
                    s1 = "liquidwing/notification/warning.png"
                }
            }
            RoundedUtil.drawRound(
                0.0f,
                0.0f,
                width2.toFloat(),
                height2.toFloat(),
                4.0f,
                Color(230, 230, 230, 255)
            )
            val f  = 9F
            Fonts.newtenacity40.drawString(content, 30.0f, f,Color(10, 10, 10, 255).rgb, false)
            Fonts.newtenacity35.drawString(
                title,
                30.0f,
                f + Fonts.newtenacity35.fontHeight + 3F,
                Color(130, 130, 130, 255).rgb,
                false
            )

            RenderUtils.drawImage(MinecraftInstance.classProvider.createResourceLocation(s1),2,6,28,28)


        }
        if (mode.equals("Tenacity2")) {
            GlStateManager.resetColor()
            val height = 17F
            val width =
                20F + net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.stringWidth(string1) + net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacity.tenacity18.tenacity18.stringWidth(
                    content
                )
            val color =
                when (type) {
                    NotifyType.SUCCESS -> Color(20, 250, 90)
                    NotifyType.ERROR -> Color(255, 30, 30)
                    NotifyType.WARNING -> Color(255, 255, 0)
                    NotifyType.INFO -> Color(255, 255, 255)
                }.rgb

            val baseColor = Color(20, 20, 20, 110)
            val colorr = ColorUtil.interpolateColorC(
                baseColor,
                Color(ColorUtil.applyOpacity(color, .3f)),
                .5f
            )

            GL11.glScaled(pct, pct, pct)
            GL11.glTranslatef(-width.toFloat() / 2, -height.toFloat() / 2, 0F)
            RoundedUtil.drawRound(
                0F, 0F, width, height, 2.5F, colorr
            )
            if (!textShadow) {
                net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.drawString(
                    string1,
                    7F,
                    (height / 2) - (net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.height / 4) - 3,
                    color
                )
                net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacity.tenacity18.tenacity18.drawString(
                    content,
                    10F + net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.stringWidth(string1),
                    (height / 2) - (net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacity.tenacity18.tenacity18.height / 2),
                    Color.WHITE.rgb
                )
            } else {
                net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.drawStringWithShadow(
                    string1,
                    7.0,
                    ((height / 2) - (net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.height / 4) - 3).toDouble(),
                    color
                )
                net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacity.tenacity18.tenacity18.drawStringWithShadow(
                    content,
                    (10F + net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35.stringWidth(
                        string1
                    )).toDouble(),
                    ((height / 2) - (net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacity.tenacity18.tenacity18.height / 2)).toDouble(),
                    Color.WHITE.rgb
                )
            }
            GlStateManager.resetColor()
            return false
        }

        if (mode.equals("Novoline")) {
            GlStateManager.resetColor()

            val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
            GL11.glScaled(pct, pct, pct)
            GL11.glTranslatef(-width.toFloat() / 2, -height.toFloat() / 2, 0F)
            RenderUtils.drawRect(0F, 0F, width.toFloat() - 20, height.toFloat(), Color(0, 0, 0, alpha.get()))
            RenderUtils.drawShadow(0f, 0f, width.toFloat() - 20, height.toFloat())
            RenderUtils.drawShadow(0f, 0f, width.toFloat() - 20, height.toFloat())

            RenderUtils.drawGradientSideways(
                0.0,
                height - 1.7,
                (width * ((nowTime - displayTime) / (animeTime * 2F + time))).toDouble(),
                height.toDouble(),
                Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()).rgb,
                Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get()).rgb
            )
            Fonts.sfbold40.drawStringWithShadow("$title", 24.5F.toInt(), 7, Color.WHITE.rgb)
            Fonts.sfbold35.drawStringWithShadow(
                "$content" + " (" + BigDecimal(((time - time * ((nowTime - displayTime) / (animeTime * 2F + time))) / 1000).toDouble()).setScale(
                    1,
                    BigDecimal.ROUND_HALF_UP
                ).toString() + "s)",
                24.5F.toInt(), 17.3F.toInt(), Color.WHITE.rgb
            )
            RenderUtils.drawFilledCircle(13, 15, 8.5F, Color.BLACK)
            Fonts.notificationIcon80.drawString(string, 3, 8, Color.WHITE.rgb)
            drawCircle(12.6f, 15.0f, 8.8f, 0, 720)
            GlStateManager.resetColor()
            return false
        }
        if (mode.equals("New")) {

            GlStateManager.resetColor()

            val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
            GL11.glScaled(pct, pct, pct)
            RenderUtils.drawRect(0F, 0F, width.toFloat() - 20, height.toFloat(), Color(0, 0, 0, alpha.get()))

            RenderUtils.drawGradientSideways(
                0.0,
                height - 1.7,
                (width * ((nowTime - displayTime) / (animeTime * 2F + time))).toDouble(),
                height.toDouble(),
                Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()).rgb,
                Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get()).rgb
            )
            Fonts.pop40.drawStringWithShadow("$title", 24.5F.toInt(), 7, Color.WHITE.rgb)
            Fonts.pop35.drawStringWithShadow(
                "$content" + " (" + BigDecimal(((time - time * ((nowTime - displayTime) / (animeTime * 2F + time))) / 1000).toDouble()).setScale(
                    1,
                    BigDecimal.ROUND_HALF_UP
                ).toString() + "s)",
                24.5F.toInt(), 17.3F.toInt(), Color.WHITE.rgb
            )
            Fonts.notificationIcon80.drawString(string, 3f, 8f, Color.WHITE.rgb,true)

            GlStateManager.resetColor()
            return false
        }
        if (mode.equals("Zork")) {
            val width = 100.coerceAtLeast((Fonts.misans40.getStringWidth(this.content))+22)
            val height = 45

            RoundedUtil.drawRound(
                0f,
                -1f, width.toFloat(), height.toFloat() - 10f, 3f, Color(29, 29, 31, 255)
            )
            // A B
            if (type == NotifyType.SUCCESS) {
                Fonts.notiicon60.drawString(
                    "A",
                    7F,
                    11F,
                    Color.WHITE.rgb
                )
            }
            if (type == NotifyType.ERROR) {
                Fonts.notiicon60.drawString(
                    "B",
                    7F,
                    11F,
                    Color.WHITE.rgb
                )
            }
            // C D
            if (type == NotifyType.WARNING) {
                Fonts.notiicon60.drawString(
                    "C",
                    7F,
                    11F,
                    Color.WHITE.rgb
                )
            }
            if (type == NotifyType.INFO) {
                Fonts.notiicon60.drawString(
                    "D",
                    7F,
                    11F,
                    Color.WHITE.rgb
                )
            }
            Fonts.misans35.drawString(
                title, 28.0F,
                ((Fonts.misans40.fontHeight / 2f).toDouble() + 5).toFloat(), Color.WHITE.rgb, false
            )
            Fonts.misans30.drawString(
                content,
                28.0F,
                ((Fonts.misans40.fontHeight / 2f).toDouble() + 15).toFloat(),
                Color(255, 255, 255, 100).rgb,
                false
            )
            RenderUtils.drawGoodCircle(
                width - 12.0,
                ((Fonts.font40.fontHeight / 2f).toDouble() + 12),
                2f,
                if (type == NotifyType.INFO || type == NotifyType.SUCCESS) Color(79, 216, 7).rgb else Color(
                    255,
                    68, 50).rgb)
            return false
        }
        if (mode.equals("NarleBone")) {
            val realY = -(index + 1) * (height*1.3)
            val nowTime = System.currentTimeMillis()
            val image = MinecraftInstance.classProvider.createResourceLocation("liquidwing/7ad/notification/" + type.name + ".png")

            //Y-Axis Animation
            if (nowY != realY.toInt()) {
                var pct = (nowTime - animeYTime) / animeTime.toDouble()
                if (pct > 1) {
                    nowY = realY.toInt()
                    pct = 1.0
                } else {
                    pct = easeOutBack(pct)
                }
                GL11.glTranslated(0.0, (realY - nowY) * (pct), 0.0)
            } else {
                animeYTime = nowTime
            }
            GL11.glTranslated(0.0, nowY.toDouble(), 0.0)

            //X-Axis Animation
            var pct = (nowTime - animeXTime) / animeTime.toDouble()
            when (fadeState) {
                FadeState.IN -> {
                    if (pct > 1) {
                        fadeState = FadeState.STAY
                        animeXTime = nowTime
                        pct = 1.0
                    }
                    pct = easeOutBack(pct)
                }

                FadeState.STAY -> {
                    pct = 1.0
                    if ((nowTime - animeXTime) > time) {
                        fadeState = FadeState.OUT
                        animeXTime = nowTime
                    }
                }

                FadeState.OUT -> {
                    if (pct > 1) {
                        fadeState = FadeState.END
                        animeXTime = nowTime
                        pct = 1.0
                    }
                    pct = 1 - easeInBack(pct)
                }

                FadeState.END -> {
                    return true
                }
            }

            val originalX =x
            val originalY =y
            val transX = width - (width * pct) - width
            var transY = nowY.toDouble()
            GL11.glTranslatef(-width.toFloat(), 0F, 0F)
            GL11.glTranslated(width - (width * pct), 0.0, 0.0)
            if (blur) {
                GL11.glTranslatef(-width.toFloat() - 183, 0F, 0F)
                GL11.glTranslated(width - (width * pct) +281, 0.0, 0.0)
                GL11.glTranslatef(-originalX + 121, -originalY  , 0F)
                GL11.glPushMatrix()
                BlurBuffer.blurAreacustomradius(x - 122, y, width + 22F, height.toFloat(), 7F)
                GL11.glPopMatrix()
                GL11.glTranslatef(originalX - 100, originalY+ 1, 0F)
            }

            var stringcolor = Color(0,155,255,255)
            var stringtitle = ""
            if (type.name.equals("SUCCESS")){
                stringtitle="Enabled"
                stringcolor =  Color(0,155,255,255)
            }
            if (type.name.equals("ERROR")){
                stringtitle="Disabled"
                stringcolor =  Color(255,40,0,255)
            }
            RoundedUtil.drawRound(-22f,-0f,(max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)), -22.0F)*0.75f),height.toFloat(),2f,Color(20,20,20,50))
            RoundedUtil.drawRound(-22f,-0f,width*0.75f,height.toFloat(),4f,Color(50,50,50,50))
            FontLoaders.F16.drawStringWithShadow(content, 0.0, 5.23, stringcolor.rgb)
            FontLoaders.F16.drawStringWithShadow(stringtitle, 0.0, 17.87, -1)
            //RenderUtils.drawImage(image, -19,  3, 10, 10)
            RenderUtils.drawFilledCircle(-11f,8.5f,2.6f,stringcolor)

            return false
        }
        when (fadeState) {
            FadeState.IN -> {
                if (x < width) {
                    if (hAnimMode.equals("smooth", true))
                        x = AnimationUtils.animate(width.toDouble(), x.toDouble(),
                            (animSpeed * 0.025F * delta).toDouble()
                        ).toFloat()
                    else
                        x = AnimationUtils.easeOut(fadeStep, width.toFloat()) * width
                    fadeStep += delta / 4F
                }
                if (x >= width) {
                    fadeState = FadeState.STAY
                    x = width.toFloat()
                    fadeStep = width.toFloat()
                }

                stay = 60F
                stayTimer.reset()
            }

            FadeState.STAY -> {
                if (stay > 0) {
                    stay = 0F
                    stayTimer.reset()
                }
                if (stayTimer.hasTimePassed(displayTime))
                    fadeState = FadeState.OUT
            }

            FadeState.OUT -> if (x > 0) {
                if (hAnimMode.equals("smooth", true))
                    x = AnimationUtils.animate((-width / 2F).toDouble(), x.toDouble(),
                        (animSpeed * 0.025F * delta).toDouble()
                    ).toFloat()
                else
                    x = AnimationUtils.easeOut(fadeStep, width.toFloat()) * width

                fadeStep -= delta / 4F
            } else
                fadeState = FadeState.END

            FadeState.END -> LiquidBounce.hud.removeNotification(this)
        }
        return false
    }

    enum class NotifyType(var renderColor: Color) {
        SUCCESS(Color(63,207,111)),
        ERROR(Color(176,57,50)),
        WARNING(Color(0xF5FD00)),
        INFO(Color(0,0,0));
    }



    enum class FadeState { IN, STAY, OUT, END }}
