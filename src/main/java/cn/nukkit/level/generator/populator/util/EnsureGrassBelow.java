package cn.nukkit.level.generator.populator.util;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

/**
 * @author DaPorkchop_
 */
public interface EnsureGrassBelow {

    static boolean ensureGrassBelow(int x, int y, int z, FullChunk chunk)  {
        return EnsureBelow.ensureBelow(x, y, z, Block.GRASS, chunk);
    }

}
