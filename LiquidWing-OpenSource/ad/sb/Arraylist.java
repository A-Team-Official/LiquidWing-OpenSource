
package ad.sb;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.font1.CFontRenderer;
import net.ccbluex.liquidbounce.ui.client.font1.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "Arraylist",description = "Jello", category = ModuleCategory.RENDER,array = false, Chinese = "果冻模块显示")
public class Arraylist extends Module {
    List<Module> modules = new ArrayList<Module>();
    private final ListValue font = new ListValue("font", new String[]{"Misans","Jello","SFUI"}, "Jello");
    private final BoolValue shadow = new BoolValue("Shadow", true);
    private final BoolValue tag = new BoolValue("Tags", true);
    private final BoolValue useTrueFont = new BoolValue("Use-TrueFont", true);
    private final IntegerValue animateSpeed = new IntegerValue("Animate-Speed", 5, 1, 20);
    public Arraylist() {
        this.setState(true);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        this.updateElements(event.getPartialTicks()); //fps async
        this.renderArraylist();
    }

    public void updateElements(float partialTicks) {
        modules = LiquidBounce.moduleManager.getModules()
                .stream()
                .filter(mod -> mod.getArray())
                .sorted(new ModComparator())
                .collect(Collectors.toCollection(ArrayList::new));

        float tick = 1F - partialTicks;

        for (Module module : modules) {
            module.setAnimation2(module.getAnimation2() + ((module.getState() ? animateSpeed.get() : -animateSpeed.get()) * tick));
            module.setAnimation2(MathHelper.clamp(module.getAnimation2(), 0F, 20F));
        }
    }

    public void renderArraylist() {
        CFontRenderer font123 = FontLoaders.F20;
        if(font.get().equals("Misans")){
            font123 = FontLoaders.M20;
        } else if (font.get().equals("Jello")) {
            font123 = FontLoaders.J20;
        }
        else if (font.get().equals("SFUI")) {
            font123 = FontLoaders.F20;
        }
        IScaledResolution sr = classProvider.createScaledResolution(mc);
        float yStart = 1;
        float xStart = 0;

        for (Module module : modules) {
            if (module.getAnimation2() <= 0F) continue;
            if(tag.get()) {
                xStart = (float) (sr.getScaledWidth() - font123.getStringWidth(module.getTagName()) - 5);
            }
            else {
                xStart = (float) (sr.getScaledWidth() - font123.getStringWidth(module.getName()) - 5);
            }
            if (shadow.get()) {
                int sbsb = 0;
                if (tag.get()){
                sbsb = (font123.getStringWidth(module.getTagName()) + 20 + 10);}
                else {sbsb = (font123.getStringWidth(module.getName()) + 20 + 10);}
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                RenderUtils.drawImage(classProvider.createResourceLocation("liquidwing/shadow/arraylistshadow.png"), (int) (xStart - 8 - 2 - 1), (int) (yStart + 2 - 2.5f - 1.5f - 1.5f - 1.5f - 6 - 1),sbsb, (int) (18.5 + 6 + 12 + 2));
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
            }
            yStart += (7.5f + 5.25f) * (module.getAnimation2() / 20F);
        }

        yStart = 1;
        xStart = 0;

        for (Module module : modules) {
            if (module.getAnimation2() <= 0F) continue;
            if (tag.get()){
            xStart = (float) (sr.getScaledWidth() -   font123.getStringWidth(module.getTagName()) - 5);}
            else {xStart = (float) (sr.getScaledWidth() -   font123.getStringWidth(module.getName()) - 5);}
            GlStateManager.pushMatrix();
            //GlStateManager.resetColor();
            if (useTrueFont.get()) {
                GlStateManager.disableAlpha();
            }
            if (tag.get()) {
                font123.drawString(module.getTagName(), xStart, yStart + 7.5f, new Color(1F, 1F, 1F, (module.getAnimation2() / 20F) * 0.7f).getRGB());
            }
            else {font123.drawString(module.getName(), xStart, yStart + 7.5f, new Color(1F, 1F, 1F, (module.getAnimation2() / 20F) * 0.7f).getRGB());}
            if (useTrueFont.get()) {
                GlStateManager.enableAlpha();
            }
            GlStateManager.popMatrix();

            yStart += (7.5f + 5.25f) * (module.getAnimation2() / 20F);
        }

        GlStateManager.resetColor();
    }

    class ModComparator implements Comparator<Module> {
        @Override
        public int compare(Module e1, Module e2) {
            CFontRenderer font123 = FontLoaders.M20;
            if(font.get().equals("Misans")){
                font123 = FontLoaders.M20;
            } else if (font.get().equals("Jello")) {
                font123 = FontLoaders.J20;
            }
            else if (font.get().equals("SFUI")) {
                font123 = FontLoaders.F20;
            }
            if(tag.get()) {
                return (font123.getStringWidth(e1.getTagName()) < font123.getStringWidth(e2.getTagName()) ? 1 : -1);
            }
            else {
                return (font123.getStringWidth(e1.getName()) < font123.getStringWidth(e2.getName()) ? 1 : -1);
            }
        }
    }
}