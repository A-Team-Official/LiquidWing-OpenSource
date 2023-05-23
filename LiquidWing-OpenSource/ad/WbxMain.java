package ad;



import net.ccbluex.liquidbounce.ui.client.fonts.api.FontManager;
import net.ccbluex.liquidbounce.ui.client.fonts.impl.SimpleFontManager;

public class WbxMain {
    public static String Name = "Noteless";
    public static String Rank = "";
    public static String version = "";
    public static String username;
    public FontManager fontManager = SimpleFontManager.create();

    public FontManager getFontManager() {
        return fontManager;
    }
}