package cn.nukkit.level.generator.task;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseChunk;
import cn.nukkit.scheduler.AsyncTask;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class LightPopulationTask extends AsyncTask {

    protected Level level;

    protected BaseChunk chunk;

    public LightPopulationTask(Level level, BaseChunk chunk) {
        this.level = level;
        this.chunk = chunk.clone();
    }

    @Override
    public void onRun() {
        chunk.recalculateHeightMap();
        chunk.populateSkyLight();
        chunk.setLightPopulated();
    }

    @Override
    public void onCompletion(Server server) {
        if (!level.isClosed()) {
            level.generateChunkCallback(chunk.getX(), chunk.getZ(), chunk);
        }
    }
}
