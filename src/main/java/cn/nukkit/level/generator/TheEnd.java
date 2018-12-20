package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.noise.PerlinOctaveGenerator;
import cn.nukkit.level.generator.noise.bukkit.OctaveGenerator;
import cn.nukkit.level.generator.populator.impl.PopulatorObsidianPillar;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

public class TheEnd extends Generator {

    protected static final int WORLD_DEPTH = 128;

    private final Map<String, Map<String, OctaveGenerator>> octaveCache = new HashMap<String, Map<String, OctaveGenerator>>();

    private static double coordinateScale = 684.412d;
    private static double heightScale = 1368.824d;
    private static double detailNoiseScaleX = 80.0d;  // mainNoiseScaleX
    private static double detailNoiseScaleY = 160.0d; // mainNoiseScaleY
    private static double detailNoiseScaleZ = 80.0d;  // mainNoiseScaleZ

    private final double[][][] density = new double[3][3][33];

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    private Random random;
    private final List<Populator> populators = new ArrayList<Populator>();
    private List<Populator> generationPopulators = new ArrayList<Populator>();

    private long localSeed1;
    private long localSeed2;

    public TheEnd() {
        this(new HashMap<String, Object>());
    }

    public TheEnd(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public int getId() {
        return Generator.TYPE_THE_END;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_THE_END;
    }

    @Override
    public String getName() {
        return "the_end";
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<String, Object>();
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.level;
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();

        PopulatorObsidianPillar obsidianPillar = new PopulatorObsidianPillar();
        obsidianPillar.setAmount(1);
        this.populators.add(obsidianPillar);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());

        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        int densityX = chunkX << 1;
        int densityZ = chunkZ << 1;

        Map<String, OctaveGenerator> octaves = this.getWorldOctaves();
        double[] roughnessNoise = ((PerlinOctaveGenerator) octaves.get("roughness")).getFractalBrownianMotion(densityX, 0, densityZ, 0.5D, 2.0D);
        double[] roughnessNoise2 = ((PerlinOctaveGenerator) octaves.get("roughness2")).getFractalBrownianMotion(densityX, 0, densityZ, 0.5D, 2.0D);
        double[] detailNoise = ((PerlinOctaveGenerator) octaves.get("detail")).getFractalBrownianMotion(densityX, 0, densityZ, 0.5D, 2.0D);

        int index = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double noiseHeight = 100.0D - Math.sqrt((densityX + i) * (densityX + i) + (densityZ + j) * (densityZ + j)) * 8.0D;
                noiseHeight = Math.max(-100.0D, Math.min(80.0D, noiseHeight));
                for (int k = 0; k < 33; k++) {
                    double noiseR = roughnessNoise[index] / 512.0D;
                    double noiseR2 = roughnessNoise2[index] / 512.0D;
                    double noiseD = (detailNoise[index] / 10.0D + 1.0D) / 2.0D;
                    // linear interpolation
                    double dens = noiseD < 0 ? noiseR : noiseD > 1 ? noiseR2 : noiseR + (noiseR2 - noiseR) * noiseD;
                    dens = dens - 8.0D + noiseHeight;
                    index++;
                    if (k < 8) {
                        double lowering = (8 - k) / 7;
                        dens = dens * (1.0D - lowering) + lowering * -30.0D;
                    } else if (k > 33 / 2 - 2) {
                        double lowering = (k - (33 / 2 - 2)) / 64.0D;
                        lowering = Math.max(0.0D, Math.min(1.0D, lowering));
                        dens = dens * (1.0D - lowering) + lowering * -3000.0D;
                    }
                    this.density[i][j][k] = dens;
                }
            }
        }

        for (int i = 0; i < 3 - 1; i++) {
            for (int j = 0; j < 3 - 1; j++) {
                for (int k = 0; k < 33 - 1; k++) {
                    double d1 = this.density[i][j][k];
                    double d2 = this.density[i + 1][j][k];
                    double d3 = this.density[i][j + 1][k];
                    double d4 = this.density[i + 1][j + 1][k];
                    double d5 = (this.density[i][j][k + 1] - d1) / 4;
                    double d6 = (this.density[i + 1][j][k + 1] - d2) / 4;
                    double d7 = (this.density[i][j + 1][k + 1] - d3) / 4;
                    double d8 = (this.density[i + 1][j + 1][k + 1] - d4) / 4;

                    for (int l = 0; l < 4; l++) {
                        double d9 = d1;
                        double d10 = d3;
                        for (int m = 0; m < 8; m++) {
                            double dens = d9;
                            for (int n = 0; n < 8; n++) {
                                // any density higher than 0 is ground, any density lower or equal to 0 is air.
                                if (dens > 0) {
                                    chunk.setBlock(m + (i << 3), l + (k << 2), n + (j << 3), END_STONE);
                                }
                                // interpolation along z
                                dens += (d10 - d9) / 8;
                            }
                            // interpolation along x
                            d9 += (d2 - d1) / 8;
                            // interpolate along z
                            d10 += (d4 - d3) / 8;
                        }
                        // interpolation along y
                        d1 += d5;
                        d3 += d7;
                        d2 += d6;
                        d4 += d8;
                    }
                }
            }
        }

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                chunk.setBiomeId(x, z, EnumBiome.THE_END.biome.getId());
            }
        }

        for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }

        EnumBiome.getBiome(chunk.getBiomeId(7, 7)).populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }

    public Vector3 getSpawn() {
        return new Vector3(100.5, 50, 0.5);
    }

    /**
     * Returns the {@link OctaveGenerator} instances for the world, which are
     * either newly created or retrieved from the cache.
     *
     * @return A map of {@link OctaveGenerator}s
     */
    protected final Map<String, OctaveGenerator> getWorldOctaves() {
        if (this.octaveCache.get(this.getName()) == null) {
            Map<String, OctaveGenerator> octaves = new HashMap<String, OctaveGenerator>();
            NukkitRandom seed = new NukkitRandom(this.level.getSeed());

            OctaveGenerator gen = new PerlinOctaveGenerator(seed, 16, 3, 33, 3);
            gen.setXScale(coordinateScale);
            gen.setYScale(heightScale);
            gen.setZScale(coordinateScale);
            octaves.put("roughness", gen);

            gen = new PerlinOctaveGenerator(seed, 16, 3, 33, 3);
            gen.setXScale(coordinateScale);
            gen.setYScale(heightScale);
            gen.setZScale(coordinateScale);
            octaves.put("roughness2", gen);

            gen = new PerlinOctaveGenerator(seed, 8, 3, 33, 3);
            gen.setXScale(coordinateScale / detailNoiseScaleX);
            gen.setYScale(heightScale / detailNoiseScaleY);
            gen.setZScale(coordinateScale / detailNoiseScaleZ);
            octaves.put("detail", gen);

            this.octaveCache.put(this.getName(), octaves);
            return octaves;
        }
        return this.octaveCache.get(this.getName());
    }
}
