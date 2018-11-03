package cn.nukkit.network.protocol.types;

public class ScorePacketEntry {

    public static final int TYPE_PLAYER = 1;
    public static final int TYPE_ENTITY = 2;
    public static final int TYPE_FAKE_PLAYER = 3;

    public int scoreboardId;
    public String objectiveName;
    public int score;

    public int type;

    public long entityUniqueId; //if type entity or player
    public String customName; //if type fake player
}