package net.ccbluex.liquidbounce

import ad.util.GuiPasswordField
import ad.util.GuiUserField
import ad.utils.Color.modules.CustomUI
import ad.utils.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce.USERNAME
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MouseUtils.mouseWithinBounds
import net.ccbluex.liquidbounce.utils.misc.RandomUtils.animtips
import net.ccbluex.liquidbounce.utils.misc.RandomUtils.animtipsrun
import net.ccbluex.liquidbounce.utils.render.AnimationUtils
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

class GuiLogin : GuiScreen() {
    private val i = false
    var UserName: String? = null
    var drag = false
    var alpha = 0
    var hwid: String = ""
    var ticks = false
    var tick = 0
    var ticksrun = false
    var tickrun = 0
    var color = Color(0, 120, 255, 255)
    private var currentX = 0f
    private var currentY = 0f

    private var unHeight = 0
    private var targetUnHeight = 0
    private var passHeight = 0
    private var targetPassHeight = 0

    override fun drawScreen(i: Int, j: Int, f: Float) {

        targetUnHeight = if (username!!.isFocused || username!!.text.isNotEmpty()) {
            (height / 2f - 58 - 3).toInt()
        } else {
            (height / 2f - 37).toInt()
        }

        targetPassHeight = if (qq!!.isFocused || qq!!.text.isNotEmpty()) {
            (height / 2f - 11 - 5).toInt()
        } else {
            (height / 2f + 8).toInt()
        }

        unHeight = AnimationUtils.animate(targetUnHeight.toDouble(), unHeight.toDouble(), 0.1).toInt()
        passHeight = AnimationUtils.animate(targetPassHeight.toDouble(), passHeight.toDouble(), 0.1).toInt()

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
                this.width / 2f - 82f,this.height /2f + 49,162f,Fonts.mon40.fontHeight+17f, i, j
            ) /*i > this.width / 2f - 70 && i < this.width / 2f - 70 + 135 && j > this.height / 2f - 25 && j < this.height / 2f - 25 + 22*/) {
            if (Firstcheck.X2name().equals(EncrypyUtil.encode(Firstcheck.getHWID()+ QQCheek.QQNumber))) {
                MySQLDemo.reg()
                hwid = MySQLDemo.X2Hwid(username!!.text)
                try {
                    HWID = MySQLDemo.getHWID1()
                    if (  HWID!!.isNotEmpty()) {
                        UserName = username!!.text
                        if (MySQLDemo.X2name(username!!.text) != null   ) {
                            if (MySQLDemo.X2name(username!!.text) == username!!.text   ) {
                                if (qq!!.text == MySQLDemo.X2sate2(username!!.text)) {
                                    if (MySQLDemo.X2pass(username!!.text) == qq!!.text) {
                                        anim *= 10
                                        color = Color(229, 8, 223, 255)
                                        b = 10



                                        encoder = Base64.getEncoder()
                                        decoder = Base64.getDecoder()
                                        USERNAME = username!!.text


                                        LiquidBounce.USERNAME = username!!.text
                                        LiquidBounce.Password = qq!!.text
                                        LiquidBounce.setQQ1(MySQLDemo.X2QQ(username!!.text))

                                        LiquidBounce.stopClient()


                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "密码错误", "错误", 0)

                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "账户错误", "错误", 0)

                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"账户不存在","错误",0)

                        }

                    }

                    //                    else JOptionPane.showInputDialog("Auth Failed Copy UR HWID TO ADMIN.", hwid)
                }catch (throwable1: Throwable) {
                    throwable1.printStackTrace()
            }
//                else JOptionPane.showInputDialog("Auth Failed Copy UR HWID TO ADMIN.", hwid)
            } else {
                Firstcheck.Update4()
                JOptionPane.showInputDialog("错误","许可已发出但未激活")
            }
        }
        if (CustomUI.Chinese.get()){
        GL11.glPushMatrix()
       FontLoaders.F30.drawCenteredString("登录", width / 2f, height / 2f - 90, Color(255,255,255).rgb,false)
        if (i > width / 2f + 57 && i < width / 2f + 57 + FontLoaders.novologo245.getStringWidth("M") && j > height / 2f - 87 && j < height / 2f - 85 + FontLoaders.novologo245.height && Mouse.isButtonDown(
                0
            )
        ) {
            ticks = true
            setClipboardString(hwid)
        }
        GL11.glPopMatrix()

        //QQ
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f + 11f,162f,0.7f,2f,Color(53,80,111))
        FontLoaders.F18.drawCenteredString("密码",
            (width / 2f - 53f).toDouble(), passHeight.toFloat().toDouble(), Color(255,255,255, 255).rgb)
        Fonts.flux45.drawCenteredString("e",width / 2f - 75f, height / 2f,Color(255,255,255).rgb)

        //USERNAME
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f - 35f,162f,0.7f,2f,Color(53,80,111))
            FontLoaders.F18.drawCenteredString("用户名",
                (width / 2f - 50f).toDouble(), unHeight.toFloat().toDouble(), Color(255,255,255, 255).rgb)
        Fonts.flux45.drawCenteredString("d",width / 2f - 77f, height / 2f - 46,Color(255,255,255).rgb)
        //Button
        RoundedUtil.drawRound(this.width / 2f - 82f,this.height /2f + 49,162f,Fonts.mon40.fontHeight+17f,5f,Color(53,80,111))
            FontLoaders.F20.drawCenteredString("登录",this.width / 2f,this.height /2f + 58,Color(230,230,230).rgb,false)



            FontLoaders.F15.drawCenteredString("没有账户? -> 注册",
                (this.width / 2f).toDouble(), (this.height / 2f + 90f).toDouble(),Color(255,255,255).rgb)
}
        else{
            GL11.glPushMatrix()
            Fonts.mon60.drawCenteredString("Login", width / 2f, height / 2f - 90, Color(255,255,255).rgb,false)
            if (i > width / 2f + 57 && i < width / 2f + 57 + FontLoaders.novologo245.getStringWidth("M") && j > height / 2f - 87 && j < height / 2f - 85 + FontLoaders.novologo245.height && Mouse.isButtonDown(
                    0
                )
            ) {
                ticks = true
                setClipboardString(hwid)
            }
            GL11.glPopMatrix()

            //QQ
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f + 11f,162f,0.7f,2f,Color(53,80,111))
            Fonts.mon35.drawCenteredString("Password", width / 2f - 43f, passHeight.toFloat(), Color(255,255,255, 255).rgb)
            Fonts.flux45.drawCenteredString("e",width / 2f - 75f, height / 2f - 1,Color(255,255,255).rgb)

            //USERNAME
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height / 2f - 35f,162f,0.7f,2f,Color(53,80,111))
            Fonts.mon35.drawCenteredString("Username", width / 2f - 45f, unHeight.toFloat(), Color(255,255,255, 255).rgb)
            Fonts.flux45.drawCenteredString("d",width / 2f - 77f, height / 2f - 46,Color(255,255,255).rgb)
            //Button
            RoundedUtil.drawRound(this.width / 2f - 82f,this.height /2f + 49,162f,Fonts.mon40.fontHeight+17f,5f,Color(53,80,111))
            Fonts.mon40.drawCenteredString("Login",this.width / 2f,this.height /2f + 58,Color(20,20,20).rgb,false)



            Fonts.mon30.drawCenteredString("Don't have any account? -> Register",this.width / 2f, this.height / 2f + 90f,Color(255,255,255).rgb)
        }
        username!!.drawTextBox2()
        qq!!.drawTextBox2()
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
        qq = GuiUserField(fontRenderer, (width / 2f - 72f).toInt(), (height / 2f - 11).toInt(), 162, 27)
    }

    override fun keyTyped(c0: Char, i: Int) {
        if (c0.toInt() == 9) {
            username!!.isFocused = true
        }
        username!!.textboxKeyTyped(c0, i)
        qq!!.textboxKeyTyped(c0, i)
    }

    override fun mouseClicked(i: Int, j: Int, k: Int) {
        try {
            super.mouseClicked(i, j, k)
        } catch (ioexception: IOException) {
            ioexception.printStackTrace()
        }
        username!!.mouseClicked(i, j, k)
        qq!!.mouseClicked(i, j, k)

        if (CustomUI.Chinese.get()){
            if (mouseWithinBounds(
                    (this.width / 2f + FontLoaders.F15.getStringWidth("没有账户? ->") - 40f),this.height / 2f + 85f,FontLoaders.F18.getStringWidth("注册") + 3f,FontLoaders.F18.height+ 2f, i, j
                )) {
                Minecraft.getMinecraft().displayGuiScreen(GuiRegister())
            }
        }
        else{
        if (mouseWithinBounds(
                (this.width / 2f + Fonts.mon30.getStringWidth("Don't have any account? ->") - 85f),this.height / 2f + 85f,Fonts.mon35.getStringWidth("Register") + 3f,Fonts.mon35.fontHeight+ 2f, i, j
            )) {
            Minecraft.getMinecraft().displayGuiScreen(GuiRegister())
        }
            }
    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
    }

    override fun updateScreen() {
        username!!.updateCursorCounter()
        qq!!.updateCursorCounter()
    }

    companion object {
        @JvmField
        var b = 0
        var anim = 20
        @JvmField
        var encoder: Base64.Encoder? = null
        @JvmField
        var decoder: Base64.Decoder? = null
        var qq: GuiUserField? = null
        var username: GuiUserField? = null
        var password: GuiPasswordField? = null
        var render = false
        var HWID: String? = null
        var timer: MSTimer? = null
    }
}