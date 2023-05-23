package net.ccbluex.liquidbounce.utils.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class BlockUtil {

    public static Block getBlock(BlockPos blockPos) {
        return Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
    }

    public static boolean isSolidFullCube(BlockPos blockPos) {
        IBlockState blockState = Minecraft.getMinecraft().world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        return block.getMaterial(blockState).blocksMovement() && block.isFullCube(blockState);
    }

}
