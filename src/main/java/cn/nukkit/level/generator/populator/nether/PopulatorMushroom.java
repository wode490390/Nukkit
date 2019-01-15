package cn.nukkit.level.generator.populator.nether;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.BlockPopulator;
import cn.nukkit.math.NukkitRandom;
import java.util.Arrays;

public class PopulatorMushroom extends BlockPopulator {

    private static final int[] MATERIALS = {NETHERRACK, QUARTZ_ORE, SOUL_SAND, GRAVEL};

    private final int type;

    public PopulatorMushroom(int type) {
        if (type != BROWN_MUSHROOM && type != RED_MUSHROOM) {
            throw new IllegalArgumentException("MushroomDecorator type must be BROWN_MUSHROOM or RED_MUSHROOM");
        }
        this.type = type;
    }

    @Override
    public void decorate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk source) {
        int sourceX = (chunkX << 4) + random.nextBoundedInt(16);
        int sourceZ = (chunkZ << 4) + random.nextBoundedInt(16);
        int sourceY = random.nextBoundedInt(128);

        for (int i = 0; i < 64; i++) {
            int x = sourceX + random.nextBoundedInt(8) - random.nextBoundedInt(8);
            int z = sourceZ + random.nextBoundedInt(8) - random.nextBoundedInt(8);
            int y = sourceY + random.nextBoundedInt(4) - random.nextBoundedInt(4);

            if (y < 128 && level.getBlockIdAt(x, y, z) == AIR && Arrays.asList(MATERIALS).contains(level.getBlockIdAt(x, y - 1, z))) level.setBlockAt(x, y, z, this.type);
        }
    }
}
