package cn.nukkit.level.biome.iceplains;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.biome.SnowyBiome;
import cn.nukkit.level.generator.populator.overworld.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class IcePlainsBiome extends SnowyBiome {

    public IcePlainsBiome() {
        super();

        PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
        trees.setBaseAmount(0);
        trees.setRandomAmount(1);
        this.addPopulator(trees);



        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Ice Plains";
    }
}
