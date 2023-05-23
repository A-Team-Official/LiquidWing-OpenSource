package net.ccbluex.liquidbounce.features.module.modules.hyt;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;
@ModuleInfo(name  ="FakeName",description = "To Cheat HytGetName",Chinese = "欺骗自动假人",category = ModuleCategory.HYT)
public class FakeName extends Module {
    private static TextValue name = new TextValue("name", "");
    private static int a;
    @EventTarget
    public void onUpdate(UpdateEvent event){
        a += 1;
        if(a==302){
            mc.getThePlayer().sendChatMessage("@a起床战争>> " + name.get() + " (橙之队)死了!.");
        };
        if(a==604){
            mc.getThePlayer().sendChatMessage("@a起床战争>> " + name.get() + " (橙之队)死了!..");
        };
        if(a==806){
            mc.getThePlayer().sendChatMessage("@a起床战争>> " + name.get() + " (橙之队)死了!...");
        };
        if(a==1008){
            mc.getThePlayer().sendChatMessage("@a起床战争>> " + name.get() + " (橙之队)死了!....");
            a = 0;
        };
    }
    public void onEnable(){
        name.set(mc.getThePlayer().getName());
        a = 0;
        mc.getThePlayer().sendChatMessage("@a起床战争>> " + name.get() + " (橙之队)死了!");

    }
}
