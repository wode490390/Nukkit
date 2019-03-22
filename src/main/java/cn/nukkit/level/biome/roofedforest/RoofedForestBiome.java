package cn.nukkit.level.biome.roofedforest;

import cn.nukkit.level.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.overworld.MushroomPopulator;
import cn.nukkit.level.generator.populator.overworld.PopulatorFlower;
import cn.nukkit.level.generator.populator.overworld.tree.DarkOakTreePopulator;

public class RoofedForestBiome extends GrassyBiome {

    public RoofedForestBiome() {
        super();

        DarkOakTreePopulator tree = new DarkOakTreePopulator();
        tree.setBaseAmount(20);
        tree.setRandomAmount(10);
        this.addPopulator(tree);

        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(2);
        this.addPopulator(flower);

        MushroomPopulator mushroom = new MushroomPopulator();
        mushroom.setBaseAmount(0);
        mushroom.setRandomAmount(1);
        this.addPopulator(mushroom);
    }

    @Override
    public String getName() {
        return "Roofed Forest";
    }

}
