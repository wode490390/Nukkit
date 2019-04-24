package cn.nukkit.level.biome.ocean;

//import cn.nukkit.level.generator.populator.overworld.WaterIcePopulator;

/**
 * author: DaPorkchop_
 * Nukkit Project
 * <p>
 * This biome does not generate naturally
 */
public class FrozenOceanBiome extends OceanBiome {

    public FrozenOceanBiome() {
        super();

        //WaterIcePopulator ice = new WaterIcePopulator();
        //this.addPopulator(ice);
    }

    @Override
    public String getName() {
        return "Frozen Ocean";
    }

    @Override
    public boolean isFreezing() {
        return true;
    }
}
