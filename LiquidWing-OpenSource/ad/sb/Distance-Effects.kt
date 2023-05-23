package ad.sb

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.PotionData
import net.ccbluex.liquidbounce.utils.Translate
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color
import java.util.*
import kotlin.math.pow

@ElementInfo(name = "Effects-NEVERLOSE")
class DisEffects(x: Double =97.0, y: Double = 141.0, scale: Float = 1F) : Element() {

    private val potionMap: MutableMap<IPotion, PotionData?> = HashMap()

    var timer = MSTimer()
    var easingwith = 0f
    var backamin = 0f
    var easinghealth = 0f
    var healthamin = 0f
    /**
     * Draw the entity.
     */
    override fun drawElement(): Border {
        RoundedUtil.drawRound(12.20f,-7.32f,
            100f,
            Fonts.sfui30.fontHeight+5f,
            2f,
            Color(0,0,0,255))
        RoundedUtil.drawRound(
            12.20f,
            -7.32f,
            100f,
            easinghealth + 0f,
            2f,
            Color(40, 40, 40, 200)
        )
        GlStateManager.pushMatrix()
        var namewith = 0f
        var namewith3 = 0f
        var namehight = 0f
        var y = 0
        for (potionEffect in Objects.requireNonNull(mc.thePlayer)!!.activePotionEffects) {
            val potion = functions.getPotionById(potionEffect.potionID)
            val name = functions.formatI18n(potion.name)
            val potionData: PotionData?
            if (potionMap.containsKey(potion) && potionMap[potion]!!.level == potionEffect.amplifier) potionData =
                potionMap[potion] else potionMap[potion] =
                PotionData(potion, Translate(0f, -40f + y), potionEffect.amplifier).also {
                    potionData = it
                }
            var flag = true
            for (checkEffect in mc.thePlayer!!.activePotionEffects) if (checkEffect.amplifier == potionData!!.level) {
                flag = false
                break
            }
            if (flag) potionMap.remove(potion)
            var potionTime: Int
            var potionMaxTime: Int
            try {
                potionTime = potionEffect.getDurationString().split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].toInt()
                potionMaxTime = potionEffect.getDurationString().split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].toInt()
            } catch (ignored: Exception) {
                potionTime = 100
                potionMaxTime = 1000
            }
            val lifeTime = potionTime * 60 + potionMaxTime
            if (potionData!!.getMaxTimer() == 0 || lifeTime > potionData.getMaxTimer().toDouble()) potionData.maxTimer =
                lifeTime
            var state = 0.0f
            if (lifeTime >= 0.0) state = (lifeTime / potionData.getMaxTimer().toFloat().toDouble() * 100.0).toFloat()
            val position = Math.round(potionData.translate.y / 1.40f - 7.22f)
            state = Math.max(state, 2.0f)
            potionData.translate.interpolate(0f, y.toFloat(), 0.1)
            potionData.animationX = RenderUtils.getAnimationState2(
                potionData.getAnimationX().toDouble(), (1.2f * state).toDouble(), (Math.max(
                    10.0f, Math.abs(
                        potionData.animationX - 1.2f * state
                    ) * 15.0f
                ) * 0.3f).toDouble()
            ).toFloat()

            val namewith2 =
                Fonts.sfui30.getStringWidth(name+" "+ intToRomanByGreedy(potionEffect.amplifier + 1))
            namewith =
                Fonts.sfui30.getStringWidth(name+" "+ intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
            if (namewith2 > namewith3) {
                namewith3 = namewith2.toFloat()
            }
            val posY = potionData.translate.y / 2.5f - 3f
            namehight = potionData.translate.y/ 2.5f-8f
            Fonts.sfui30.drawString(
                name+" "+intToRomanByGreedy(potionEffect.amplifier + 1),
                15f,
                -(posY - Fonts.sfui30.fontHeight),
                -1
            )

            Fonts.sfui30.drawString(potionEffect.getDurationString(), 93f, -(posY - 9.76f), -1)
            y -= 35
        }
        updateAnimwith(namewith3 * 0.98f + 42.68f)
        updateAnimhealth(-namehight * 1.1f +14.63f)


        GlStateManager.popMatrix()
        net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.Icons.icons_20.icons_20.drawString("q",16,-2,Color(4, 188, 255).rgb)
        Fonts.sfui35.drawString("Potions",33,-3,Color(255,255,255,255).rgb)

        return Border(12.20f,
            -7.32f,
            100f,
            easinghealth + 2f,2f)
    }
    fun updateAnimwith(easing: Float) {
        easingwith += ((easing - easingwith) / 2.0F.pow(10.0F - 3.5f)) * RenderUtils.deltaTime

        if (!timer.hasTimePassed(2)){
            return

        }else{
            backamin+=1
        }


        timer.reset()

    }

    fun updateAnimhealth(easing: Float) {
        easinghealth += ((easing - easinghealth) / 2.0F.pow(10.0F - 3.5f)) * RenderUtils.deltaTime

        if (!timer.hasTimePassed(2)){
            return

        }else{
            healthamin+=1
        }


        timer.reset()

    }

    private fun intToRomanByGreedy(num: Int): String {
        var num = num
        val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val symbols = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
        val stringBuilder = StringBuilder()
        var i = 0
        while (i < values.size && num >= 0) {
            while (values[i] <= num) {
                num -= values[i]
                stringBuilder.append(symbols[i])
            }
            i++
        }
        return stringBuilder.toString()
    }

    companion object {
        private val blur = BoolValue("Blur", true)
    }
}