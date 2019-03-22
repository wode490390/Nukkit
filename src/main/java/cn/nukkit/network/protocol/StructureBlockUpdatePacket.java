package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;

public class StructureBlockUpdatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET;

    public static final int TYPE_SAVE = 1;
    public static final int TYPE_LOAD = 2;

    public int x;
    public int z;
    public int y;
    public int structureType;

    public String customName;
    public String metadata;
    public int offsetX;
    public int offsetY;
    public int offsetZ;
    public int sizeX;
    public int sizeY;
    public int sizeZ;
    public boolean includingEntities;
    public boolean ignoringBlocks;
    public boolean includingPlayers;
    public boolean showingAir;

    public float integrity;
    public int seed;
    public int mirror;
    public int rotation;
    public boolean ignoringEntities;
    public boolean ignoringStructureBlocks;
    public Vector3f bbMin;
    public Vector3f bbMax;

    public boolean boundingBoxVisible;
    public boolean powered;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockPosition(this.x, this.y, this.z);
        this.putUnsignedVarInt(this.structureType);

        this.putString(this.customName);
        this.putString(this.metadata);
        this.putBlockPosition(this.offsetX, this.offsetY, this.offsetZ);
        this.putBlockPosition(this.sizeX, this.sizeY, this.sizeZ);
        this.putBoolean(this.includingEntities);
        this.putBoolean(this.ignoringBlocks);
        this.putBoolean(this.includingPlayers);
        this.putBoolean(this.showingAir);

        this.putLFloat(this.integrity);
        this.putUnsignedVarInt(this.seed);
        this.putUnsignedVarInt(this.mirror);
        this.putUnsignedVarInt(this.rotation);
        this.putBoolean(this.ignoringEntities);
        this.putBoolean(this.ignoringStructureBlocks);
        this.putVector3(this.bbMin);
        this.putVector3(this.bbMax);

        this.putBoolean(this.boundingBoxVisible);
        this.putBoolean(this.powered);
    }
}
