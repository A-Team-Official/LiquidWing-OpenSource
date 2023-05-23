package net.ccbluex.liquidbounce.features.module.modules.hyt;


import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "BedFucker",description = "eee",category = ModuleCategory.WORLD, Chinese = "自动挖床")
public class BedFucker extends Module {
    private final FloatValue radius = new FloatValue("Radius", 4.5f, 1.0f, 6.0f);
    private final ListValue modeValue = new ListValue("Break Target Mode", new String[]{"Cake","Egg","Bed"}, "Bed");


    @EventTarget
    private void onUpdate(UpdateEvent event) {
        int x = -radius.getValue().intValue();
        while (x < radius.getValue().intValue()) {
            int y = radius.getValue().intValue();
            while (y > -radius.getValue().intValue()) {
                int z = -radius.getValue().intValue();
                while (z < radius.getValue().intValue()) {
                    int xPos = (int) mc.getThePlayer().getPosX() + x;
                    int yPos = (int) mc.getThePlayer().getPosY() + y;
                    int zPos = (int) mc.getThePlayer().getPosZ() + z;
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    Block block = mc2.world.getBlockState(blockPos).getBlock();
                    if (block.getBlockState().getBlock() == Block.getBlockById(92) && this.modeValue.getValue() == "Cake") {
                        mc2.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc2.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.getThePlayer().swingItem();
                    } else if (block.getBlockState().getBlock() == Block.getBlockById(122) && this.modeValue.getValue() == "Egg") {
                        mc2.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc2.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.getThePlayer().swingItem();
                    } else if (block.getBlockState().getBlock() == Block.getBlockById(121) && this.modeValue.getValue() == "Bed") {
                        mc2.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc2.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.getThePlayer().swingItem();
                    }
                    ++z;
                }
                --y;
            }
            ++x;
        }
    }
    
}
