package net.ccbluex.liquidbounce.features.module.modules.render

import ad.FLineButtonRenderer
import ad.sb.novoline.button.AbstractButtonRenderer
import ad.sb.novoline.button.RiseButtonRenderer
import ad.utils.Color.modules.CustomUI
import ad.utils.ColorUtils.hud
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.GuiLogin
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.client.button.BadlionTwoButtonRenderer
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil
import net.ccbluex.liquidbounce.utils.render.tenacity.GradientUtil
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.GuiIngameForge
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.roundToLong


@ModuleInfo(
    name = "HUD",
    description = "Toggles visibility of the HUD.",
    category = ModuleCategory.RENDER,
    array = false, Chinese = "客户端界面"
)
object
HUD : Module() {
    val glow = BoolValue("FontGlow",false)
    val statsValue = BoolValue("RenderStats", false)
    val crossValue = BoolValue("Normal Crosshair", false)
    private val toggleMessageValue = BoolValue("DisplayToggleMessage", true)
    private val toggleSoundValue = ListValue("ToggleSound", arrayOf("None", "Default", "Custom"), "Custom")
    private val inventoryrender = BoolValue("Inventory-render",false)
    val hotbarEaseValue = BoolValue("HotbarEase", true)
    val ColorItem = BoolValue("HotbarRect", true)
    val blackHotbarValue = BoolValue("BlackHotbar", true)
    private val OtherRender = BoolValue("OtherRender", true)
    private val RenderArmor = BoolValue("RenderArmor", true)
    private val hotbar = BoolValue("Hotbar", true)

    val shadowValue = ListValue("TextShadowMode", arrayOf("LiquidBounce", "Outline","Default", "Autumn"), "LiquidBounce")
    val MusicDisplay = BoolValue("MusicDisplay", true)
    val fontChatValue = BoolValue("FontChat", false)
    val ChatBlurvalue = FloatValue("ChatBlur-Value",5F,0F,50F)
    val blurchat = BoolValue("blurchat", false)
    val shadowchat = BoolValue("shadowchat", false)
    val normalchat = BoolValue("normalchat", true)
    val ChatShadowvalue = FloatValue("ChatShadow-Value",5F,0F,20F)
    private val infoValue = BoolValue("Info", true)

    val blurStrength = IntegerValue("GlobalBlurStrength", 5, 1, 20)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val blurValue = BoolValue("GuiBlur", false)
    val buttonValue = ListValue("Button", arrayOf("Rise","Badlion","FLine"), "Rise")
    // Logo
    val changeValue = BoolValue("ChangeLogoName", false)
    val modeValue = ListValue("LogoMode", arrayOf("None", "Sigma", "Neverlose","Jello","LiquidWing","Distance","New","Novo"), "None")

    val logoradius = FloatValue("Neverlose-Radius", 3f, 0f, 10f)

    val novoradius = FloatValue("Novo-Radius", 5f, 0f, 10f)
    val nb = IntegerValue("Novo-Alpha", 150, 0, 255)
    val ss = FloatValue("Novo-ShadowStrength", 5f, 0f, 30f)
    val bs = FloatValue("Novo-BlurStrength", 5f, 0f, 30f)
    val nfont = ListValue("Novo-TitleFont", arrayOf("Title","TenacityBold","Neverlose900"),"Title")

    @JvmField
    val domainValue = TextValue("Scoreboard-Domain", "LW#2023")
    val clientname = TextValue("clientname", "LiquidWing")

    val ChineseFontButton = BoolValue("ChineseFontButton", true)
    val ChineseScore = BoolValue("ChineseScore", true)
    @JvmField
    val gradientSpeed = IntegerValue("DoubleColor-Speed", 100, 10, 1000)
    val hueInterpolation = BoolValue("DoubleColor-Interpolate", false)

    val Radius = IntegerValue("BlurRadius", 10 , 1 , 50 )
    val A = IntegerValue("Chat-Alpha", 90, 0, 255)

    var High1123 = 0.0
    val x = 380
    private val decimalFormat:DecimalFormat = DecimalFormat()
    private var easingHealth: Float = 0F
    private var easingarmor: Float = 0F
    private var easingxp: Float = 0F
    private var easingfood: Float = 0F
    private var easingValue = 0

    private val bottomLeftText: MutableMap<String, String> = LinkedHashMap()
    private val dateFormat = SimpleDateFormat("HH:mm:ss")
    private fun getClientColor(hud: HUD): Color {
        return Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get())
    }
    private fun getClientColor2(hud: HUD): Color {
        return Color(20,80,130)
    }
    fun getHotbar(): BoolValue {
        return hotbar
    }
    fun getInventoryrender():BoolValue{
        return inventoryrender
    }
    private fun getAlternateClientColor(hud: HUD): Color {
        return Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get())
    }
    private fun getAlternateClientColor2(hud: HUD): Color {
        return Color(110,183,225)

    }
    @EventTarget
    fun onTick(event: TickEvent) {
        LiquidBounce.moduleManager.shouldNotify = toggleMessageValue.get()
        LiquidBounce.moduleManager.toggleSoundMode = toggleSoundValue.values.indexOf(toggleSoundValue.get())
    }
    private fun getClientColors(hud: HUD): Array<Color> {
        val firstColor: Color = mixColors(
            getClientColor(hud),
            getAlternateClientColor(hud)
        )
        val secondColor: Color = mixColors(
            getAlternateClientColor(hud),
            getClientColor(hud)
        )
        return arrayOf(firstColor, secondColor)
    }

    @EventTarget
    fun shader(event: BlurEvent) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return
    }
    fun getHotbarEasePos(x: Int): Int {
        if (!state || !hotbarEaseValue.get()) return x
        easingValue = x
        return easingValue
    }
    val clientColors: Array<Color>
        get() {
            val firstColor: Color
            val secondColor: Color
            firstColor =
                mixColors(getClientColor(hud), getAlternateClientColor(hud))
            secondColor =
                mixColors(getAlternateClientColor(hud), getClientColor(hud))
            return arrayOf(firstColor, secondColor)}
    val mainColor: Array<Color>
        get() {
            val firstColor: Color
            val secondColor: Color
            firstColor =
                mixColors(getClientColor2(hud), getAlternateClientColor2(hud))
            secondColor =
                mixColors(getAlternateClientColor2(hud), getClientColor2(hud))
            return arrayOf(firstColor, secondColor)}
    val sigmaY = 4
    val sigmaX = 8
    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        val sr = ScaledResolution(mc2)
        val left = sr.scaledWidth / 2 + 91
        val top= sr.scaledHeight - 49
        val x = left - 1 * 8 - 180
        val scaledResolution = ScaledResolution(mc2)
        if (infoValue.get()) {
            val bps = Math.hypot(
                mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
                mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
            ) * mc.timer.timerSpeed * 20
            val Info = String.format(
                "| 230513 | User:" + WEnumChatFormatting.YELLOW + GuiLogin.username!!.text
            )
            val Version = String.format(
                "Wing"
            )
            GradientUtil.applyGradientHorizontal(
                2f,
                2f,
                Fonts.tenacitybold40.getStringWidth(Version).toFloat(),
                Fonts.tenacitybold40.fontHeight.toFloat(),
                1f,
                clientColors[0],
                clientColors[1]
            ) {
                Fonts.never900_40.drawString(Version, 2f,  (scaledResolution.scaledHeight - 10).toFloat(),-1,false)
            }

                Fonts.newtenacity40.drawString(
                    Info,
                    2f + Fonts.never900_40.getStringWidth(Version) + 1f,
                    (scaledResolution.scaledHeight - 10).toFloat(),
                    Color(54,54,54).rgb,
                    false
                )
        }
        when (modeValue.get()) {
            "New" -> {
                RenderUtils.drawImage(ResourceLocation("liquidwing/7ad/newlogo.png"),sigmaX,(sigmaY+1),106,37)
            }
            "Novo" -> {
                val username = LiquidBounce.USERNAME
                val servername = if (mc2.isSingleplayer) "Singleplayer" else mc2.currentServerData?.serverIP
                val fps = Minecraft.getDebugFPS().toString() + " FPS"
                val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD

                ShadowUtils.shadow(ss.get(),{
                    GL11.glPushMatrix()
                    if(nfont.get().equals("TenacityBold")) {
                    RenderUtils.drawRoundedRect(7f,6f,Fonts.tenacitybold40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 19f),22f,novoradius.get(),Color(0,0,0).rgb)
                    }
                    else if(nfont.get().equals("Title")) {
                        RenderUtils.drawRoundedRect(7f,6f,Fonts.title40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 19f),22f,novoradius.get(),Color(0,0,0).rgb)
                    }
                    else if(nfont.get().equals("Neverlose900")) {
                        RenderUtils.drawRoundedRect(7f,6f,Fonts.never900_40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 19f),22f,novoradius.get(),Color(0,0,0).rgb)
                    }
                    GL11.glPopMatrix()

                },{
                    GL11.glPushMatrix()
                    GlStateManager.enableBlend()
                    GlStateManager.disableTexture2D()
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                    if(nfont.get().equals("TenacityBold")) {
                        RenderUtils.drawRoundedRect(7f,6f,Fonts.tenacitybold40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 20f),23f,novoradius.get(),Color(0,0,0).rgb)
                    }
                    else if(nfont.get().equals("Title")) {
                        RenderUtils.drawRoundedRect(7f,6f,Fonts.title40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 20f),22f,novoradius.get(),Color(0,0,0).rgb)
                    }
                    else if(nfont.get().equals("Neverlose900")) {
                        RenderUtils.drawRoundedRect(7f,6f,Fonts.never900_40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 20f),22f,novoradius.get(),Color(0,0,0).rgb)
                    }
                    GlStateManager.enableTexture2D()
                    GlStateManager.disableBlend()
                    GL11.glPopMatrix()
                })

                if (nfont.get().equals("TenacityBold")){
                RenderUtils.drawRoundedRect(7f,6f,Fonts.tenacitybold40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 20f),22f,novoradius.get(),Color(0,0,0,nb.get()).rgb)
                    }
                if (nfont.get().equals("Title")){
                    RenderUtils.drawRoundedRect(7f,6f,Fonts.title40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 20f),22f,novoradius.get(),Color(0,0,0,nb.get()).rgb)
                }
                if (nfont.get().equals("Neverlose900")){
                    RenderUtils.drawRoundedRect(7f,6f,Fonts.never900_40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 20f),22f,novoradius.get(),Color(0,0,0,nb.get()).rgb)
                }
                if (nfont.get().equals("TenacityBold")){
                BlurBuffer.CustomBlurRoundArea(7f,6f,Fonts.tenacitybold40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 13f),17f,novoradius.get(),bs.get())
                }
                if (nfont.get().equals("Title")){
                    BlurBuffer.CustomBlurRoundArea(7f,6f,Fonts.title40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 13f),17f,novoradius.get(),bs.get())
                }
                if (nfont.get().equals("Neverlose900")){
                    BlurBuffer.CustomBlurRoundArea(7f,6f,Fonts.never900_40.getStringWidth("LIQUIDWING").toFloat() + (Fonts.pop35.getStringWidth("| Reborn | $username | $fps | $servername") + 13f),17f,novoradius.get(),bs.get())
                }
                if(nfont.get().equals("TenacityBold")) {
                    GradientUtil.applyGradientHorizontal(
                        10f,
                        10f,
                        Fonts.tenacitybold40.getStringWidth("LIQUIDWIG") + 1f,
                        Fonts.tenacitybold40.fontHeight + 1f,
                        1f,
                        clientColors[0],
                        clientColors[1]
                    ) {
                        Fonts.tenacitybold40.drawString("LIQUIDWING", 11f, 11f, -1, false)
                    }
                }
                else if(nfont.get().equals("Neverlose900")) {
                    GradientUtil.applyGradientHorizontal(
                        10f,
                        10f,
                        Fonts.never900_40.getStringWidth("LIQUIDWIG") + 1f,
                        Fonts.never900_40.fontHeight + 1f,
                        1f,
                        clientColors[0],
                        clientColors[1]
                    ) {
                        Fonts.never900_40.drawString("LIQUIDWING", 11f, 12f, -1, false)
                    }
                }
                else if(nfont.get().equals("Title")) {
                    GradientUtil.applyGradientHorizontal(
                        10f,
                        10f,
                        Fonts.title40.getStringWidth("LIQUIDWIG") + 1f,
                        Fonts.title40.fontHeight + 1f,
                        1f,
                        clientColors[0],
                        clientColors[1]
                    ) {
                        Fonts.title40.drawString("LIQUIDWING", 11f, 13f, -1, true)
                    }
                }
                if (nfont.get().equals("TenacityBold")){
                Fonts.pop35.drawString("| Reborn | $username | $fps | $servername",Fonts.tenacitybold40.getStringWidth("LIQUIDWING") + 14f,11f,Color(255,255,255).rgb,true)

                    }
                if (nfont.get().equals("Neverlose900")){
                    Fonts.pop35.drawString("| Reborn | $username | $fps | $servername",Fonts.never900_40.getStringWidth("LIQUIDWING") + 14f,11f,Color(255,255,255).rgb,true)

                }
                if (nfont.get().equals("Title")){
                    Fonts.pop35.drawString("| Reborn | $username | $fps | $servername",Fonts.title40.getStringWidth("LIQUIDWING") + 14f,11f,Color(255,255,255).rgb,true)

                }
            }
            "Sigma" -> {
                val fr = net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.jello.jello18.jello18
                if (!changeValue.get()) {
                    RenderUtils.drawShadowImage(
                            sigmaX - 12 - fr.stringWidth("Sigma") / 2 - 8,
                            sigmaY + 1,
                            125,
                            50,
                            ResourceLocation("liquidwing/shadow/arraylistshadow.png")
                    )
                    net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.jello.jello45.jello45.drawString(
                            "Sigma",
                            sigmaX.toFloat(),
                            (sigmaY + 1).toFloat(),
                            Color(255, 255, 255, 130).rgb
                    )
                    net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.jello.jello18.jello18.drawCenteredString(
                            "Jello",
                            (sigmaX + 10).toFloat(),
                            (sigmaY + 28).toFloat(),
                            Color(255, 255, 255, 170).rgb
                    )
                } else {
                    RenderUtils.drawShadowImage(
                            sigmaX - 12 - fr.stringWidth(clientname.get()) / 2 - 8,
                            sigmaY + 1,
                            125,
                            50,
                            ResourceLocation("liquidwing/shadow/arraylistshadow.png")
                    )
                    net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.jello.jello45.jello45.drawString(
                            clientname.get(),
                            sigmaX.toFloat(),
                            (sigmaY + 1).toFloat(),
                            Color(255, 255, 255, 130).rgb
                    )
                    net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.jello.jello18.jello18.drawCenteredString(
                            "Jello",
                            (sigmaX + 10).toFloat(),
                            (sigmaY + 28).toFloat(),
                            Color(255, 255, 255, 170).rgb
                    )
                }
            }
            "Jello" -> {
                net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.jello.jello45.jello45.drawString(
                            "LIQUIDWING",
                            sigmaX - 4f,
                            (sigmaY + 10).toFloat(),
                            Color(255, 255, 255, 130).rgb
                        )
                    }

            "LiquidWing" -> {
                val username = LiquidBounce.USERNAME
                val servername = if (mc2.isSingleplayer) "Singleplayer" else mc2.currentServerData?.serverIP
                val fps = Minecraft.getDebugFPS().toString() + "fps"
                val times = dateFormat.format(Date())
                if (!changeValue.get()) {
                    RenderUtils.drawRect(5.0f, 2.5f, Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBOEN").toFloat() + (Fonts.productSans35.getStringWidth(" | $username | $servername | $fps") + 3.0).toFloat() + 15, 20.0f,Color(0, 0, 0, 180).rgb)
                    RenderUtils.drawShadow(5.0f, 2.5f, Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBOEN").toFloat() + (Fonts.productSans35.getStringWidth(" | $username | $servername | $fps") + 3.0).toFloat() + 10, 17.5f)
                    Fonts.tenacitybold40.drawString("LIQUIDWING REBORN", 11f, 7f, Color(4, 188, 255).rgb)

                    Fonts.tenacitybold40.drawString("LIQUIDWING REBORN", 10f, 7f, -1)
                    Fonts.productSans35.drawString(" | $username | $servername | $fps", Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBORN") + 12, 8, -1)
                }
            }
            "Distance" -> {
                val username = LiquidBounce.USERNAME
                val servername = if (mc2.isSingleplayer) "Singleplayer" else mc2.currentServerData?.serverIP
                val fps = Minecraft.getDebugFPS().toString() + "fps"
                val times = dateFormat.format(Date())

                RoundedUtil.drawRound(5.0f, 5f, (Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBOEN").toFloat() + (Fonts.sfui35.getStringWidth(" | $username | $servername | $times | $fps") + 3.0).toFloat() + 15) - 5f, 15f,2.5f,Color(30, 30, 30, 200))
                Fonts.tenacitybold40.drawString("LIQUIDWING REBORN", 10f, 7f, -1)
                    Fonts.sfui35.drawString(" | $username | $servername | $times | $fps", Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBORN") + 12, 8, -1)
            }

            "Neverlose" -> {
                val username = LiquidBounce.USERNAME
                val servername = if (mc2.isSingleplayer) "Singleplayer" else mc2.currentServerData?.serverIP
                val fps = Minecraft.getDebugFPS().toString() + "fps"
                val times = dateFormat.format(Date())
                if (!changeValue.get()) {
                    RenderUtils.drawRoundedRect(5.0f, 2.5f, Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBOEN").toFloat() + (Fonts.productSans35.getStringWidth(" | $username | $servername | $times | $fps") + 3.0).toFloat() + 15, 20.0f,logoradius.get(),Color(0, 0, 0, 110).rgb)
                    Fonts.tenacitybold40.drawString("LIQUIDWING REBORN", 11f, 7f, Color(4, 188, 255).rgb)

                    Fonts.tenacitybold40.drawString("LIQUIDWING REBORN", 10f, 7f, -1)
                    Fonts.productSans35.drawString(" | $username | $servername | $times | $fps", Fonts.tenacitybold40.getStringWidth("LIQUIDWING REBORN") + 12, 8, -1)
                }
            }

        }
        GuiIngameForge.renderHealth = statsValue.get()
        GuiIngameForge.renderArmor = statsValue.get()
        GuiIngameForge.renderFood = statsValue.get()
        GuiIngameForge.renderExperiance = statsValue.get()
        GuiIngameForge.renderCrosshairs  = crossValue.get()
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return
        LiquidBounce.hud.render(false)
        if (glow.get()){
        LiquidBounce.hud.render(false)}
        if (hotbar.get() && mc.thePlayer != null && mc.theWorld != null ) {
            var color2 = Color(212 ,48 ,48).rgb
            if (easingHealth <= 0f ){
                easingHealth  = 0F
            }
            if (easingHealth >= mc.thePlayer!!.maxHealth ){
                easingHealth  = mc.thePlayer!!.maxHealth
            }
            if (easingarmor <= 0){
                easingarmor = 0F
            }
            if (easingarmor >= 20f){
                easingarmor = 20F
            }
            if (easingfood <= 0){
                easingfood = 0F
            }
            if (easingfood >= 20f){
                easingfood = 20F
            }
            if (mc.thePlayer!!.isPotionActive(classProvider.getPotionEnum(PotionType.REGENERATION))){
                color2 = Color(200, 90, 90).rgb
            }
            RoundedUtil.drawRound(x.toFloat(), top.toFloat(), 100F, 11f, 3f, Color(59,59,59))
            RoundedUtil.drawRound(
                x.toFloat(),
                top.toFloat(),
                (easingHealth / mc.thePlayer!!.maxHealth) * 100F,
                11f,
                3f,
                Color(color2)
            )
            RoundedUtil.drawRound(x.toFloat(), top.toFloat() - 3f - 15f, 100f, 11f, 3f,Color(65, 205, 125))
            RoundedUtil.drawRound(x.toFloat() + 110F, top.toFloat() - 3f - 15f, 100f, 11f, 3f, Color(59,59,59))
            RoundedUtil.drawRound(
                x.toFloat() + 110F,
                top.toFloat() - 3f - 15f,
                easingxp,
                11f,
                3f,
                Color(80,80,80)
            )
            RoundedUtil.drawRound(x.toFloat() + 110F, top.toFloat(), 100f, 11f, 3f, Color(59,59,59))
            RoundedUtil.drawRound(
                x.toFloat() + 110F,
                top.toFloat(),
                (easingfood / 20F) * 100F,
                11f,
                3f,
                Color(255 ,140 ,25)
            )
            Fonts.newtenacity30.drawString(
                decimalFormat.format((easingarmor / 20f) * 100f) + "%",
                x.toFloat()+110F +2,
                ((top + 7 - Fonts.newtenacity30.fontHeight / 2).toFloat()) - 3f - 15f,
                Color(41,41,41).rgb
            )
            var reasingHealth = Math.round(easingHealth / mc.thePlayer!!.maxHealth * 100f).toFloat()
            var s = StringBuilder().append(DecimalFormat().format(java.lang.Float.valueOf(reasingHealth))).append("%")
                .toString()
            Fonts.newtenacity30.drawString(
                s,
                x.toFloat() +2,
                ((top + 7 - Fonts.newtenacity30.fontHeight / 2).toFloat()),
                Color(131,30,30).rgb
            )
            Fonts.newtenacity30.drawString(
                "Level " + mc2.player.experienceLevel.toString(),
                x.toFloat() +2,
                ((top + 7 - Fonts.newtenacity30.fontHeight / 2).toFloat()) - 3f - 15f,

                Color(42,131,78).rgb
            )
            Fonts.newtenacity30.drawString(
                decimalFormat.format((easingfood / 20F) * 100F) + "%",
                x.toFloat()+110F +2,
                ((top + 7 - Fonts.newtenacity30.fontHeight / 2).toFloat()),
                Color(132,73,14).rgb
            )
            easingfood += (mc2.player.foodStats.foodLevel - easingfood) / 2.0F.pow(10.0F - 5F) * RenderUtils.deltaTime
            easingxp += ((mc2.player.experience * 100F) - easingxp) / 2.0F.pow(10.0F - 5F) * RenderUtils.deltaTime
            easingHealth += ((mc.thePlayer!!.health - easingHealth) / 2.0F.pow(10.0F - 5F)) * RenderUtils.deltaTime
            easingarmor += ((mc2.player.totalArmorValue - easingarmor) / 2.0F.pow(10.0F - 5F)) * RenderUtils.deltaTime
        }
        if (!mc2.ingameGUI.chatGUI.chatOpen) {
            if (OtherRender.get()) {
                High1123 = if (mc2.ingameGUI.chatGUI.chatOpen) {
                    (RenderUtils.height() - 35).toDouble()
                } else {
                    if (mc.playerController.isNotCreative && RenderArmor.get()) {
                        onArmor(mc.thePlayer!!) }
                    (RenderUtils.height() - 20).toDouble()
                }
                (RenderUtils.height() - 20).toDouble()
            }
        }
//        LiquidBounce.hud.render(false)
        if (!mc2.ingameGUI.chatGUI.chatOpen) {
            if (OtherRender.get()) {
                High1123 = if (mc2.ingameGUI.chatGUI.chatOpen) {
                    (RenderUtils.height() - 35).toDouble()
                } else {
                    if (mc.playerController.isNotCreative && RenderArmor.get()) {
                        onArmor(mc.thePlayer!!) }
                    (RenderUtils.height() - 20).toDouble()
                }
                (RenderUtils.height() - 20).toDouble()
            }
        }
//        LiquidBounce.hud.render(false)
    }
    private fun onArmor(target: IEntityLivingBase) {
    }

    private fun mixColors(color1: Color, color2: Color): Color {
        return ColorUtil.interpolateColorsBackAndForth(
            15,
            1,
            color1,
            color2,
            hueInterpolation.get()
        )
    }
    private fun calculateBPS(): Double {
        val bps = hypot(
            mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
            mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
        ) * mc.timer.timerSpeed * 20
        return (bps * 100.0).roundToLong() / 100.0
    }
    fun getButtonRenderer(button: GuiButton): AbstractButtonRenderer? {
        return when (buttonValue.get().toLowerCase()) {
            "rise" -> RiseButtonRenderer(button)
            "badlion" -> BadlionTwoButtonRenderer(button)
            "fline" -> FLineButtonRenderer(button)
            else -> null // vanilla or unknown
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
            !(classProvider.isGuiChat(event.guiScreen) || classProvider.isGuiHudDesigner(event.guiScreen))
        ) mc.entityRenderer.loadShader(classProvider.createResourceLocation("liquidwing" + "/blur.json")) else if (mc.entityRenderer.shaderGroup != null &&
            mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("liquidwing/blur.json")
        ) mc.entityRenderer.stopUseShader()
    }

    init {
        state = true
    }
}