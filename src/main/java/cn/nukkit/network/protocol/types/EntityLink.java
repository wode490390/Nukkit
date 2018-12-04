package cn.nukkit.network.protocol.types;

public class EntityLink {

    public static final int TYPE_REMOVE = 0;
    public static final int TYPE_RIDER = 1;
    public static final int TYPE_PASSENGER = 2;

    public long fromEntityUniqueId;
    public long toEntityUniqueId;
    public int type;
    public boolean immediate; //for dismounting on mount death

    public EntityLink(long fromEntityUniqueId, long toEntityUniqueId, int type) {
        this(fromEntityUniqueId, toEntityUniqueId, type, false)
    }

    public EntityLink(long fromEntityUniqueId, long toEntityUniqueId, int type, boolean immediate) {
        this.fromEntityUniqueId = fromEntityUniqueId;
        this.toEntityUniqueId = toEntityUniqueId;
        this.type = type;
        this.immediate = immediate;
    }
}
