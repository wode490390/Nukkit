package cn.nukkit.level.biome;

//import cn.nukkit.level.generator.populator.overworld.WaterIcePopulator;

/**
 * author: DaPorkchop_
 * Nukkit Project
 */
public abstract class SnowyBiome extends GrassyBiome {

    public SnowyBiome() {
        super();

        //WaterIcePopulator waterIce = new WaterIcePopulator();
        //this.addPopulator(waterIce);
    }

    @Override
    public int getCoverBlock() {
        return SNOW_LAYER;
    }
}
