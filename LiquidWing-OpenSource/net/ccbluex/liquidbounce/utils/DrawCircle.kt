package net.ccbluex.liquidbounce.utils;

import ad.utils.Color.modules.CustomUI
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

object DrawCircle {
    fun drawCircle(x: Float, y: Float, radius: Float, start: Int, end: Int) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(2f)
        GL11.glBegin(GL11.GL_LINE_STRIP)
        var i = end.toFloat()
        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
        while (i >= start) {
            var c = RenderUtils.getGradientOffset(
                Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get()),
                Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get(), 1),
                (Math.abs(System.currentTimeMillis() / 360.0 + (i * 34 / 360) * 56 / 100) / 10)
            ).rgb
            val f2 = (c shr 24 and 255).toFloat() / 255.0f
            val f22 = (c shr 16 and 255).toFloat() / 255.0f
            val f3 = (c shr 8 and 255).toFloat() / 255.0f
            val f4 = (c and 255).toFloat() / 255.0f
            GlStateManager.color(f22, f3, f4, f2)
            GL11.glVertex2f(
                (x + Math.cos(i * Math.PI / 180) * (radius * 1.001f)).toFloat(),
                (y + Math.sin(i * Math.PI / 180) * (radius * 1.001f)).toFloat()
            )
            i -= 360f / 90.0f
        }
        GL11.glEnd()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }
}
