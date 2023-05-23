package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.PotionData
import net.ccbluex.liquidbounce.utils.Translate
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import kotlin.math.pow

@ElementInfo(name = "NarleBone-Effects")
class NarleEffects(x: Double =97.0, y: Double = 141.0, scale: Float = 1F) : Element() {

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
            //  RenderUtils.drawRectPotion(0, potionData.translate.getY(), 120, potionData.translate.getY() + 30F, ClientUtils.reAlpha(HanaBiColors.GREY.c, 0.1F));
            // RenderUtils.drawRectPotion(0, potionData.translate.getY(), potionData.animationX, potionData.translate.getY() + 30F, ClientUtils.reAlpha((new Color(34, 24, 20)).brighter().getRGB(), 0.3F));
            //  RenderUtils.drawShadowWithCustomAlpha(0, Math.round(potionData.translate.getY()), 120, 30, 200);
            val namewith2 =
                Fonts.font35.getStringWidth(name + " " + intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
            namewith =
                Fonts.font35.getStringWidth(name + " " + intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
            if (namewith2 > namewith3) {
                namewith3 = namewith2
            }
            val posY = potionData.translate.y / 2.5f
            namehight = potionData.translate.y/ 2.5f-8f
            Fonts.font35.drawString(
                name + " " + intToRomanByGreedy(potionEffect.amplifier + 1),
                29f,
                -(posY - mc.fontRendererObj.fontHeight),
                -1
            )

            Fonts.font35.drawString(potionEffect.getDurationString(), namewith + 32, -(posY - 9.76f), -1)
            if (potion.hasStatusIcon) {
                GlStateManager.pushMatrix()
                GL11.glDisable(2929)
                GL11.glEnable(3042)
                GL11.glDepthMask(false)
                OpenGlHelper.glBlendFunc(770, 771, 1, 0)
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                val statusIconIndex = potion.statusIconIndex
                mc.textureManager.bindTexture(classProvider.createResourceLocation("textures/gui/container/inventory.png"))
                GL11.glPushMatrix()
                GL11.glScaled(0.55, 0.55, 1.0)
                mc2.ingameGUI.drawTexturedModalRect(
                    30f,
                    -(position + 15.85f + 7.07f), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18
                )
                GL11.glPopMatrix()
                GL11.glDepthMask(true)
                GL11.glDisable(3042)
                GL11.glEnable(2929)
                GlStateManager.popMatrix()
            }
            y -= 35
        }
        updateAnimwith(namewith3 * 0.98f + 42.68f)
        updateAnimhealth(-namehight * 1.1f +14.63f)

        RoundedUtil.drawRound(
            12.20f,
            -7.32f,
            easingwith,
            easinghealth,
            2f,
            Color(50, 50, 50, 50)
        )

        GlStateManager.popMatrix()
        FontLoaders.F20.drawStringWithShadow("Effects",17.07.toDouble(),-3.66.toDouble(),Color(0,155,255,255).rgb)

        return Border(0f, 0f, 120f, 30f,0f)
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