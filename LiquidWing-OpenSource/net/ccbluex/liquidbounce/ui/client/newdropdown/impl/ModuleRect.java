package net.ccbluex.liquidbounce.ui.client.newdropdown.impl;


import ad.utils.ClientMain;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.clickgui.ClickGUI;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.EaseInOutQuad;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Main;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModuleRect extends Component {
    public final ClientMain client = ClientMain.getInstance();
    public final Module module;
    private final SettingComponents settingComponents;
    private final Animation animation = new EaseInOutQuad(300, 1, Direction.BACKWARDS);
    private final Animation arrowAnimation = new EaseInOutQuad(250, 1, Direction.BACKWARDS);
    private final Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);
    public Animation settingAnimation;
    public Animation openingAnimation;
    public float x, y, width, height, panelLimitY;
    public int alphaAnimation;
    int clickX, clickY;
    private double settingSize;

    public ModuleRect(Module module) {
        this.module = module;
        settingComponents = new SettingComponents(module);
    }

    @Override
    public void initGui() {
        animation.setDirection(module.getState() ? Direction.FORWARDS : Direction.BACKWARDS);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (module.getExpanded()) {
            settingComponents.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        HUD hud = ( HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        Color rectColor = new Color(43, 45, 50, alphaAnimation);
        Color textColor = new Color(255, 255, 255, alphaAnimation);
        Color debcolor = new Color(ClickGUI.generateRGB());

        Color clickModColor = DrRenderUtils.applyOpacity(debcolor, alphaAnimation / 255f);
        //    Color clickModColor2 = DrRenderUtils.applyOpacity(ClickDrRenderUtilsMod.color2.getColor(), alphaAnimation / 255f);
        // HudMod hudMod = (HudMod) Tenacity.INSTANCE.getModuleCollection().get(HudMod.class);


        float alpha = alphaAnimation / 255f;

        boolean hoveringModule = DrRenderUtils.isHovering(x, y, width, height, mouseX, mouseY);
        hoverAnimation.setDirection(hoveringModule ? Direction.FORWARDS : Direction.BACKWARDS);

        // Normal Grey rect
        DrRenderUtils.drawRect2(x, y, width, height, DrRenderUtils.interpolateColor(rectColor.getRGB(), DrRenderUtils.brighter(rectColor, .8f).getRGB(), (float) hoverAnimation.getOutput()));

        DrRenderUtils.drawRect2(x, y, width, height, DrRenderUtils.applyOpacity(clickModColor, (float) animation.getOutput()).getRGB());

        Fonts.tenacity.tenacity20.tenacity20.drawString(module.getName(), x + 5, y + Fonts.tenacity.tenacity20.tenacity20.getMiddleOfBox(height), textColor.getRGB());

        if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && module.getKeyBind() != 0) {
            String keyName = Keyboard.getKeyName(module.getKeyBind());
            Fonts.tenacity.tenacity20.tenacity20.drawString(keyName, x + width - Fonts.tenacity.tenacity20.tenacity20.stringWidth(keyName) - 5, y + Fonts.tenacity.tenacity20.tenacity20.getMiddleOfBox(height), textColor.getRGB());
        } else {
            float arrowSize = 6;
            arrowAnimation.setDirection(module.getExpanded() ? Direction.FORWARDS : Direction.BACKWARDS);
            DrRenderUtils.setAlphaLimit(0);
            DrRenderUtils.resetColor();
            DrRenderUtils.drawClickGuiArrow(x + width - (arrowSize + 5), y + height / 2f - 2, arrowSize, arrowAnimation, textColor.getRGB());
        }

        Color settingRectColor = new Color(32, 32, 32, alphaAnimation);


        double settingHeight = (settingComponents.settingSize) * settingAnimation.getOutput();
        if (module.getExpanded() || !settingAnimation.isDone()) {
            //绘制下拉列表背景颜色
            DrRenderUtils.drawRect2(x, y + height, width, settingHeight * height, settingRectColor.getRGB());

            boolean hoveringSettingsOrModule = DrRenderUtils.isHovering(x, y, width, (float) (height + (settingHeight * height)), mouseX, mouseY);


            if (ClickGUI.backback.get()) {

                DrRenderUtils.resetColor();

                float accentAlpha = (float) (.85 * animation.getOutput()) * alpha;

                DrRenderUtils.drawRect2(x, y + height, width, (float) (settingHeight * height), DrRenderUtils.applyOpacity(clickModColor, accentAlpha).getRGB());
            }


            settingComponents.x = x;
            settingComponents.y = y + height;
            settingComponents.width = width;
            settingComponents.rectHeight = height;
            settingComponents.panelLimitY = panelLimitY;
            settingComponents.alphaAnimation = alphaAnimation;
            settingComponents.settingHeightScissor = settingAnimation;
            if (!settingAnimation.isDone()) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                DrRenderUtils.scissor(x, y + height, width, settingHeight * height);

                settingComponents.drawScreen(mouseX, mouseY);
                DrRenderUtils.drawGradientRect2(x, y + height, width, 6, new Color(0, 0, 0, 60).getRGB(), new Color(0, 0, 0, 0).getRGB());
                DrRenderUtils.drawGradientRect2(x, y + 11 + (settingHeight * height), width, 6, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 60).getRGB());
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            } else {
                settingComponents.drawScreen(mouseX, mouseY);
                DrRenderUtils.drawGradientRect2(x, y + height, width, 6, new Color(0, 0, 0, 60).getRGB(), new Color(0, 0, 0, 0).getRGB());
                DrRenderUtils.drawGradientRect2(x, y + 11 + (settingHeight * height), width, 6, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 60).getRGB());
            }

        }
        settingSize = settingHeight;


    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hoveringModule = isClickable(y, panelLimitY) && DrRenderUtils.isHovering(x, y, width, height, mouseX, mouseY);
        if (hoveringModule) {
            switch (button) {
                case 0:
                    clickX = mouseX;
                    clickY = mouseY;
                    animation.setDirection(!module.getState() ? Direction.FORWARDS : Direction.BACKWARDS);
                    module.toggle();
                    break;
                case 1:
                    module.setExpanded(!module.getExpanded());
                    break;
            }
        }
        if (module.getExpanded()) {
            settingComponents.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (module.getExpanded()) {
            settingComponents.mouseReleased(mouseX, mouseY, state);
        }
    }

    public double getSettingSize() {
        return settingSize;
    }

    public boolean isClickable(float y, float panelLimitY) {
        return y > panelLimitY && y < panelLimitY + Main.allowedClickGuiHeight + 17;
    }
}