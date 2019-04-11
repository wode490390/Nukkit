package cn.nukkit.level.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.Populator;
import cn.nukkit.level.generator.populator.overworld.PopulatorOre;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@Log4j2
public class Flat extends Generator {

    protected int[][] structure;
    protected int floorLevel;
    protected String preset;
    protected int biome;

    protected ChunkManager level;
    protected NukkitRandom random;
    protected final List<Populator> populators = new ArrayList<>();
    protected final Map<String, Object> options;

    protected boolean init = false;

    @Override
    public int getId() {
        return TYPE_FLAT;
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public Map<String, Object> getSettings() {
        return this.options;
    }

    @Override
    public String getName() {
        return "flat";
    }

    public Flat() {
        this(new HashMap<>());
    }

    public Flat(Map<String, Object> options) {
        this.preset = "2;7,2x3,2;1;";
        this.options = options;

        if (this.options.containsKey("decoration")) {
            PopulatorOre ores = new PopulatorOre();
            ores.setOreTypes(new OreType[]{
                    new OreType(Block.get(COAL_ORE), 20, 16, 0, 128),
                    new OreType(Block.get(IRON_ORE), 20, 8, 0, 64),
                    new OreType(Block.get(REDSTONE_ORE), 8, 7, 0, 16),
                    new OreType(Block.get(LAPIS_ORE), 1, 6, 0, 32),
                    new OreType(Block.get(GOLD_ORE), 2, 8, 0, 32),
                    new OreType(Block.get(DIAMOND_ORE), 1, 7, 0, 16),
                    new OreType(Block.get(DIRT), 20, 32, 0, 128),
                    new OreType(Block.get(GRAVEL), 20, 16, 0, 128),
            });
            this.populators.add(ores);
        }
    }

    protected void parsePreset(String preset, int chunkX, int chunkZ) {
        try {
            this.preset = preset;
            String[] presetArray = preset.split(";");
            int version = Integer.valueOf(presetArray[0]);
            String blocks = presetArray.length > 1 ? presetArray[1] : "";
            this.biome = presetArray.length > 2 ? Integer.valueOf(presetArray[2]) : 1;
            String options = presetArray.length > 3 ? presetArray[1] : "";
            this.structure = new int[256][];
            int y = 0;
            for (String block : blocks.split(",")) {
                int id, meta = 0, cnt = 1;
                if (Pattern.matches("^[0-9]{1,3}x[0-9]$", block)) {
                    //AxB
                    String[] s = block.split("x");
                    cnt = Integer.valueOf(s[0]);
                    id = Integer.valueOf(s[1]);
                } else if (Pattern.matches("^[0-9]{1,3}:[0-9]{0,2}$", block)) {
                    //A:B
                    String[] s = block.split(":");
                    id = Integer.valueOf(s[0]);
                    meta = Integer.valueOf(s[1]);
                } else if (Pattern.matches("^[0-9]{1,3}$", block)) {
                    //A
                    id = Integer.valueOf(block);
                } else {
                    continue;
                }
                int cY = y;
                y += cnt;
                if (y > 0xFF) {
                    y = 0xFF;
                }
                for (; cY < y; ++cY) {
                    this.structure[cY] = new int[]{id, meta};
                }
            }
            this.floorLevel = y;
            for (; y <= 0xFF; ++y) {
                this.structure[y] = new int[]{0, 0};
            }
            for (String option : options.split(",")) {
                if (Pattern.matches("^[0-9a-z_]+$", option)) {
                    this.options.put(option, true);
                } else if (Pattern.matches("^[0-9a-z_]+\\([0-9a-z_ =]+\\)$", option)) {
                    String name = option.substring(0, option.indexOf('('));
                    String extra = option.substring(option.indexOf('(') + 1, option.indexOf(')'));
                    Map<String, Float> map = new HashMap<>();
                    for (String kv : extra.split(" ")) {
                        String[] data = kv.split("=");
                        map.put(data[0], Float.valueOf(data[1]));
                    }
                    this.options.put(name, map);
                }
            }
        } catch (NumberFormatException e) {
            log.error("error while parsing the preset", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.random = random;
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        if (!this.init) {
            this.init = true;
            if (this.options.containsKey("preset") && !"".equals(this.options.get("preset"))) {
                this.parsePreset((String) this.options.get("preset"), chunkX, chunkZ);
            } else {
                this.parsePreset(this.preset, chunkX, chunkZ);
            }
        }
        this.generateChunk(level.getChunk(chunkX, chunkZ));
    }

    protected void generateChunk(FullChunk chunk) {
        chunk.setGenerated();

        for (int Z = 0; Z < 16; ++Z) {
            for (int X = 0; X < 16; ++X) {
                chunk.setBiomeId(X, Z, biome);

                for (int y = 0; y < 256; ++y) {
                    int k = this.structure[y][0];
                    int l = this.structure[y][1];
                    chunk.setBlock(X, y, Z, this.structure[y][0], this.structure[y][1]);
                }
            }
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        this.random.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.random, chunk);
        }
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(128, this.floorLevel, 128);
    }
}
