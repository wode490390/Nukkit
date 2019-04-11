package cn.nukkit.level.light;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.scheduler.AsyncTask;

/**
 * Created by CreeperFace on 22.7.2017.
 */
public class ChunkLightPopulator extends AsyncTask {

    private final Level level;
    protected BaseFullChunk chunk;

    protected LightUpdate blockLightUpdates;
    protected LightUpdate skyLightUpdates;

    public ChunkLightPopulator(Level level, BaseFullChunk chunk) {
        this.level = level;
        this.chunk = chunk;
    }

    @Override
    public void onRun() {
        BaseFullChunk chunk = this.chunk.clone();
        if (chunk == null) {
            return;
        }

        chunk.recalculateHeightMap();
        chunk.populateSkyLight();
        chunk.setLightPopulated();

        this.chunk = chunk.clone();
    }

    @Override
    public void onCompletion(Server server) {
        BaseFullChunk chunk = this.chunk.clone();
        if (this.level != null) {
            if (chunk == null) {
                return;
            }

            this.level.generateChunkCallback(chunk.getX(), chunk.getZ(), chunk);
        }
    }
}
