//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.ccbluex.liquidbounce.ui.client.clickgui.skeet;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;


public class HyperGui extends GuiScreen {
    public static int mainx = 240;
    public static int mainy = 90;
    private int x2;
    private int y2;
    private boolean dragging;
    private final List<TypeScreen> types = new ArrayList();
    private final ResourceLocation hudIcon = new ResourceLocation(LiquidBounce.CLIENT_NAME.toLowerCase() + "/custom_hud_icon.png");

    public HyperGui() {
        int x = 0;
        ModuleCategory[] var2 = ModuleCategory.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ModuleCategory category = var2[var4];
            this.types.add(new TypeScreen(category, x));
            x +=  28;
        }

    }

    public void initGui() {
        super.initGui();
    }

    public String newcatename(ModuleCategory moduleCategory) {
        return moduleCategory.getDisplayName();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50)
            MinecraftInstance.mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));

        if (this.getSelectedTab() == null && !this.types.isEmpty()) {
            ((TypeScreen)this.types.get(0)).setSelected(true);
        }

        RenderUtils.drawImage(hudIcon, 9, height - 41, 32, 32);

        if (this.dragging) {
            this.mainx = this.x2 + mouseX;
            this.mainy = this.y2 + mouseY;
        }
    	Color baseColor = new Color(3,11,23, 240);
   		Color colorr = ColorUtil.interpolateColorC(baseColor, new Color(ColorUtil.applyOpacity(baseColor.getRGB(), .3f)), 0.5F);
   	 RenderUtils.drawRect((float) ((float)this.mainx-1.5), (float)this.mainy, this.mainx,this.mainy+ 350.0F,new Color(5,23,37, 255).getRGB());
   	 RenderUtils.drawRect((float)this.mainx-100, (float)this.mainy, (float) (this.mainx-1.5),this.mainy+ 350.0F,colorr.getRGB());
    //    RenderUtils.drawRect((float)this.mainx-80, (float)this.mainy, this.mainx, this.mainy+350.0F,  new Color(28, 28, 28, 180).getRGB());
        RenderUtils.drawRect((float)this.mainx, (float)this.mainy, this.mainx+350.0F, this.mainy+350.0F,  new Color(9,8,14).getRGB());
        RenderUtils.drawRect((float)this.mainx+2, (float)this.mainy+30, this.mainx+348.0F, this.mainy+31.0F,  new Color(5,23,37).getRGB());
        //RenderUtils.drawRect((float)(this.mainx + 348), (float)(this.mainy + 130), this.mainx+ 2.0F,this.mainy+ 20.0F,  new Color(28, 28, 28, 220).getRGB());
      //  RenderUtils.drawRect((float)this.mainx, (float)(this.mainy + 300), this.mainx+350.0F,this.mainy+ 20.0F,new Color(28, 28, 28, 220).getRGB());
        float var10002 = (float)(this.mainx + 3);
        FontLoaders.SB35.drawString("LiquidWing", (float)(this.mainx -92.5 ), (float)(this.mainy + 15),  (new Color(24,114,165)).getRGB());
        FontLoaders.SB35.drawString("LiquidWing", (float)(this.mainx -93.5 ), (float)(this.mainy + 15),  (new Color(255, 255, 255)).getRGB());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        FontLoaders.NL18.drawString("User:", (float)(this.mainx -95 ), (float)(this.mainy + 332),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString("Time:"+sdf.format(date), (float)(this.mainx -95 ), (float)(this.mainy + 342),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString(LiquidBounce.INSTANCE.getUSERNAME(), (float)(this.mainx -65 ), (float)(this.mainy + 332),  new Color(76, 255, 32,255).getRGB());

    //    Client.INSTANCE.getFontManager().check22.drawString("X", var10002, (float)(this.mainy + 300 + 8), (new Color(Hud.colorValue.getValue())).getRGB());
        var10002 = (float)(this.mainx + 13);

        this.types.forEach((e) -> {
            e.draw(mouseX, mouseY);
        });
        FontLoaders.NL16.drawString("Main", (float)(this.mainx -90 ), (float)(this.mainy + 42),  (new Color(130,140,150)).getRGB());

        //FontLoaders.NL18.drawString("Player", (float)(this.mainx -90 ), (float)(this.mainy + 70),  (new Color(130,140,150)).getRGB());
        //FontLoaders.NL18.drawString("Movement", (float)(this.mainx -90 ), (float)(this.mainy + 98),  (new Color(130,140,150)).getRGB());
       // FontLoaders.NL18.drawString("Render", (float)(this.mainx -90 ), (float)(this.mainy + 126),  (new Color(130,140,150)).getRGB());
        //FontLoaders.NL18.drawString("World", (float)(this.mainx -90 ), (float)(this.mainy + 154),  (new Color(130,140,150)).getRGB());

        FontLoaders.NL14.drawString("Other", (float)(this.mainx -90 ), (float)(this.mainy + 182),  (new Color(130,140,150)).getRGB());
        //FontLoaders.NL18.drawString("Exploit", (float)(this.mainx -90 ), (float)(this.mainy + 210),  (new Color(130,140,150)).getRGB());
        //FontLoaders.NL18.drawString("Script", (float)(this.mainx -90 ), (float)(this.mainy + 238),  (new Color(130,140,150)).getRGB());

        FontLoaders.NL24.drawString("Combat", (float)(this.mainx -70 ), (float)(this.mainy + 55),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.N2Icon24.drawString("B", (float)(this.mainx -90 ), (float)(this.mainy + 55),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("Player ", (float)(this.mainx -70 ), (float)(this.mainy + 83),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.N2Icon24.drawString("J", (float)(this.mainx -90 ), (float)(this.mainy + 84),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("Movement", (float)(this.mainx -70 ), (float)(this.mainy + 111),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.Check24.drawString("f", (float)(this.mainx -90 ), (float)(this.mainy + 111),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("Render", (float)(this.mainx -70 ), (float)(this.mainy + 139),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.Check24.drawString("d", (float)(this.mainx -90 ), (float)(this.mainy + 139),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("World", (float)(this.mainx -70 ), (float)(this.mainy + 167),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.NIcon24.drawString("e", (float)(this.mainx -90 ), (float)(this.mainy + 167),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("Misc", (float)(this.mainx -70 ), (float)(this.mainy + 195),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.Check24.drawString("b", (float)(this.mainx -90 ), (float)(this.mainy + 195),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("Exploit", (float)(this.mainx -70 ), (float)(this.mainy + 223),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.Check24.drawString("h", (float)(this.mainx -90 ), (float)(this.mainy + 223),  (new Color(28,133,192)).getRGB());

        FontLoaders.NL24.drawString("Color", (float)(this.mainx -70 ), (float)(this.mainy + 251),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.N2Icon24.drawString("H", (float)(this.mainx -90 ), (float)(this.mainy + 251),  (new Color(28,133,192)).getRGB());
        FontLoaders.NL24.drawString("HYT", (float)(this.mainx -70 ), (float)(this.mainy + 279),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.N2Icon24.drawString("I", (float)(this.mainx -90 ), (float)(this.mainy + 279),  (new Color(28,133,192)).getRGB());
        FontLoaders.NL24.drawString("GUI", (float)(this.mainx -70 ), (float)(this.mainy + 307),  (new Color(255, 255, 255)).getRGB());

        FontLoaders.NIcon24.drawString("a", (float)(this.mainx -90 ), (float)(this.mainy + 307),  (new Color(28,133,192)).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            Iterator var4 = this.types.iterator();

            label35:
            while(true) {
                TypeScreen typeScreen;
                do {
                    if (!var4.hasNext()) {
                        break label35;
                    }

                    typeScreen = (TypeScreen)var4.next();
                } while(!typeScreen.isHovered(mouseX, mouseY));

                Iterator var6 = this.types.iterator();

                while(var6.hasNext()) {
                    TypeScreen other = (TypeScreen)var6.next();
                    other.setSelected(false);
                }

                typeScreen.setSelected(true);
            }
        }

        TypeScreen selectedTab = this.getSelectedTab();
        if (selectedTab != null) {
            selectedTab.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (RenderUtils.isHovering((float)this.mainx, (float)this.mainy, 500.0F, 35.0F, mouseX, mouseY)) {
            this.x2 = this.mainx - mouseX;
            this.y2 = this.mainy - mouseY;
            this.dragging = true;
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.types.forEach((e) -> {
            e.mouseReleased(mouseX, mouseY, state);
        });
        if (state == 0) {
            this.dragging = false;
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.types.forEach((e) -> {
            e.keyTyped(typedChar, keyCode);
        });
        super.keyTyped(typedChar, keyCode);
    }

    public TypeScreen getSelectedTab() {
        return (TypeScreen)this.types.stream().filter(TypeScreen::isSelected).findAny().orElse(null);
    }
}
