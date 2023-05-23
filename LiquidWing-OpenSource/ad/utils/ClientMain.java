package ad.utils;

import net.ccbluex.liquidbounce.ui.client.fonts.api.FontManager;
import net.ccbluex.liquidbounce.ui.client.fonts.impl.SimpleFontManager;
import net.ccbluex.liquidbounce.ui.client.newdropdown.SideGui.SideGui;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class ClientMain {

    private static ClientMain INSTANCE;

    public static ClientMain getInstance() {
        try {
            if (INSTANCE == null) INSTANCE = new ClientMain();
            return INSTANCE;
        } catch (Throwable t) {
            ClientUtils.getLogger().warn(t);
            throw t;
        }
    }

    public FontManager fontManager = SimpleFontManager.create();
    public FontManager getFontManager() {
        return fontManager;
    }

    private final SideGui sideGui = new SideGui();

    public SideGui getSideGui() {
        return sideGui;
    }

}
