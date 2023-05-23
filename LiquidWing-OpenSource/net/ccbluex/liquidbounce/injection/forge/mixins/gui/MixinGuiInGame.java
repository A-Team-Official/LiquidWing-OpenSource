/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import ad.gui.ui.MusicOverlayRenderer;
import ad.utils.Color.modules.CustomUI;
import ad.utils.blur.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.Hotbar;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.BanCkeckHelper;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.utils.ShadowUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiIngame.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiInGame extends MixinGui {

    @Shadow
    @Final
    protected static ResourceLocation WIDGETS_TEX_PATH;
    @Shadow
    @Final
    protected Minecraft mc;
    @Shadow public GuiPlayerTabOverlay overlayPlayerList;
    @Shadow
    protected abstract void renderHotbarItem(int xPos, int yPos, float partialTicks, EntityPlayer player, ItemStack stack);

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    private void renderScoreboard(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(HUD.class).getState() || NoScoreboard.INSTANCE.getState())
            callbackInfo.cancel();
    }

    @Overwrite
    protected void renderHotbar(ScaledResolution sr, float partialTicks) {
        BanCkeckHelper.bancheckhelper = true;
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getMusicDisplay().get()) MusicOverlayRenderer.INSTANCE.renderOverlay();

        if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && hud.getState() && hud.getBlackHotbarValue().get()) {

            EntityPlayer entityPlayer = (EntityPlayer) this.mc.getRenderViewEntity();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            ItemStack itemstack = entityPlayer.getHeldItemOffhand();
            EnumHandSide enumhandside = entityPlayer.getPrimaryHand().opposite();
            int middleScreen = sr.getScaledWidth() / 2;
            float f = this.zLevel;
            int j = 182;
            int k = 91;
            this.zLevel = -90.0F;
            int a = 29;
            int b = 5;
            int c = 7;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (hud.getInventoryrender().get() || !hud.getState()) {
                ShadowUtils.shadow(6f, () -> {
                    GL11.glPushMatrix();
                    RenderUtils.drawRoundedRect(middleScreen - 101 + b, sr.getScaledHeight() - a, middleScreen + 120 - c, sr.getScaledHeight(), 5f, new Color(0,0,0).getRGB());
                    GL11.glPopMatrix();

                    return null;
                }, () -> {
                    GL11.glPushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    RenderUtils.drawRoundedRect(middleScreen - 101 + b, sr.getScaledHeight() - a, middleScreen + 120 - c, sr.getScaledHeight(), 5f, new Color(0,0,0).getRGB());
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GL11.glPopMatrix();
                    return null;
                });
                RenderUtils.drawRoundedRect(middleScreen - 101 + b, sr.getScaledHeight() - a, middleScreen + 120 - c, sr.getScaledHeight(), 5f, new Color(255,247,230,100).getRGB());
                BlurBuffer.CustomBlurRoundArea(middleScreen - 101 + b, sr.getScaledHeight() - a, (middleScreen + 120 - c) - ( middleScreen - 101 + b ), sr.getScaledHeight() - (sr.getScaledHeight() -a), 5f, CustomUI.blurValue.get());
                RoundedUtil.drawRoundOutline(middleScreen - 101 + b, sr.getScaledHeight() - a, (middleScreen + 120 - c) - (middleScreen - 101 + b),sr.getScaledHeight() - (sr.getScaledHeight() -a), 5f,0.1f,new Color(255,247,230,0), new Color(255,255,255,130));

                this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);


                int i = sr.getScaledWidth() / 2;
                int itemX = i - 91 + hud.getHotbarEasePos(entityPlayer.inventory.currentItem * 20);

                //GlStateManager.disableTexture2D();
                Hotbar.render(sr, itemX, partialTicks);
                //GlStateManager.enableTexture2D();


                this.zLevel = f;
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderHelper.enableGUIStandardItemLighting();

                for (int l = 0; l < 9; ++l) {
                    int i1 = middleScreen - 90 + l * 20 + 2;
                    int j1 = sr.getScaledHeight() - 16 - 3 - 2;
                    this.renderHotbarItem(i1 + 5, j1, partialTicks, entityPlayer, entityPlayer.inventory.mainInventory.get(l));
                }


                if (this.mc.gameSettings.attackIndicator == 2) {
                    float f1 = this.mc.player.getCooledAttackStrength(0.0F);

                    if (f1 < 1.0F) {
                        int i2 = sr.getScaledHeight() - 20;
                        int j2 = middleScreen + 91 + 6;

                        if (enumhandside == EnumHandSide.RIGHT) {
                            j2 = middleScreen - 91 - 22;
                        }

                        this.mc.getTextureManager().bindTexture(Gui.ICONS);
                        int k1 = (int) (f1 * 19.0F);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                        this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
                    }
                }
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }


    @Inject(method = "renderHotbar", at = @At("RETURN"))
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            LiquidBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
            AWTFontRenderer.Companion.garbageCollectionTick();
        }
    }
    @Inject(method = "renderExpBar",at = @At("HEAD"),cancellable = true)
    private void renderExpBar(ScaledResolution p_renderExpBar_1_, int p_renderExpBar_2_, CallbackInfo ci) {
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (!hud.getHotbar().get()){
            ci.cancel();
        }
    }
    @Inject(method = { "renderPotionEffects"}, at = @At("HEAD"), cancellable = true)
    protected void renderPotionEffects(ScaledResolution p_renderPotionEffects_1_, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderSelectedItem",at = @At("HEAD"),cancellable = true)
    private void renderSelectedItem(ScaledResolution p_renderPlayerStats_1_, CallbackInfo ci) {
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (!hud.getHotbar().get()){
            ci.cancel();
        }
    }


    @Inject(method = "renderPlayerStats",at = @At("HEAD"),cancellable = true)
    private void renderPlayerStats(ScaledResolution p_renderPlayerStats_1_, CallbackInfo ci) {
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (!hud.getHotbar().get()){
            ci.cancel();
        }
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPumpkinOverlay(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind) LiquidBounce.moduleManager.getModule(AntiBlind.class);

        if (antiBlind.getState() && antiBlind.getPumpkinEffect().get())
            callbackInfo.cancel();
    }
}