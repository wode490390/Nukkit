package cn.nukkit.level.biome.desert;

import cn.nukkit.level.biome.SandyBiome;
import cn.nukkit.level.generator.populator.overworld.PopulatorCactus;
import cn.nukkit.level.generator.populator.overworld.PopulatorDeadBush;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DesertBiome extends SandyBiome {
    public DesertBiome() {
        PopulatorCactus cactus = new PopulatorCactus();
        cactus.setBaseAmount(2);
        this.addPopulator(cactus);

        PopulatorDeadBush deadbush = new PopulatorDeadBush();
        deadbush.setBaseAmount(2);
        this.addPopulator(deadbush);

        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
    }

    @Override
    public String getName() {
        return "Desert";
    }
}
