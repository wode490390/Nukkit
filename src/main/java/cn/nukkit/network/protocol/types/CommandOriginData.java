package cn.nukkit.network.protocol.types;


import java.util.OptionalLong;
import java.util.UUID;

/**
 * @author SupremeMortal
 * Nukkit project
 */
public final class CommandOriginData {

    public static final int ORIGIN_PLAYER = 0;
    public static final int ORIGIN_BLOCK = 1;
    public static final int ORIGIN_MINECART_BLOCK = 2;
    public static final int ORIGIN_DEV_CONSOLE = 3;
    public static final int ORIGIN_TEST = 4;
    public static final int ORIGIN_AUTOMATION_PLAYER = 5;
    public static final int ORIGIN_CLIENT_AUTOMATION = 6;
    public static final int ORIGIN_DEDICATED_SERVER = 7;
    public static final int ORIGIN_ENTITY = 8;
    public static final int ORIGIN_VIRTUAL = 9;
    public static final int ORIGIN_GAME_ARGUMENT = 10;
    public static final int ORIGIN_ENTITY_SERVER = 11; //???

    public int type;
    public UUID uuid;

    public String requestId;

    public long varlong1;

    /*public final Origin type;
    public final UUID uuid;
    public final String requestId;
    private final Long varlong;

    public CommandOriginData(Origin type, UUID uuid, String requestId, Long varlong) {
        this.type = type;
        this.uuid = uuid;
        this.requestId = requestId;
        this.varlong = varlong;
    }

    public OptionalLong getVarLong() {
        if (varlong == null) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(varlong);
    }

    public enum Origin {
        PLAYER,
        BLOCK,
        MINECART_BLOCK,
        DEV_CONSOLE,
        TEST,
        AUTOMATION_PLAYER,
        CLIENT_AUTOMATION,
        DEDICATED_SERVER,
        ENTITY,
        VIRTUAL,
        GAME_ARGUMENT,
        ENTITY_SERVER
    }*/
}
