//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.ccbluex.liquidbounce.ui.client.clickgui.skeet.settings;

import ad.Client;
import net.ccbluex.liquidbounce.ui.client.clickgui.skeet.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.skeet.ModuleRender;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.misc.MathUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.awt.*;


public class NumberSetting extends Downward<IntegerValue> {
    public int mainx;
    public int mainy;
    public int y;
    public float percent = 0.0F;
    private boolean iloveyou;

    public NumberSetting(IntegerValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    public void draw(int mouseX, int mouseY) {
    	   this.mainx = Client.instance.skeet.mainx;
           this.mainy = Client.instance.skeet.mainy;
        this.y = (int)(this.pos.y + (float)this.getScrollY());
        FontLoaders.NL18.drawString(((IntegerValue)this.setting).getName(), (float)(this.mainx + 17) + this.pos.x, (float)(this.mainy + 38 + this.y), new Color(130,140,150).getRGB());
        RenderUtils.drawRect((float)(this.mainx + 91) + this.pos.x, (float)(this.mainy + 40 + this.y),(float)(this.mainx + 161) + this.pos.x,(float)(this.mainy + 45 + this.y), new Color(5,23,37).getRGB());
        double clamp = (double) MathHelper.clamp((float)Minecraft.getDebugFPS() / 30.0F, 1.0F, 9999.0F);
        double percentBar = (((Number) ((IntegerValue)this.setting).get()).doubleValue() - ((IntegerValue)this.setting).getMinimum()) / (((IntegerValue)this.setting).getMaximum() - ((IntegerValue)this.setting).getMinimum());
        this.percent = Math.max(0.0F, Math.min(1.0F, (float)((double)this.percent + (Math.max(0.0, Math.min(percentBar, 1.0)) - (double)this.percent) * (0.2 / clamp))));
    //    RenderUtils.drawRect((float)(this.mainx + 91) + this.pos.x, (float)(this.mainy + 40 + this.y),this.mainx+161 + this.pos.x +percent,(float)(this.mainy + 44 + this.y), -1);
        RenderUtils.drawRect((float)(this.mainx + 91) + this.pos.x + 70.0F * this.percent - 1.0F, (float)(this.mainy + 40 + this.y) - 0.1F - 0.5F,(float)(this.mainx + 91) + this.pos.x , (float)(this.mainy + 45 + this.y) - 0.1F - 0.5F, new Color(5,166,238).getRGB());
    //    RenderUtils.drawRect((float)this.mainx + 87.5F + this.pos.x + 70.0F * this.percent - 0.5F, (float)this.mainy + 40.5F + (float)this.y - 0.1F - 0.5F,this.mainx+  5.0F,this.mainy+  5.0F,  -1);
        if (this.iloveyou) {
            float percentt = Math.min(1.0F, Math.max(0.0F, ((float)mouseX - ((float)(this.mainx + 91) + this.pos.x)) / 99.0F * 1.4F));
            double newValue = (double)percentt * (((IntegerValue)this.setting).getMaximum() - ((IntegerValue)this.setting).getMinimum()) + ((IntegerValue)this.setting).getMinimum();
            double set = MathUtils.incValue(newValue, 1);
            ((IntegerValue)this.setting).set(set);
        }

        FontLoaders.NL16.drawString(((IntegerValue)this.setting).get() + ((IntegerValue)this.setting).getDisplayName(), (float)(this.mainx + 90) + this.pos.x + 70.0F - (float)FontLoaders.NL16.getStringWidth(((IntegerValue)this.setting).get() + ((IntegerValue)this.setting).getDisplayName()), (float)this.mainy + 31.0F + (float)this.y, new Color(130,140,150).getRGB());
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovering((float)(this.mainx + 91) + this.pos.x, (float)(this.mainy + 40 + this.y), 70.0F, 5.0F, mouseX, mouseY) && mouseButton == 0) {
            this.iloveyou = true;
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.iloveyou = false;
        }

    }
}
