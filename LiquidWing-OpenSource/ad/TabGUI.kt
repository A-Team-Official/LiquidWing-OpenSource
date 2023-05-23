//package net.ccbluex.liquidbounce.features.module.modules.render
//
//
//import net.ccbluex.liquidbounce.LiquidBounce
//import net.ccbluex.liquidbounce.event.EventTarget
//import net.ccbluex.liquidbounce.event.KeyEvent
//import net.ccbluex.liquidbounce.event.Render2DEvent
//import net.ccbluex.liquidbounce.event.TickEvent
//import net.ccbluex.liquidbounce.features.module.Module
//import net.ccbluex.liquidbounce.features.module.ModuleCategory
//import net.ccbluex.liquidbounce.features.module.ModuleInfo
//import net.ccbluex.liquidbounce.font.FontLoaders
//import net.ccbluex.liquidbounce.utils.Translate
//import net.ccbluex.liquidbounce.utils.blur.BlurBuffer
//import net.ccbluex.liquidbounce.utils.render.RenderUtils
//import net.ccbluex.liquidbounce.utils.tenacity.render.DrRenderUtils
//import net.ccbluex.liquidbounce.utils.tenacity.render.RoundedUtil
//import net.ccbluex.liquidbounce.utils.timer.MSTimer
//import net.ccbluex.liquidbounce.value.BoolValue
//import net.ccbluex.liquidbounce.value.IntegerValue
//import net.ccbluex.liquidbounce.value.ListValue
//import net.minecraft.client.renderer.GlStateManager
//import org.lwjgl.input.Keyboard
//import org.lwjgl.opengl.GL11
//import java.awt.Color
//
//@ModuleInfo(name = "TabGui", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER, array = false, Chinese = "IDK")
//class TabGui : Module() {
//    val Shader = BoolValue("Shader", true)
//    val colorMode = ListValue("Color", arrayOf("Sync","Tenacity"), "Tenacity")
//    val notselectrect = BoolValue("SelectNotDrawRect", true)
//    val selectgradientBackground = BoolValue("SelectGradientRect", false)
//    val gradientBackground = BoolValue("GradientRect", false)
//
//    val x = 5F
//    val y = 50F
//    val height = 78.5f
//    var width = 75f
//
//    var openModuleGui = false;
//    var selectedCategory = 0
//    var selecteModuleindex = 0
//
//    var selecteModule = emptyList<Module>()
//    var Modulecategory = mutableListOf<AnimaitonCategory>()
//
//    val categoryAnimaiton = Translate(0f, 0f)
//    val moduleAnimaiton = Translate(0f, 0f)
//    var categoryPositonY = 0f
//    var ModulePositonY = 0f
//
//    var msTimer = MSTimer()
//
//    init {
//        state = true
//        for(index in  0..ModuleCategory.values().lastIndex) {
//            val animationcategory = AnimaitonCategory(ModuleCategory.values()[index].displayName , Translate(0f , 0f))
//            Modulecategory.add(animationcategory)
//        }
//    }
//
//    //  ʼ  ɫ
//    var top = Color(255, 255, 255, 255)
//    var bottom = Color(255, 255, 255, 255)
//
//    //top gradient color
//    var tRed = 0
//    var tGreen= 0
//    var tBlue = 0
//
//    //last top gradient color
//    var lasttRed = 0
//    var lasttGreen = 0
//    var lasttBlue= 0
//
//    //bottom gradient color
//    var bRed = 0
//    var bGreen = 0
//    var bBlue = 0
//
//    //bottom top gradient color
//    var lastbRed = 0
//    var lastbGreen = 0
//    var lastbBlue = 0
//
//    //defaults
//    var colorTop = 0
//    var colorTopRight = 0
//    var colorBottom = 0
//    var colorBottomRight = 0
//
//    @EventTarget
//    fun onTick(event: TickEvent) {
//        lasttRed = tRed
//        lasttGreen = tGreen
//        lasttBlue = tBlue
//        lastbRed = bRed
//        lastbGreen = bGreen
//        lastbBlue = bBlue
//    }
//
//    var tR = 0
//    var tG = 0
//    var tB = 0
//
//    var bR = 0
//    var bG = 0
//    var bB = 0
//
//    @EventTarget
//    fun rendertabGui(evnet: Render2DEvent) {
//        tR = smoothAnimation(tRed.toDouble(), lasttRed.toDouble())
//        tG = smoothAnimation(tGreen.toDouble(), lasttGreen.toDouble())
//        tB = smoothAnimation(tBlue.toDouble(), lasttBlue.toDouble())
//
//        bR = smoothAnimation(bRed.toDouble(), lastbRed.toDouble())
//        bG = smoothAnimation(bGreen.toDouble(), lastbGreen.toDouble())
//        bB = smoothAnimation(bBlue.toDouble(), lastbBlue.toDouble())
//
//        if(gradientBackground.get()) {
//            RenderUtils.R2DUtils.drawGradientRect(5f , 50f , 80f ,  127.5f,
//                    Color(tR, tG, tB, 255).rgb,
//                    Color(bR, bG, bB, 255).rgb)
//        }
//
//        updateBackGound()
//        enabler()
//        enablerScissorBox()
//
//        categoryAnimaiton.translate(0f , selectedCategory * 15f , 2.0)
//        val categorysupery = if(categoryAnimaiton.y - 60f > 0) categoryAnimaiton.y - 60f else 0f
//
//        if(!notselectrect.get()) {
//            RenderUtils.drawRect(
//                    x,
//                    y + categoryAnimaiton.y - categorysupery,
//                    width + 5,
//                    y + categoryAnimaiton.y + 17f - categorysupery,
//                    Color(55, 55, 55, 50)
//            )
//        }
//
//        if(selectgradientBackground.get()) {
//            RenderUtils.R2DUtils.drawGradientRect(x, y + categoryAnimaiton.y - categorysupery, width + 5, y + categoryAnimaiton.y + 17f - categorysupery,  /*415277420*/
//                    Color(tR, tG, tB, 255).rgb,
//                    Color(bR, bG, bB, 255).rgb)
//        }
//
//        categoryPositonY = 0f
//        Modulecategory.forEachIndexed { index, category ->
//
//            category.animation.translate(if(selectedCategory == index) 15f else 5f , 0f ,  2.0 )
//
//            val font = FontLoaders.T18
//            font.drawString(category.displayname, x + category.animation.x , y + 5f + categoryPositonY - categorysupery, -1)
//            categoryPositonY += 15f
//        }
//
//        disablerScissorBox()
//        disabler()
//
//        enabler()
//        moduleAnimaiton.translate(0f , selecteModuleindex * 15f , 2.0)
//
//        val modulesupery = if(moduleAnimaiton.y - 150f > 0) moduleAnimaiton.y - 150f else 0f
//        val positiony = (if(ModulePositonY >= 165) 165f else ModulePositonY)
//
//        if(openModuleGui) {
//            if(gradientBackground.get()) {
//                RenderUtils.R2DUtils.drawGradientRect(90f, 50f, 170f, 216.67f,
//                        Color(tR, tG, tB, 255).rgb,
//                        Color(bR, bG, bB, 255).rgb)
//            }
//
//            RenderUtils.drawShadow((x + width + 10).toInt(), y.toInt(), (width + 5).toInt(), (positiony + 2f).toInt())
//
//            BlurBuffer.CustomBlurArea((x + width + 10), y, (width + 5), (positiony + 2f),3f)
//
//            val hudMod = LiquidBounce.moduleManager.getModule(
//                    HUD::class.java
//            ) as HUD
//            fun getClientColor(): Color {
//                return Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get())
//            }
//
//            fun getAlternateClientColor(): Color {
//                return Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get())
//            }
//            if (Shader.get()) {
//                if (colorMode.get() == "Tenacity") {
//                    val gradientColor1 = RenderUtils.interpolateColorsBackAndForth(
//                            15,
//                            0,
//                            getClientColor(),
//                            getAlternateClientColor(),
//                            hudMod.hueInterpolation.get()
//                    )
//                    val gradientColor2 = RenderUtils.interpolateColorsBackAndForth(
//                            15,
//                            90,
//                            getClientColor(),
//                            getAlternateClientColor(),
//                            hudMod.hueInterpolation.get()
//                    )
//                    val gradientColor3 = RenderUtils.interpolateColorsBackAndForth(
//                            15,
//                            180,
//                            getClientColor(),
//                            getAlternateClientColor(),
//                            hudMod.hueInterpolation.get()
//                    )
//                    val gradientColor4 = RenderUtils.interpolateColorsBackAndForth(
//                            15,
//                            270,
//                            getClientColor(),
//                            getAlternateClientColor(),
//                            hudMod.hueInterpolation.get()
//                    )
//                    RoundedUtil.drawGradientRound(
//                            x + width + 10
//                            ,y
//                            , (width + 5)
//                            , (positiony + 2f),
//                            5f,
//                            DrRenderUtils.applyOpacity(gradientColor4, .85f),
//                            gradientColor1,
//                            gradientColor3,
//                            gradientColor2
//                    )
//                }
//                if (colorMode.get() == "Sync") {
//                    val colors = arrayOf(ClickGUI.generateColor(), ClickGUI.generateColor())
//                    val gradientColor1 = RenderUtils.interpolateColorsBackAndForth(
//                            15, 0,
//                            colors[0], colors[1], hudMod.hueInterpolation.get()
//                    )
//                    val gradientColor2 = RenderUtils.interpolateColorsBackAndForth(
//                            15, 90,
//                            colors[0], colors[1], hudMod.hueInterpolation.get()
//                    )
//                    val gradientColor3 = RenderUtils.interpolateColorsBackAndForth(
//                            15, 180,
//                            colors[0], colors[1], hudMod.hueInterpolation.get()
//                    )
//                    val gradientColor4 = RenderUtils.interpolateColorsBackAndForth(
//                            15, 270,
//                            colors[0], colors[1], hudMod.hueInterpolation.get()
//                    )
//                    RoundedUtil.drawGradientRound(
//                            (x + width + 10), y, (width + 5), (positiony + 2f),
//                            0f,
//                            DrRenderUtils.applyOpacity(gradientColor4, .85f),
//                            gradientColor1,
//                            gradientColor3,
//                            gradientColor2
//                    )
//                }
//            }
//            if(!notselectrect.get()) {
//                RenderUtils.drawRect(
//                        x + width + 10,
//                        y + moduleAnimaiton.y - modulesupery,
//                        x + width + width + 15,
//                        y + moduleAnimaiton.y - modulesupery + 17f,
//                        Color(55, 55, 55, 50)
//                )
//            }
//
//            if(selectgradientBackground.get()) {
//                RenderUtils.R2DUtils.drawGradientRect(x + width + 10, y + moduleAnimaiton.y - modulesupery, x + width + width + 15, y + moduleAnimaiton.y - modulesupery + 17f,
//                        Color(tR, tG, tB, 255).rgb,
//                        Color(bR, bG, bB, 255).rgb)
//            }
//        }
//
//        RenderUtils.makeScissorBox(x + width + 10, y,  x + width + width + 15, y + positiony)
//        GL11.glEnable(GL11.GL_SCISSOR_TEST)
//        ModulePositonY = 0f
//        selecteModule.forEachIndexed{index , module ->
//            module.moduleTranslate.translate(if(selecteModuleindex == index) 15f else 5f , 0f , 2.0)
//            val font = if(module.state)
//                FontLoaders.T18 else FontLoaders.T18
//            font.drawString(module.name, x + width + 10 + module.moduleTranslate.x, y + 5f + ModulePositonY - modulesupery , -1)
//            ModulePositonY += 15f
//        }
//        GL11.glDisable(GL11.GL_SCISSOR_TEST)
//        disabler()
//    }
//
//    private fun updateBackGound() {
//        RenderUtils.drawShadow(x.toInt(), y.toInt(), width.toInt(), (height - 1).toInt())
//        val hudMod = LiquidBounce.moduleManager.getModule(
//                HUD::class.java
//        ) as HUD
//        fun getClientColor(): Color {
//            return Color(CustomUI.r.get(),CustomUI.g.get(),CustomUI.b.get())
//        }
//
//        fun getAlternateClientColor(): Color {
//            return Color(CustomUI.r2.get(),CustomUI.g2.get(),CustomUI.b2.get())
//        }
//        BlurBuffer.CustomBlurArea(x, y, width, (height - 1),7f)
//        if (Shader.get()) {
//            if (colorMode.get() == "Tenacity") {
//                val gradientColor1 = RenderUtils.interpolateColorsBackAndForth(
//                        15,
//                        0,
//                        getClientColor(),
//                        getAlternateClientColor(),
//                        hudMod.hueInterpolation.get()
//                )
//                val gradientColor2 = RenderUtils.interpolateColorsBackAndForth(
//                        15,
//                        90,
//                        getClientColor(),
//                        getAlternateClientColor(),
//                        hudMod.hueInterpolation.get()
//                )
//                val gradientColor3 = RenderUtils.interpolateColorsBackAndForth(
//                        15,
//                        180,
//                        getClientColor(),
//                        getAlternateClientColor(),
//                        hudMod.hueInterpolation.get()
//                )
//                val gradientColor4 = RenderUtils.interpolateColorsBackAndForth(
//                        15,
//                        270,
//                        getClientColor(),
//                        getAlternateClientColor(),
//                        hudMod.hueInterpolation.get()
//                )
//                RoundedUtil.drawGradientRound(
//                        x, y, width, (height - 1),
//                        5f,
//                        DrRenderUtils.applyOpacity(gradientColor4, .85f),
//                        gradientColor1,
//                        gradientColor3,
//                        gradientColor2
//                )
//            }
//            if (colorMode.get() == "Sync") {
//                val colors = arrayOf(ClickGUI.generateColor(), ClickGUI.generateColor())
//                val gradientColor1 = RenderUtils.interpolateColorsBackAndForth(
//                        15, 0,
//                        colors[0], colors[1], hudMod.hueInterpolation.get()
//                )
//                val gradientColor2 = RenderUtils.interpolateColorsBackAndForth(
//                        15, 90,
//                        colors[0], colors[1], hudMod.hueInterpolation.get()
//                )
//                val gradientColor3 = RenderUtils.interpolateColorsBackAndForth(
//                        15, 180,
//                        colors[0], colors[1], hudMod.hueInterpolation.get()
//                )
//                val gradientColor4 = RenderUtils.interpolateColorsBackAndForth(
//                        15, 270,
//                        colors[0], colors[1], hudMod.hueInterpolation.get()
//                )
//                RoundedUtil.drawGradientRound(
//                        x, y, width, (height - 1),
//                        5f,
//                        DrRenderUtils.applyOpacity(gradientColor4, .85f),
//                        gradientColor1,
//                        gradientColor3,
//                        gradientColor2
//                )
//            }
//        }
//    }
//
//    private fun enablerScissorBox() {
//        RenderUtils.makeScissorBox(x, y,x + width, y + height )
//        GL11.glEnable(GL11.GL_SCISSOR_TEST)
//    }
//
//    private fun disablerScissorBox() {
//        GL11.glDisable(GL11.GL_SCISSOR_TEST)
//    }
//
//    private fun enabler() {
//        GlStateManager.pushMatrix()
//    }
//
//    private fun disabler() {
//        GlStateManager.popMatrix()
//    }
//
//
//    @EventTarget
//    fun keyevent(event : KeyEvent) {
//        val key = event.key
//        handleKey(key)
//    }
//
//    private fun handleKey(keyCode: Int) {
//        when (keyCode) {
//            Keyboard.KEY_UP  -> { parseAction(Action.UP) }
//            Keyboard.KEY_DOWN -> { parseAction(Action.DOWN) }
//            Keyboard.KEY_LEFT -> { parseAction(Action.LEFT) }
//            Keyboard.KEY_RIGHT -> { parseAction(Action.RIGHT) }
//            Keyboard.KEY_RETURN-> { parseAction(Action.TOGGLE) }
//        }
//    }
//
//    fun smoothAnimation(current: Double, last: Double): Int {
//        return (current * mc.timer.renderPartialTicks + last * (1.0f - mc.timer.renderPartialTicks)).toInt()
//    }
//
//    private fun parseAction(action: Action) {
//        when (action) {
//            Action.UP -> {
//                if (selectedCategory > 0 && !openModuleGui) selectedCategory--
//                if (selecteModuleindex > 0) selecteModuleindex--
//            }
//            Action.DOWN -> {
//                if (selectedCategory < Modulecategory.lastIndex && !openModuleGui) selectedCategory++
//                if (selecteModuleindex < selecteModule.lastIndex) selecteModuleindex++
//            }
//            Action.LEFT -> {
//                if(openModuleGui) {
//                    openModuleGui = false
//                    selecteModuleindex = 0
//                    selecteModule = listOf()
//                }
//            }
//            Action.RIGHT -> {
//                if(!openModuleGui) {
//                    openModuleGui = true
//                    selecteModule = LiquidBounce.moduleManager.modules.filter { it.category == ModuleCategory.values()[selectedCategory]}.sortedBy { 0 }
//                }
//            }
//            Action.TOGGLE -> {
//                if(openModuleGui) {
//                    val selecetd = selecteModule[selecteModuleindex]
//                    selecetd.toggle()
//                }
//            }
//        }
//    }
//
//    /**
//     * TabGUI Action
//     */
//    enum class Action { UP, DOWN, LEFT, RIGHT, TOGGLE }
//
//    class AnimaitonCategory(var displayname : String , var animation : Translate) {}
//}
