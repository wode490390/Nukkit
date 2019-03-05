package cn.nukkit.level.generator.populator.overworld;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.util.PopulatorHelpers;
import cn.nukkit.level.generator.populator.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

/**
 * author: DaPorkchop_
 * Nukkit Project
 */
public class PopulatorGrass extends PopulatorSurfaceBlock {
    @Override
    protected boolean canStay(int x, int y, int z, FullChunk chunk) {
        return PopulatorHelpers.canGrassStay(x, y, z, chunk);
    }

    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return TALL_GRASS << 4;
    }
}
