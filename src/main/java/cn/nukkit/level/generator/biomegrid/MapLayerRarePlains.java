package cn.nukkit.level.generator.biomegrid;

import cn.nukkit.level.biome.EnumBiome;

import java.util.HashMap;
import java.util.Map;

public class MapLayerRarePlains extends MapLayer {

    private static final Map<Integer, Integer> RARE_PLAINS = new HashMap<Integer, Integer>();

    static {
        RARE_PLAINS.put(EnumBiome.PLAINS.id, EnumBiome.SUNFLOWER_PLAINS.id);
    }

    private final MapLayer belowLayer;

    public MapLayerRarePlains(long seed, MapLayer belowLayer) {
        super(seed);
        this.belowLayer = belowLayer;
    }

    @Override
    public int[] generateValues(int x, int z, int sizeX, int sizeZ) {
        int gridX = x - 1;
        int gridZ = z - 1;
        int gridSizeX = sizeX + 2;
        int gridSizeZ = sizeZ + 2;

        int[] values = this.belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ);

        int[] finalValues = new int[sizeX * sizeZ];
        for (int i = 0; i < sizeZ; i++) {
            for (int j = 0; j < sizeX; j++) {
                setCoordsSeed(x + j, z + i);
                int centerValue = values[j + 1 + (i + 1) * gridSizeX];
                if (nextInt(57) == 0 && RARE_PLAINS.containsKey(centerValue)) {
                    centerValue = RARE_PLAINS.get(centerValue);
                }
                finalValues[j + i * sizeX] = centerValue;
            }
        }
        return finalValues;
    }
}
