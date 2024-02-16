package me.paradise.swagcraftsg.utils;

import net.minestom.server.instance.block.Block;

public class WoodUtil {
    public static boolean isWood(Block block) {
        if(block == Block.ACACIA_WOOD || block == Block.BIRCH_WOOD || block == Block.DARK_OAK_WOOD || block == Block.JUNGLE_WOOD || block == Block.OAK_WOOD || block == Block.SPRUCE_WOOD) {
            return true;
        }

        if(block == Block.OAK_LOG || block == Block.BIRCH_LOG || block == Block.DARK_OAK_LOG || block == Block.JUNGLE_LOG || block == Block.SPRUCE_LOG || block == Block.ACACIA_LOG) {
            return true;
        }

        return false;
    }
}
