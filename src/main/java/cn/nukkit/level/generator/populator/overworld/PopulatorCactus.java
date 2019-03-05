package cn.nukkit.level.generator.populator.overworld;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.util.EnsureBelow;
import cn.nukkit.level.generator.populator.util.EnsureCover;
import cn.nukkit.level.generator.populator.Populator;
import cn.nukkit.level.generator.populator.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

import static cn.nukkit.block.BlockID.CACTUS;

/**
 * @author DaPorkchop_
 */
public class PopulatorCactus extends PopulatorSurfaceBlock {
    @Override
    protected boolean canStay(int x, int y, int z, FullChunk chunk) {
        return EnsureCover.ensureCover(x, y, z, chunk) && EnsureBelow.ensureBelow(x, y, z, SAND, chunk);
    }

    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return (CACTUS << 4) | 1;
    }
}
