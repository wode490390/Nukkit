package cn.nukkit.level.generator.populator.overworld;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.Populator;
import cn.nukkit.math.NukkitRandom;

public class WaterIcePopulator extends Populator {

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Biome biome = Biome.getBiome(chunk.getBiomeId(x, z));
                if (biome.isFreezing()) {
                    int topBlock = chunk.getHighestBlockAt(x, z);
                    if (chunk.getBlockId(x, topBlock, z) == Block.STILL_WATER)     {
                        chunk.setBlockId(x, topBlock, z, Block.ICE);
                    }
                }
            }
        }
    }
}
