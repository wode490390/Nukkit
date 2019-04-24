package cn.nukkit.level.biome.jungle;

/**
 * @author DaPorkchop_
 */
public class JungleHillsBiome extends JungleBiome {
    public JungleHillsBiome() {
        super();

        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
    }

    @Override
    public String getName() {
        return "Jungle Hills";
    }
}
