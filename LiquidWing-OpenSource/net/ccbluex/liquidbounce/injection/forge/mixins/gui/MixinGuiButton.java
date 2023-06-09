/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import ad.sb.novoline.button.AbstractButtonRenderer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiButton.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiButton extends Gui {

    @Shadow
    @Final
    protected static ResourceLocation BUTTON_TEXTURES;
    @Shadow
    public boolean visible;
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public boolean enabled;
    @Shadow
    public String displayString;
    @Shadow
    protected boolean hovered;
    private float cut;
    private float alpha;
    private float moveX = 0F;
    @Shadow
    protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);
    float roundCorner = (float) Math.max(0F, 2.4F + moveX - (this.width - 2.4F));
    /**
     * @author CCBlueX
     *
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        final AbstractButtonRenderer buttonRenderer = hud.getButtonRenderer((GuiButton)(Object)this);
        if (buttonRenderer != null) {
            if (!visible) {
                return;
            }
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.mouseDragged(mc, mouseX, mouseY);
            buttonRenderer.render(mouseX, mouseY, mc);
            GlStateManager.resetColor();
            buttonRenderer.drawButtonText(mc);
            }
        }
    }
