package cn.nukkit.level.biome.mesa;

/**
 * @author DaPorkchop_
 */
public class MesaPlateauBiome extends MesaBiome {
    public MesaPlateauBiome() {
        super();

        this.setBaseHeight(1.5f);
        this.setHeightVariation(0.025f);

        this.setMoundHeight(0);
    }

    @Override
    public String getName() {
        return "Mesa Plateau";
    }
}
