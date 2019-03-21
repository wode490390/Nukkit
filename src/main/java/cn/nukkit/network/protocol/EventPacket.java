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
    public static final int TYPE_PATTERN_REMOVED = 10;
    public static final int TYPE_COMMANED_EXECUTED = 11;
    public static final int TYPE_FISH_BUCKETED = 12;

    public long playerRuntimeId;
    public int eventData;
    public byte type;

    public int id;
    public int cause;
    public int unknown0;
    public short unknown1;
    public long mobEntityId;
    public long unknownEid;
    public String unknown2;
    public String unknown3;
    public String unknown4;
    public int unknown5;
    public int unknown6;
    public String unknown7;
    public boolean unknown8;

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

        switch (this.type) {
            case TYPE_ACHIEVEMENT_AWARDED:
            case TYPE_PORTAL_BUILT:
                this.putVarInt(this.id);
                break;
            case TYPE_ENTITY_INTERACT:
                this.putVarInt(this.cause);
                this.putVarInt(this.id);
                this.putVarInt(this.unknown0);
                this.putLShort(this.unknown1);
                break;
            case TYPE_PORTAL_USED:
            case TYPE_PLAYER_DEATH:
                this.putVarInt(this.id);
                this.putVarInt(this.cause);
                break;
            case TYPE_MOB_KILLED:
                this.putEntityUniqueId(this.unknownEid);
                this.putEntityUniqueId(this.mobEntityId);
                this.putVarInt(this.cause);
                break;
            case TYPE_CAULDRON_USED:
                this.putUnsignedVarInt(this.cause);
                this.putVarInt(this.id);
                this.putVarInt(this.unknown0);
                break;
            case TYPE_BOSS_KILLED:
                this.putEntityUniqueId(this.mobEntityId);
                this.putVarInt(this.id);
                this.putVarInt(this.cause);
                break;
            case TYPE_AGENT_COMMAND:
                this.putVarInt(this.id);
                this.putVarInt(this.cause);
                this.putString(this.unknown2);
                this.putString(this.unknown3);
                this.putString(this.unknown4);
                break;
            case TYPE_PATTERN_REMOVED:
                this.putVarInt(this.id);
                this.putVarInt(this.cause);
                this.putVarInt(this.unknown0);
                this.putVarInt(this.unknown5);
                this.putVarInt(this.unknown6);
                break;
            case TYPE_COMMANED_EXECUTED:
                this.putVarInt(this.id);
                this.putVarInt(this.cause);
                this.putString(this.unknown2);
                this.putString(this.unknown7);
                break;
            case TYPE_FISH_BUCKETED:
                this.putVarInt(this.id);
                this.putVarInt(this.cause);
                this.putVarInt(this.unknown0);
                this.putBoolean(this.unknown8);
                break;
        }
    }
}
