package ad.blur;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class BlurShader{

    private final ShaderProgram blurShader = new ShaderProgram("fragment/blur2.frag");

    private static Framebuffer blurBuffer = new Framebuffer(1, 1, false);


    private final float radius;

    public BlurShader() {
        this.radius = 25;
    }

    public BlurShader(float radius) {
        this.radius = radius;
    }

    public void blur() {
        blur(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }

    public void blur(float x, float y, float width, float height) {

        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        blurBuffer = RenderUtils.createFramebuffer(blurBuffer, false);

        // horizontal blur
        blurShader.init();
        setupUniforms(1, 0, width, height);
        blurBuffer.framebufferClear();
        blurBuffer.bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, Minecraft.getMinecraft().getFramebuffer().framebufferTexture);
        blurShader.renderCanvas(scaledResolution);
        blurBuffer.unbindFramebuffer();

        // vertical blur
        blurShader.init();
        setupUniforms(0, 1, width, height);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, blurBuffer.framebufferTexture);
        blurShader.renderCanvas(scaledResolution);
        blurShader.uninit();
    }

    public void setupUniforms(int x, int y, float width, float height) {
        blurShader.setUniformi("originalTexture", 0);
        blurShader.setUniformf("texelSize", 1 / width, 1 / height);
        blurShader.setUniformf("direction", x, y);
        blurShader.setUniformf("radius", radius);
    }
}
