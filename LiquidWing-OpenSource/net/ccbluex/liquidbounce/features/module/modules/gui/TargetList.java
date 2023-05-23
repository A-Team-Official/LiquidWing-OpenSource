package net.ccbluex.liquidbounce.features.module.modules.gui;


import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name = "TargetList", description = "Target list.", category = ModuleCategory.MISC, Chinese = "实体选择器")
public class TargetList extends Module {
    public static final BoolValue playerValue = new BoolValue("Players", true);
    public static final BoolValue mobsValue = new BoolValue("Mobs", false);
    public static final BoolValue animalsValue = new BoolValue("Animals", false);
    public static final BoolValue invisibleValue = new BoolValue("Invisible", false);
    public static final BoolValue  deadValue = new BoolValue("Dead", false);

}