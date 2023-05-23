package ad.sb.novoline;

import net.ccbluex.liquidbounce.GuiLogin;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import org.lwjgl.opengl.Display;

public class FuckMixin {



    public static void setwrapper(){
        LiquidBounce.wrapper = WrapperImpl.INSTANCE;
    }
    public static void I1lI1l1iIlI1I1ilI11I(){
        if(GuiLogin.b == 10){
            LiquidBounce.INSTANCE.setInitclient(true);
        }
    }
    public static void SetTtile(){
        Display.setTitle(LiquidBounce.CLIENT_NAME + " " + LiquidBounce.CLIENT_VERSION + " Loading...");
    }

}
