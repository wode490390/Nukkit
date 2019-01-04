package cn.nukkit.level;

import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.generic.BaseChunk;
import cn.nukkit.level.format.generic.EmptyChunkSection;

/**
 * @author CreeperFace
 */
public class SubChunkIteratorManager {

    public final ChunkManager level;

    public BaseChunk currentChunk;

    public ChunkSection currentSection;

    protected int currentX;
    protected int currentY;
    protected int currentZ;

    protected boolean allocateEmptySubs = true;

    public SubChunkIteratorManager(ChunkManager level) {
        this(level, true);
    }

    public SubChunkIteratorManager(ChunkManager level, boolean allocateEmptySubs) {
        this.level = level;
        this.allocateEmptySubs = allocateEmptySubs;
    }

    public boolean moveTo(int x, int y, int z) {
        if (this.currentChunk == null || this.currentX != (x >> 4) || this.currentZ != (z >> 4)) {
            this.currentX = x >> 4;
            this.currentZ = z >> 4;
            this.currentSection = null;

            this.currentChunk = (BaseChunk) this.level.getChunk(this.currentX, this.currentZ);
            if (this.currentChunk == null) {
                return false;
            }
        }

        if (this.currentSection == null || this.currentY != (y >> 4)) {
            this.currentY = y >> 4;

            this.currentSection = this.currentChunk.getSection(y >> 4);

            return !(this.currentSection instanceof EmptyChunkSection);
        }

        return true;
    }

    public void invalidate() {
        this.currentChunk = null;
        this.currentSection = null;
    }
}
