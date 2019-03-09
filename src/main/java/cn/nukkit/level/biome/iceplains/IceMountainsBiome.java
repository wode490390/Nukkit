package cn.nukkit.level.biome.iceplains;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.biome.SnowyBiome;
import cn.nukkit.level.generator.populator.overworld.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class IceMountainsBiome extends SnowyBiome {

    public IceMountainsBiome() {
        super();

        PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
        trees.setBaseAmount(0);
        trees.setRandomAmount(1);
        this.addPopulator(trees);

        this.setBaseHeight(1f);
        this.setHeightVariation(0.5f);
    }

    @Override
    public String getName() {
        return "Ice Mountains";
    }

    @Override
    public boolean doesOverhang() {
        return true;
    }
}
