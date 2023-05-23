/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;


public final class ESPUtils {

    public static void renderOne() {
        checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }

    public static void cylinder(final Entity player, final double x, final double y, final double z, final double range,int s) {
        GL11.glPushMatrix();
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GlStateManager.translate(x, y, z);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.75f);
        GlStateManager.rotate(180.0f, 90.0f, 0.0f, 2.0f);
        GlStateManager.rotate(180.0f, 0.0f, 90.0f, 90.0f);
        Cylinder c = new Cylinder();
        c.setDrawStyle(100011);
        c.draw((float) (range - 0.5),
                (float) (range - 0.5), 0.0f, s, 0);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void shadow(final Entity player, final double x, final double y, final double z, final double range,int s) {
        GL11.glPushMatrix();
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GlStateManager.translate(x, y, z);
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.75f);
        GlStateManager.rotate(180.0f, 90.0f, 0.0f, 2.0f);
        GlStateManager.rotate(180.0f, 0.0f, 90.0f, 90.0f);
        Cylinder c = new Cylinder();
        c.setDrawStyle(100011);
        c.draw((float) (range - 0.45),
                (float) (range - 0.5), 0.0f, s, 0);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawBoundingBox(IAxisAlignedBB aa) {
        ITessellator tessellator = classProvider.getTessellatorInstance();
        IWorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMinZ()).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMinX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMinZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMaxY(), aa.getMaxZ()).endVertex();
        worldRenderer.pos(aa.getMaxX(), aa.getMinY(), aa.getMaxZ()).endVertex();
        tessellator.draw();
    }

    public static void setColor(IEntity entity) {
        if (entity != null) {
            int color = -1;//team
            //glColor(color);
        }
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit ,240f ,240f);
    }

    public static void drawCylinderESP(EntityLivingBase entity, int color, double x, double y, double z, int s) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-entity.width, 0.0f, 1.0f, 0.0f);
        glColor(color);//color4f
        enableSmoothLine(1.0f);//line
        Cylinder c = new Cylinder();
        GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        c.setDrawStyle(100011);
        c.draw(0.5f, 0.5f, entity.height-0.2F, s, 1);
        disableSmoothLine();
        GL11.glPopMatrix();
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 255) / 255.0f;
        float red = (float)(hex >> 16 & 255) / 255.0f;
        float green = (float)(hex >> 8 & 255) / 255.0f;
        float blue = (float)(hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha == 0.0f ? 1.0f : alpha);
    }

    public static void enableSmoothLine(float width) {//鐬庡嚑鎶婅捣鐨勫悕瀛�
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static void disableSmoothLine() {//鐬庨浮宸磋捣鐨勫悕瀛�
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

}