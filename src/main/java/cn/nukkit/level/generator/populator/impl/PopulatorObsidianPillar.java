package cn.nukkit.level.generator.populator.impl;

//import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.BlockPopulator;
import cn.nukkit.math.NukkitRandom;

public class PopulatorObsidianPillar extends BlockPopulator {

    @Override
    public void decorate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if (random.nextBoundedInt(5) == 0) {
            int x = (chunkX << 4) + random.nextBoundedInt(16);
            int z = (chunkZ << 4) + random.nextBoundedInt(16);
            int y = this.getHighestWorkableBlock(level, x, z, chunk);
            if (y < 1) {
                return;
            }

            if (level.getBlockIdAt(x, y, z) != 0 || level.getBlockIdAt(x, y - 1, z) != END_STONE) {
                return;
            }

            int height = random.nextBoundedInt(32) + 6;
            int radius = random.nextBoundedInt(4) + 1;

            // check under the pillar that there's no gap
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    if (i * i + j * j <= radius * radius + 1 && level.getBlockIdAt(x + i, y - 1, z + j) != END_STONE) {
                        return;
                    }
                }
            }

            // build a pillar
            for (int k = 0; k < height && y + k < 256; k++) {
                for (int i = -radius; i <= radius; i++) {
                    for (int j = -radius; j <= radius; j++) {
                        if (i * i + j * j <= radius * radius + 1) {
                            level.setBlockAt(x + i, y + k, z + j, OBSIDIAN);
                        }
                    }
                }
            }

            //TODO: level.spawnEntity(new EntityEndCrystal(), x + 0.5D, y + height + 1, z + 0.5D, random.nextFloat() * 360, 0);
            level.setBlockAt(x, y + height, z, BEDROCK);
        }
    }
}
