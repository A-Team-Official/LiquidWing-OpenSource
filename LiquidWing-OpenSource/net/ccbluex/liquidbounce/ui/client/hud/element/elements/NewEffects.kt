package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.PotionData
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.Translate
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import kotlin.math.pow

@ElementInfo(name = "Effects-CN")
class NewEffects(x: Double =97.0, y: Double = 141.0, scale: Float = 1F) : Element() {

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
        if (CustomUI.drawMode.get().equals("阴影") || CustomUI.drawMode.get().equals("高斯模糊和阴影"))
        {
            RenderUtils.drawRoundedRect(
                12.20f,
                -7.32f,
                easingwith + 12.20f,
                easinghealth + -7.32f,
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
                RenderUtils.drawRoundedRect(12.20f,
                    -7.32f,
                    easingwith + 12.20f,
                    easinghealth + -7.32f, CustomUI.radius.get(), Color(0,0,0).rgb)
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.drawRoundedRect(12.20f,
                    -7.32f,
                    easingwith + 12.20f,
                    easinghealth + -7.32f,CustomUI.radius.get(), Color(0,0,0).rgb)
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
                renderX.toFloat() + 12.20f,
                renderY.toFloat() - 7.32f,
                easingwith,
                easinghealth, CustomUI.radius.get(),CustomUI.blurValue.get()
            )
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (CustomUI.drawMode.get().equals("圆角矩形")) {
            RenderUtils.drawRoundedRect(
                12.20f,
                -7.32f,
                easingwith + 12.20f,
                easinghealth + -7.32f,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
        }
        if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
            RenderUtils.drawRoundedRect(
                12.20f,
                -7.32f,
                easingwith + 12.20f,
                easinghealth + -7.32f,
                CustomUI.radius.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), CustomUI.a.get()).rgb
            )
            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat() + 12.20f,
                renderY.toFloat() - 7.32f,
                easingwith,
                easinghealth,
                CustomUI.radius.get(),
                CustomUI.outlinet.get(),
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), 0),
                Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
        }
        GlStateManager.pushMatrix()
        GlStateManager.resetColor()
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


            if (CustomUI.Chinese.get()) {
                val namewith2 =
                    FontLoaders.F18.getStringWidth(name + " " + intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
                namewith =
                    FontLoaders.F18.getStringWidth(name + " " + intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
                if (namewith2 > namewith3) {
                    namewith3 = namewith2
                }
                val posY = potionData.translate.y / 2.5f
                namehight = potionData.translate.y / 2.5f - 8f
                FontLoaders.F18.drawString(
                    name + " " + intToRomanByGreedy(potionEffect.amplifier + 1),
                    29f,
                    -(posY - mc.fontRendererObj.fontHeight),
                    -1
                )

                FontLoaders.F18.drawString(potionEffect.getDurationString(), namewith + 32, -(posY - 9.0f), -1)
            }
            else{
                val namewith2 =
                    Fonts.font35.getStringWidth(name + " " + intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
                namewith =
                    Fonts.font35.getStringWidth(name + " " + intToRomanByGreedy(potionEffect.amplifier + 1)).toFloat()
                if (namewith2 > namewith3) {
                    namewith3 = namewith2
                }
                val posY = potionData.translate.y / 2.5f
                namehight = potionData.translate.y / 2.5f - 8f
                Fonts.font35.drawString(
                    name + " " + intToRomanByGreedy(potionEffect.amplifier + 1),
                    29f,
                    -(posY - mc.fontRendererObj.fontHeight),
                    -1
                )

                Fonts.font35.drawString(potionEffect.getDurationString(), namewith + 32, -(posY - 9.76f), -1)
            }
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

        GlStateManager.popMatrix()
        if (CustomUI.Chinese.get()){
            FontLoaders.F18.drawStringWithShadow("药水显示",17.07.toDouble(),-3.66.toDouble(),Color.WHITE.rgb)
        }
        else{
            FontLoaders.F20.drawStringWithShadow("Effects",17.07.toDouble(),-3.66.toDouble(),Color.WHITE.rgb)
        }

        return Border(12.20f,
            -7.32f,
            easingwith,
            easinghealth,CustomUI.radius.get())
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

}