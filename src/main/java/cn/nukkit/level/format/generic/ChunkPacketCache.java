package cn.nukkit.level.format.generic;

import cn.nukkit.network.protocol.BatchPacket;

public class ChunkPacketCache {

    private final BatchPacket packet;
    private final BatchPacket packetOld;

    public ChunkPacketCache(BatchPacket packet, BatchPacket packetOld) {
        this.packet = packet;
        this.packetOld = packetOld;
    }

    public BatchPacket getPacket() {
        return packet;
    }

    public BatchPacket getPacketOld() {
        return packetOld;
    }

    public void compress() {
        this.packet.trim();
        this.packetOld.trim();
    }
}
