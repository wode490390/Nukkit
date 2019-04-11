package cn.nukkit.network.protocol.types;

import cn.nukkit.entity.data.Skin;

import java.util.UUID;

public class PlayerListEntry {

    public final UUID uuid;
    public long entityUniqueId = 0;
    public String username = "";
    public Skin skin = new Skin();
    public String xboxUserId = "";
    public String platformChatId = "";

    public PlayerListEntry(UUID uuid) { //createRemovalEntry
        this.uuid = uuid;
    }

    public PlayerListEntry(UUID uuid, long entityUniqueId, String username, Skin skin) {
        this(uuid, entityUniqueId, username, skin, "");
    }

    public PlayerListEntry(UUID uuid, long entityUniqueId, String username, Skin skin, String xboxUserId) {
        this(uuid, entityUniqueId, username, skin, xboxUserId, "");
    }

    public PlayerListEntry(UUID uuid, long entityUniqueId, String username, Skin skin, String xboxUserId, String platformChatId) { //createAdditionEntry
        this.uuid = uuid;
        this.entityUniqueId = entityUniqueId;
        this.username = username;
        this.skin = skin;
        this.xboxUserId = xboxUserId != null ? xboxUserId : "";
        this.platformChatId = platformChatId != null ? platformChatId : "";
    }
}
