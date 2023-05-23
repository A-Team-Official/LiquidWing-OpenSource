/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package ad.utils;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class MovementUtils extends MinecraftInstance {
/*
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (mc2.player.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0D + 0.2D * (double) (mc2.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    public static double getBaseMoveSpeed(double customSpeed) {
        double baseSpeed = isOnIce() ? 0.258977700006 : customSpeed;
        if (mc2.player.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc2.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }




    public static boolean isOnIce() {
        final EntityPlayerSP player = mc2.player;
        final Block blockUnder = mc2.world.getBlockState(new BlockPos(player.posX, player.posY - 1.0, player.posZ)).getBlock();
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }

    public static int getJumpEffect() {
        return mc2.player.isPotionActive(Potion.jump) ? mc2.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0;
    }

    public static int getSpeedEffect() {
        return mc2.player.isPotionActive(Potion.moveSpeed) ? mc2.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        return getJumpBoostModifier(baseJumpHeight, true);
    }

    public static double getJumpBoostModifier(double baseJumpHeight, boolean potionJump) {
        if (mc2.player.isPotionActive(Potion.jump) && potionJump) {
            int amplifier = mc2.player.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += ((float) (amplifier + 1) * 0.1f);
        }

        return baseJumpHeight;
    }


 */



    public static void setMotion(MoveEvent event, double speed, double motion, boolean smoothStrafe) {
        double forward = mc2.player.movementInput.moveForward;
        double strafe = mc2.player.movementInput.moveStrafe;
        double yaw = mc2.player.rotationYaw;
        int direction = smoothStrafe ? 45 : 90;

        if ((forward == 0.0) && (strafe == 0.0)) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (forward > 0.0 ? -direction : direction);
                } else if (strafe < 0.0) {
                    yaw += (forward > 0.0 ? direction : -direction);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }

            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            event.setX((forward * speed * cos + strafe * speed * sin) * motion);
            event.setZ((forward * speed * sin - strafe * speed * cos) * motion);
        }
    }

    public static void setMotion(double speed, boolean smoothStrafe) {
        double forward = mc2.player.movementInput.moveForward;
        double strafe = mc2.player.movementInput.moveStrafe;
        float yaw = mc2.player.rotationYaw;
        int direction = smoothStrafe ? 45 : 90;

        if (forward == 0.0 && strafe == 0.0) {
            mc2.player.motionX = 0.0;
            mc2.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float) (forward > 0.0 ? -direction : direction);
                } else if (strafe < 0.0) {
                    yaw += (float) (forward > 0.0 ? direction : -direction);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }

            mc2.player.motionX = forward * speed * (-Math.sin(Math.toRadians(yaw))) + strafe * speed * Math.cos(Math.toRadians(yaw));
            mc2.player.motionZ = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * (-Math.sin(Math.toRadians(yaw)));
        }
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc2.player.motionX * mc2.player.motionX + mc2.player.motionZ * mc2.player.motionZ);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static boolean isMoving() {
        return mc2.player != null && (mc2.player.movementInput.moveForward != 0F || mc2.player.movementInput.moveStrafe != 0F);
    }

    public static boolean hasMotion() {
        return mc2.player.motionX != 0D && mc2.player.motionZ != 0D && mc2.player.motionY != 0D;
    }

    public static void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc2.player.motionX = -Math.sin(yaw) * speed;
        mc2.player.motionZ = Math.cos(yaw) * speed;
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc2.player.rotationYaw);
        mc2.player.setPosition(mc2.player.posX + (-Math.sin(yaw) * length), mc2.player.posY, mc2.player.posZ + (Math.cos(yaw) * length));
    }

    public static double getDirection() {
        float rotationYaw = mc2.player.rotationYaw;

        if(mc2.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc2.player.moveForward < 0F)
            forward = -0.5F;
        else if(mc2.player.moveForward > 0F)
            forward = 0.5F;

        if(mc2.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc2.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}