/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package ad.sb

import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.render.blur.BlurUtils
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil.applyOpacity
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil.interpolateColorsBackAndForth
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * A target hud
 */
@ElementInfo(name = "TargetHud")
class TargetHud : Element() {

    private val modeValue = ListValue("Style", arrayOf("Novoline", "LiquidBounce"), "Novoline")
    private val blur = BoolValue("Blur", false)
    private val outline = BoolValue("Outline", false)
    private val shadow = BoolValue("Shadow", true)

    private val decimalFormat = DecimalFormat("##0.00", DecimalFormatSymbols(Locale.ENGLISH))
    private val fadeSpeed = FloatValue("FadeSpeed", 2F, 1F, 9F)

    private var easingHealth: Float = 0F

    var fontRenderer: IFontRenderer = Fonts.misans40

    var mainTarget: IEntityLivingBase? = null
    var animProgress = 0F

    class RiseParticle {
        val color = ColorUtils.rainbow(RandomUtils.nextInt(0, 30))
        val alpha = RandomUtils.nextInt(150, 255)
        val time = System.currentTimeMillis()
        val x = RandomUtils.nextInt(-50, 50)
        val y = RandomUtils.nextInt(-50, 50)
    }
    override fun drawElement(): Border {

        fontRenderer = Fonts.misans40

        var actualTarget = LiquidBounce.combatManager.target
        if (classProvider.isGuiHudDesigner(mc.currentScreen)) actualTarget = mc.thePlayer

        if (classProvider.isGuiChat(mc.currentScreen)) actualTarget = mc.thePlayer
        var width: Float

        width =
            128f
        if (this.modeValue.get().toLowerCase() == "novoline") {
            80F
        }else {
            128F
        }
        val height =
                36F
        if (this.modeValue.get().toLowerCase() == "novoline") {
            34F
        }else {
            36f
        }
        val num = if (actualTarget != null) 0F else 1F

        animProgress += ((num - animProgress) / 2.0F.pow(10.0F - 5.50f)) * RenderUtils.deltaTime

        animProgress = animProgress.coerceIn(0F, 1F)

        if (actualTarget != null)
            mainTarget = actualTarget
        else if (animProgress >= 1F) {
            mainTarget = null
        }

        if (mainTarget == null) {
            easingHealth = 0F
            return Border(0F, 0F, width, height,0f)
        }

        if (this.modeValue.get().toLowerCase().equals("novoline")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(80)
                .toFloat()
        }  else {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(100)
                .toFloat()
        }


        val calcScaleX = animProgress
        val calcScaleY = animProgress
        val calcTranslateX = width / 2F * calcScaleX
        val calcTranslateY = height / 2F * calcScaleY

        GL11.glPushMatrix()
        GL11.glTranslatef(calcTranslateX, calcTranslateY, 0F)
        GL11.glScalef(1F - calcScaleX, 1F - calcScaleY, 1F - calcScaleX)
        if (this.modeValue.get().toLowerCase().equals("novoline")) {
            this.novoline(mainTarget!!, width, height)
        } else if (this.modeValue.get().toLowerCase() == "liquidwing") {
            this.liquidwing(mainTarget!!)
        } else {
            this.liquidbounce(mainTarget!!, width, height)
        }

        GL11.glPopMatrix()

        GlStateManager.resetColor()
        return Border(0F, 0F, width, height,0f)
    }

    private fun liquidbounce(target: IEntityLivingBase, width: Float, height: Float) {
        if (easingHealth < 0 || easingHealth > target.maxHealth ||
            abs(easingHealth - target.health) < 0.01
        ) {
            easingHealth = target.health
        }

        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.blurArea(renderX.toFloat(), renderY.toFloat(), width, height)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }

        // Draw rect box
        RenderUtils.rectangle(0.0, 0.0, width.toDouble(), height.toDouble(), Color(0, 0, 0, 70).rgb)

        val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD

        val c = Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get())
        val c2 = Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get())


        val startPos = 0.0
        val healthBar = width.toDouble()
        -startPos * 2.0
        RenderUtils.drawGradientSideways(
            startPos,
            height.toDouble() - 2.0,
            startPos + ((easingHealth / target.maxHealth) * (healthBar)),
            height.toDouble(),
            c2.rgb,
            c.rgb
        )

        easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

        target.name?.let { fontRenderer.drawStringWithShadow(it, 36, 7, 0xffffff) }

        fontRenderer.drawStringWithShadow(
            "Distance: ${
                decimalFormat.format(
                    mc.thePlayer!!.getDistanceToEntityBox(
                        target
                    )
                )
            }", 36,19, 0xffffff
        )

        // Draw info

        //绘制头像
        //如果target是玩家，将会绘制其头像。
        var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (classProvider.isEntityPlayer(target)) {
            playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
        }
        if (playerInfo != null) {
            // Draw head
            val locationSkin = playerInfo.locationSkin

            val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) {
                Minecraft.getMinecraft().timer.renderPartialTicks
            } else {
                0f
            }
            // 受伤的红色效果
            val hurtPercent = renderHurtTime / 10.0F
            GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)

            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 30

            GL11.glPushMatrix()
            // 受伤的缩放效果
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                3, 4, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )
            GL11.glPopMatrix()
        }
    }

    private val riseParticleList = mutableListOf<RiseParticle>()
    private fun novoline(target: IEntityLivingBase, width: Float, height: Float) {
        val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
        if (target != null) {
            if (easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }

            var c = Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get())
            var c2 =Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get())
            // Draw rect box

            //ColorUtils.reAlpha(c.rgb, 0.4F)
            if(blur.get()) {
                GL11.glTranslated(-renderX, -renderY, 0.0)
                //            GL11.glPushMatrix()
                BlurBuffer.blurArea((renderX + 0.0F).toFloat(),
                    (renderY+0.0f).toFloat(), width.toDouble().toFloat(), height.toDouble().toFloat() )
                //            GL11.glPopMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
            }
            RenderUtils.rectangleBordered(0.0, 0.0, width.toDouble(), height.toDouble(), 0.5, Color(0,0,0,30).rgb,
                if (this.outline.get()) RenderUtils.reAlpha(c.rgb, 0.4F) else Color(0,0,0,80).rgb)
            if (shadow.get()){
                RenderUtils.drawShadowWithCustomAlpha(0.0f, 0.0f, width.toDouble().toFloat(), height.toDouble().toFloat(), 255f)
            }


            RenderUtils.rectangle(36.0, 17.0, width.toDouble() - 4, 27.0, Color(35,35,35,20).rgb)

            RenderUtils.drawGradientSideways(36.0, 17.0, 36.0 + ((easingHealth / target.maxHealth) * (width - 40)).toDouble(),
                27.0, c2.rgb, c.rgb)

            fontRenderer.drawCenteredString(((target.health/target.maxHealth * 1000.0).toInt() / 10.0F).toString() + "%", 36.0F + (width - 40) / 2.0F, 19.0F, -1, true)
            // Health bar

            //RenderUtils.drawGradientSideways(0.0, 34.0, ((easingHealth / target.maxHealth) * width).toDouble(),
            //   36.0, ColorUtils.rainbow().darker().rgb, ColorUtils.rainbow().rgb)

            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

            target.name?.let { fontRenderer.drawStringWithShadow(it, 36, 5, 0xffffff) }

            // Draw info

            //绘制头像
            //如果target是玩家，将会绘制其头像。
            var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
            if (classProvider.isEntityPlayer(target)) {
                playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
            }
            if (playerInfo != null) {

                // Draw head
                val locationSkin = playerInfo.locationSkin

                val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) { Minecraft.getMinecraft().timer.renderPartialTicks } else { 0f }
                // 受伤的红色效果
                val hurtPercent = renderHurtTime / 10.0F
                GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)

                val scale = if (hurtPercent == 0f) { 1f } else if (hurtPercent < 0.5f) {
                    1 - (0.2f * hurtPercent * 2)
                } else {
                    0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
                }
                val size = 30

                GL11.glPushMatrix()
                // 受伤的缩放效果
                GL11.glScalef(scale, scale, scale)
                GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

                mc.textureManager.bindTexture(locationSkin)
                RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8,size, size,
                    64F, 64F)

                GL11.glPopMatrix()
            }
        }
    }
    private fun liquidwing(target: IEntityLivingBase) {
        if (easingHealth < 0 || easingHealth > target.maxHealth ||
            abs(easingHealth - target.health) < 0.01
        ) {
            easingHealth = target.health
        }
        val hudMod = LiquidBounce.moduleManager.getModule(
            HUD::class.java
        ) as HUD

        fun getClientColor(): Color {
            return Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get())
        }

        fun getAlternateClientColor(): Color {
            return Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get())
        }

        val gradientColor1 = interpolateColorsBackAndForth(
            15,
            0,
            getClientColor(),
            getAlternateClientColor(),
            hudMod.hueInterpolation.get()
        )
        val gradientColor2 = interpolateColorsBackAndForth(
            15,
            90,
            getClientColor(),
            getAlternateClientColor(),
            hudMod.hueInterpolation.get()
        )
        val gradientColor3 = interpolateColorsBackAndForth(
            15,
            180,
            getClientColor(),
            getAlternateClientColor(),
            hudMod.hueInterpolation.get()
        )
        val gradientColor4 = interpolateColorsBackAndForth(
            15,
            270,
            getClientColor(),
            getAlternateClientColor(),
            hudMod.hueInterpolation.get()
        )

        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.blurRoundArea(renderX.toFloat(), renderY.toFloat(), 150F, 40F, 4f)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }

        RoundedUtil.drawRound(
            0f,
            0f,
            150F,
            40F,
            4f,
            Color(0, 0, 0, 70)
        )


        RoundedUtil.drawGradientRound(
            36F,
            27f,
            ((easingHealth / target.maxHealth) * (105f)),
            3f,
            1.3f,
            applyOpacity(gradientColor4, .85f),
            gradientColor1,
            gradientColor3,
            gradientColor2
        )
        RoundedUtil.drawGradientRound(
            36F, 27f, 105f, 3f, 1.3f, Color(0, 0, 0, 70),
            Color(0, 0, 0, 70),
            Color(0, 0, 0, 70),
            Color(0, 0, 0, 70)
        )

        "${decimalFormat.format((easingHealth / target.maxHealth) * 100)}%".also {
            FontLoaders.F18.drawStringWithShadow(
                it,
                (40f + (easingHealth / target.maxHealth) * 105f - FontLoaders.F18.getStringWidth(it)).coerceAtLeast(
                    40f
                ), 19F, -1
            )
        }


        easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

        FontLoaders.F20.drawStringWithShadow(target.name!!, 36.0, 8.0, -1)


        var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (classProvider.isEntityPlayer(target)) {
            playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
        }
        if (playerInfo != null) {
            // Draw head
            val locationSkin = playerInfo.locationSkin

            val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) {
                Minecraft.getMinecraft().timer.renderPartialTicks
            } else {
                0f
            }
            // 受伤的红色效果
            val hurtPercent = renderHurtTime / 10.0F
            GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)

            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 30

            GL11.glPushMatrix()
            // 受伤的缩放效果
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                3, 5, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )
            GL11.glPopMatrix()
        }
    }
}