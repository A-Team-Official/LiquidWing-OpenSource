/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.clickgui.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@SideOnly(Side.CLIENT)
public class AstolfoStyle extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    private Color getCategoryColor(String categoryName) {
        categoryName = categoryName.toLowerCase();
        if (categoryName.equals("combat")) {
            return new Color(231, 75, 58, 175);
        }
        if (categoryName.equals("player")) {
            return new Color(142, 69, 174, 175);
        }
        if (categoryName.equals("movement")) {
            return new Color(46, 205, 111, 175);
        }
        if (categoryName.equals("render")) {
            return new Color(76, 143, 200, 175);
        }
        if (categoryName.equals("world")) {
            return new Color(233, 215, 100, 175);
        }
        if (categoryName.equals("misc")) {
            return new Color(244, 157, 19, 175);
        }
        return ClickGUI.generateColor();
    }

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawRect((float) panel.getX() - 3, (float) panel.getY() - 1, (float) panel.getX() + panel.getWidth() + 3,
                panel.getY() + 22 + panel.getFade(), getCategoryColor(panel.getName()).getRGB());
        RenderUtils.drawRect(panel.getX() - 2, panel.getY(), panel.getX() + panel.getWidth() + 2, panel.getY() + 21 + panel.getFade(),
                new Color(17, 17, 17).getRGB());
        RenderUtils.drawRect((float) panel.getX() + 1, (float) panel.getY() + 19, (float) panel.getX() + panel.getWidth() - 1,
                panel.getY() + 18 + panel.getFade(), new Color(26, 26, 26).getRGB());

        GlStateManager.resetColor();
        Fonts.pop35.drawString("§l" + panel.getName().toLowerCase(), panel.getX() + 2, panel.getY() + 6, Integer.MAX_VALUE);
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.pop35.getStringWidth(text);

        RenderUtils.drawRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + Fonts.pop35.getFontHeight() + 3, new Color(26, 26, 26).getRGB());
        GlStateManager.resetColor();
        Fonts.pop35.drawString(text.toLowerCase(), mouseX + 12, mouseY + (Fonts.pop35.getFontHeight() / 2), Integer.MAX_VALUE);
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        Gui.drawRect(buttonElement.getX() - 1, buttonElement.getY() + 1, buttonElement.getX() + buttonElement.getWidth() + 1,
                buttonElement.getY() + buttonElement.getHeight() + 2, hoverColor(buttonElement.getColor() != Integer.MAX_VALUE
                        ? ClickGUI.generateColor() : new Color(26, 26, 26), buttonElement.hoverTime).getRGB());

        GlStateManager.resetColor();
        Fonts.pop35.drawString(buttonElement.getDisplayName().toLowerCase(), buttonElement.getX() + 3,
                buttonElement.getY() + 6, Color.RED.getRGB());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        Gui.drawRect(moduleElement.getX() + 1, moduleElement.getY() + 1, moduleElement.getX() + moduleElement.getWidth() - 1,
                moduleElement.getY() + moduleElement.getHeight() + 2, hoverColor(new Color(26, 26, 26), moduleElement.hoverTime).getRGB());
        Gui.drawRect(moduleElement.getX() + 1, moduleElement.getY() + 1, moduleElement.getX()
                + moduleElement.getWidth() - 1, moduleElement.getY() + moduleElement.getHeight() + 2, hoverColor(
                new Color(getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getRed(), getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getGreen(), getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getBlue(), moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());

        final int guiColor = ClickGUI.generateColor().getRGB();

        GlStateManager.resetColor();
        Fonts.pop35.drawString(moduleElement.getDisplayName().toLowerCase(), moduleElement.getX() + 3,
                moduleElement.getY() + 7, Integer.MAX_VALUE, true);

        final List<Value<?>> moduleValues = moduleElement.getModule().getValues();

        if (!moduleValues.isEmpty()) {
            Fonts.pop35.drawString("+", moduleElement.getX() + moduleElement.getWidth() - 8,
                    moduleElement.getY() + (moduleElement.getHeight() / 2), new Color(255, 255, 255, 200).getRGB());

            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;

                for (final Value value : moduleValues) {

                    if (value instanceof BoolValue) {
                        String text = value.getName();
                        float textWidth = Fonts.pop35.getStringWidth(text);

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() +
                                moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, new Color(26, 26, 26).getRGB());

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() +
                                moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                            if (Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                final BoolValue boolValue = (BoolValue) value;

                                boolValue.set(!boolValue.get());
                            }
                        }

                        GlStateManager.resetColor();
                        Fonts.pop35.drawString(text.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6,
                                yPos + 4, ((BoolValue) value).get() ? guiColor : Integer.MAX_VALUE);

                        yPos += 12;
                    } else if (value instanceof ListValue) {
                        ListValue listValue = (ListValue) value;
                        String text = value.getName();
                        float textWidth = Fonts.pop35.getStringWidth(text);

                        if (moduleElement.getSettingsWidth() < textWidth + 16)
                            moduleElement.setSettingsWidth(textWidth + 16);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() +
                                moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, new Color(26, 26, 26).getRGB());
                        GlStateManager.resetColor();
                        Fonts.pop35.drawString("§c" + text.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        Fonts.pop35.drawString(listValue.openList ? "-" : "+", (int) (moduleElement.getX() + moduleElement.getWidth() +
                                moduleElement.getSettingsWidth() - (listValue.openList ? 5 : 6)), yPos + 4, 0xffffff);

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() +
                                moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                            if (Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                listValue.openList = !listValue.openList;
                            }
                        }

                        yPos += 12;

                        for (final String valueOfList : listValue.getValues()) {
                            final float textWidth2 = Fonts.pop35.getStringWidth(">" + valueOfList);

                            if (moduleElement.getSettingsWidth() < textWidth2 + 12)
                                moduleElement.setSettingsWidth(textWidth2 + 12);

                            if (listValue.openList) {
                                RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() +
                                        moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, new Color(26, 26, 26).getRGB());

                                if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() +
                                        moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                                    if (Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                        listValue.set(valueOfList);
                                    }
                                }

                                GlStateManager.resetColor();
                                Fonts.pop35.drawString(">", moduleElement.getX() +
                                        moduleElement.getWidth() + 6, yPos + 4, Integer.MAX_VALUE);
                                Fonts.pop35.drawString(valueOfList.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 14,
                                        yPos + 4, listValue.get() != null && listValue.get().equalsIgnoreCase(valueOfList) ? guiColor : Integer.MAX_VALUE);
                                yPos += 12;
                            }
                        }
                    } else if (value instanceof FloatValue) {
                        FloatValue floatValue = (FloatValue) value;
                        String text = value.getName() + "§f: §c" + round(floatValue.get());
                        float textWidth = Fonts.pop35.getStringWidth(text);

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2,
                                moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 24, new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 8, yPos + 18,
                                moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4, yPos + 19, Integer.MAX_VALUE);
                        float sliderValue = moduleElement.getX() + moduleElement.getWidth() + ((moduleElement.getSettingsWidth() - 12) *
                                (floatValue.get() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum()));
                        RenderUtils.drawRect(8 + sliderValue, yPos + 15, sliderValue + 11, yPos + 21,
                                guiColor);

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4 && mouseY >= yPos + 15 && mouseY <= yPos + 21) {
                            if (Mouse.isButtonDown(0)) {
                                double i = MathHelper.clamp((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (
                                        moduleElement.getSettingsWidth() - 12), 0, 1);
                                floatValue.set(round((float) (floatValue.getMinimum() + (floatValue.getMaximum() - floatValue.getMinimum()) * i)).floatValue());
                            }
                        }

                        GlStateManager.resetColor();
                        Fonts.pop35.drawString(text.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        yPos += 22;
                    } else if (value instanceof IntegerValue) {
                        IntegerValue integerValue = (IntegerValue) value;
                        String text = value.getName() + "§f: §c" + (value instanceof BlockValue ? BlockUtils.getBlockName(integerValue.get()) + " (" + integerValue.get() + ")" : integerValue.get());
                        float textWidth = Fonts.pop35.getStringWidth(text);

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() +
                                moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 24, new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 8, yPos + 18, moduleElement.getX() +
                                moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4, yPos + 19, Integer.MAX_VALUE);
                        float sliderValue = moduleElement.getX() + moduleElement.getWidth() + ((moduleElement.getSettingsWidth() - 12) *
                                (integerValue.get() - integerValue.getMinimum()) / (integerValue.getMaximum() - integerValue.getMinimum()));
                        RenderUtils.drawRect(8 + sliderValue, yPos + 15, sliderValue + 11, yPos + 21, guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() +
                                moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21) {
                            if (Mouse.isButtonDown(0)) {
                                double i = MathHelper.clamp((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) /
                                        (moduleElement.getSettingsWidth() - 12), 0, 1);
                                integerValue.set((int) (integerValue.getMinimum() + (integerValue.getMaximum() - integerValue.getMinimum()) * i));
                            }
                        }

                        GlStateManager.resetColor();
                        Fonts.pop35.drawString(text.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        yPos += 22;
                    } else if (value instanceof FontValue) {
                        final FontValue fontValue = (FontValue) value;
                        final IFontRenderer fontRenderer = fontValue.get();

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() +
                                moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, new Color(26, 26, 26).getRGB());

                        String displayString;

                        displayString = "Font: " + Fonts.getFontDetails(fontValue.get()).getName();

                        assert Fonts.pop35 != null;
                        Fonts.pop35.drawString(displayString.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        int stringWidth = Fonts.pop35.getStringWidth(displayString);

                        if (moduleElement.getSettingsWidth() < stringWidth + 8)
                            moduleElement.setSettingsWidth(stringWidth + 8);

                        if ((Mouse.isButtonDown(0) && !mouseDown || Mouse.isButtonDown(1) && !rightMouseDown) && mouseX >= moduleElement.getX() + moduleElement.getWidth() +
                                4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 4 && mouseY <= yPos + 12) {
                            final List<IFontRenderer> fonts = Fonts.getFonts();

                            if (Mouse.isButtonDown(0)) {
                                for (int i = 0; i < fonts.size(); i++) {
                                    final IFontRenderer font = fonts.get(i);

                                    if (font == fontRenderer) {
                                        i++;

                                        if (i >= fonts.size())
                                            i = 0;

                                        fontValue.set(fonts.get(i));
                                        break;
                                    }
                                }
                            } else {
                                for (int i = fonts.size() - 1; i >= 0; i--) {
                                    final IFontRenderer font = fonts.get(i);

                                    if (font == fontRenderer) {
                                        i--;

                                        if (i >= fonts.size())
                                            i = 0;

                                        if (i < 0)
                                            i = fonts.size() - 1;

                                        fontValue.set(fonts.get(i));
                                        break;
                                    }
                                }
                            }
                        }
                        yPos += 11;
                    } else {
                        String text = value.getName() + "§f: §c" + value.get();
                        float textWidth = Fonts.pop35.getStringWidth(text);

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() +
                                moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, new Color(26, 26, 26).getRGB());
                        GlStateManager.resetColor();
                        Fonts.tenacitybold35.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        yPos += 12;
                    }
                }

                moduleElement.updatePressed();
                mouseDown = Mouse.isButtonDown(0);
                rightMouseDown = Mouse.isButtonDown(1);

                if (moduleElement.getSettingsWidth() > 0F && yPos > moduleElement.getY() + 4)
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, moduleElement.getX() +
                            moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 2, 1F, new Color(26, 26, 26).getRGB(), 0);
            }
        }
    }

    private BigDecimal round(final float f) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }

    private Color hoverColor(final Color color, final int hover) {
        final int r = color.getRed() - (hover * 2);
        final int g = color.getGreen() - (hover * 2);
        final int b = color.getBlue() - (hover * 2);

        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0), color.getAlpha());
    }
}
