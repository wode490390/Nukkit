package cn.nukkit.level.biome.taiga;

/**
 * author: DaPorkchop_
 * Nukkit Project
 */
public class MegaTaigaHillsBiome extends MegaTaigaBiome {
    public MegaTaigaHillsBiome() {
        super();

        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        return "Mega Taiga Hills";
    }
}
