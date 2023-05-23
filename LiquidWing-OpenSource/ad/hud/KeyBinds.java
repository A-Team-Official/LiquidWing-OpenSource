package ad.hud;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;

import java.awt.*;

@ElementInfo(name = "KeyBinds-NEVERLOSE")
public class KeyBinds extends Element {
    public final ListValue md = new ListValue("Mode", new String[]{"Distance"},"Distance");
    public final BoolValue onlyState = new BoolValue("OnlyModuleState", true);

    @Override
    public Border drawElement() {
        int y2 =0;


        //draw Background

        RoundedUtil.drawRound(0, 0, 100, 15 + getmoduley(), 2f, new Color(30, 30, 30, 200));
        RoundedUtil.drawRound(0, 0, 100, 15, 2f, new Color(20, 20, 20, 190));

        //draw Title
        net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts.Icons.icons_20.icons_20.drawString("n", 3, 6f, new Color(4, 188, 255).getRGB(), false);
        net.ccbluex.liquidbounce.ui.font.Fonts.sfui35.drawString("Binds", 20, 5.5f, -1, false);

        //draw Module Bind
        for (Module module : LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0) continue;
            if(onlyState.get()) {
                if (!module.getState()) continue;
            }

            net.ccbluex.liquidbounce.ui.font.Fonts.sfui30.drawString(module.getName(), 3, y2 + 21f, -1, false);

            net.ccbluex.liquidbounce.ui.font.Fonts.sfui30.drawString("[Toggle]", 95 - net.ccbluex.liquidbounce.ui.font.Fonts.sfui30.getStringWidth("[Toggle]"), y2 + 21f, new Color(255, 255, 255).getRGB(), false);
            y2 += 12;
        }

        return new Border(0,0,100, 15 + getmoduley(),0f);
    }

    public int getmoduley(){
        int y=0;
        for (Module module: LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0) continue;
            if(onlyState.get()) {
                if (!module.getState()) continue;
            }
            y+=12;
        }

        return y;
    }

}
