/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.BlurEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.BloomUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;

@ModuleInfo(name = "BlurSettings", description = "Shader effect.", category = ModuleCategory.RENDER, Chinese = "模糊设置")
public class BlurSettings extends Module {

    public final IntegerValue iterations = new IntegerValue("BlurIterations", 4, 1, 15);
    public final IntegerValue offset = new IntegerValue("BlurOffset", 3, 1, 20);
    public ListValue blurMode = new ListValue("BlurMode", new String[]{"Kawase", "Gaussian"}, "Gaussian");
    public FloatValue blurRadius = new FloatValue("BlurRadius", 6f, 1f, 30f);
    public BoolValue shadowValue = new BoolValue("ShadowOn", false);
    public IntegerValue shadowRadius = new IntegerValue("ShadowRadius", 2, 1, 20);
    public IntegerValue shadowOffset = new IntegerValue("ShadowOffset", 2, 1, 15);

    public Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);


    public void drawBlurShadow() {
        if (this.shadowValue.get() && OpenGlHelper.isFramebufferEnabled()) {
            bloomFramebuffer = RenderUtils.createFrameBuffer(bloomFramebuffer);

            bloomFramebuffer.framebufferClear();
            bloomFramebuffer.bindFramebuffer(true);
            LiquidBounce.eventManager.callEvent(new BlurEvent(true));

            bloomFramebuffer.unbindFramebuffer();

            BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture, shadowRadius.get(), shadowOffset.get());
        }
    }
}
