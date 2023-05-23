package net.ccbluex.liquidbounce.features.module.modules.render;

import ad.gui.ui.GuiCloudMusic;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

/**
 * @author ChengFeng
 * @since 2022/12/5
 */

@ModuleInfo(name = "MusicPlayer", description = "Netease music api", category = ModuleCategory.RENDER, canEnable = false, Chinese = "音乐播放器")
public class MusicPlayer extends Module {
    @Override
    public void onEnable() {
        minecraft.displayGuiScreen(new GuiCloudMusic());
    }
}
