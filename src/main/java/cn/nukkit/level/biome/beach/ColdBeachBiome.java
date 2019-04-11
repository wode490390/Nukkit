package cn.nukkit.level.biome.beach;

import cn.nukkit.level.biome.SandyBiome;
//import cn.nukkit.level.generator.populator.overworld.WaterIcePopulator;

public class ColdBeachBiome extends SandyBiome {

    public ColdBeachBiome() {
        //WaterIcePopulator ice = new WaterIcePopulator();
        //this.addPopulator(ice);

        this.setBaseHeight(0f);
        this.setHeightVariation(0.025f);
    }

    @Override
    public int getCoverBlock() {
        return SNOW_LAYER;
    }

    @Override
    public String getName() {
        return "Cold Beach";
    }

    @Override
    public boolean isFreezing() {
        return true;
    }
}
