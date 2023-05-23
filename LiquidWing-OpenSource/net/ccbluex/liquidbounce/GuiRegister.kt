package net.ccbluex.liquidbounce

import ad.util.GuiUserField
import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.FontFamily
import net.ccbluex.liquidbounce.utils.MouseUtils.mouseWithinBounds
import net.ccbluex.liquidbounce.utils.misc.RandomUtils.animtips
import net.ccbluex.liquidbounce.utils.misc.RandomUtils.animtipsrun
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import verify.EncrypyUtil
import verify.Firstcheck
import verify.MySQLDemo
import verify.QQCheek
import java.awt.Color
import java.io.IOException
import java.util.*
import javax.swing.JOptionPane

class GuiRegister : GuiScreen() {
    private val i = false
    var UserName: String? = null
    var drag = false
    var alpha = 0
    var hwid: String
    var ticks = false
    var tick = 0
    var ticksrun = false
    var tickrun = 0
    var color = Color(0, 120, 255, 255)
    private var currentX = 0f
    private var currentY = 0f

    init {
        hwid = FontFamily.getFontFamily()
    }

    override fun drawScreen(i: Int, j: Int, f: Float) {
        card!!.maxStringLength = 114514;
        username!!.maxStringLength = 114514;
        password!!.maxStringLength = 114514;
        JOptionPane.getRootFrame().isAlwaysOnTop = true
        if (this.i && alpha < 255) {
            alpha += 5
        }
        val h = ScaledResolution(mc).scaledHeight
        val w = ScaledResolution(mc).scaledWidth
        RenderUtils.drawGradientSideways(
            0.0,
            0.0,
            w.toDouble(),
            h.toDouble(),
            Color(60, 96, 203).rgb,
            Color(51, 201, 217).rgb
        )
        RenderUtils.drawImage4(ResourceLocation("liquidwing/mainmenu/bg1.png"), 0, 0, width, height)
        val sr = ScaledResolution(mc)
        val xDiff = ((i - h / 2).toFloat() - currentX) / sr.scaleFactor.toFloat()
        val yDiff = ((j - w / 2).toFloat() - currentY) / sr.scaleFactor.toFloat()
        currentX += xDiff * 0.3f
        currentY += yDiff * 0.3f
        drag = Mouse.isButtonDown(0)
//       RenderUtils.drawShadow(this.width / 2f - 100f, this .height / 2f - 120, 200f, 235f)
//        RenderUtils.drawShadow(this.width / 2f - 100f, this .height / 2f - 120, 200f, 235f)
        BlurBuffer.CustomBlurRoundArea(this.width / 2f - 100f, this .height / 2f - 120, 200f, 235f,12f,100f)
        RoundedUtil.drawRoundOutline(this.width / 2f - 100f, this .height / 2f - 120, 200f, 235f,12f,0.3f,Color(240,240,240,0),Color(17,34,54))
        RoundedUtil.drawRound(this.width / 2f - 100f, this .height / 2f - 120, 200f, 235f,12f,Color(255,255,255,10))

        if (Mouse.isButtonDown(0) && mouseWithinBounds(
                this.width / 2f - 82f,this.height /2f + 69,162f,Fonts.mon40.fontHeight+17f, i, j
            )) {
            if (Firstcheck.X2name().equals(EncrypyUtil.encode(Firstcheck.getHWID() + QQCheek.QQNumber))) {

                if (username!!.text.isNotEmpty() && password!!.text.isNotEmpty() && card!!.text.isNotEmpty()) {
                    MySQLDemo.reg()
                    if (MySQLDemo.x2UseCardPass(card!!.text, username!!.text, password!!.text)) {
                        mc.displayGuiScreen(GuiLogin())
                    } else JOptionPane.showMessageDialog(null, "注册失败！")
                } else {
                    JOptionPane.showMessageDialog(null, "用户名、密码和注册码不能为空。")
                }
            }else {
                Firstcheck.Update4()

                JOptionPane.showMessageDialog(
                    null,
                    "许可未激活,但已发送请求",
                    "failed",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
        if (CustomUI.Chinese.get()){
        GL11.glPushMatrix()
            FontLoaders.F30.drawCenteredString("注册", width / 2f, height / 2f - 90, Color(255,255,255).rgb,false)
        if (i > width / 2f + 57 && i < width / 2f + 57 + FontLoaders.novologo245.getStringWidth("M") && j > height / 2f - 87 && j < height / 2f - 85 + FontLoaders.novologo245.height && Mouse.isButtonDown(
                0
            )
        ) {
            ticks = true
            setClipboardString(hwid)
        }
        GL11.glPopMatrix()

        //code
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f + 57f,162f,0.7f,2f,Color(53,80,111))
        FontLoaders.F18.drawCenteredString("卡密", width / 2f - 53.0, height / 2f + 30.0, Color(255,255,255, 255).rgb)
        Fonts.flux45.drawCenteredString("q",width / 2f - 75f, height / 2f + 44,Color(255,255,255).rgb)

        //pass
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f + 11f,162f,0.7f,2f,Color(53,80,111))
            FontLoaders.F18.drawCenteredString("密码", width / 2f - 53.0, height / 2f - 14.0 , Color(255,255,255, 255).rgb)
        Fonts.flux45.drawCenteredString("e",width / 2f - 75f, height / 2f - 1,Color(255,255,255).rgb)

        //USERNAME
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f - 35f,162f,0.7f,2f,Color(53,80,111))
            FontLoaders.F18.drawCenteredString("用户名", width / 2f - 50.0, height / 2f - 58.0, Color(255,255,255, 255).rgb)
        Fonts.flux45.drawCenteredString("d",width / 2f - 77f, height / 2f - 46,Color(255,255,255).rgb)

        //Button
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height /2f + 69,162f,Fonts.mon40.fontHeight+17f,5f,Color(53,80,111))
            FontLoaders.F20.drawCenteredString("注册",this.width / 2f,this.height /2f + 78,Color(230,230,230).rgb,false)

}
        else{
            GL11.glPushMatrix()
            Fonts.mon60.drawCenteredString("Register", width / 2f, height / 2f - 90, Color(255,255,255).rgb,false)
            if (i > width / 2f + 57 && i < width / 2f + 57 + FontLoaders.novologo245.getStringWidth("M") && j > height / 2f - 87 && j < height / 2f - 85 + FontLoaders.novologo245.height && Mouse.isButtonDown(
                    0
                )
            ) {
                ticks = true
                setClipboardString(hwid)
            }
            GL11.glPopMatrix()

            //code
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f + 57f,162f,0.7f,2f,Color(53,80,111))
            Fonts.mon35.drawCenteredString("Register code", width / 2f - 38f, height / 2f + 30, Color(255,255,255, 255).rgb)
            Fonts.flux45.drawCenteredString("q",width / 2f - 75f, height / 2f + 44,Color(255,255,255).rgb)

            //pass
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f + 11f,162f,0.7f,2f,Color(53,80,111))
            Fonts.mon35.drawCenteredString("Password", width / 2f - 45f, height / 2f - 14, Color(255,255,255, 255).rgb)
            Fonts.flux45.drawCenteredString("e",width / 2f - 75f, height / 2f - 1,Color(255,255,255).rgb)

            //USERNAME
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f - 35f,162f,0.7f,2f,Color(53,80,111))
            Fonts.mon35.drawCenteredString("Username", width / 2f - 45f, height / 2f - 58, Color(255,255,255, 255).rgb)
            Fonts.flux45.drawCenteredString("d",width / 2f - 77f, height / 2f - 46,Color(255,255,255).rgb)

            //Button
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height /2f + 69,162f,Fonts.mon40.fontHeight+17f,5f,Color(53,80,111))
            Fonts.mon40.drawCenteredString("Register",this.width / 2f,this.height /2f + 78,Color(20,20,20).rgb,false)
        }


        if (CustomUI.Chinese.get()) {
            if (Mouse.isButtonDown(0) && mouseWithinBounds(
                    (this.width / 2f + FontLoaders.F15.getStringWidth("已经拥有账户? ->") - 40f),
                    this.height / 2f + 103f,
                    FontLoaders.F18.getStringWidth("登录") + 3f,
                    FontLoaders.F18.height + 2f,
                    i,
                    j
                )
            ) {
                Minecraft.getMinecraft().displayGuiScreen(GuiLogin())
            }
        }
        else{
            if (Mouse.isButtonDown(0) && mouseWithinBounds(
                    (this.width / 2f + Fonts.mon30.getStringWidth("Already have an account? ->") - 85f),this.height / 2f + 103f,Fonts.mon35.getStringWidth("Register") + 3f,Fonts.mon35.fontHeight+ 2f, i, j
                )){
                Minecraft.getMinecraft().displayGuiScreen(GuiLogin())
            }
        }
        if (CustomUI.Chinese.get()) {
            FontLoaders.F15.drawCenteredString(
                "已经拥有账户? -> 登录",
                this.width / 2.0,
                this.height / 2f + 103.0,
                Color(255, 255, 255).rgb
            )
        }
        else{
            Fonts.mon30.drawCenteredString(
                "Already have an account? -> Login",
                this.width / 2f,
                this.height / 2f + 103f,
                Color(255, 255, 255).rgb
            )
        }

        username!!.drawTextBox2()
        password!!.drawTextBox2()
        card!!.drawTextBox2()
        GL11.glPushMatrix()
        if (ticks) {
            tick++
            animtips(60)
        }
        if (tick > 60) {
            tick = 0
            ticks = false
        }
        if (!ticks) {
            animtips(-60)
        }
//        GL11.glTranslated(0.0, tiph.toDouble(), 0.0)
//        RoundedUtil.drawRound(width / 2f - 55, height / -10f, 105f, 15f, 2f, Color(30, 30, 30, 255))
//        FontLoaders.bold20.drawString("Logged as " + USERNAME, (width / 2f - 35), height.toFloat() / -10f + 3f, -1)
//        GL11.glPopMatrix()
//        GL11.glPushMatrix()
        if (ticksrun) {
            tickrun++
        }
        if (tickrun > 20) {
            animtipsrun(60)
        }
        if (tickrun > 80) {
            tick = 0
            ticksrun = false
        }
        if (!ticksrun) {
            animtipsrun(-60)
        }
        GL11.glPopMatrix()
        super.drawScreen(i, j, f)
    }

    override fun initGui() {
        super.initGui()
        render = true
        username = GuiUserField(fontRenderer, (width / 2f - 72f).toInt(), (height / 2f - 56).toInt(), 162, 27)
        password = GuiUserField(fontRenderer, (width / 2f - 72f).toInt(), (height / 2f - 11).toInt(), 162, 27)
        card = GuiUserField(fontRenderer, (width / 2f - 72f).toInt(), (height / 2f + 34).toInt(), 162, 27)
    }

    override fun keyTyped(c0: Char, i: Int) {
        if (c0.toInt() == 9) {
            username!!.isFocused = true
        }
        username!!.textboxKeyTyped(c0, i)
        password!!.textboxKeyTyped(c0, i)
        card!!.textboxKeyTyped(c0, i)
    }

    override fun mouseClicked(i: Int, j: Int, k: Int) {
        try {
            super.mouseClicked(i, j, k)
        } catch (ioexception: IOException) {
            ioexception.printStackTrace()
        }
        username!!.mouseClicked(i, j, k)
        password!!.mouseClicked(i, j, k)
        card!!.mouseClicked(i, j, k)

        if (mouseWithinBounds(
                (this.width / 2f + Fonts.mon30.getStringWidth("Already have an account? ->") - 85f),this.height / 2f + 85f,Fonts.mon35.getStringWidth("Register") + 3f,Fonts.mon35.fontHeight+ 2f, i, j
            )) {
            Minecraft.getMinecraft().displayGuiScreen(GuiLogin())
        }
    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
    }

    override fun updateScreen() {
        username!!.updateCursorCounter()
        password!!.updateCursorCounter()
        card!!.updateCursorCounter()
    }

    companion object {
        @JvmField
        var b = 0
        var anim = 20
        @JvmField
        var encoder: Base64.Encoder? = null
        @JvmField
        var decoder: Base64.Decoder? = null
        var password: GuiUserField? = null
        var username: GuiUserField? = null
        var card: GuiUserField? = null
        var render = false
        var HWID: String? = null
        var timer: MSTimer? = null
    }
}