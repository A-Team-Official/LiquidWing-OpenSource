/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package ad.hud

import ad.utils.RenderUtils
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding
import net.ccbluex.liquidbounce.ui.client.fonts.api.FontRenderer
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.utils.BlurUtils2
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

@ElementInfo(name = "Keystrokes")
class Keystrokes : Element(5.0, 25.0, 1.5F, Side.default()) {
    private val keys = ArrayList<KeyStroke>()
    private val juulkeys = ArrayList<KeyStroke>()

    private val backGroundRedValue = IntegerValue("BackGroundRed", 0, 0, 255)
    private val backGroundGreenValue = IntegerValue("BackGroundGreen", 0, 0, 255)
    private val backGroundBlueValue = IntegerValue("BackGroundBlue", 0, 0, 255)
    private val backGroundAlphaValue = IntegerValue("BackGroundAlpha", 170, 0, 255)
    private val textRedValue = IntegerValue("TextRed", 255, 0, 255)
    private val textGreenValue = IntegerValue("TextGreen", 255, 0, 255)
    private val textBlueValue = IntegerValue("TextBlue", 255, 0, 255)
    private val textAlphaValue = IntegerValue("TextAlpha", 255, 0, 255)
    private val highLightPercent = FloatValue("HighLightPercent", 0.5F, 0F, 1F)
    private val animSpeedValue = IntegerValue("AnimationSpeed", 300, 0, 700)
    val keyStyleValue = ListValue("Mode", arrayOf("Tenacity","Jello"), "Tenacity")
    private val radius = FloatValue("T-Radius", 3F, 0F, 10F)

    var floatX: Float? = null
    var floatY: Float? = null
    init {
        keys.add(KeyStroke(mc.gameSettings.keyBindForward, 16, 0, 15, 15).initKeyName())
        keys.add(KeyStroke(mc.gameSettings.keyBindLeft, 0, 16, 15, 15).initKeyName())
        keys.add(KeyStroke(mc.gameSettings.keyBindBack, 16, 16, 15, 15).initKeyName())
        keys.add(KeyStroke(mc.gameSettings.keyBindRight, 32, 16, 15, 15).initKeyName())
        keys.add(KeyStroke(mc.gameSettings.keyBindAttack, 0, 32, 23, 15).initKeyName("L"))
        keys.add(KeyStroke(mc.gameSettings.keyBindUseItem, 24, 32, 23, 15).initKeyName("R"))
        juulkeys.add(KeyStroke(mc.gameSettings.keyBindForward, 17, 0, 15, 15).initKeyName())
        juulkeys.add(KeyStroke(mc.gameSettings.keyBindLeft, 0, 17, 15, 15).initKeyName())
        juulkeys.add(KeyStroke(mc.gameSettings.keyBindBack, 17, 17, 15, 15).initKeyName())
        juulkeys.add(KeyStroke(mc.gameSettings.keyBindRight, 34, 17, 15, 15).initKeyName())
//        juulkeys.add(KeyStroke(mc.gameSettings.keyBindAttack, 0, 32, 23, 15).initKeyName("L"))
//        juulkeys.add(KeyStroke(mc.gameSettings.keyBindUseItem, 24, 32, 23, 15).initKeyName("R"))
        juulkeys.add(KeyStroke(mc.gameSettings.keyBindJump,0,34,49,15).initKeyName("SPACE"))
    }


    override fun drawElement(): Border {
        floatX = renderX.toFloat()
        floatY = renderY.toFloat()
        val backGroundColor = Color(backGroundRedValue.get(), backGroundGreenValue.get(), backGroundBlueValue.get(), backGroundAlphaValue.get())
        val textColor = Color(textRedValue.get(), textGreenValue.get(), textBlueValue.get(), textAlphaValue.get())

        if(keyStyleValue.get().equals("Jello")) {
            for (keyStroke in keys) { keyStroke.renderJelloBlur(this.renderX.toFloat(), this.renderY.toFloat(), scale) }
            RenderUtils.drawImage2(ResourceLocation("liquidwing/keystrokes.png"), -3.5f, -3.5f, 54, 54)
            for (keyStroke in keys) { keyStroke.renderJelloIndicator(animSpeedValue.get(), backGroundColor, textColor, highLightPercent.get(), this.renderX.toFloat(), this.renderY.toFloat(), scale) }
        }

        if(keyStyleValue.get().equals("Tenacity")) {
            for (keyStroke in juulkeys) {
                keyStroke.renderJuul(animSpeedValue.get(), backGroundColor, highLightPercent.get(), net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.tenacity.tenacity14.tenacity14, 10f, this.renderX.toFloat(), this.renderY.toFloat(), scale,radius.get())
            }

//            RenderUtils.drawRoundedCornerRect(0f, 32f, 23f, 47f, 0f, if (mc.gameSettings.keyBindAttack.isKeyDown) { Color(160, 160, 160, 130).rgb } else { Color(0, 0, 0, 130).rgb } )
//            RenderUtils.drawRoundedCornerRect(24f, 32f, 47f, 47f, 0f, if (mc.gameSettings.keyBindUseItem.isKeyDown) { Color(160, 160, 160, 130).rgb } else { Color(0, 0, 0, 130).rgb } )
//            val juulLeft = "LMB"
//            val juulRight ="RMB"
//            Fonts.rise25.drawString(juulLeft, 10.4f - (Fonts.rise25.getStringWidth(juulLeft) / 2f) + 1f, 41f - (Fonts.rise25.fontHeight / 2f) + 2f, if (mc.gameSettings.keyBindAttack.isKeyDown) { Color(0, 0, 0, 255).rgb } else { Color(255, 255, 255, 255).rgb })
//            Fonts.rise25.drawString(juulRight, 34.6f - (Fonts.rise25.getStringWidth(juulRight).toFloat() / 2f) + 1f, 41f - (Fonts.rise25.fontHeight.toFloat() / 2f) + 2f, if (mc.gameSettings.keyBindUseItem.isKeyDown) { Color(0, 0, 0, 255).rgb } else { Color(255, 255, 255, 255).rgb })
        }
        return Border(0F, 0F, 47F, 47F,0f)
    }
}

class KeyStroke(val key: IKeyBinding, val posX: Int, val posY: Int, val width: Int, val height: Int) {
    var keyName = "KEY"

    private var lastClick = false
    private val animations = ArrayList<Long>()

    fun renderJuul(speed: Int, bgColor: Color, highLightPct: Float, font: FontRenderer, blurRadius: Float, renderX: Float, renderY: Float, scale: Float,radius:Float) {
        GL11.glPushMatrix()
        GL11.glTranslatef(posX.toFloat(), posY.toFloat(), 0F)

        if (blurRadius != 0f) {
            BlurUtils2.draw((renderX + posX) * scale, (renderY + posY) * scale, width * scale, height * scale, blurRadius)
        }

        val highLightColor = Color(255 - ((255 - bgColor.red) * highLightPct).toInt(), 255 - ((255 - bgColor.blue) * highLightPct).toInt(), 255 - ((255 - bgColor.green) * highLightPct).toInt())
        val clickAlpha = 255 - (255 - bgColor.alpha) * highLightPct
        val centerX = width / 2
        val centerY = height / 2
        val nowTime = System.currentTimeMillis()

        val rectColor = if (lastClick && animations.isEmpty()) { ColorUtils.reAlpha(highLightColor, clickAlpha.toInt()) } else { bgColor }
        RoundedUtil.drawRound(0F, 0F, width.toFloat(), height.toFloat(), radius,rectColor)

        val removeAble = ArrayList<Long>()
        for (time in animations) {
            val pct = (nowTime - time) / (speed.toFloat())
            if (pct> 1) {
                removeAble.add(time)
                continue
            }
           RenderUtils.drawLimitedCircle(0F, 0F, width.toFloat(), height.toFloat(), centerX, centerY, (width * 0.7F) * pct, Color(255 - ((255 - highLightColor.red) * pct).toInt(), 255 - ((255 - highLightColor.green) * pct).toInt(), 255 - ((255 - highLightColor.blue) * pct).toInt(), 255 - ((255 - clickAlpha) * pct).toInt()))
        }
        for (time in removeAble) {
            animations.remove(time)
        }
        if (!lastClick && key.isKeyDown) {
            animations.add(nowTime)
        }
        lastClick = key.isKeyDown

        font.drawString(keyName, centerX - (font.stringWidth(keyName) / 2), centerY - (font.height/2), Color(255,255,255).rgb)

        GL11.glPopMatrix()
    }

    fun renderJelloBlur(renderX: Float, renderY: Float, scale: Float) {
        GL11.glPushMatrix()
        GL11.glTranslatef(posX.toFloat(), posY.toFloat(), 0F)
        BlurUtils2.draw((renderX + posX) * scale, (renderY + posY) * scale, width * scale, height * scale, 10f)
        GL11.glPopMatrix()
    }


    fun renderJelloIndicator(speed: Int, bgColor: Color, textColor: Color, highLightPct: Float, renderX: Float, renderY: Float, scale: Float) {
        GL11.glPushMatrix()
        GL11.glTranslatef(posX.toFloat(), posY.toFloat(), 0F)

        val highLightColor = Color(255 - ((255 - bgColor.red) * highLightPct).toInt(), 255 - ((255 - bgColor.blue) * highLightPct).toInt(), 255 - ((255 - bgColor.green) * highLightPct).toInt())
        val clickAlpha = 255 - (255 - bgColor.alpha) * highLightPct
        val centerX = width / 2
        val centerY = height / 2
        val nowTime = System.currentTimeMillis()
        val rectColor = if (lastClick && animations.isEmpty()) { ColorUtils.reAlpha(highLightColor, clickAlpha.toInt()) } else { Color(0f,0f,0f,0f) }
        RenderUtils.drawRect(-0.1F, 0F, width.toFloat() + 0.1f, height.toFloat() + 0.1f, rectColor)

        val removeAble = ArrayList<Long>()
        for (time in animations) {
            val pct = (nowTime - time) / (speed.toFloat())
            if (pct> 1) {
                removeAble.add(time)
                continue
            }
            RenderUtils.drawLimitedCircle(0F, 0F, width.toFloat(), height.toFloat(), centerX, centerY, (width * 0.7F) * pct, Color(255 - ((255 - highLightColor.red) * pct).toInt(), 255 - ((255 - highLightColor.green) * pct).toInt(), 255 - ((255 - highLightColor.blue) * pct).toInt(), 255 - ((255 - clickAlpha) * pct).toInt()))
        }
        for (time in removeAble) {
            animations.remove(time)
        }
        if (!lastClick && key.isKeyDown) {
            animations.add(nowTime)
        }
        lastClick = key.isKeyDown
        GL11.glPopMatrix()
    }

    fun initKeyName(): KeyStroke {
        keyName = Keyboard.getKeyName(key.keyCode)
        return this
    }

    fun initKeyName(name: String): KeyStroke {
        keyName = name
        return this
    }
}
