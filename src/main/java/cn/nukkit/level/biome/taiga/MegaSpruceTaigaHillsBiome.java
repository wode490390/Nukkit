package cn.nukkit.level.biome.taiga;

import cn.nukkit.level.generator.populator.overworld.tree.SpruceBigTreePopulator;

public class MegaSpruceTaigaHillsBiome extends TaigaBiome {

    public MegaSpruceTaigaHillsBiome() {
        super();

        SpruceBigTreePopulator bigTrees = new SpruceBigTreePopulator();
        bigTrees.setBaseAmount(6);
        this.addPopulator(bigTrees);
    }

    @Override
    public String getName() {
        return "Mega Spruce Taiga Hills";
    }
}
