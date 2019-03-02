package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class SimpleChunkManager implements ChunkManager {

    protected long seed;

    public SimpleChunkManager(long seed) {
        this.seed = seed;
    }

    @Override
    public int getBlockIdAt(int x, int y, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockId(x & 0xf, y & 0xff, z & 0xf);
        }
        return 0;
    }

    @Override
    public void setBlockIdAt(int x, int y, int z, int id) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockId(x & 0xf, y & 0xff, z & 0xf, id);
        }
    }

    @Override
    public void setBlockAt(int x, int y, int z, int id, int data) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlock(x & 0xf, y & 0xff, z & 0xf, id, data);
        }
    }


    @Override
    public void setBlockFullIdAt(int x, int y, int z, int fullId) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setFullBlockId(x & 0xf, y & 0xff, z & 0xf, fullId);
        }
    }

    @Override
    public int getBlockDataAt(int x, int y, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockData(x & 0xf, y & 0xff, z & 0xf);
        }
        return 0;
    }

    @Override
    public void setBlockDataAt(int x, int y, int z, int data) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockData(x & 0xf, y & 0xff, z & 0xf, data);
        }
    }

    @Override
    public void setBlockLightAt(int x, int y, int z, int level) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockLight(x & 0xf, y & 0xff, z & 0xf, level);
        }
    }

    @Override
    public int getBlockLightAt(int x, int y, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockLight(x & 0xf, y & 0xff, z & 0xf);
        }

        return 0;
    }

    @Override
    public void setBlockSkyLightAt(int x, int y, int z, int level) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockSkyLight(x & 0xf, y & 0xff, z & 0xf, level);
        }
    }

    @Override
    public int getBlockSkyLightAt(int x, int y, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockSkyLight(x & 0xf, y & 0xff, z & 0xf);
        }
        return 0;
    }

    @Override
    public void setChunk(int chunkX, int chunkZ) {
        this.setChunk(chunkX, chunkZ, null);
    }

    @Override
    public int getHeightMap(int x, int z) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getHeightMap(x & 0x0f, z & 0x0f);
        }
        return 0;
    }

    @Override
    public void setHeightMap(int x, int z, int value) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setHeightMap(x & 0x0f, z & 0x0f, value);
        }
    }

    @Override
    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void cleanChunks(long seed) {
        this.seed = seed;
    }
}
