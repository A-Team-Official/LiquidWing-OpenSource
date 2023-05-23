package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.pathfinder.AStarCustomPathFinder;
import net.ccbluex.liquidbounce.utils.pathfinder.Vec3;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@ModuleInfo(name = "Tpaura", description = "Automatically attacks targets with infinity range.", category = ModuleCategory.COMBAT, Chinese = "百米大刀")
public final class TpAura extends Module
{
    private ArrayList<Vec3> path;
    private final FloatValue rangeValue;
    private final FloatValue cpsValue;
    private final BoolValue aac;
    private final BoolValue hyt;
    private MSTimer attackDelay;
    private MSTimer cps;
    public List<IEntityLivingBase> targets;
    private final LinkedBlockingQueue<IPacket> packets;
    private boolean disableLogger;
    private List<Vec3> test;

    public TpAura() {
        this.attackDelay = new MSTimer();
        this.cps = new MSTimer();
        this.targets = new ArrayList<IEntityLivingBase>();
        this.test = new ArrayList<Vec3>();
        this.packets = new LinkedBlockingQueue<IPacket>();
        this.rangeValue = new FloatValue("Range", 100.0f, 1.0f, 200.0f);
        this.cpsValue = new FloatValue("CPS", 5.0f, 1.0f, 20.0f);
        this.aac = new BoolValue("AAC", false);
        this.hyt = new BoolValue("HYT", true);
    }

    @Override
    public void onEnable() {
        this.disableLogger = false;
        this.attackDelay.reset();
        this.targets.clear();
    }

    @Override
    public void onDisable() {
        this.disableLogger = false;
        this.attackDelay.reset();
        this.targets.clear();
        if (this.hyt.get()) {
            this.blink();
        }
    }

    public void drawPath(final Vec3 vec) {
        final double x = vec.getX() - TpAura.mc.getRenderManager().getRenderPosX();
        final double y = vec.getY() - TpAura.mc.getRenderManager().getRenderPosY();
        final double z = vec.getZ() - TpAura.mc.getRenderManager().getRenderPosZ();
        final double width = 0.3;
        final double height = TpAura.mc.getThePlayer().getEyeHeight();
        RenderUtils.pre3D();
        GL11.glLoadIdentity();
        TpAura.mc.getEntityRenderer().setupCameraTransform(TpAura.mc.getTimer().getRenderPartialTicks(), 2);
        final int[] colors = { Color.black.getRGB(), Color.white.getRGB() };
        for (int i = 0; i < 2; ++i) {
            RenderUtils.glColorHex(colors[i]);
            GL11.glLineWidth((float)(3 - i * 2));
            GL11.glBegin(3);
            GL11.glVertex3d(x - width, y, z - width);
            GL11.glVertex3d(x - width, y, z - width);
            GL11.glVertex3d(x - width, y + height, z - width);
            GL11.glVertex3d(x + width, y + height, z - width);
            GL11.glVertex3d(x + width, y, z - width);
            GL11.glVertex3d(x - width, y, z - width);
            GL11.glVertex3d(x - width, y, z + width);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(x + width, y, z + width);
            GL11.glVertex3d(x + width, y + height, z + width);
            GL11.glVertex3d(x - width, y + height, z + width);
            GL11.glVertex3d(x - width, y, z + width);
            GL11.glVertex3d(x + width, y, z + width);
            GL11.glVertex3d(x + width, y, z - width);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(x + width, y + height, z + width);
            GL11.glVertex3d(x + width, y + height, z - width);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(x - width, y + height, z + width);
            GL11.glVertex3d(x - width, y + height, z - width);
            GL11.glEnd();
        }
        RenderUtils.post3D();
    }

    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!this.path.isEmpty()) {
            if (this.test != null) {
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                RenderUtils.glColor(Color.WHITE);

                double posX =
                        mc.getThePlayer().getLastTickPosX() + (mc.getThePlayer().getPosX() - mc.getThePlayer().getLastTickPosX()) * mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosX();
                double posY =
                        mc.getThePlayer().getLastTickPosY() + (mc.getThePlayer().getPosY() - mc.getThePlayer().getLastTickPosY()) * mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosY();
                double posZ =
                        mc.getThePlayer().getLastTickPosZ() + (mc.getThePlayer().getPosZ() - mc.getThePlayer().getLastTickPosZ()) * mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosZ();

                GL11.glLineWidth(1.0F);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glBegin(GL11.GL_LINE_STRIP);

                // GL11.glVertex3d(posX, posY, posZ);

                for (final Vec3 pos : this.test) {
                    if (pos != null) {
                        double x = pos.getX() - mc.getRenderManager().getRenderPosX();
                        double y = pos.getY() - mc.getRenderManager().getRenderPosY();
                        double z = pos.getZ() - mc.getRenderManager().getRenderPosZ();
                        //mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2)

                        GL11.glVertex3d(x, y, z);

                        //  this.drawPath(pos);
                    }
                }

                GL11.glEnd();
                GL11.glDisable(GL11.GL_LINE_SMOOTH);

                GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
            }
            if (this.attackDelay.hasTimePassed(1000L)) {
                this.test = new ArrayList<Vec3>();
                this.path.clear();
            }
        }
    }

    @EventTarget
    public void onUpdate(final UpdateEvent e) {
        final float delayValue = 20.0f / this.cpsValue.get() * 50.0f;
        this.targets = this.getTargets();
        if (this.targets.isEmpty()) {
            this.attackDelay.reset();
            this.blink();
            this.test = new ArrayList<Vec3>();
        }
        if (this.attackDelay.hasTimePassed((long)delayValue) && this.targets.size() > 0) {
            final Vec3 topFrom = new Vec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
            final Vec3 to = new Vec3(this.targets.get(0).getPosX(), this.targets.get(0).getPosY(), this.targets.get(0).getPosZ());
            this.path = this.computePath(topFrom, to);
            this.test = this.path;
            for (final Vec3 pathElm : this.path) {
                mc.getThePlayer().getSendQueue().addToSendQueue(classProvider.createCPacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
            }
            if (this.aac.get() && !this.hyt.get()) {
                mc.getThePlayer().getSendQueue().addToSendQueue(classProvider.createCPacketPlayerPosition(this.targets.get(0).getPosX(), this.targets.get(0).getPosY(), this.targets.get(0).getPosZ(), true));
            }
            if (this.hyt.get()) {
                this.blink();
            }
            TpAura.mc.getNetHandler().addToSendQueue(TpAura.classProvider.createCPacketUseEntity(this.targets.get(0), ICPacketUseEntity.WAction.ATTACK));
            mc.getThePlayer().swingItem();
            Collections.reverse(this.path);
            for (final Vec3 pathElm : this.path) {
                mc.getThePlayer().getSendQueue().addToSendQueue(classProvider.createCPacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
            }
            if (this.hyt.get()) {
                this.blink();
            }
            this.attackDelay.reset();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull final PacketEvent event) {
        if (this.hyt.get()) {
            Intrinsics.checkParameterIsNotNull(event, "event");
            final IPacket packet = event.getPacket();
            if (TpAura.mc.getThePlayer() == null || this.disableLogger) {
                return;
            }
            if (TpAura.classProvider.isCPacketPlayer(packet)) {
                event.cancelEvent();
            }
            if (TpAura.classProvider.isCPacketPlayerPosition(packet) || TpAura.classProvider.isCPacketPlayerPosLook(packet) || TpAura.classProvider.isCPacketPlayerBlockPlacement(packet) || TpAura.classProvider.isCPacketAnimation(packet) || TpAura.classProvider.isCPacketEntityAction(packet) || TpAura.classProvider.isCPacketUseEntity(packet)) {
                event.cancelEvent();
                this.packets.add(packet);
            }
        }
    }

    private boolean isEnemy(final IEntity entity) {
        if (!TpAura.classProvider.isEntityLivingBase(entity) || entity == null || (!EntityUtils.targetDead && !this.isAlive(entity.asEntityLivingBase())) || entity == TpAura.mc.getThePlayer()) {
            return false;
        }
        if (!EntityUtils.targetInvisible && entity.isInvisible()) {
            return false;
        }
        if (!EntityUtils.targetPlayer || !TpAura.classProvider.isEntityPlayer(entity)) {
            return (EntityUtils.targetMobs && TpAura.classProvider.isEntityMob(entity)) || (EntityUtils.targetAnimals && TpAura.classProvider.isEntityAnimal(entity));
        }
        final IEntityPlayer player = entity.asEntityPlayer();
        if (player.isSpectator() || AntiBot.isBot(player)) {
            return false;
        }
        if (LiquidBounce.fileManager.friendsConfig.isFriend(ColorUtils.stripColor(player.getName())) && !LiquidBounce.moduleManager.getModule(NoFriends.class).getState()) {
            return false;
        }
        final Teams teams = (Teams)LiquidBounce.moduleManager.getModule(Teams.class);
        return !teams.getState() || !teams.isInYourTeam(entity.asEntityLivingBase());
    }

    private boolean isAlive(final IEntityLivingBase entity) {
        return entity.isEntityAlive() && entity.getHeight() > 0.0f;
    }

    private List<IEntityLivingBase> getTargets() {
        final List<IEntityLivingBase> targets = new ArrayList<IEntityLivingBase>();
        for (final IEntity entity : TpAura.mc.getTheWorld().getLoadedEntityList()) {
            if (entity.getDistanceToEntity(TpAura.mc.getThePlayer()) <= this.rangeValue.get()) {
                if (!this.isEnemy(entity)) {
                    continue;
                }
                targets.add(entity.asEntityLivingBase());
            }
        }
        targets.sort((o1, o2) -> (int)(o1.getDistanceToEntity(TpAura.mc.getThePlayer()) * 1000.0f - o2.getDistanceToEntity(TpAura.mc.getThePlayer()) * 1000.0f));
        return targets;
    }

    private ArrayList<Vec3> computePath(Vec3 topFrom, final Vec3 to) {
        if (!this.canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        final AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        final ArrayList<Vec3> path = new ArrayList<Vec3>();
        final ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (final Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            }
            else {
                boolean canContinue = true;
                Label_0350: {
                    if (pathElm.squareDistanceTo(lastDashLoc) > 25.0) {
                        canContinue = false;
                    }
                    else {
                        final double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                        final double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                        final double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                        final double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                        final double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                        final double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                        for (int x = (int)smallX; x <= bigX; ++x) {
                            for (int y = (int)smallY; y <= bigY; ++y) {
                                for (int z = (int)smallZ; z <= bigZ; ++z) {
                                    if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                        canContinue = false;
                                        break Label_0350;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        IBlockState blockState = Minecraft.getMinecraft().world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getMaterial(blockState) == Material.AIR || block.getMaterial(blockState) == Material.PLANTS || block.getMaterial(blockState) == Material.VINE || block == Blocks.LADDER || block == Blocks.WATER || block == Blocks.FLOWING_WATER || block == Blocks.WALL_SIGN || block == Blocks.STANDING_SIGN;
    }

    private void blink() {
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                final INetworkManager networkManager = TpAura.mc.getNetHandler().getNetworkManager();
                final IPacket take = this.packets.take();
                Intrinsics.checkExpressionValueIsNotNull(take, "packets.take()");
                networkManager.sendPacket(take);
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
    }

    @Override
    public String getTag() {
        return this.rangeValue.get().toString();
    }
}
