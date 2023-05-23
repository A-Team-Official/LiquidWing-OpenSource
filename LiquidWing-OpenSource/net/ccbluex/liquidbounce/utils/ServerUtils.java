package net.ccbluex.liquidbounce.utils;

import ad.sb.novoline.ui.GuiMainMenu;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImplKt;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ServerUtils extends MinecraftInstance {

    public static IServerData serverData;

    public static void connectToLastServer() {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if(serverData == null)
            return;

            mc2.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc2, ServerDataImplKt.unwrap(serverData)));

    }

    public static String getRemoteIp() {
        String serverIp = "Singleplayer";

        if (mc.getTheWorld() != null) {
            if (mc.getTheWorld().isRemote()) {
                final IServerData serverData = mc.getCurrentServerData();

                if(serverData != null)
                    serverIp = serverData.getServerIP();
            }
        }

        return serverIp;
    }
}