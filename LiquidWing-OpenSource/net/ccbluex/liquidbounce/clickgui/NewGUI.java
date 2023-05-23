/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.color.ColorMixer;
import net.ccbluex.liquidbounce.ui.client.newui.NewUi;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import verify.WbxMain;

import java.awt.*;

@ModuleInfo(name = "NewGUI", description = "next generation tomo.", category = ModuleCategory.GUI, Chinese = "模块界面(新)", canEnable = false)
public class NewGUI extends Module {
    public static final BoolValue fastRenderValue = new BoolValue("FastRender", false);

    private static final ListValue colorModeValue = new ListValue("Color", new String[] {"Custom", "Sky", "Rainbow", "LiquidSlowly", "Fade", "Mixer"}, "Custom");
    private static final IntegerValue colorRedValue = new IntegerValue("Red", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("Green", 140, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    private static final FloatValue saturationValue = new FloatValue("Saturation", 1F, 0F, 1F);
    private static final FloatValue brightnessValue = new FloatValue("Brightness", 1F, 0F, 1F);
    private static final IntegerValue mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    public double slide, progress = 0;
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
                                slide = progress = 0;
                                mc2.displayGuiScreen(NewUi.getInstance());
                            }
                        } else {
                            slide = progress = 0;
                            mc2.displayGuiScreen(NewUi.getInstance());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    ClientUtils.displayChatMessage("§c你还未加载任何参数,请输入.ConfigSettings list查看参数");

                }

            } else if (LiquidBounce.INSTANCE.getConfigchose() == 234675) {
                slide = progress = 0;
                mc2.displayGuiScreen(NewUi.getInstance());

            }
        }else {
            slide = progress = 0;
            mc2.displayGuiScreen(NewUi.getInstance());
        }

    }
    public static Color getAccentColor() {
        Color c = new Color(255, 255, 255, 255);
        switch (colorModeValue.get().toLowerCase()) {
            case "custom":
                c = new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
                break;
            case "rainbow":
                c = new Color(RenderUtils.getRainbowOpaque(mixerSecondsValue.get(), saturationValue.get(), brightnessValue.get(), 0));
                break;
            case "sky":
                c = RenderUtils.skyRainbow(0, saturationValue.get(), brightnessValue.get());
                break;
            case "liquidslowly":
                c = ColorUtils.LiquidSlowly(System.nanoTime(), 0, saturationValue.get(), brightnessValue.get());
                break;
            case "fade":
                c = ColorUtils.fade(new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), 0, 100);
                break;
            case "mixer":
                c = ColorMixer.getMixedColor(0, mixerSecondsValue.get());
                break;
        }
        return c;
    }
}
