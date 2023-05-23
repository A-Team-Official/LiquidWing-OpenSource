//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.ccbluex.liquidbounce.ui.client.clickgui.skeet.settings;

import ad.Client;
import net.ccbluex.liquidbounce.ui.client.clickgui.skeet.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.skeet.ModuleRender;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

import java.awt.*;




public class OptionSetting extends Downward<BoolValue> {
    public int mainx;
    public int mainy;
    public int y;

    public OptionSetting(BoolValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    public void draw(int mouseX, int mouseY) {
        this.mainx = Client.instance.skeet.mainx;
        this.mainy = Client.instance.skeet.mainy;
        this.y = (int)(this.pos.y + (float)this.getScrollY());
        FontLoaders.NL18.drawString(((BoolValue)this.setting).getName(), (float)(this.mainx + 17) + this.pos.x, (float)(this.mainy + 38 + this.y), new Color(130,140,150).getRGB());
        RenderUtils.drawRect((float)(this.mainx + 153) + this.pos.x, (float)(this.mainy + 38 + this.y), (float)(this.mainx + 153) + this.pos.x+8.0F,(float)(this.mainy + 38 + this.y)+ 8.0F, new Color(5,23,37).getRGB());
        if((boolean) ((BoolValue)this.setting).get()) {
            RenderUtils.drawCheck((double)((float)this.mainx + 154.5F + this.pos.x), (double)((float)this.mainy + 41.5F + (float)this.y), 2, (new Color(5,166,238)).getRGB());
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovering((float)(this.mainx + 153) + this.pos.x, (float)(this.mainy + 38 + this.y),this.mainx+ 8.0F, 8.0F, mouseX, mouseY) && mouseButton == 0) {
            ((BoolValue)this.setting).set(!(boolean)setting.get());;
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
