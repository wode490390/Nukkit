package cn.nukkit.level.generator.task;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.scheduler.AsyncTask;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class LightPopulationTask extends AsyncTask {

    private final Level level;
    public BaseFullChunk chunk;

    public LightPopulationTask(Level level, BaseFullChunk chunk) {
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
