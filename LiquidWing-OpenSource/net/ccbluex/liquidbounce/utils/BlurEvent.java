package net.ccbluex.liquidbounce.utils;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.Hotbar;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BlurEvent implements Listenable {
    @Override
    public boolean handleEvents() {
        return true;
    }
    static GuiNewChat guiNewChat = new GuiNewChat(Minecraft.getMinecraft());
    static float width = guiNewChat.getChatWidth();
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

            float lvt_7_1_ = guiNewChat.getChatScale();
            int lvt_8_1_ = MathHelper.ceil((float) width / lvt_7_1_);
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            GL11.glPushMatrix();
        if (hud.getBlurchat().get()) {
            if (hud.getShadowchat().get()) {
                ShadowUtils.shadow(hud.getChatShadowvalue().get(), () -> {
                    GL11.glPushMatrix();
                    RoundedUtil.drawRound(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 25F, lvt_8_1_ + 7.2F, (scaledResolution.getScaledHeight() - 27F) - (scaledResolution.getScaledHeight() + Hotbar.render2 - 22F), 0f, new Color(0, 0, 0));
                    GL11.glPopMatrix();

                    return null;
                }, () -> {
                    GL11.glPushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    RoundedUtil.drawRound(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 25F, lvt_8_1_ + 7.2F, (scaledResolution.getScaledHeight() - 27F) - (scaledResolution.getScaledHeight() + Hotbar.render2 - 22F), 0f, new Color(0, 0, 0));
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GL11.glPopMatrix();
                    return null;
                });
            }

                GL11.glPushMatrix();
                BlurUtils.blurAreaRounded(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 28F, lvt_8_1_ + 8F, scaledResolution.getScaledHeight() - 28F, 0f, hud.getChatBlurvalue().get());
                GL11.glPopMatrix();

        }
        GL11.glPopMatrix();
    }
}
