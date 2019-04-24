package cn.nukkit.level.generator.populator.overworld;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitRandom;

/**
 * @author Niall Lindsay (Niall7459)
 * <p>
 * Nukkit Project
 * </p>
 */

public class PopulatorTallSugarcane extends PopulatorSugarcane {

    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        int height = random.nextBoundedInt(3) + 1;
        for (int i = 0; i < height; i++)    {
            chunk.setFullBlockId(x, y + i, z, id);
        }
    }
}
