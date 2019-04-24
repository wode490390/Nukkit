package cn.nukkit.network.protocol.types;

import lombok.ToString;

@ToString
public class MapTrackedObject {

    public static final int TYPE_ENTITY = 0;
    public static final int TYPE_BLOCK = 1;

    public int type;

    public long entityUniqueId; //Only set if is TYPE_ENTITY

    //Only set if is TYPE_BLOCK
    public int x;
    public int y;
    public int z;
}
