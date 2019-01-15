package cn.nukkit.level.light;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.SubChunkIteratorManager;
import cn.nukkit.math.BlockVector3;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * author: dktapps
 */
public abstract class LightUpdate {

    protected ChunkManager level;

    protected Object2IntOpenHashMap<BlockVector3> updateNodes = new Object2IntOpenHashMap<BlockVector3>();

    protected Queue<BlockVector3> spreadQueue;

    protected Set<BlockVector3> spreadVisited = new HashSet<>();

    protected Queue<Entry> removalQueue;

    protected Set<BlockVector3> removalVisited = new HashSet<>();

    protected SubChunkIteratorManager subChunkHandler;

    public LightUpdate(ChunkManager level) {
        this.level = level;
        this.removalQueue = new ArrayDeque<>();
        this.spreadQueue = new ArrayDeque<>();

        this.subChunkHandler = new SubChunkIteratorManager(this.level);
    }

    abstract protected int getLight(int x, int y, int z);

    abstract protected void setLight(int x, int y, int z, int level);

    public void setAndUpdateLight(int x, int y, int z, int newLevel) {
        this.updateNodes.put(new BlockVector3(x, y, z), newLevel);
    }

    private void prepareNodes() {
        for (Map.Entry<BlockVector3, Integer> entry : updateNodes.entrySet()) {
            BlockVector3 pos = entry.getKey();
            int newLevel = entry.getValue();

            if (this.subChunkHandler.moveTo(pos.x, pos.y, pos.z)) {
                int oldLevel = this.getLight(pos.x, pos.y, pos.z);

                if (oldLevel != newLevel) {
                    this.setLight(pos.x, pos.y, pos.z, newLevel);

                    if (oldLevel < newLevel) {
                        this.spreadVisited.add(pos);
                        this.spreadQueue.add(pos);
                    } else { //light removed
                        this.removalVisited.add(pos);
                        this.removalQueue.add(new Entry(pos, oldLevel));
                    }
                }
            }
        }
    }

    public void execute() {
        prepareNodes();

        while (!this.removalQueue.isEmpty()) {
            Entry entry = this.removalQueue.poll();

            int x = entry.pos.x;
            int y = entry.pos.y;
            int z = entry.pos.z;

            int[][] points = new int[][]{
                    {x + 1, y, z},
                    {x - 1, y, z},
                    {x, y + 1, z},
                    {x, y - 1, z},
                    {x, y, z + 1},
                    {x, y, z - 1}
            };

            for (int[] i : points) {
                if (this.subChunkHandler.moveTo(i[0], i[1], i[2])) {
                    this.computeRemoveLight(i[0], i[1], i[2], entry.oldLight);
                }
            }
        }

        while (!spreadQueue.isEmpty()) {
            BlockVector3 pos = this.spreadQueue.poll();
            int x = pos.x;
            int y = pos.y;
            int z = pos.z;

            if (!this.subChunkHandler.moveTo(x, y, z)) {
                continue;
            }

            int newAdjacentLight = this.getLight(x, y, z);
            if (newAdjacentLight <= 0) {
                continue;
            }

            int[][] points = new int[][]{
                    {x + 1, y, z},
                    {x - 1, y, z},
                    {x, y + 1, z},
                    {x, y - 1, z},
                    {x, y, z + 1},
                    {x, y, z - 1}
            };

            for (int[] i : points) {
                if (this.subChunkHandler.moveTo(i[0], i[1], i[2])) {
                    this.computeSpreadLight(i[0], i[1], i[2], newAdjacentLight);
                }
            }
        }
    }

    protected void computeRemoveLight(int x, int y, int z, int oldAdjacentLevel) {
        int current = this.getLight(x, y, z);

        BlockVector3 index;
        if (current != 0 && current < oldAdjacentLevel) {
            this.setLight(x, y, z, 0);

            if (!removalVisited.contains(index = new BlockVector3(x, y, z))) {
                removalVisited.add(index);
                if (current > 1) {
                    this.removalQueue.add(new Entry(new BlockVector3(x, y, z), current));
                }
            }
        } else if (current >= oldAdjacentLevel) {
            if (!spreadVisited.contains(index = new BlockVector3(x, y, z))) {
                spreadVisited.add(index);
                spreadQueue.add(new BlockVector3(x, y, z));
            }
        }
    }

    protected void computeSpreadLight(int x, int y, int z, int newAdjacentLevel) {
        int current = this.getLight(x, y, z);
        int potentialLight = newAdjacentLevel - Block.lightFilter[this.subChunkHandler.currentSection.getBlockId(x & 0x0f, y & 0x0f, z & 0x0f)];

        if (current < potentialLight) {
            this.setLight(x, y, z, potentialLight);

            BlockVector3 index;
            if (!spreadVisited.contains(index = new BlockVector3(x, y, z))) {
                spreadVisited.add(index);
                spreadQueue.add(new BlockVector3(x, y, z));
            }
        }
    }

    private class Entry {

        public BlockVector3 pos;

        public Integer oldLight;

        public Entry(BlockVector3 pos) {
            this(pos, null);
        }

        public Entry(BlockVector3 pos, Integer light) {
            this.pos = pos;
            this.oldLight = light;
        }
    }
}
