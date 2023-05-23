package ad.sb.novoline.ui


import com.mojang.realmsclient.gui.ChatFormatting
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.EaseUtils.easeOutQuart
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.io.IOException

class GuiMainMenu : GuiScreen() {
    var mc: Minecraft = Minecraft.getMinecraft()
    var sr: ScaledResolution? = null
    private var progress = 0f
    private var lastMS = 0L
    override fun initGui() {
        sr = ScaledResolution(Minecraft.getMinecraft())
        lastMS = System.currentTimeMillis()
        progress = 0f
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        RenderUtils.drawImage4(ResourceLocation("liquidwing/mainmenu/bg1.png"), 0, 0, width, height)

        val multi = ResourceLocation("liquidwing/mainmenu/mp.png")
        val multih = ResourceLocation("liquidwing/mainmenu/mph.png")

        val single = ResourceLocation("liquidwing/mainmenu/sp.png")
        val singleh = ResourceLocation("liquidwing/mainmenu/sph.png")

        val alt = ResourceLocation("liquidwing/mainmenu/hp.png")
        val alth = ResourceLocation("liquidwing/mainmenu/hph.png")
        val goto = ResourceLocation("liquidwing/mainmenu/goto.png")

        val logo = ResourceLocation("liquidwing/mainmenu/logo.png")
        val options = ResourceLocation("liquidwing/mainmenu/icon.png")
        val pr = ResourceLocation("liquidwing/mainmenu/pr.png")

        val str = "Dev"
        val str1 = "Tester"
        val str2 = "Contributor"
        val str3 = "User"

        val user = LiquidBounce.USERNAME + " "
        // Group
        RenderUtils.drawImage4(pr,5,5,12,12)
        if (LiquidBounce.group.equals("Dev"))
        {
            Fonts.tenacitybold40.drawString( user+ ChatFormatting.GREEN + str,17f,16f-Fonts.tenacitybold40.fontHeight,Color.WHITE.rgb)
        }
        else if (LiquidBounce.group.equals("Tester")){
            Fonts.tenacitybold40.drawString(user + LiquidBounce.USERNAME + ChatFormatting.DARK_BLUE + str1,17f,16f-Fonts.tenacitybold40.fontHeight,Color.WHITE.rgb)
        }
        else if (LiquidBounce.group.equals("Contributor")){
            Fonts.tenacitybold40.drawString(user + LiquidBounce.USERNAME + ChatFormatting.YELLOW + str2,17f,16f-Fonts.tenacitybold40.fontHeight,Color.WHITE.rgb)
        }
        else if (LiquidBounce.group.equals("User")){
            Fonts.tenacitybold40.drawString(user + LiquidBounce.USERNAME + ChatFormatting.GRAY + str3,17f,16f-Fonts.tenacitybold40.fontHeight,Color.WHITE.rgb)
        }
        // Logo
        RenderUtils.drawImage4(logo,this.width/2 - 221/2,this.height/2 - 55,221/2,49/2)
        Fonts.tenacitybold40.drawCenteredString("@A-Team",this.width/2f + 5f,this.height - 15f,Color.WHITE.rgb)
        //Options
        RenderUtils.drawImage4(options,this.width/2 + 119/2 + 30,this.height/2 - 50,18,18)
        // MultiPlayers
        RenderUtils.drawImage4(multi,this.width/2 - 110,this.height/2 - 30,220,50)
        RenderUtils.drawImage4(goto,this.width/2 -5,this.height/2 - 5,10,10)
        Fonts.tenacitybold40.drawCenteredString("Multi Players",this.width/2f,this.height/2f - 15,Color.WHITE.rgb)

        if (RenderUtils.isHovered(this.width/2 - 110f,this.height/2f - 30,220f,50f,mouseX,mouseY)){
            RenderUtils.drawImage4(multih,this.width/2 - 110,this.height/2- 30,220,50)
            RenderUtils.drawImage4(goto,this.width/2 -5,this.height/2 - 5,10,10)
            Fonts.tenacitybold40.drawCenteredString("Multi Players",this.width/2f,this.height/2f - 15,Color.WHITE.rgb)
        }

        //SinglePlayer
        RenderUtils.drawImage4(single,this.width/2 - 110,this.height/2 + 25,215/2,67/2)
        RenderUtils.drawImage4(goto,this.width/2 - 60,this.height/2 + 42,10,10)
        Fonts.tenacitybold40.drawCenteredString("SinglePlayers",this.width/2f - 55,this.height/2f + 32,Color.WHITE.rgb)

        if (RenderUtils.isHovered(this.width/2 - 110f,this.height/2f + 25,215/2f,67/2f,mouseX,mouseY)){
            RenderUtils.drawImage4(singleh,this.width/2 - 110,this.height/2 + 25,215/2,67/2)
            RenderUtils.drawImage4(goto,this.width/2 - 60,this.height/2 + 42,10,10)
            Fonts.tenacitybold40.drawCenteredString("SinglePlayers",this.width/2f - 55,this.height/2f + 32,Color.WHITE.rgb)
        }
        //Alt
        RenderUtils.drawImage4(alt,this.width/2 + 3,this.height/2 + 25,215/2,67/2)
        RenderUtils.drawImage4(goto,this.width/2 + 52,this.height/2 + 42,10,10)
        Fonts.tenacitybold40.drawCenteredString("AltManager",this.width/2f + 56,this.height/2f + 32,Color.WHITE.rgb)

        if (RenderUtils.isHovered(this.width/2f,this.height/2f + 25,215/2f,67/2f,mouseX,mouseY)){
            RenderUtils.drawImage4(alth,this.width/2 + 3,this.height/2 + 25,215/2,67/2)
            RenderUtils.drawImage4(goto,this.width/2 + 52,this.height/2 + 42,10,10)
            Fonts.tenacitybold40.drawCenteredString("AltManager",this.width/2f + 56,this.height/2f + 32,Color.WHITE.rgb)
        }
    }

    @Throws(IOException::class)
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        progress = if (progress >= 1f) 1f else (System.currentTimeMillis() - lastMS).toFloat() / 2000F

        val trueAnim = easeOutQuart(progress.toDouble())

        GL11.glTranslated(0.0, (1 - trueAnim) * -height, 0.0)
        if (RenderUtils.isHovered(
                this.width/2 - 110f,this.height/2f + 25,215/2f,67/2f,mouseX,mouseY
            )
        ) {
            Minecraft.getMinecraft().displayGuiScreen(GuiWorldSelection(this))
        }
        if (RenderUtils.isHovered(
                this.width/2 - 110f,this.height/2f - 30f,220f,50f,mouseX,mouseY
            )
        ) {
            Minecraft.getMinecraft().displayGuiScreen(GuiMultiplayer(this))
        }
        if (RenderUtils.isHovered(
                this.width/2 + 119/2 + 30f,this.height/2 - 50f,18f,18f, mouseX, mouseY
            )
        ) {
            Minecraft.getMinecraft().displayGuiScreen(GuiOptions(this, Minecraft.getMinecraft().gameSettings))
        }
        if (RenderUtils.isHovered(
                this.width/2f,this.height/2f + 25,215/2f,67/2f,mouseX,mouseY
            )
        ) {
            LiquidBounce.wrapper.minecraft.displayGuiScreen(
                LiquidBounce.wrapper.classProvider.wrapGuiScreen(
                    GuiAltManager()
                )
            )
        }
    }
}