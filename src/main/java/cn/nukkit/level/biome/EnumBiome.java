package cn.nukkit.level.biome;

import cn.nukkit.level.biome.river.FrozenRiverBiome;
import cn.nukkit.level.biome.river.RiverBiome;
import cn.nukkit.level.biome.forest.ForestBiome;
import cn.nukkit.level.biome.forest.FlowerForestBiome;
import cn.nukkit.level.biome.forest.ForestHillsBiome;
import cn.nukkit.level.biome.jungle.JungleBiome;
import cn.nukkit.level.biome.jungle.JungleMBiome;
import cn.nukkit.level.biome.jungle.JungleEdgeMBiome;
import cn.nukkit.level.biome.jungle.JungleEdgeBiome;
import cn.nukkit.level.biome.jungle.JungleHillsBiome;
import cn.nukkit.level.biome.ocean.LukewarmOceanBiome;
import cn.nukkit.level.biome.ocean.WarmOceanBiome;
import cn.nukkit.level.biome.ocean.DeepColdOceanBiome;
import cn.nukkit.level.biome.ocean.DeepLukewarmOceanBiome;
import cn.nukkit.level.biome.ocean.DeepWarmOceanBiome;
import cn.nukkit.level.biome.ocean.DeepFrozenOceanBiome;
import cn.nukkit.level.biome.ocean.DeepOceanBiome;
import cn.nukkit.level.biome.ocean.ColdOceanBiome;
import cn.nukkit.level.biome.ocean.FrozenOceanBiome;
import cn.nukkit.level.biome.ocean.OceanBiome;
import cn.nukkit.level.biome.plains.PlainsBiome;
import cn.nukkit.level.biome.plains.SunflowerPlainsBiome;
import cn.nukkit.level.biome.mushroom.MushroomIslandBiome;
import cn.nukkit.level.biome.mushroom.MushroomIslandShoreBiome;
import cn.nukkit.level.biome.mesa.MesaPlateauFMBiome;
import cn.nukkit.level.biome.mesa.MesaPlateauMBiome;
import cn.nukkit.level.biome.mesa.MesaBryceBiome;
import cn.nukkit.level.biome.mesa.MesaBiome;
import cn.nukkit.level.biome.mesa.MesaPlateauFBiome;
import cn.nukkit.level.biome.mesa.MesaPlateauBiome;
import cn.nukkit.level.biome.iceplains.IceMountainsBiome;
import cn.nukkit.level.biome.iceplains.IcePlainsSpikesBiome;
import cn.nukkit.level.biome.iceplains.IcePlainsBiome;
import cn.nukkit.level.biome.roofedforest.RoofedForestMBiome;
import cn.nukkit.level.biome.roofedforest.RoofedForestBiome;
import cn.nukkit.level.biome.swamp.SwamplandMBiome;
import cn.nukkit.level.biome.swamp.SwampBiome;
import cn.nukkit.level.biome.savanna.SavannaPlateauBiome;
import cn.nukkit.level.biome.savanna.SavannaBiome;
import cn.nukkit.level.biome.savanna.SavannaMBiome;
import cn.nukkit.level.biome.savanna.SavannaPlateauMBiome;
import cn.nukkit.level.biome.taiga.MegaSpruceTaigaBiome;
import cn.nukkit.level.biome.taiga.MegaTaigaHillsBiome;
import cn.nukkit.level.biome.taiga.ColdTaigaMBiome;
import cn.nukkit.level.biome.taiga.ColdTaigaBiome;
import cn.nukkit.level.biome.taiga.TaigaHillsBiome;
import cn.nukkit.level.biome.taiga.ColdTaigaHillsBiome;
import cn.nukkit.level.biome.taiga.TaigaBiome;
import cn.nukkit.level.biome.taiga.MegaSpruceTaigaHillsBiome;
import cn.nukkit.level.biome.taiga.TaigaMBiome;
import cn.nukkit.level.biome.taiga.MegaTaigaBiome;
import cn.nukkit.level.biome.end.EndBarrensBiome;
import cn.nukkit.level.biome.end.SmallEndIslandsBiome;
import cn.nukkit.level.biome.end.EndMidlandsBiome;
import cn.nukkit.level.biome.end.TheEndBiome;
import cn.nukkit.level.biome.end.EndHighlandsBiome;
import cn.nukkit.level.biome.extremehills.ExtremeHillsMBiome;
import cn.nukkit.level.biome.extremehills.ExtremeHillsPlusMBiome;
import cn.nukkit.level.biome.extremehills.ExtremeHillsBiome;
import cn.nukkit.level.biome.extremehills.StoneBeachBiome;
import cn.nukkit.level.biome.extremehills.ExtremeHillsEdgeBiome;
import cn.nukkit.level.biome.extremehills.ExtremeHillsPlusBiome;
import cn.nukkit.level.biome.desert.DesertBiome;
import cn.nukkit.level.biome.desert.DesertMBiome;
import cn.nukkit.level.biome.desert.DesertHillsBiome;
import cn.nukkit.level.biome.beach.ColdBeachBiome;
import cn.nukkit.level.biome.beach.BeachBiome;
import cn.nukkit.level.biome.nether.HellBiome;

/**
 * @author DaPorkchop_
 * <p>
 * A more effective way of accessing specific biomes (to prevent Biome.getBiome(Biome.OCEAN) and such)
 * Also just looks cleaner than listing everything as static final in {@link Biome}
 * </p>
 */
public enum EnumBiome {
    OCEAN(0, new OceanBiome()),//
    PLAINS(1, new PlainsBiome()),
    DESERT(2, new DesertBiome()),
    EXTREME_HILLS(3, new ExtremeHillsBiome()),
    FOREST(4, new ForestBiome()),
    TAIGA(5, new TaigaBiome()),
    SWAMP(6, new SwampBiome()),
    RIVER(7, new RiverBiome()),//
    HELL(8, new HellBiome()),
    THE_END(9, new TheEndBiome()),
    FROZEN_OCEAN(10, new FrozenOceanBiome()), //DOES NOT GENERATE NATUALLY
    FROZEN_RIVER(11, new FrozenRiverBiome()),
    ICE_PLAINS(12, new IcePlainsBiome()),
    ICE_MOUNTAINS(13, new IceMountainsBiome()),
    MUSHROOM_ISLAND(14, new MushroomIslandBiome()),//
    MUSHROOM_ISLAND_SHORE(15, new MushroomIslandShoreBiome()),
    BEACH(16, new BeachBiome()),
    DESERT_HILLS(17, new DesertHillsBiome()),
    FOREST_HILLS(18, new ForestHillsBiome()),
    TAIGA_HILLS(19, new TaigaHillsBiome()),
    EXTREME_HILLS_EDGE(20, new ExtremeHillsEdgeBiome()), //DOES NOT GENERATE NATUALLY
    JUNGLE(21, new JungleBiome()),
    JUNGLE_HILLS(22, new JungleHillsBiome()),
    JUNGLE_EDGE(23, new JungleEdgeBiome()),
    DEEP_OCEAN(24, new DeepOceanBiome()),
    STONE_BEACH(25, new StoneBeachBiome()),
    COLD_BEACH(26, new ColdBeachBiome()),
    BIRCH_FOREST(27, new ForestBiome(ForestBiome.TYPE_BIRCH)),
    BIRCH_FOREST_HILLS(28, new ForestHillsBiome(ForestHillsBiome.TYPE_BIRCH)),
    ROOFED_FOREST(29, new RoofedForestBiome()),
    COLD_TAIGA(30, new ColdTaigaBiome()),
    COLD_TAIGA_HILLS(31, new ColdTaigaHillsBiome()),
    MEGA_TAIGA(32, new MegaTaigaBiome()),
    MEGA_TAIGA_HILLS(33, new MegaTaigaHillsBiome()),
    EXTREME_HILLS_PLUS(34, new ExtremeHillsPlusBiome()),
    SAVANNA(35, new SavannaBiome()),
    SAVANNA_PLATEAU(36, new SavannaPlateauBiome()),
    MESA(37, new MesaBiome()),
    MESA_PLATEAU_F(38, new MesaPlateauFBiome()),
    MESA_PLATEAU(39, new MesaPlateauBiome()),
    SMALL_END_ISLANDS(40, new SmallEndIslandsBiome()),
    END_MIDLANDS(41, new EndMidlandsBiome()),
    END_HIGHLANDS(42, new EndHighlandsBiome()),
    END_BARRENS(43, new EndBarrensBiome()),
    WARM_OCEAN(44, new WarmOceanBiome()),
    LUKEWARM_OCEAN(45, new LukewarmOceanBiome()),
    COLD_OCEAN(46, new ColdOceanBiome()),
    DEEP_WARM_OCEAN(47, new DeepWarmOceanBiome()),
    DEEP_LUKEWARM_OCEAN(48, new DeepLukewarmOceanBiome()),
    DEEP_COLD_OCEAN(49, new DeepColdOceanBiome()),
    DEEP_FROZEN_OCEAN(50, new DeepFrozenOceanBiome()),

    //    All biomes below this comment are mutated variants of existing biomes
    SUNFLOWER_PLAINS(129, new SunflowerPlainsBiome()),
    DESERT_M(130, new DesertMBiome()),
    EXTREME_HILLS_M(131, new ExtremeHillsMBiome()),
    FLOWER_FOREST(132, new FlowerForestBiome()),
    TAIGA_M(133, new TaigaMBiome()),
    SWAMPLAND_M(134, new SwamplandMBiome()),
    //no, the following jumps in IDs are NOT mistakes
    ICE_PLAINS_SPIKES(140, new IcePlainsSpikesBiome()),
    JUNGLE_M(149, new JungleMBiome()),
    JUNGLE_EDGE_M(151, new JungleEdgeMBiome()),
    BIRCH_FOREST_M(155, new ForestBiome(ForestBiome.TYPE_BIRCH_TALL)),
    BIRCH_FOREST_HILLS_M(156, new ForestHillsBiome(ForestBiome.TYPE_BIRCH_TALL)),
    ROOFED_FOREST_M(157, new RoofedForestMBiome()),
    COLD_TAIGA_M(158, new ColdTaigaMBiome()),
    MEGA_SPRUCE_TAIGA(160, new MegaSpruceTaigaBiome()),
    MEGA_SPRUCE_TAIGA_HILLS(161, new MegaSpruceTaigaHillsBiome()),
    EXTREME_HILLS_PLUS_M(162, new ExtremeHillsPlusMBiome()),
    SAVANNA_M(163, new SavannaMBiome()),
    SAVANNA_PLATEAU_M(164, new SavannaPlateauMBiome()),
    MESA_BRYCE(165, new MesaBryceBiome()),
    MESA_PLATEAU_F_M(166, new MesaPlateauFMBiome()),
    MESA_PLATEAU_M(167, new MesaPlateauMBiome());

    public final int id;
    public final Biome biome;

    EnumBiome(int id, Biome biome) {
        Biome.register(id, biome);
        this.id = id;
        this.biome = biome;
    }

    /**
     * You really shouldn't use this method if you can help it, reference the biomes directly!
     *
     * @param id biome id
     * @return biome
     */
    @Deprecated
    public static Biome getBiome(int id) {
        return Biome.getBiome(id);
    }

    /**
     * You really shouldn't use this method if you can help it, reference the biomes directly!
     *
     * @param name biome name
     * @return biome
     */
    @Deprecated
    public static Biome getBiome(String name) {
        return Biome.getBiome(name);
    }
}
