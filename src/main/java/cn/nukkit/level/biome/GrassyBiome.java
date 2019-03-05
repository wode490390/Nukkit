package cn.nukkit.level.biome;

import cn.nukkit.level.biome.CoveredBiome;
import cn.nukkit.block.BlockDoublePlant;
import cn.nukkit.level.generator.populator.overworld.PopulatorDoublePlant;
import cn.nukkit.level.generator.populator.overworld.PopulatorGrass;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class GrassyBiome extends CoveredBiome {
    public GrassyBiome() {
        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(30);
        this.addPopulator(grass);

        PopulatorDoublePlant tallGrass = new PopulatorDoublePlant(BlockDoublePlant.TALL_GRASS);
        tallGrass.setBaseAmount(5);
        this.addPopulator(tallGrass);
    }

    @Override
    public int getSurfaceBlock(int y) {
        return GRASS;
    }

    @Override
    public int getGroundBlock(int y) {
        return DIRT;
    }
}
