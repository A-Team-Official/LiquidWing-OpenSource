package ad;

import ad.utils.ScaleUtils;
import net.ccbluex.liquidbounce.ui.client.clickgui.skeet.HyperGui;
import net.ccbluex.liquidbounce.ui.client.fonts.api.FontManager;
import net.ccbluex.liquidbounce.ui.client.fonts.impl.SimpleFontManager;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Client {
    public static double fontScaleOffset = 1;//round((double)1600/1080, 1) * s.getScaleFactor();//2.75;
    public static FontManager fontManager = SimpleFontManager.create();

    public static FontManager getFontManager() {
        return fontManager;
    }
    public static String name = "LiquidWing";
    public static String version = "230304";
    public HyperGui skeet;
    public static int THEME_RGB_COLOR = new Color(36, 240, 0).getRGB();

    public static Client instance = new Client();
    public static ScaleUtils scaleUtils = new ScaleUtils(2);
    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

}
