package cn.nukkit.network.protocol.types;

import lombok.ToString;

@ToString
public class ScoreboardIdentityPacketEntry {

    public long scoreboardId;
    public long entityUniqueId;
}
