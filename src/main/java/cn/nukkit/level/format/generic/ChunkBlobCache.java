package cn.nukkit.level.format.generic;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ChunkBlobCache {

    private final int subChunkCount;
    private final long[] blobIds;
    private final Long2ObjectOpenHashMap<byte[]> clientBlobs;
    private final byte[] clientBlobCachedPayload;

    public ChunkBlobCache(int subChunkCount, long[] blobIds, Long2ObjectOpenHashMap<byte[]> clientBlobs, byte[] clientBlobCachedPayload) {
        this.subChunkCount = subChunkCount;
        this.blobIds = blobIds;
        this.clientBlobs = clientBlobs;
        this.clientBlobCachedPayload = clientBlobCachedPayload;
    }

    public int getSubChunkCount() {
        return subChunkCount;
    }

    public long[] getBlobIds() {
        return blobIds;
    }

    public Long2ObjectOpenHashMap<byte[]> getClientBlobs() {
        return clientBlobs;
    }

    public byte[] getClientBlobCachedPayload() {
        return clientBlobCachedPayload;
    }
}
