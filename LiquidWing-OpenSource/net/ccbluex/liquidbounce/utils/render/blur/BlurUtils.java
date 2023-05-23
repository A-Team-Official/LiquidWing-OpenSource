package net.ccbluex.liquidbounce.utils.render.blur;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.features.module.modules.render.BlurSettings;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.BloomUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;

import java.awt.*;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;
import static net.ccbluex.liquidbounce.utils.render.blur.GaussianBlur.framebuffer;
import static net.ccbluex.liquidbounce.utils.render.blur.StencilUtil.mc;
import static org.omg.CORBA.ORB.init;

public class BlurUtils {
    private static float lastX;
    private static float lastY;
    private static float lastW;
    private static float lastH;
    private static float lastStrength = 5F;
    private static ShaderGroup shaderGroup;
    private static Framebuffer frbuffer;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    private static int lastWeight;

    public static void blurArea(float x, float y, float width, float height) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(hud.getBlurStrength().get().floatValue());
        StencilUtil.uninitStencilBuffer();
    }
    public static void preCustomBlur(float blurStrength, float x, float y, float x2, float y2, boolean renderClipLayer) {
        if (!OpenGlHelper.isFramebufferEnabled()) return;

        if (x > x2) {
            float z = x;
            x = x2;
            x2 = z;
        }

        if (y > y2) {
            float z = y;
            y = y2;
            y2 = y;
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();


        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            init();
        }

        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        float _w = x2 - x;
        float _h = y2 - y;

        setValues(blurStrength, x, y, _w, _h, width, height);

        framebuffer.bindFramebuffer(true);
        shaderGroup.render(mc.timer.renderPartialTicks);

        mc.getFramebuffer().bindFramebuffer(true);

        Stencil.write(renderClipLayer);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void postCustomBlur() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        Stencil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask(true, true, true, false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = (float)width;
        float f1 = (float)height;
        float f2 = (float)frbuffer.framebufferWidth / (float)frbuffer.framebufferTextureWidth;
        float f3 = (float)frbuffer.framebufferHeight / (float)frbuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(0.0D, (double)f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)f, (double)f1, 0.0D).tex((double)f2, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos((double)f, 0.0D, 0.0D).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        Stencil.dispose();
        GlStateManager.enableAlpha();
    }
    public static void drawBlur(float radius, Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }
    public static void Shadow(Runnable content) {
        BlurSettings blur = (BlurSettings) LiquidBounce.moduleManager.getModule(BlurSettings.class);
        Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);
        bloomFramebuffer = RenderUtils.createFrameBuffer(bloomFramebuffer);

        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);

        content.run();

        bloomFramebuffer.unbindFramebuffer();

        BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture,blur.shadowRadius.get(), blur.shadowOffset.get());
    }
    public static void Shadow2(float x, float y, float width, float height, float radius) {
        BlurSettings blur = (BlurSettings) LiquidBounce.moduleManager.getModule(BlurSettings.class);
        Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);
        bloomFramebuffer = RenderUtils.createFrameBuffer(bloomFramebuffer);

        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);

        RoundedUtil.drawRound(x, y,width,height,radius,new Color(-2));

        bloomFramebuffer.unbindFramebuffer();

        BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture,blur.shadowRadius.get(), blur.shadowOffset.get());
    }
    private static void setValues(float strength, float x, float y, float w, float h, float width, float height) {
        if (strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) return;
        lastStrength = strength;
        lastX = x;
        lastY = y;
        lastW = w;
        lastH = h;

        for (int i = 0; i < 2; i++) {
            shaderGroup.listShaders.get(i).getShaderManager().getShaderUniform("Radius").set(strength);
            shaderGroup.listShaders.get(i).getShaderManager().getShaderUniform("BlurXY").set(x, height - y - h);
            shaderGroup.listShaders.get(i).getShaderManager().getShaderUniform("BlurCoord").set(w, h);
        }
    }

    private static void setValues(float strength) {
        if (strength == lastStrength) return;
        lastStrength = strength;
        for (int i = 0; i < 2; i++) {
            shaderGroup.listShaders.get(i).getShaderManager().getShaderUniform("Radius").set(strength);
        }
    }
    public static void CustomBlurArea(float x, float y, float width, float height, float BlurStrength) {
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(BlurStrength);
        StencilUtil.uninitStencilBuffer();
    }

    public static void CustomBlurRoundArea(float x, float y, float width, float height, float radius, float BlurStrength) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect2(x, y, x + width, y + height, radius, 6, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(BlurStrength);
        StencilUtil.uninitStencilBuffer();
    }
    public static void CustomBlurRoundedArea2(float x, float y, float width, float height, float radius, float BlurStrength) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, x + width, y + height, radius, new Color(-2));
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(BlurStrength);
        StencilUtil.uninitStencilBuffer();
    }
    public static void Guiblur(int x, int y, int width, int height, float BlurStrength) {
        StencilUtil.initStencilToWrite();
        Gui.drawRect(x, y, width, height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(BlurStrength);
        StencilUtil.uninitStencilBuffer();
    }
    public static void shapeBlur(Runnable content) {
        BlurSettings blur = (BlurSettings) LiquidBounce.moduleManager.getModule(BlurSettings.class);
        StencilUtil.initStencilToWrite();
        content.run();
        StencilUtil.readStencilBuffer(1);
        switch (blur.blurMode.get()) {
            case "Gaussian":
                GaussianBlur.renderBlur(blur.blurRadius.getValue().floatValue());
                break;
            case "Kawase":
                KawaseBlur.renderBlur(blur.iterations.getValue(),blur.offset.getValue());
                break;
        }
        StencilUtil.uninitStencilBuffer();
    }

    private static boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }
    public static void blurRoundArea(float x, float y, float width, float height, float radius) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y,width,height,radius,new Color(-2));
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur((hud.getBlurStrength().get().floatValue()));
        StencilUtil.uninitStencilBuffer();
    }
}
