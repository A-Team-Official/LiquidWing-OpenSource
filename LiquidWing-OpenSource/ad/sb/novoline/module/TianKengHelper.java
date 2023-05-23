package ad.sb.novoline.module;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name = "TianKengHelper",description = "ee",category = ModuleCategory.RENDER, Chinese = "天坑助手")
public class TianKengHelper extends Module {

    //super big god by paimonqwq
    @EventTarget
    public void OnUpdate(UpdateEvent event){
        if (mc.getThePlayer().getHealth() <= 3F && mc.getThePlayer().getHurtTime() > 0){
            mc.getThePlayer().sendChatMessage("草你妈");
            mc.getThePlayer().sendChatMessage("/hub");
        }
    }
}
