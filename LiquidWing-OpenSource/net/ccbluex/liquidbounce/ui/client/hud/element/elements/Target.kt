package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import ad.sb.PlayerUtils
import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import com.mojang.realmsclient.gui.ChatFormatting
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.Stencil
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.novoline.ScaleUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.render.blur.BlurUtils
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat

@ElementInfo(name = "Target")
class Target : Element(-46.0,-40.0,1F,Side(Side.Horizontal.MIDDLE,Side.Vertical.MIDDLE)) {

    private val modeValue = ListValue("Mode", arrayOf("liquidwing","newliquidwing2","newliquidwing","zork","novoline","novo","rise","distance", "rise2","NewRound"), "novo")

    private val blur = BoolValue("Blur", true)
    private val bg = BoolValue("Lw-Bg", true)
    private val shadowValue = FloatValue("Novo-ShadowStrength", 10f,0f,20f)
    private val novoshadow = BoolValue("Novo-Shadow", true)
    private val novoradi = FloatValue("Novo-Radius", 8f,0f,10f)
    private val targetradius = FloatValue("Novo-TargetRadius", 10f,0f,15f)
    val alpha = IntegerValue("Novo-BG-Alpha", 100, 0, 255)
    private val novoblur = BoolValue("Novoline-Blur", false)
    private val blurstr = FloatValue("Novo-BlurStrength", 10f,0f,20f)
    val novoalpha = IntegerValue("Novoline-Alpha", 60, 0, 255)
    private val lwparticles = BoolValue("Particles", true)
    private val switchModeValue = ListValue("SwitchMode", arrayOf("Slide","Zoom","None"), "None")

    private val animSpeedValue = IntegerValue("AnimSpeed",10,5,20)
    private val switchAnimSpeedValue = IntegerValue("SwitchAnimSpeed",8,5,40)
    val CountValue = IntegerValue("Count", 4, 1, 20)
    val MoveTimeValue = IntegerValue("MoveTime", 10, 5, 40)
    val DistanceValue = FloatValue("Distance", 1f, 0.5f, 2f)
    val FadeTimeValue = IntegerValue("FadeTime", 20, 5, 40)
    val AlphaValue = FloatValue("Alpha", 0.7f, 0.1f, 1f)
    val SizeValue = FloatValue("Size", 1f, 0.5f, 3f)



    val r = IntegerValue("Particles-Red", 0, 0, 255)
    val g = IntegerValue("Particles-Green", 0, 0, 255)
    val b = IntegerValue("Particles-Blue", 0, 0, 255)
    val r2 = IntegerValue("Particles-Red2", 255, 0, 255)
    val g2 = IntegerValue("Particles-Green2", 255, 0, 255)
    val b2 = IntegerValue("Particles-Blue2", 255, 0, 255)
    private val riseParticleList = mutableListOf<Target.RiseParticle>()

    private var prevTarget:IEntityLivingBase?=null
    private var lastHealth=20F
    private var lastChangeHealth=20F
    private var changeTime=System.currentTimeMillis()
    private var displayPercent=0f
    private var lastUpdate = System.currentTimeMillis()
    private val decimalFormat = DecimalFormat("0.0")
    private  val counter1 = intArrayOf(50)
    private val counter2 = intArrayOf(80)
    private fun getHealth(entity: IEntityLivingBase?):Float{
        return if(entity==null || entity.isDead){ 0f }else{ entity.health }
    }

    override fun drawElement(): Border? {
        var target=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target
        val time=System.currentTimeMillis()
        val pct = (time - lastUpdate) / (switchAnimSpeedValue.get()*50f)
        lastUpdate=System.currentTimeMillis()

        if (classProvider.isGuiHudDesigner(mc.currentScreen)|| classProvider.isGuiChat(mc.currentScreen)) {
            target=mc.thePlayer
        }
        if (target != null) {
            prevTarget = target
        }
        prevTarget ?: return getTBorder()

        if (target!=null) {
            if (displayPercent < 1) {
                displayPercent += pct
            }
            if (displayPercent > 1) {
                displayPercent = 1f
            }
        } else {
            if (displayPercent > 0) {
                displayPercent -= pct
            }
            if (displayPercent < 0) {
                displayPercent = 0f
                prevTarget=null
                return getTBorder()
            }
        }

        if(getHealth(prevTarget)!=lastHealth){
            lastChangeHealth=lastHealth
            lastHealth=getHealth(prevTarget)
            changeTime=time
        }
        val nowAnimHP=if((time-(animSpeedValue.get()*50))<changeTime){
            getHealth(prevTarget)+(lastChangeHealth-getHealth(prevTarget))*(1-((time-changeTime)/(animSpeedValue.get()*50F)))
        }else{
            getHealth(prevTarget)
        }

        when(switchModeValue.get().toLowerCase()){
            "zoom" -> {
                val border=getTBorder() ?: return null
                GL11.glScalef(displayPercent,displayPercent,displayPercent)
                GL11.glTranslatef(((border.x2 * 0.5f * (1-displayPercent))/displayPercent), ((border.y2 * 0.5f * (1-displayPercent))/displayPercent).toFloat(), 0f)
            }
            "slide" -> {
                val percent= EaseUtils.easeInQuint(1.0-displayPercent)
                val xAxis= classProvider.createScaledResolution(mc).scaledWidth-renderX
                GL11.glTranslated(xAxis*percent,0.0,0.0)
            }
        }

        when(modeValue.get().toLowerCase()) {
            "liquidwing" -> drawLw(prevTarget!!, nowAnimHP)
            "novoline" -> drawNovo(prevTarget!!, nowAnimHP)
            "novo" -> drawNovo2(prevTarget!!, nowAnimHP)
            "newliquidwing" -> liquidwing(prevTarget!!,nowAnimHP)
            "newliquidwing2"-> liquidwing2(prevTarget!!,nowAnimHP)
            "zork" -> drawZork(prevTarget!!,nowAnimHP)
            "newround" -> drawRound(prevTarget!!,nowAnimHP)
            "rise" -> rise111(prevTarget!!,nowAnimHP)
            "distance" -> distance(prevTarget!!,nowAnimHP)
            "rise2" -> rise2(prevTarget!!, nowAnimHP)
        }

        return getTBorder()
    }
    private fun drawHeadRounded(target: IEntityLivingBase,x : Int,y: Int, u : Float, v: Float, uWidth: Int, vHeight: Int, width: Int, height: Int, tileWidth: Float, tileHeight: Float,radius : Float,locationSkin:IResourceLocation,size:Float,red: Boolean) {
        Stencil.write(false)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.resetColor();
        RoundedUtil.drawRoundTextured(x.toFloat(),y.toFloat(),size,size,radius,1F)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        Stencil.erase(true)
        GL11.glColor4f(1F, 1F, 1F, 1F)
        if (red)
        {
            val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) {
                Minecraft.getMinecraft().timer.renderPartialTicks
            } else {
                0f
            }
            // 受伤的红色效果
            val hurtPercent = renderHurtTime / 10.0F
            GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)
            GL11.glPushMatrix()
        }
        mc.textureManager.bindTexture(locationSkin)
        RenderUtils.drawScaledCustomSizeModalRect(x,y,u,v,uWidth, vHeight, width, height, tileWidth, tileHeight)
        Stencil.dispose()
    }
    var animProgress = 0F
    private fun rise2(target: IEntityLivingBase, easingHealth: Float) {
        GL11.glPushMatrix()

        RoundedUtil.drawRound(0F, 0F, 160F, 40F, 8F, Color(0, 0, 0, 180))
        RoundedUtil.drawGradientRound(40F, 25F, 90F * (easingHealth / 20f), 5F, 3F, Color(70, 200, 200), Color.WHITE, Color(70, 200, 200), Color.WHITE)
        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.CustomBlurRoundArea(renderX.toFloat() + 1f, renderY.toFloat(), 140f,40.0f, 10F, 4F)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        RoundedUtil.drawGradientRound(40F, 25F, 90F * (easingHealth / 20f), 5F, 3F, Color(70, 200, 200), Color.WHITE, Color(70, 200, 200), Color.WHITE)
        Fonts.rise35.drawStringWithShadow("Name: ", 40, 8, Color.WHITE.rgb)
        Fonts.rise35.drawStringWithShadow(target.name!!, 40 + Fonts.rise35.getStringWidth("Name: "), 8, Color(70, 150, 140).rgb)
        Fonts.rise35.drawStringWithShadow(decimalFormat.format(easingHealth), 135, 24, Color(70, 150, 140).rgb)

        var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (classProvider.isEntityPlayer(target)) {
            playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
        }
        if (playerInfo != null) {
            // Draw head
            val locationSkin = playerInfo.locationSkin

            val size = 26

            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            drawHeadRounded(
                target,
                5, 7, 8F, 8F, 8, 8, size, size,
                64F, 64F,5f,locationSkin,size.toFloat(),false
            )

            GL11.glPopMatrix()

            if (target.hurtTime >= 9) {
                for (i in 0 until CountValue.get()) {
                    riseParticleList.add(Target.RiseParticle())
                }
            }
            val curTime = System.currentTimeMillis()
            riseParticleList.map { it }.forEach { rp ->
                if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                    riseParticleList.remove(rp)
                }
                val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                    (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                } else {
                    1f
                }
                val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                    1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(
                        1f
                    )
                } else {
                    1f
                } * AlphaValue.get()
                val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
                RenderUtils.drawCircle(
                    x,
                    y,
                    SizeValue.get() * 2 +2f,
                    ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                RenderUtils.drawCircle(
                    x,
                    y,
                    SizeValue.get() * 2 +1f,
                    ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb, 1))
                RenderUtils.drawCircle(
                    x,
                    y,
                    SizeValue.get() * 2,
                    ScaleUtils.fadeBetween(Color(235, 235, 235, 160).rgb, Color(235, 235, 235, 160).rgb, 1))
                RenderUtils.drawCircle(
                    x,
                    y,
                    SizeValue.get() *2 - 0.3f,
                    ScaleUtils.fadeBetween(Color(r.get(),g.get(),b.get(), 200).rgb, Color(r2.get(),g2.get(),b2.get(), 200).rgb, 1)
                )
            }
        }

        GL11.glPopMatrix()
    }
    fun drawHead2(skin: IResourceLocation, x: Int = 2, y: Int = 2, width: Int, height: Int, alpha: Float = 1F) {
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
            64F, 64F)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }
    fun drawHead(skin: IResourceLocation, x: Int = 2, y: Int = 2, width: Int, height: Int, alpha: Float = 1F) {
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
            64F, 64F)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }
    fun drawHead(skin: IResourceLocation, x: Float, y: Float, scale: Float, width: Int, height: Int, red: Float, green: Float, blue: Float, alpha: Float = 1F) {
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y, 0F)
        GL11.glScalef(scale, scale, scale)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glColor4f(red.coerceIn(0F, 1F), green.coerceIn(0F, 1F), blue.coerceIn(0F, 1F), alpha.coerceIn(0F, 1F))
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(0, 0, 8F, 8F, 8, 8, width, height,
            64F, 64F)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glPopMatrix()
        GL11.glColor4f(1f, 1f, 1f, 1f)
    }
    private fun drawLw(target: IEntityLivingBase, easingHealth: Float) {
        // particle engine
        RenderUtils.drawRoundedCornerRect(
            1f, 0f, 150.0f, 50.0f,
            0.0f, Color(0, 0, 0, 180).rgb
        )
        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.blurArea(renderX.toFloat() + 1f, renderY.toFloat(), 148.95f,50.0f)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        RenderUtils.drawShadow(1f, 0f, 148f, 50.0f)
        // draw entity
        // target text
        FontLoaders.F20.drawStringWithShadow("${target.name}", 45f, 10f, Color.WHITE.rgb)
        val df = DecimalFormat("0.00"); //bar
        RenderUtils.drawRoundedCornerRect(
            45f,
            35f,
            45f + (easingHealth / target.maxHealth) * 100f,
            42f,
            0f,
            Color(240, 240, 240, 230).rgb
        )
//        FontLoaders.F16.drawStringWithShadow(
//            "${((df.format((easingHealth / target.maxHealth) * 100)))}%",
//            80f,
//            35f,
//            Color(0, 0, 0).rgb
//        )
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
            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 34

            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                6, 9, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )

            GL11.glPopMatrix()
            if (lwparticles.get()) {
                if (target.hurtTime >= 9) {
                    for (i in 0 until CountValue.get()) {
                        riseParticleList.add(Target.RiseParticle())
                    }
                }
                val curTime = System.currentTimeMillis()
                riseParticleList.map { it }.forEach { rp ->
                    if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                        riseParticleList.remove(rp)
                    }
                    val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                        (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                    } else {
                        1f
                    }
                    val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                    val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                    val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                        1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(
                            1f
                        )
                    } else {
                        1f
                    } * AlphaValue.get()
                    val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2 +2f,
                        ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2 +1f,
                        ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2,
                        ScaleUtils.fadeBetween(Color(235, 235, 235, 160).rgb, Color(235, 235, 235, 160).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() *2 - 0.3f,
                        ScaleUtils.fadeBetween(Color(r.get(),g.get(),b.get(), 200).rgb, Color(r2.get(),g2.get(),b2.get(), 200).rgb, 1)
                    )
                }
            }
        }
    }
    private fun drawNovo2(target: IEntityLivingBase, easingHealth: Float) {
        val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
        if(novoshadow.get()){
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef( 1F,  1F,  1F)
            GL11.glPushMatrix()
            ShadowUtils.shadow(shadowValue.get(),{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.drawRoundedRect2(0F, 0F, 94f + FontLoaders.pop18.getStringWidth(target.name) + (easingHealth / 20f), 45F, novoradi.get(), Color(0, 0, 0).rgb)
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.drawRoundedRect2(0F, 0F, 94f + FontLoaders.pop18.getStringWidth(target.name) + (easingHealth / 20f), 45F, novoradi.get(), Color(0, 0, 0).rgb)
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
            GL11.glPopMatrix()
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        RenderUtils.drawRoundedRect2(0F, 0F, 94f + FontLoaders.pop18.getStringWidth(target.name) + (easingHealth / 20f), 45F, novoradi.get(), Color(0, 0, 0, alpha.get()).rgb)
        RoundedUtil.drawGradientRound(40F, 27F, 40F * (easingHealth / 20f) + FontLoaders.pop18.getStringWidth(target.name)-7f, 5F, 3F, Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()), Color.WHITE, Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get()), Color.WHITE)
        if (blur.get()) {

            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.CustomBlurRoundArea(renderX.toFloat(), renderY.toFloat(), 94f + FontLoaders.pop18.getStringWidth(target.name) + (easingHealth / 20f),45f, novoradi.get(), blurstr.get())
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        RoundedUtil.drawGradientRound(40F, 27F, 40F * (easingHealth / 20f) + FontLoaders.pop18.getStringWidth(target.name)-7f, 5F, 3F, Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()), Color.WHITE, Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get()), Color.WHITE)
        Fonts.pop35.drawStringWithShadow("Name: ", 40, 10, Color.WHITE.rgb)
        FontLoaders.pop18.drawStringWithShadow(target.name!!,
            (42 + Fonts.pop35.getStringWidth("Name: ")).toDouble(), 9.0, Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()).rgb)
        Fonts.pop35.drawStringWithShadow(decimalFormat.format(easingHealth), ((94f + FontLoaders.pop18.getStringWidth(target.name) + (easingHealth / 20f) - 18f).toInt()), 26, Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get()).rgb)

        var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (classProvider.isEntityPlayer(target)) {
            playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
        }
        if (playerInfo != null) {

            // Draw head
            val locationSkin = playerInfo.locationSkin

            val size = 31


            drawHeadRounded(
                target,
                5, 7, 8F, 8F, 8, 8, size, size,
                64F, 64F,targetradius.get(),locationSkin,size.toFloat(),true
            )

            GL11.glPopMatrix()

            if (target.hurtTime >= 9) {
                for (i in 0 until CountValue.get()) {
                    riseParticleList.add(Target.RiseParticle())
                }
            }
            val curTime = System.currentTimeMillis()
            riseParticleList.map { it }.forEach { rp ->
                if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                    riseParticleList.remove(rp)
                }
                val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                    (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                } else {
                    1f
                }
                val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                    1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(
                        1f
                    )
                } else {
                    1f
                } * AlphaValue.get()

                if (lwparticles.get()){
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2 +1f,
                        ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2,
                        ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb,1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() *2 - 0.3f,
                        ScaleUtils.fadeBetween(Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get(), 200).rgb, Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get(), 160).rgb, 1)
                    )
                }
            }
        }
        if (playerInfo == null) GL11.glPopMatrix()
    }

    private fun drawNovo(target: IEntityLivingBase, easingHealth: Float) {
        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glScalef( 1F,  1F,  1F)
        GL11.glPushMatrix()
        ShadowUtils.shadow(shadowValue.get(),{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            RenderUtils.drawRoundedRect2(
                1f, 0f, 145.0f, 60.0f,
                novoradi.get(), Color(0, 0, 0).rgb
            )
            GL11.glPopMatrix()

        },{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.drawRoundedRect2(
                1f, 0f, 145.0f, 60.0f,
                novoradi.get(), Color(0, 0, 0).rgb
            )
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)
        RenderUtils.drawRoundedRect2(
            1f, 0f, 145.0f, 60.0f,
            novoradi.get(), Color(0, 0, 0, novoalpha.get()).rgb
        )
        if (novoblur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.CustomBlurRoundArea(renderX.toFloat() + 1f, renderY.toFloat(), 148f,60.0f,novoradi.get(),blurstr.get())
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }

        // target text
        FontLoaders.T20.drawStringWithShadow("${target.name}", 47f, 11f, Color.WHITE.rgb)
        FontLoaders.F20.drawStringWithShadow("Health: ${Math.round(easingHealth)}", 47.0,
            FontLoaders.F20.FONT_HEIGHT.toDouble() + 3f, Color.WHITE.rgb)
        FontLoaders.F20.drawStringWithShadow("Distance: ${Math.round(target.getDistance(mc.thePlayer!!.posX,mc.thePlayer!!.posY,mc.thePlayer!!.posZ))}m",
            47.0, (FontLoaders.F20.FONT_HEIGHT + 13f).toDouble(), Color.WHITE.rgb)

        RoundedUtil.drawRound(6f, 48f, 132f, 1.4f, 0f, Color(0, 0, 0, 100))
        RoundedUtil.drawRound(
            6f,
            48f,
            32f + ((easingHealth / target.maxHealth) * 100f),
            1.4f,
            0f,
            Color(110, 240, 153))
        RoundedUtil.drawRound(6f, 53f, 132f, 1.4f, 0f, Color(0, 0, 0, 100))
        RoundedUtil.drawRound(
            6f,
            53f,
            32f+(PlayerUtils.getAr(target)).toFloat() - 4f,
            1.4f,
            0f,
            Color(100,150,255))

//        FontLoaders.F16.drawStringWithShadow(
//            "${((df.format((easingHealth / target.maxHealth) * 100)))}%",
//            80f,
//            35f,
//            Color(0, 0, 0).rgb
//        )
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
            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 38
            // 受伤的缩放效果
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                5, 8, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )

            GL11.glPopMatrix()
            if (lwparticles.get()) {
                if (target.hurtTime >= 9) {
                    for (i in 0 until CountValue.get()) {
                        riseParticleList.add(Target.RiseParticle())
                    }
                }
                val curTime = System.currentTimeMillis()
                riseParticleList.map { it }.forEach { rp ->
                    if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                        riseParticleList.remove(rp)
                    }
                    val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                        (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                    } else {
                        1f
                    }
                    val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                    val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                    val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                        1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(
                            1f
                        )
                    } else {
                        1f
                    } * AlphaValue.get()
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2 +2f,
                        ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2 +1f,
                        ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2,
                        ScaleUtils.fadeBetween(Color(235, 235, 235, 160).rgb, Color(235, 235, 235, 160).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() *2 - 0.3f,
                        ScaleUtils.fadeBetween(Color(r.get(),g.get(),b.get(), 200).rgb, Color(r2.get(),g2.get(),b2.get(), 200).rgb, 1)
                    )
                }
            }
        }
    }
    private fun drawRound(target: IEntityLivingBase,easingHealth: Float){
        if (CustomUI.drawMode.get().equals("描边和圆角矩形")) {
            GL11.glTranslatef((-renderX).toFloat(), (-renderY).toFloat(), 0F)
            GL11.glPushMatrix()
            RoundedUtil.drawRoundOutline(
                renderX.toFloat()-1,
                renderY.toFloat()-1,
                152.0f, 52.0f,
                CustomUI.radius.get(),
                CustomUI.outlinet.get() * 1.25f,
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get(), 0),
                Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), CustomUI.a.get())
            )
            GL11.glPopMatrix()
            GL11.glTranslatef(renderX.toFloat(), renderY.toFloat(), 0F)
        }
        RoundedUtil.drawRound(
            0f, 0f, 150.0f, 50.0f,
            CustomUI.radius.get(), Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get(),CustomUI.a.get())
        )
        // draw entity
        // target text
        if (CustomUI.Chinese.get()){
            FontLoaders.F20.drawStringWithShadow("目标名称: ${target.name}", 45f, 7f, Color.WHITE.rgb)
            FontLoaders.F20.drawStringWithShadow("血量: ${Math.round(easingHealth)}",45f,4f + FontLoaders.pop20.height - 5f,Color.WHITE.rgb)
            FontLoaders.F20.drawStringWithShadow("距离: ${Math.round(target.getDistance(mc.thePlayer!!.posX,mc.thePlayer!!.posY,mc.thePlayer!!.posZ))}米",45f,FontLoaders.pop20.height * 2 - 10f,Color.WHITE.rgb)
        }
        else {
            FontLoaders.pop20.drawStringWithShadow("${target.name}", 45f, 7f, Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get()).rgb)
            Fonts.pop40.drawStringWithShadow("Health: ${Math.round(easingHealth)}",44,4 + FontLoaders.pop20.height - 5,Color.WHITE.rgb)
            Fonts.pop40.drawStringWithShadow("Distance: ${Math.round(target.getDistance(mc.thePlayer!!.posX,mc.thePlayer!!.posY,mc.thePlayer!!.posZ))}m",44, FontLoaders.pop20.height * 2 - 10,Color.WHITE.rgb)
        }
        RoundedUtil.drawRound(45f, 41f, 100f, 3f, 0f, Color(0, 0, 0, 255))
        RoundedUtil.drawRound(
            45f,
            41f,
            ((easingHealth / target.maxHealth) * 100f),
            3f,
            0f,
            Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get())
        )
        var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (classProvider.isEntityPlayer(target)) {
            playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        }
        if (playerInfo != null) {

            // Draw head
            val size = 34
            Stencil.write(false)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RoundedUtil .drawRoundTextured(6F, 9F, size.toFloat(), size.toFloat(),CustomUI.radius.get(),1f)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            Stencil.erase(true)
            drawHead(playerInfo.locationSkin, 6, 9, size, size, 1F)
            Stencil.dispose()

        }
    }
    private fun drawZork(target: IEntityLivingBase, easingHealth: Float) {
        RoundedUtil.drawRound(
            1f, 0f, 149.0f, 50.0f,
            5.0f, Color(30, 30, 31, 255)
        )
        RoundedUtil.drawRound(
            2f, 0f, 148.0f, 50.0f,
            5.0f, Color(30, 30, 31, 255)
        )
        // draw entity
        // target text
        FontLoaders.F20.drawStringWithShadow("${target.name}", 45f, 6f, Color.WHITE.rgb)
        val df = DecimalFormat("0.00"); //bar
        RoundedUtil.drawRound(45f, 23f, 100f, 5f, 2f, Color(0, 0, 0, 255))
        RoundedUtil.drawRound(
            45f,
            23f,
            ((easingHealth / target.maxHealth) * 100f),
            5f,
            2f,
            Color(100, 149, 237, 250)
        )
        // armour text
        RoundedUtil.drawRound(45f, 33f, 100f, 5f, 2f, Color(0, 0, 0, 255))
        RoundedUtil.drawRound(
            45f,
            33f,
            (PlayerUtils.getAr(target)).toFloat(),
            5f,
            2f,
            Color(255, 255, 237, 250)
        )
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
            val hurtPercent = renderHurtTime / 10.0F
            // 受伤的红色效果
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

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                8, 10, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )

            GL11.glPopMatrix()
            if (lwparticles.get()) {
                if (target.hurtTime >= 9) {
                    for (i in 0 until CountValue.get()) {
                        riseParticleList.add(Target.RiseParticle())
                    }
                }
                val curTime = System.currentTimeMillis()
                riseParticleList.map { it }.forEach { rp ->
                    if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                        riseParticleList.remove(rp)
                    }
                    val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                        (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                    } else {
                        1f
                    }
                    val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                    val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                    val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                        1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(
                            1f
                        )
                    } else {
                        1f
                    } * AlphaValue.get()
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2 +2f,
                        ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2 +1f,
                        ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() * 2,
                        ScaleUtils.fadeBetween(Color(235, 235, 235, 160).rgb, Color(235, 235, 235, 160).rgb, 1))
                    RenderUtils.drawCircle(
                        x + 1.7f,
                        y + 5f,
                        SizeValue.get() *2 - 0.3f,
                        ScaleUtils.fadeBetween(Color(r.get(),g.get(),b.get(), 200).rgb, Color(r2.get(),g2.get(),b2.get(), 200).rgb, 1)
                    )
                }
            }
        }
        /*
                // draw items
                 GlStateManager.resetColor()
                GL11.glPushMatrix()
                GL11.glColor4f(1f, 1f, 1f, 1f - getFadeProgress())
                GlStateManager.enableRescaleNormal()
                GlStateManager.enableBlend()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderHelper.enableGUIStandardItemLighting()

                val renderItem = mc.renderItem

                var x = 45
                var y = 28

                for (index in 3 downTo 0) {
                    val stack = entity.inventory.armorInventory[index] ?: continue

                    if (stack.item == null)
                        continue

                    renderItem.renderItemIntoGUI(stack, x, y)
                    renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
                    RenderUtils.drawExhiEnchants(stack, x.toFloat(), y.toFloat())

                    x += 16
                }

                val mainStack = entity.heldItem
                if (mainStack != null && mainStack.item != null) {
                    renderItem.renderItemIntoGUI(mainStack, x, y)
                    renderItem.renderItemOverlays(mc.fontRendererObj, mainStack, x, y)
                    RenderUtils.drawExhiEnchants(mainStack, x.toFloat(), y.toFloat())
                }

                RenderHelper.disableStandardItemLighting()
                GlStateManager.disableRescaleNormal()
                GlStateManager.enableAlpha()
                GlStateManager.disableBlend()
                GlStateManager.disableLighting()
                GlStateManager.disableCull()
                GL11.glPopMatrix()
                 */
    }

    //    private fun drawBest(target: IEntityLivingBase, easingHealth: Float){
//        val addedLen = (60 +  Fonts.misans40.getStringWidth(target.name!!) * 1.60f).toFloat()
//
//        RenderUtils.drawRect(0f, 0f, addedLen, 47f, Color(0, 0, 0, 120).rgb)
//        RenderUtils.drawRoundedCornerRect(0f, 0f, (easingHealth / target.maxHealth) * addedLen, 47f, 3f, Color(0, 0, 0, 90).rgb)
//
//        RenderUtils.drawShadowWithCustomAlpha(0f, 0f, addedLen, 47f,200F)
//
//        val hurtPercent = target.hurtPercent
//        val scale = if (hurtPercent == 0f) { 1f } else if (hurtPercent < 0.5f) {
//            1 - (0.1f * hurtPercent * 2)
//        } else {
//            0.9f + (0.1f * (hurtPercent - 0.5f) * 2)
//        }
//        val size = 35
//
//        GL11.glPushMatrix()
//        GL11.glTranslatef(5f, 5f, 0f)
//        // 受伤的缩放效果
//        GL11.glScalef(scale, scale, scale)
//        GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)
//        // 受伤的红色效果
//        GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)
//        // 绘制头部图片
//        RenderUtils.drawHead(mc.netHandler.getPlayerInfo(target.uniqueID)!!.locationSkin,0,0, size, size)
//        GL11.glPopMatrix()
//
//        GL11.glPushMatrix()
//        GL11.glScalef(1.5f, 1.5f, 1.5f)
//        Fonts.misans35.drawString(target.name!!, 39, 8, Color.WHITE.rgb)
//
//        GL11.glPopMatrix()
//        Fonts.misans35.drawString("Health ${target.health.roundToInt()}", 56, 20 + ( Fonts.misans35.fontHeight* 1.5).toInt(), Color.WHITE.rgb)
//    }
    private fun liquidwing2(target: IEntityLivingBase,easingHealth: Float) {
        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glScalef(1F, 1F, 1F)
        GL11.glPushMatrix()
        ad.utils.ShadowUtils.shadow(10F, {
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            RenderUtils.drawRoundedRect(0F, 0F, 150F, 50F, 5F, Color(0, 0, 0, 255).rgb)
            GL11.glPopMatrix()
        }, {
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.fastRoundedRect(0F, 0F, 150F, 50F, 5F)
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)
        //draw Background
        RenderUtils.drawRoundedRect(0F,0F,150F,50F,5F,Color(255 ,247 ,230,170).rgb)

        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glPushMatrix()
        BlurBuffer.CustomBlurRoundArea(renderX.toFloat(), renderY.toFloat()  ,150F, 50F ,
            5F, CustomUI.blurValue.get()
        )
        GL11.glPopMatrix()
        GL11.glTranslated(renderX, renderY, 0.0)

        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glPushMatrix()
        RoundedUtil.drawRoundOutline(
            renderX.toFloat(),
            renderY.toFloat(),
            150F,
            50F,
            5F,
            0.1F,
            Color(0,0,0,0),
            Color(255,255,255,110)
        )
        GL11.glPopMatrix()
        GL11.glTranslated(renderX, renderY, 0.0)
        val health = ((easingHealth / target.maxHealth) * (100F))
        val health2 = StringBuilder().append(DecimalFormat().format(java.lang.Float.valueOf(health))).append("%")
        Fonts.newtenacity35.drawString(target.name!!, 46, 13, Color(255 ,247 ,230,255).rgb)


        var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (classProvider.isEntityPlayer(target)) {
            playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
        }
        if (playerInfo != null) {
            // Draw head
            val locationSkin = playerInfo.locationSkin
            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            drawHeadRounded(
                target,
                10, 10, 8F, 8F, 8, 8, 30, 30,
                64F, 64F,5F,locationSkin,30F,false
            )
            GL11.glPopMatrix()
        }
        RoundedUtil.drawGradientHorizontal(
            46F,
            28f,
            ((easingHealth / target.maxHealth) * (90F)),
            10f,
            4F,
            Color(255,93,182),
            Color(251,111,48)
        )
        Fonts.newtenacity30.drawString(health2.toString(), 48, 31, Color(0 ,0 ,0,255).rgb)


    }
    private fun liquidwing(target: IEntityLivingBase, easingHealth: Float) {
        // particle engine
        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.blurRoundArea(renderX.toFloat(), renderY.toFloat(), 150F, 40F, 4f)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (bg.get()) {
            RoundedUtil.drawRound(
                0f,
                0f,
                150F,
                40F,
                4f,
                Color(0, 0, 0, 70)
            )
        }

        RoundedUtil.drawGradientRound(
            36F,
            27f,
            ((easingHealth / target.maxHealth) * (105f)),
            3f,
            1.3f,
            ColorUtil.applyOpacity(Color(100, 200, 255, 250), .85f),
            Color(100, 200, 255, 250),
            Color(100, 200, 255, 250),
            Color(100, 200, 255, 250)
        )
        RoundedUtil.drawGradientRound(
            36F, 27f, 105f, 3f, 1.3f, Color(0, 0, 0, 70),
            Color(0, 0, 0, 70),
            Color(0, 0, 0, 70),
            Color(0, 0, 0, 70)
        )

        "${decimalFormat.format((easingHealth / target.maxHealth) * 100)}%".also {
            Fonts.misans40.drawString(
                it,
                (40f + (easingHealth / target.maxHealth) * 105f - Fonts.misans40.getStringWidth(it)).coerceAtLeast(
                    40f
                ), 19F, -1, false
            )
        }


        Fonts.misans40.drawString(target.name!!, 36, 8, -1)


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
            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 30

            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                3, 5, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )
            GL11.glPopMatrix()
            if (lwparticles.get()) {
                if (target.hurtTime >= 9) {
                    for (i in 0 until CountValue.get()) {
                        riseParticleList.add(Target.RiseParticle())
                    }
                }
                val curTime = System.currentTimeMillis()
                riseParticleList.map { it }.forEach { rp ->
                    if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                        riseParticleList.remove(rp)
                    }
                    val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                        (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                    } else {
                        1f
                    }
                    val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                    val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                    val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                        1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(1f)
                    } else {
                        1f
                    } * AlphaValue.get()
                    val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2 +2f,
                        ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2 +1f,
                        ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2,
                        ScaleUtils.fadeBetween(Color(235, 235, 235, 160).rgb, Color(235, 235, 235, 160).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() *2 - 0.3f,
                        ScaleUtils.fadeBetween(Color(r.get(),g.get(),b.get(), 200).rgb, Color(r2.get(),g2.get(),b2.get(), 200).rgb, 1)
                    )
                }
            }
        }
    }
    private fun distance(target: IEntityLivingBase, easingHealth: Float) {
        RenderUtils.drawShadow(0f,
            0f,
            150F,
            30F)
        RoundedUtil.drawRound(
            0f,
            0f,
            150F,
            30F,
            4f,
            Color(25, 25, 25, 255)
        )

        Fonts.productSans35.drawString(target.name!!, 36, 6, -1)
        Fonts.productSans35.drawString("Distance:   " + ChatFormatting.WHITE + Math.round(target.getDistance(mc.thePlayer!!.posX,mc.thePlayer!!.posY,mc.thePlayer!!.posZ)) + "m", 36, 18, Color(41,132,163).rgb)
        /*        RoundedUtil.drawRound(113f, 2.5f,Fonts.productSans35.fontHeight.toFloat()+15,Fonts.productSans35.fontHeight.toFloat()+15,12f,
                    Color(41,132,163))
                RoundedUtil.drawRound(114f, 3.5f,Fonts.productSans35.fontHeight.toFloat()+13.0f,Fonts.productSans35.fontHeight.toFloat()+13.0f,11f,
                    Color(25, 25, 25))*/
        RenderUtils.drawCircle(123f, 15f,10f, -90, (270f * (easingHealth / 20f)).toInt(), Color(41,132,163))
        Fonts.productSans35.drawCenteredString(Math.round(easingHealth).toString(), 123.1f, 12f, -1)// 你还没口血呢，当然是完整的圆什么

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
            val size = 24

            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalCircle(
                3, 3, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )
            GL11.glPopMatrix()
        }
    }
    private fun rise111(target: IEntityLivingBase, easingHealth: Float) {
        // particle engine
        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurUtils.blurRoundArea(renderX.toFloat(), renderY.toFloat(), 150F, 40F, 4f)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (bg.get()) {
            RoundedUtil.drawRound(
                0f,
                0f,
                150F,
                45F,
                6f,
                Color(0, 0, 0, 130)
            )
        }

        RoundedUtil.drawGradientRound(
            42F,
            35f,
            ((easingHealth / target.maxHealth) * (103f)),
            4f,
            1.3f,
            ColorUtil.applyOpacity(Color(100, 200, 255, 250), .85f),
            Color(220, 100, 255, 250),
            Color(220, 100, 255, 250),
            Color(220, 100, 255, 250)
        )
        RoundedUtil.drawGradientRound(
            42F, 35f, 103f, 4f, 1.3f, Color(0, 0, 0, 70),
            Color(0, 0, 0, 70),
            Color(0, 0, 0, 70),
            Color(0, 0, 0, 70)
        )

        "${decimalFormat.format((easingHealth / target.maxHealth) * 100)} HP".also {
            Fonts.misans35.drawString(
                it,
                40f
                , 21F, -1, false
            )
        }


        Fonts.misans40.drawString(target.name!!, 40, 8, -1)


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
            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 35

            GL11.glPushMatrix()

            mc.textureManager.bindTexture(locationSkin)
            RenderUtils.drawScaledCustomSizeModalRect(
                3, 5, 8F, 8F, 8, 8, size, size,
                64F, 64F
            )
            GL11.glPopMatrix()
            if (lwparticles.get()) {
                if (target.hurtTime >= 9) {
                    for (i in 0 until CountValue.get()) {
                        riseParticleList.add(Target.RiseParticle())
                    }
                }
                val curTime = System.currentTimeMillis()
                riseParticleList.map { it }.forEach { rp ->
                    if ((curTime - rp.time) > ((MoveTimeValue.get() + FadeTimeValue.get()) * 50)) {
                        riseParticleList.remove(rp)
                    }
                    val movePercent = if ((curTime - rp.time) < MoveTimeValue.get() * 50) {
                        (curTime - rp.time) / (MoveTimeValue.get() * 50f)
                    } else {
                        1f
                    }
                    val x = (movePercent * rp.x * 0.5f * DistanceValue.get()) + 20
                    val y = (movePercent * rp.y * 0.5f * DistanceValue.get()) + 20
                    val alpha = if ((curTime - rp.time) > MoveTimeValue.get() * 50) {
                        1f - ((curTime - rp.time - MoveTimeValue.get() * 50) / (FadeTimeValue.get() * 50f)).coerceAtMost(1f)
                    } else {
                        1f
                    } * AlphaValue.get()
                    val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2 +2f,
                        ScaleUtils.fadeBetween(Color(255, 255, 255, 60).rgb, Color(255, 255, 255, 60).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2 +1f,
                        ScaleUtils.fadeBetween(Color(240, 240, 240, 110).rgb, Color(240, 240, 240, 120).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() * 2,
                        ScaleUtils.fadeBetween(Color(235, 235, 235, 160).rgb, Color(235, 235, 235, 160).rgb, 1))
                    RenderUtils.drawCircle(
                        x,
                        y,
                        SizeValue.get() *2 - 0.3f,
                        ScaleUtils.fadeBetween(Color(r.get(),g.get(),b.get(), 200).rgb, Color(r2.get(),g2.get(),b2.get(), 200).rgb, 1)
                    )
                }
            }
        }
    }
    class RiseParticle {
        val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
        val alpha = RandomUtils.nextInt(150, 255)
        val time = System.currentTimeMillis()
        val x = RandomUtils.nextInt(-50, 50)
        val y = RandomUtils.nextInt(-50, 50)
    }
    private fun getTBorder():Border?{
        return when(modeValue.get().toLowerCase()){
            "zork" -> Border(0F,0F,150F,50F,0F)
            "newliquidwing"-> Border(0F,0F,150F,50F,0F)
            "newliquidwing2"-> Border(0F,0F,150F,50F,0F)
            "liquidwing"-> Border(0F,0F,150F,50F,0F)
            "novoline"-> Border(0F,0F,150F,60F,0F)
            "novo"-> Border(0F,0F,150F,60F,0F)
            "rise"->Border(0F,0F,150F,60F,0F)
            "distance"->Border(0F,0F,150F,30F,0F)
            "rise2" -> Border(0F, 0F, 150F, 40F, 0F)
            "newround" -> Border(0F, 0F, 150F, 40F, 0F)
            else -> null
        }

    }
}