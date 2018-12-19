package cn.nukkit.level.biome.impl.ocean;

public class DeepFrozenOceanBiome extends FrozenOceanBiome {

    public DeepFrozenOceanBiome() {
        super();
        this.setBaseHeight(-1.8f);
    }

    @Override
    public String getName() {
        return "Deep Frozen Ocean";
    }
}
