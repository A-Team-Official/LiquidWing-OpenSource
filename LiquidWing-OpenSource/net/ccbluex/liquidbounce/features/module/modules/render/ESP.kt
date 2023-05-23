package net.ccbluex.liquidbounce.features.module.modules.render

import ad.utils.Color.modules.CustomUI
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer.Companion.getColorIndex
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.WorldToScreen
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

@ModuleInfo(name = "ESP", description = "Allows you to see targets through walls.", category = ModuleCategory.RENDER, Chinese = "玩家透视")
class ESP : Module() {
    @JvmField
    val modeValue = ListValue("Mode", arrayOf("Cylinder","Box", "OtherBox", "WireFrame", "2D", "Real2D","2dbox", "Outline", "ShaderOutline", "ShaderGlow","datou","Jello"), "Box")


    private val healthValue = BoolValue("Real2D-Health", false)
    private val widthValue = FloatValue("Real2D-Width", 0f, 0f, 0.5f)
    private val outlineValue = BoolValue("Real2D-Outline", false)
    private val nameValue = BoolValue("Real2D-Name", false)

    private val shaderOutlineRadius = FloatValue("ShaderOutline-Radius", 1.35f, 1f, 2f)
    val datouValue = ListValue("datou", arrayOf("yaoer","caomou"), "yaoer")
    private val shaderGlowRadius = FloatValue("ShaderGlow-Radius", 2.3f, 2f, 3f)
    private val colorRedValue = IntegerValue("R", 255, 0, 255)
    private val colorGreenValue = IntegerValue("G", 255, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)

    private val healthcolor = BoolValue("Health-Color",true)
    private val hRedValue = IntegerValue("Health-R", 255, 0, 255)
    private val hGreenValue = IntegerValue("Health-G", 255, 0, 255)
    private val hBlueValue = IntegerValue("Health-B", 255, 0, 255)
    private val healthrainbow = BoolValue("Health-Rainbow",true)

    private val colorRainbow = BoolValue("Rainbow", false)
    private val gident = BoolValue("Gradient", true)
    private val colorTeam = BoolValue("Team", false)
    private val botValue = BoolValue("Bots", true)

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        val mode = modeValue.get()
        val mvMatrix = WorldToScreen.getMatrix(GL11.GL_MODELVIEW_MATRIX)
        val projectionMatrix = WorldToScreen.getMatrix(GL11.GL_PROJECTION_MATRIX)
        val real2d = mode.equals("real2d", ignoreCase = true)

        if (real2d) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glMatrixMode(GL11.GL_PROJECTION)
            GL11.glPushMatrix()
            GL11.glLoadIdentity()
            GL11.glOrtho(0.0, mc.displayWidth.toDouble(), mc.displayHeight.toDouble(), 0.0, -1.0, 1.0)
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glPushMatrix()
            GL11.glLoadIdentity()
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            classProvider.getGlStateManager().enableTexture2D()
            GL11.glDepthMask(true)
            GL11.glLineWidth(1.0f)
        }

        for (entity in mc.theWorld!!.loadedEntityList) {
            if (!classProvider.isEntityLivingBase(entity) || !botValue.get() && AntiBot.isBot(entity.asEntityLivingBase())) continue
            if (EntityUtils.isSelected(entity, false)
                || (entity == mc.thePlayer && mc.gameSettings.thirdPersonView != 0 )) {
                val entityLiving = entity.asEntityLivingBase()
                val color = getColor(entityLiving)
                when (mode.toLowerCase()) {

                    "box", "otherbox" -> RenderUtils.drawEntityBox(entity, color, !mode.equals("otherbox", ignoreCase = true))
                    "2dbox" -> RenderUtils.draw2DBox(entity, color, event)
                    "cylinder" -> {
                        val renderManager = mc.renderManager
                        val timer = mc.timer
                        val posX: Double = entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX
                        val posY: Double = entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY
                        val posZ: Double = entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ
                        RenderUtils.drawWolframEntityESP(entity, color.rgb, posX, posY, posZ)
                    }
                    "2d" -> {
                        val renderManager = mc.renderManager
                        val timer = mc.timer
                        val posX: Double = entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX
                        val posY: Double = entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY
                        val posZ: Double = entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ
                        RenderUtils.draw2D(entityLiving, posX, posY, posZ, color.rgb, Color.BLACK.rgb)
                    }
                    "real2d" -> {
                        val renderManager = mc.renderManager
                        val timer = mc.timer
                        val bb = entityLiving.entityBoundingBox
                            .offset(-entityLiving.posX, -entityLiving.posY, -entityLiving.posZ)
                            .offset(entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks,
                                entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks,
                                entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks)
                            .offset(-renderManager.renderPosX, -renderManager.renderPosY, -renderManager.renderPosZ)
                        val boxVertices = arrayOf(doubleArrayOf(bb.minX - this.widthValue.get(), bb.minY, bb.minZ - this.widthValue.get()), doubleArrayOf(bb.minX - this.widthValue.get(), bb.maxY + 0.1, bb.minZ - this.widthValue.get()), doubleArrayOf(bb.maxX + this.widthValue.get(), bb.maxY + 0.1, bb.minZ - this.widthValue.get()), doubleArrayOf(bb.maxX + this.widthValue.get(), bb.minY, bb.minZ - this.widthValue.get()), doubleArrayOf(bb.minX - this.widthValue.get(), bb.minY, bb.maxZ + this.widthValue.get()), doubleArrayOf(bb.minX - this.widthValue.get(), bb.maxY + 0.1, bb.maxZ + this.widthValue.get()), doubleArrayOf(bb.maxX + this.widthValue.get(), bb.maxY + 0.1, bb.maxZ + this.widthValue.get()), doubleArrayOf(bb.maxX + this.widthValue.get(), bb.minY, bb.maxZ + this.widthValue.get()))
                        var minX = Float.MAX_VALUE
                        var minY = Float.MAX_VALUE
                        var maxX = -1f
                        var maxY = -1f
                        for (boxVertex in boxVertices) {
                            val screenPos = WorldToScreen.worldToScreen(Vector3f(boxVertex[0].toFloat(), boxVertex[1].toFloat(), boxVertex[2].toFloat()), mvMatrix, projectionMatrix, mc.displayWidth, mc.displayHeight)
                                ?: continue
                            minX = min(screenPos.x, minX)
                            minY = min(screenPos.y, minY)
                            maxX = max(screenPos.x, maxX)
                            maxY = max(screenPos.y, maxY)
                        }
                        if (minX > 0 || minY > 0 || maxX <= mc.displayWidth || maxY <= mc.displayWidth) {

                            if (this.healthValue.get()) {
                                GL11.glColor4f(0F, 0F, 0F, 1.0f)
                                GL11.glBegin(GL11.GL_QUADS)
                                GL11.glVertex2f(minX - 8F, minY - 1F)
                                GL11.glVertex2f(minX - 8F, maxY + 1F)
                                GL11.glVertex2f(minX - 3F, maxY + 1F)
                                GL11.glVertex2f(minX - 3F, minY - 1F)
                                GL11.glEnd()

                                GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0f)
                                GL11.glBegin(GL11.GL_QUADS)
                                GL11.glVertex2f(minX - 7F, minY)
                                GL11.glVertex2f(minX - 7F, maxY)
                                GL11.glVertex2f(minX - 4F, maxY)
                                GL11.glVertex2f(minX - 4F, minY)
                                GL11.glEnd()

                                RenderUtils.glColorHex(if(!healthcolor.get()){ColorUtils.getHealthColor(entityLiving.health, entityLiving.maxHealth)} else if(healthrainbow.get()){
                                    rainbow().rgb}else {
                                    Color(hRedValue.get(),hGreenValue.get(),hBlueValue.get()).rgb
                                }
                                )
                                //GL11.glColor4f(0F, 1F, 0F, 1.0f)
                                GL11.glBegin(GL11.GL_QUADS)
                                GL11.glVertex2f(minX - 7F, maxY - (entityLiving.health / entityLiving.maxHealth) * (maxY - minY))
                                GL11.glVertex2f(minX - 7F, maxY)
                                GL11.glVertex2f(minX - 4F, maxY)
                                GL11.glVertex2f(minX - 4F, maxY - (entityLiving.health / entityLiving.maxHealth) * (maxY - minY))
                                GL11.glEnd()
                            }

                            if (this.outlineValue.get()) {
                                GL11.glColor4f(0F, 0F, 0F, 1.0f)
                                GL11.glLineWidth(1F);
                                GL11.glBegin(GL11.GL_LINE_LOOP)
                                GL11.glVertex2f(minX - 1F, minY - 1F)
                                GL11.glVertex2f(minX - 1F, maxY + 1F)
                                GL11.glVertex2f(maxX + 1F, maxY + 1F)
                                GL11.glVertex2f(maxX + 1F, minY - 1F)
                                GL11.glEnd()

                                GL11.glColor4f(0F, 0F, 0F, 1.0f)
                                GL11.glLineWidth(1F);
                                GL11.glBegin(GL11.GL_LINE_LOOP)
                                GL11.glVertex2f(minX + 1F, minY + 1F)
                                GL11.glVertex2f(minX + 1F, maxY - 1F)
                                GL11.glVertex2f(maxX - 1F, maxY - 1F)
                                GL11.glVertex2f(maxX - 1F, minY + 1F)
                                GL11.glEnd()
                            }

                            GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, 1.0f)
                            GL11.glLineWidth(1F);
                            GL11.glBegin(GL11.GL_LINE_LOOP)
                            GL11.glVertex2f(minX, minY)
                            GL11.glVertex2f(minX, maxY)
                            GL11.glVertex2f(maxX, maxY)
                            GL11.glVertex2f(maxX, minY)
                            GL11.glEnd()

                            if (this.nameValue.get()) {
                                //NAME
                                if (!LiquidBounce.moduleManager.getModule(NameTags::class.java).state) {
                                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                                    GL11.glEnable(GL11.GL_DEPTH_TEST)
                                    mc.fontRendererObj.drawCenteredString(entityLiving.displayName!!.formattedText, minX + (maxX - minX) / 2.0f, minY - 2.0f - mc.fontRendererObj.fontHeight, -1, true)
                                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                                }

                                GL11.glEnable(GL11.GL_TEXTURE_2D)
                                GL11.glEnable(GL11.GL_DEPTH_TEST)
                                mc.fontRendererObj.drawCenteredString(entityLiving.unwrap().heldItemMainhand.displayName, minX + (maxX - minX) / 2.0f, maxY + 2.0f, -1, true)
                                GL11.glDisable(GL11.GL_TEXTURE_2D)
                                GL11.glDisable(GL11.GL_DEPTH_TEST)
                            }
                        }
                    }

                    "datou" -> {
                        val pX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks -
                                mc.renderManager.renderPosX;
                        val  pY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks -
                                mc.renderManager.renderPosY
                        val pZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks -
                                mc.renderManager.renderPosZ

                        GL11.glPushMatrix();
                        GL11.glTranslatef(pX.toFloat(), (pY + if(entity.sneaking)0.8F else 1.3F).toFloat(),
                            pZ.toFloat()
                        )
                        GL11.glNormal3f(1.0F, 1.0F, 1.0F);
                        mc2.getRenderManager(); GL11.glRotatef(-mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                        mc2.getRenderManager(); GL11.glRotatef(mc.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                        val scale = 0.06F
                        GL11.glScalef(-scale, -scale, scale);

                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);

                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        if (datouValue.get() == ("yaoer")) {
                            RenderUtils.image(ResourceLocation("liquidwing/yaoer.png"), -8, -14, 16, 16);
                        }
                        if (datouValue.get() == ("caomou")) {
                            RenderUtils.image(ResourceLocation("liquidwing/caomou.png"), -8, -15, 16, 16);
                        }
                        GL11.glPopMatrix();
                        GL11.glPopMatrix();
                    }
                }
            }
        }
        if (real2d) {
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glMatrixMode(GL11.GL_PROJECTION)
            GL11.glPopMatrix()
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glPopMatrix()
            GL11.glPopAttrib()
        }
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val partialTicks = event.partialTicks
        val mode = modeValue.get().toLowerCase()
        if (mode.equals("jello", ignoreCase = true)) {
            val hurtingEntities = ArrayList<IEntityLivingBase>()
            var shader: FramebufferShader = GlowShader.GLOW_SHADER
            var radius = 3f
            var color = Color(120, 120, 120)
            var hurtColor = Color(120, 0, 0)
            var firstRun = true

            for (i in 0..1) {
                shader.startDraw(partialTicks)
                for (entity in mc.theWorld!!.loadedEntityList) {
                    if (EntityUtils.isSelected(entity, false)) {
                        val entityLivingBase = entity.asEntityLivingBase()
                        if (firstRun && entityLivingBase.hurtTime > 0) {
                            hurtingEntities.add(entityLivingBase)
                            continue
                        }
                        mc.renderManager.renderEntityStatic(entity, partialTicks, true)
                    }
                }
                shader.stopDraw(color, radius, 1f)

                // hurt
                if (hurtingEntities.size > 0) {
                    shader.startDraw(partialTicks)
                    for (entity in hurtingEntities) {
                        mc.renderManager.renderEntityStatic(entity, partialTicks, true)
                    }
                    shader.stopDraw(hurtColor, radius, 1f)
                }
                shader = OutlineShader.OUTLINE_SHADER
                radius = 1.2f
                color = Color(255, 255, 255, 170)
                hurtColor = Color(255, 0, 0, 170)
                firstRun = false
            }
            return
        }

        val shader = (if (mode.equals("shaderoutline", ignoreCase = true)) OutlineShader.OUTLINE_SHADER else if (mode.equals("shaderglow", ignoreCase = true)) GlowShader.GLOW_SHADER else null)
            ?: return
        shader.startDraw(event.partialTicks)
        renderNameTags = false
        try {
            for (entity in mc.theWorld!!.loadedEntityList) {
                if (!EntityUtils.isSelected(entity, false)) continue
                mc.renderManager.renderEntityStatic(entity, mc.timer.renderPartialTicks, true)
            }
        } catch (ex: Exception) {
            ClientUtils.getLogger().error("An error occurred while rendering all entities for shader esp", ex)
        }
        renderNameTags = true
        val radius = if (mode.equals("shaderoutline", ignoreCase = true)) shaderOutlineRadius.get() else if (mode.equals("shaderglow", ignoreCase = true)) shaderGlowRadius.get() else 1f
        shader.stopDraw(getColor(null), radius, 1f)
    }

    override val tag: String
        get() = modeValue.get()

    fun getColor(entity: IEntity?): Color {
        run {
            if (entity != null && classProvider.isEntityLivingBase(entity)) {
                val entityLivingBase = entity.asEntityLivingBase()

                if (entityLivingBase.hurtTime > 0) return Color.RED
                if (EntityUtils.isFriend(entityLivingBase)) return Color.BLUE
                if (colorTeam.get()) {
                    val chars: CharArray = (entityLivingBase.displayName ?: return@run).formattedText.toCharArray()
                    var color = Int.MAX_VALUE
                    for (i in chars.indices) {
                        if (chars[i] != '§' || i + 1 >= chars.size) continue
                        val index = getColorIndex(chars[i + 1])
                        if (index < 0 || index > 15) continue
                        color = ColorUtils.hexColors[index]
                        break
                    }
                    return Color(color)
                }
            }
        }
            return if (colorRainbow.get()) rainbow() else if (gident.get()) {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.getGradientOffset3(
                    Color(CustomUI.r.get(), CustomUI.g.get(), CustomUI.b.get()),
                    Color(CustomUI.r2.get(), CustomUI.g2.get(), CustomUI.b2.get()),
                    (Math.abs(
                        System.currentTimeMillis() / 100f
                            .toDouble() + 1 * 337f
                    ) / 10)
                )
            } else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
    }
    companion object {
        @JvmField
        var renderNameTags = true
    }
}