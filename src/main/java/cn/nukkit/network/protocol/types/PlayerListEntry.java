package cn.nukkit.network.protocol.types;

import cn.nukkit.entity.data.Skin;

import java.util.UUID;

public class PlayerListEntry {

    public UUID uuid;
    public long entityUniqueId;
    public String username;
    public Skin skin;
    public String xboxUserId;
    public String platformChatId;

    public PlayerListEntry(UUID uuid, int entityUniqueId, String username, Skin skin, String xboxUserId, String platformChatId) {
        this.uuid = uuid;
        this.entityUniqueId = entityUniqueId;
        this.username = username;
        this.skin = skin;
        this.xboxUserId = xboxUserId;
        this.platformChatId = platformChatId;
    }

    public static PlayerListEntry createAdditionEntry(UUID uuid, int entityUniqueId, String username, Skin skin) {
        return createAdditionEntry(uuid, entityUniqueId, username, skin, "");
    }

    public static PlayerListEntry createAdditionEntry(UUID uuid, int entityUniqueId, String username, Skin skin, String xboxUserId) {
        return createAdditionEntry(uuid, entityUniqueId, username, skin, xboxUserId, "");
    }

    public static PlayerListEntry createAdditionEntry(UUID uuid, int entityUniqueId, String username, Skin skin, String xboxUserId, String platformChatId) {
        return new PlayerListEntry(uuid, entityUniqueId, username, skin, xboxUserId, platformChatId);
    }

    public static PlayerListEntry createRemovalEntry(UUID uuid) {
        return new PlayerListEntry(uuid, -1, null, null, "", "");
    }
}
