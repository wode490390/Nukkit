package cn.nukkit.level.light;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.anvil.Chunk;
import cn.nukkit.scheduler.AsyncTask;

/**
 * Created by CreeperFace on 22.7.2017.
 */
public class ChunkLightPopulator extends AsyncTask {

    protected Level level;

    protected Chunk chunk;

    protected LightUpdate blockLightUpdates = null;
    protected LightUpdate skyLightUpdates = null;

    public ChunkLightPopulator(Level level, Chunk chunk) {
        this.level = level;
        this.chunk = (Chunk) chunk.clone();
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
