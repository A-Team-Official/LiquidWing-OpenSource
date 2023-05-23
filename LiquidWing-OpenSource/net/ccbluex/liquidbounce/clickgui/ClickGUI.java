package net.ccbluex.liquidbounce.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.skeet.HyperGui;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.AstolfoStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.ui.client.newdropdown.DropdownClickGui;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.input.Keyboard;
import verify.WbxMain;

import java.awt.*;

@ModuleInfo(name = "ClickGUI",Chinese = "点击用户界面", description = "Opens the ClickGUI.", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class
ClickGUI extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"LiquidBounce", "Null", "Slowly", "Astolfo"}, "Astolfo") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };
    private final ListValue clickguimodeValue = new ListValue("Mode", new String[] {"LiquidBounce", "Tenacity","Neverlose"}, "Tenacity");
    public static final ListValue scrollMode = new ListValue("Scroll Mode", new String[]{"Screen Height", "Value"},"Value");
    public static final IntegerValue clickHeight = new IntegerValue("Tab Height", 250, 100, 500);
    public static final ListValue colormode = new ListValue("Setting Accent", new String[]{"White", "Color"},"Color");
    public static final BoolValue backback = new BoolValue("Background Accent",true);
    public static IntegerValue speed = new IntegerValue("NewUI-AstolfoSpeed", 35, 10, 100);

    public static BoolValue blur = new BoolValue("NewUI-Blur", false);
    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public static int generateRGB() {
        return colorRainbow.get() ? ColorUtils.rainbow().getRGB() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()).getRGB();
    }
    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }
    @Override
    public void onEnable() {
        if (LiquidBounce.INSTANCE.getConfigchose() != 980340) {
            if (LiquidBounce.INSTANCE.getConfigchose() == 114514) {
                if (ConfigSettingsCommand.INSTANCE.getConfig() != 0L) {
                    try {
                        if (WbxMain.cheakuser() == 0) {
                            if (ConfigSettingsCommand.INSTANCE.getConfig() != 11451455890L) {
                                ClientUtils.displayChatMessage("§c你使用了云加载模式，且参数不可被查看，且你是user你无权使用ClickGui");
                            } else {
                                if (clickguimodeValue.get().equalsIgnoreCase("Neverlose")) {
                                    updateStyle();
                                    mc2.displayGuiScreen(new HyperGui());
                                }

                                if (clickguimodeValue.get().equalsIgnoreCase("LiquidBounce")) {
                                    updateStyle();
                                    mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
                                }
                                if (clickguimodeValue.get().equalsIgnoreCase("Tenacity")) {
                                    mc.displayGuiScreen(classProvider.wrapGuiScreen(new DropdownClickGui()));
                                }
                            }
                        } else {
                            if (clickguimodeValue.get().equalsIgnoreCase("Neverlose")) {
                                updateStyle();
                                mc2.displayGuiScreen(new HyperGui());
                            }

                            if (clickguimodeValue.get().equalsIgnoreCase("LiquidBounce")) {
                                updateStyle();
                                mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
                            }
                            if (clickguimodeValue.get().equalsIgnoreCase("Tenacity")) {
                                mc.displayGuiScreen(classProvider.wrapGuiScreen(new DropdownClickGui()));
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    ClientUtils.displayChatMessage("§c你还未加载任何参数,请输入.ConfigSettings list查看参数");
                }


            } else if (LiquidBounce.INSTANCE.getConfigchose() == 234675) {
                if (clickguimodeValue.get().equalsIgnoreCase("Neverlose")) {
                    updateStyle();
                    mc2.displayGuiScreen(new HyperGui());
                }

                if (clickguimodeValue.get().equalsIgnoreCase("LiquidBounce")) {
                    updateStyle();
                    mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
                }
                if (clickguimodeValue.get().equalsIgnoreCase("Tenacity")) {
                    mc.displayGuiScreen(classProvider.wrapGuiScreen(new DropdownClickGui()));
                }

            }
        }else {
            if (clickguimodeValue.get().equalsIgnoreCase("Neverlose")) {
                updateStyle();
                mc2.displayGuiScreen(new HyperGui());
            }

            if (clickguimodeValue.get().equalsIgnoreCase("LiquidBounce")) {
                updateStyle();
                mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
            }
            if (clickguimodeValue.get().equalsIgnoreCase("Tenacity")) {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new DropdownClickGui()));
            }
        }
    }



    private void updateStyle() {

        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LiquidBounce.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            case "slowly":
                LiquidBounce.clickGui.style = new SlowlyStyle();
                break;
            case "astolfo":
                LiquidBounce.clickGui.style = new AstolfoStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final IPacket packet = event.getPacket();

        if (classProvider.isSPacketCloseWindow(packet) && classProvider.isClickGui(mc.getCurrentScreen())) {
            event.cancelEvent();
        }
    }
}
