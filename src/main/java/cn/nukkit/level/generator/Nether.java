package cn.nukkit.level.generator;

import cn.nukkit.block.*;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.noise.PerlinOctaveGenerator;
import cn.nukkit.level.generator.noise.bukkit.OctaveGenerator;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorGlowStone;
import cn.nukkit.level.generator.populator.impl.PopulatorGroundFire;
import cn.nukkit.level.generator.populator.impl.PopulatorLava;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.*;

public class Nether extends Generator {

    protected static final int WORLD_DEPTH = 128;

    private final Map<String, Map<String, OctaveGenerator>> octaveCache = new HashMap<String, Map<String, OctaveGenerator>>();

    private static double coordinateScale = 684.412d;
    private static double heightScale = 2053.236d;
    private static double heightNoiseScaleX = 100.0d; // depthNoiseScaleX
    private static double heightNoiseScaleZ = 100.0d; // depthNoiseScaleZ
    private static double detailNoiseScaleX = 80.0d;  // mainNoiseScaleX
    private static double detailNoiseScaleY = 60.0d;  // mainNoiseScaleY
    private static double detailNoiseScaleZ = 80.0d;  // mainNoiseScaleZ
    private static double surfaceScale = 0.0625d;

    private final double[][][] density = new double[5][5][17];

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    private Random random;
    private final List<Populator> populators = new ArrayList<Populator>();
    private List<Populator> generationPopulators = new ArrayList<Populator>();

    private long localSeed1;
    private long localSeed2;

    public Nether() {
        this(new HashMap<String, Object>());
    }

    public Nether(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public int getId() {
        return Generator.TYPE_NETHER;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_NETHER;
    }

    @Override
    public String getName() {
        return "nether";
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
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.random = new Random();
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();

        PopulatorLava lava = new PopulatorLava();
        lava.setBaseAmount(1);
        lava.setRandomAmount(2);
        this.populators.add(lava);

        PopulatorGroundFire groundFire = new PopulatorGroundFire();
        groundFire.setBaseAmount(1);
        groundFire.setRandomAmount(1);
        this.populators.add(groundFire);

        PopulatorOre ores = new PopulatorOre(Block.NETHERRACK);
        ores.setOreTypes(new OreType[]{
                new OreType(new BlockLava(), 32, 1, 0, 32, Block.NETHERRACK),
                new OreType(new BlockOreQuartz(), 13, 16, 10, 118, Block.NETHERRACK),
                new OreType(new BlockMagma(), 32, 16, 26, 37, Block.NETHERRACK),
        });
        this.populators.add(ores);

        this.populators.add(new PopulatorGlowStone());
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        int cx = chunkX << 4;
        int cz = chunkZ << 4;
        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());

        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        int densityX = chunkX << 2;
        int densityZ = chunkZ << 2;

        Map<String, OctaveGenerator> octaves = this.getWorldOctaves();
        double[] heightNoise = ((PerlinOctaveGenerator) octaves.get("height")).getFractalBrownianMotion(densityX, densityZ, 0.5D, 2.0D);
        double[] roughnessNoise = ((PerlinOctaveGenerator) octaves.get("roughness")).getFractalBrownianMotion(densityX, 0, densityZ, 0.5D, 2.0D);
        double[] roughnessNoise2 = ((PerlinOctaveGenerator) octaves.get("roughness2")).getFractalBrownianMotion(densityX, 0, densityZ, 0.5D, 2.0D);
        double[] detailNoise = ((PerlinOctaveGenerator) octaves.get("detail")).getFractalBrownianMotion(densityX, 0, densityZ, 0.5D, 2.0D);

        double[] nv = new double[17];
        for (int i = 0; i < 17; i++) {
            nv[i] = Math.cos(i * Math.PI * 6.0D / 17.0D) * 2.0D;
            double nh = i > 17 / 2 ? 17 - 1 - i : i;
            if (nh < 4.0D) {
                nh = 4.0D - nh;
                nv[i] -= nh * nh * nh * 10.0D;
            }
        }

        int index = 0;
        int indexHeight = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                double noiseH = heightNoise[indexHeight++] / 8000.0D;
                if (noiseH < 0) {
                    noiseH = Math.abs(noiseH);
                }
                noiseH = noiseH * 3.0D - 3.0D;
                if (noiseH < 0) {
                    noiseH = Math.max(noiseH * 0.5D, -1) / 1.4D * 0.5D;
                } else {
                    noiseH = Math.min(noiseH, 1) / 6.0D;
                }

                noiseH = noiseH * 17 / 16.0D;
                for (int k = 0; k < 17; k++) {
                    double noiseR = roughnessNoise[index] / 512.0D;
                    double noiseR2 = roughnessNoise2[index] / 512.0D;
                    double noiseD = (detailNoise[index] / 10.0D + 1.0D) / 2.0D;
                    double nh = nv[k];
                    // linear interpolation
                    double dens = noiseD < 0 ? noiseR : noiseD > 1 ? noiseR2 : noiseR + (noiseR2 - noiseR) * noiseD;
                    dens -= nh;
                    index++;
                    if (k > 13) {
                        double lowering = (k - 13) / 3.0D;
                        dens = dens * (1.0D - lowering) + lowering * -10.0D;
                    }
                    this.density[i][j][k] = dens;
                }
            }
        }

        for (int i = 0; i < 5 - 1; i++) {
            for (int j = 0; j < 5 - 1; j++) {
                for (int k = 0; k < 17 - 1; k++) {
                    double d1 = this.density[i][j][k];
                    double d2 = this.density[i + 1][j][k];
                    double d3 = this.density[i][j + 1][k];
                    double d4 = this.density[i + 1][j + 1][k];
                    double d5 = (this.density[i][j][k + 1] - d1) / 8;
                    double d6 = (this.density[i + 1][j][k + 1] - d2) / 8;
                    double d7 = (this.density[i][j + 1][k + 1] - d3) / 8;
                    double d8 = (this.density[i + 1][j + 1][k + 1] - d4) / 8;

                    for (int l = 0; l < 8; l++) {
                        double d9 = d1;
                        double d10 = d3;
                        for (int m = 0; m < 4; m++) {
                            double dens = d9;
                            for (int n = 0; n < 4; n++) {
                                // any density higher than 0 is ground, any density lower or equal to 0 is air (or lava if under the lava level).
                                if (dens > 0) {
                                    chunk.setBlock(m + (i << 2), l + (k << 3), n + (j << 2), Block.NETHERRACK);
                                } else if (l + (k << 3) < 32) {
                                    chunk.setBlock(m + (i << 2), l + (k << 3), n + (j << 2), Block.STILL_LAVA);
                                    chunk.setBlockLight(m + (i << 2), l + (k << 3) + 1, n + (j << 2), 15);
                                }
                                // interpolation along z
                                dens += (d10 - d9) / 4;
                            }
                            // interpolation along x
                            d9 += (d2 - d1) / 4;
                            // interpolate along z
                            d10 += (d4 - d3) / 4;
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

        double[] surfaceNoise = ((PerlinOctaveGenerator) getWorldOctaves().get("surface")).getFractalBrownianMotion(cx, cz, 0, 0.5D, 2.0D);
        double[] soulsandNoise = ((PerlinOctaveGenerator) getWorldOctaves().get("soulsand")).getFractalBrownianMotion(cx, cz, 0, 0.5D, 2.0D);
        double[] gravelNoise = ((PerlinOctaveGenerator) getWorldOctaves().get("gravel")).getFractalBrownianMotion(cx, 0, cz, 0.5D, 2.0D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk.setBiomeId(x, z, EnumBiome.HELL.biome.getId());

                int columnX = (cx + x) & 0xF;
                int columnZ = (cz + z) & 0xF;

                int topMat = Block.NETHERRACK;
                int groundMat = Block.NETHERRACK;

                boolean soulSand = soulsandNoise[x | z << 4] + this.nukkitRandom.nextDouble() * 0.2D > 0;
                boolean gravel = gravelNoise[x | z << 4] + this.nukkitRandom.nextDouble() * 0.2D > 0;

                int surfaceHeight = (int) (surfaceNoise[x | z << 4] / 3.0D + 3.0D + this.nukkitRandom.nextDouble() * 0.25D);
                int deep = -1;
                for (int y = 127; y >= 0; y--) {
                    if (y <= this.nukkitRandom.nextBoundedInt(5) || y >= 127 - this.nukkitRandom.nextBoundedInt(5)) {
                        chunk.setBlock(columnX, y, columnZ, Block.BEDROCK);
                        continue;
                    }
                    int mat = chunk.getBlockId(columnX, y, columnZ);
                    if (mat == Block.AIR) {
                        deep = -1;
                    } else if (mat == Block.NETHERRACK) {
                        if (deep == -1) {
                            if (surfaceHeight <= 0) {
                                topMat = Block.AIR;
                                groundMat = Block.NETHERRACK;
                            } else if (y >= 60 && y <= 65) {
                                topMat = Block.NETHERRACK;
                                groundMat = Block.NETHERRACK;
                                if (gravel) {
                                    topMat = Block.GRAVEL;
                                    groundMat = Block.NETHERRACK;
                                }
                                if (soulSand) {
                                    topMat = Block.SOUL_SAND;
                                    groundMat = Block.SOUL_SAND;
                                }
                            }

                            deep = surfaceHeight;
                            if (y >= 63) {
                                chunk.setBlock(columnX, y, columnZ, topMat);
                            } else {
                                chunk.setBlock(columnX, y, columnZ, groundMat);
                            }
                        } else if (deep > 0) {
                            deep--;
                            chunk.setBlock(columnX, y, columnZ, groundMat);
                        }
                    }
                }
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

        Biome biome = EnumBiome.getBiome(chunk.getBiomeId(7, 7));
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.5, 64, 0.5);
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

            OctaveGenerator gen = new PerlinOctaveGenerator(seed, 16, 5, 5);
            gen.setXScale(heightNoiseScaleX);
            gen.setZScale(heightNoiseScaleZ);
            octaves.put("height", gen);

            gen = new PerlinOctaveGenerator(seed, 16, 5, 17, 5);
            gen.setXScale(coordinateScale);
            gen.setYScale(heightScale);
            gen.setZScale(coordinateScale);
            octaves.put("roughness", gen);

            gen = new PerlinOctaveGenerator(seed, 16, 5, 17, 5);
            gen.setXScale(coordinateScale);
            gen.setYScale(heightScale);
            gen.setZScale(coordinateScale);
            octaves.put("roughness2", gen);

            gen = new PerlinOctaveGenerator(seed, 8, 5, 17, 5);
            gen.setXScale(coordinateScale / detailNoiseScaleX);
            gen.setYScale(heightScale / detailNoiseScaleY);
            gen.setZScale(coordinateScale / detailNoiseScaleZ);
            octaves.put("detail", gen);

            gen = new PerlinOctaveGenerator(seed, 4, 16, 16, 1);
            gen.setScale(surfaceScale);
            octaves.put("surface", gen);

            gen = new PerlinOctaveGenerator(seed, 4, 16, 16, 1);
            gen.setXScale(surfaceScale / 2.0);
            gen.setYScale(surfaceScale / 2.0);
            octaves.put("soulsand", gen);

            gen = new PerlinOctaveGenerator(seed, 4, 16, 1, 16);
            gen.setXScale(surfaceScale / 2.0);
            gen.setZScale(surfaceScale / 2.0);
            octaves.put("gravel", gen);

            this.octaveCache.put(this.getName(), octaves);
            return octaves;
        }
        return this.octaveCache.get(this.getName());
    }
}
