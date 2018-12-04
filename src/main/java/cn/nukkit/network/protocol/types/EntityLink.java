package cn.nukkit.network.protocol.types;

public class EntityLink {

    public static final int TYPE_REMOVE = 0;
    public static final int TYPE_RIDER = 1;
    public static final int TYPE_PASSENGER = 2;

    public long fromEntityUniqueId = 0;
    public long toEntityUniqueId = 0;
    public int type = 0;
    public boolean immediate = false; //for dismounting on mount death

    public EntityLink() {
        this(0)
    }

    public EntityLink(long fromEntityUniqueId) {
        this(fromEntityUniqueId, 0)
    }

    public EntityLink(long fromEntityUniqueId, long toEntityUniqueId) {
        this(fromEntityUniqueId, toEntityUniqueId, 0)
    }

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
