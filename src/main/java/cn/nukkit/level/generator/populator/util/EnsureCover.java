package cn.nukkit.level.generator.populator.util;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

/**
 * @author DaPorkchop_
 */
public interface EnsureCover {

    static boolean ensureCover(int x, int y, int z, FullChunk chunk)    {
        int id = chunk.getBlockId(x, y, z);
        return id == Block.AIR || id == Block.SNOW_LAYER;
    }
}
