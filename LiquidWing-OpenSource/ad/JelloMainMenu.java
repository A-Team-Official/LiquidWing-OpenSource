package ad;//package ad;
//
//import net.ccbluex.liquidbounce.LiquidBounce;
//import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
//import net.ccbluex.liquidbounce.ui.cnfont.FontDrawer;
//import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
//import net.ccbluex.liquidbounce.utils.MinecraftInstance;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiMultiplayer;
//import net.minecraft.client.gui.GuiOptions;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.GuiWorldSelection;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.entity.Render;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.GuiModList;
//import org.lwjgl.input.Mouse;
//import org.lwjgl.opengl.GL11;
//
//import java.awt.*;
//import java.io.IOException;
//
//import static net.ccbluex.liquidbounce.utils.MinecraftInstance.mc2;
//
//
//public class JelloMainMenu extends GuiScreen {
//    private FontDrawer bigfr = FontLoaders.F14;
//    private FontDrawer namefr = FontLoaders.F14;
//    private float animatedMouseX;
//    private float animatedMouseY;
//    private boolean hovered;
//    private double val;
//    private double max;
//
//
//    @Override
//    public void initGui() {
//        animatedMouseX = 0;
//        animatedMouseY = 0;
//        val = 0d;
//
//        max = 8d;
//        super.initGui();
//    }
//
//    @Override
//    public void updateScreen() {
//        double scaleVal = 2;
//        if (hovered) {
//            if (val < 8 || val < 0) {
//                val += scaleVal;
//            }
//        } else {
//            if (val == max || val > max) {
//                val -= scaleVal;
//            }
//        }
//        super.updateScreen();
//    }
//
//    @Override
//    public boolean doesGuiPauseGame() {
//        return true;
//    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        super.drawScreen(mouseX, mouseY, partialTicks);
//        MinecraftInstance.mc.getTextureManager().bindTexture2(new ResourceLocation("liquidwing/7ad/jellomain.png"));
//        DrawUtils.drawModalRectWithCustomSizedTexture(-animatedMouseX / 1.43f,  -animatedMouseY / 10.15f, 0.0f, 0.0f, this.width * 1.7, this.height * 1.1, this.width * 1.7, this.height* 1.1);
//
//        bigfr.drawCenteredStringScaled("LiquidWing", this.width / 2 - 5, this.height / 2 - 93, -1, 10f);
//
//        double posX = this.width / 2 - 153;
//        int idk = 64;
//
//        for (String name : new String[] {"Singleplayer", "Multiplayer", "Connect", "Settings", "AltManager"}) {
//            boolean hovered = DrawUtils.isHovered(posX + 8, this.height / 2 + 13, posX + 56, this.height / 2 + 61, mouseX, mouseY);
//            {
//                boolean hoveAndClicked = hovered && Mouse.isButtonDown(0);
//                double daVal = val;
//                boolean nameuh = name.equals("Singleplayer") || name.equals("Multiplayer") || name.equals("Connect") || name.equals("Settings") || name.equals("AltManager");
//                double x = posX +
//                        (hovered ? (nameuh ? -(daVal / 2) : 0) : 0),
//                        y = this.height / 2 + 5 +
//                                (hovered ? (nameuh ? -daVal : 0) : 0),
//                        wi = idk +
//                                (hovered ? (nameuh ? daVal : 0) : 0),
//                        he = idk +
//                                (hovered ? (nameuh ? daVal : 0) : 0);
//                GlStateManager.enableBlend();
//                GlStateManager.disableAlpha();
//                GL11.glDepthMask(false);
//                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//                GlStateManager.color(hoveAndClicked ? 0.9f : 1.0f, hoveAndClicked ? 0.9f : 1.0f, hoveAndClicked ? 0.9f : 1.0f, 1.0f);
//                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("liquidwing/mainmenu/" + name.toLowerCase() + ".png"));
//                GL11.glTexParameteri(3553, 10241, 9729);
//                GL11.glTexParameteri(3553, 10240, 9729);
//                DrawUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, wi, he, wi ,he);
//                GL11.glDepthMask(true);
//                GlStateManager.disableBlend();
//                GlStateManager.enableAlpha();
//
//            }
//            if (hovered) {
//                namefr.drawCenteredStringScaled(name, (float) (posX + 32), this.height / 2 + 75, new Color(255, 255, 255, 180).getRGB(), 1.4f); // 180
//                this.hovered = true;
//            } else {
//                this.hovered = false;
//            }
//            posX += 61;
//        }
//        animatedMouseX = mouseX;
//        animatedMouseY = mouseY;
//    }
//
//    @Override
//    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//        super.mouseClicked(mouseX, mouseY, mouseButton);
//        double posX = this.width / 2 - 152.5;
//        for (String name : new String[] {"Singleplayer", "Multiplayer", "Connect", "Settings", "AltManager"}) {
//            if (DrawUtils.isHovered(posX + 8, this.height / 2 + 13, posX + 56, this.height / 2 + 61, mouseX, mouseY) && mouseButton == 0) {
//                switch(name) {
//                    case "Singleplayer":
//                        mc.displayGuiScreen(new GuiWorldSelection(this));
//                        break;
//                    case "Multiplayer":
//                        mc.displayGuiScreen(new GuiMultiplayer(this));
//                        break;
//                    case "Connect":
//                        mc.displayGuiScreen(new GuiModList(this));
//                        break;
//                    case "Settings":
//                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
//                        break;
//                    case "AltManager":
//                        LiquidBounce.wrapper.getMinecraft().displayGuiScreen(
//                                LiquidBounce.wrapper.getClassProvider().wrapGuiScreen(
//                                        new GuiAltManager()
//                                ));
//                        break;
//
//                }
//            }
//
//            posX += 61;
//        }
//    }
//
//
//}
