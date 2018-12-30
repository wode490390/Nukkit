package cn.nukkit.network.protocol;

public class EventPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.EVENT_PACKET;

    public static final int TYPE_ACHIEVEMENT_AWARDED = 0;
    public static final int TYPE_ENTITY_INTERACT = 1;
    public static final int TYPE_PORTAL_BUILT = 2;
    public static final int TYPE_PORTAL_USED = 3;
    public static final int TYPE_MOB_KILLED = 4;
    public static final int TYPE_CAULDRON_USED = 5;
    public static final int TYPE_PLAYER_DEATH = 6;
    public static final int TYPE_BOSS_KILLED = 7;
    public static final int TYPE_AGENT_COMMAND = 8;
    public static final int TYPE_AGENT_CREATED = 9;
    public static final int TYPE_PATTERN_REMOVED = 10; //???
    public static final int TYPE_COMMANED_EXECUTED = 11;
    public static final int TYPE_FISH_BUCKETED = 12;

    public long playerRuntimeId;
    public int eventData;
    public byte type;

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
        this.putEntityRuntimeId(this.playerRuntimeId);
        this.putVarInt(this.eventData);
        this.putByte(this.type);

        //TODO: nice confusing mess
    }
}
