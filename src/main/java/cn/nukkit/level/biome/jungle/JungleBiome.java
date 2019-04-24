package cn.nukkit.level.biome.jungle;

import cn.nukkit.level.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.overworld.PopulatorMelon;
import cn.nukkit.level.generator.populator.overworld.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.overworld.tree.JungleTreePopulator;

/**
 * @author DaPorkchop_
 */
public class JungleBiome extends GrassyBiome {
    public JungleBiome() {
        super();

        JungleTreePopulator trees = new JungleTreePopulator();
        trees.setBaseAmount(10);
        this.addPopulator(trees);

        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(6);
        this.addPopulator(bigTrees);

        PopulatorMelon melon = new PopulatorMelon();
        melon.setBaseAmount(-65);
        melon.setRandomAmount(70);
        this.addPopulator(melon);
    }

    @Override
    public String getName() {
        return "Jungle";
    }
}
