package cn.nukkit.level.biome.river;

//import cn.nukkit.level.generator.populator.overworld.WaterIcePopulator;

/**
 * author: DaPorkchop_
 * Nukkit Project
 */
public class FrozenRiverBiome extends RiverBiome {

    public FrozenRiverBiome() {
        super();

        //WaterIcePopulator ice = new WaterIcePopulator();
        //this.addPopulator(ice);
    }

    @Override
    public String getName() {
        return "Frozen River";
    }

    @Override
    public boolean isFreezing() {
        return true;
    }
}
