package ad;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;

public class DrawUtils extends GuiScreen{
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void setColor(int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0F;
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawModalRectWithCustomSizedTexture2(float g, float h, float u, float v, double d, double e, double i, double j)
    {
        float f = (float) (1.0F / i);
        float f1 = (float) (1.0F / j);
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(g, h + e, 0.0D).tex(u * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h + e, 0.0D).tex((u + (float)d) * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h, 0.0D).tex((u + (float)d) * f, v * f1).endVertex();
        worldrenderer.pos(g, h, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawModalRectWithCustomSizedTexture3(double g, double h, double u, double v, double d, double e, double i, double j)
    {
        float f = (float) (1.0F / i);
        float f1 = (float) (1.0F / j);
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(g, h + e, 0.0D).tex(u * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h + e, 0.0D).tex((u + (float)d) * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h, 0.0D).tex((u + (float)d) * f, v * f1).endVertex();
        worldrenderer.pos(g, h, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static boolean isHovered(double x, double y, double x1, double y1, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= x1 && mouseY <= y1;
    }

    public static boolean isHovered(int x, int y, int x1, int y1, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= x1 && mouseY <= y1;
    }

    public static void drawImage(final int x, final int y, final int width, final int height, final ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        setColor(color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawImage(final int x, final int y, int width, int height, final ResourceLocation image) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawShadowImage(final int x, final int y, final int width, final int height, final ResourceLocation image) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawShadowImage1(final int x, final int y, final int width, final int height, final ResourceLocation image) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, x - width, y - height, (float) x - width, (float) y - height);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawImage(final double x, final double y, final double width, final double height, final ResourceLocation image) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawImageDiv2(float x, float y, float width, float height, final ResourceLocation image) {
        width /= 2.0f;
        height /= 2.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void drawModalRectWithCustomSizedTexture(float g, float h, float u, float v, double d, double e, double i, double j)
    {
        float f = (float) (1.0F / i);
        float f1 = (float) (1.0F / j);
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(g, h + e, 0.0D).tex(u * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h + e, 0.0D).tex((u + (float)d) * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h, 0.0D).tex((u + (float)d) * f, v * f1).endVertex();
        worldrenderer.pos(g, h, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawModalRectWithCustomSizedTexture(double g, double h, double u, double v, double d, double e, double i, double j)
    {
        float f = (float) (1.0F / i);
        float f1 = (float) (1.0F / j);
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(g, h + e, 0.0D).tex(u * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h + e, 0.0D).tex((u + (float)d) * f, (v + (float)e) * f1).endVertex();
        worldrenderer.pos(g + d, h, 0.0D).tex((u + (float)d) * f, v * f1).endVertex();
        worldrenderer.pos(g, h, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        IWorldRenderer worldrenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
