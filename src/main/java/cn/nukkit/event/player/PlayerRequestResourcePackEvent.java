package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.resourcepacks.ResourcePack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRequestResourcePackEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final Map<UUID, ResourcePack> resourcePacks = new HashMap<>();
    private final Map<UUID, ResourcePack> behaviourPacks = new HashMap<>();
    private boolean mustAccept;

    public PlayerRequestResourcePackEvent(Player player, Map<UUID, ResourcePack> resourcePacks, Map<UUID, ResourcePack> behaviourPacks, boolean mustAccept) {
        this.player = player;
        this.resourcePacks.putAll(resourcePacks);
        this.behaviourPacks.putAll(behaviourPacks);
        this.mustAccept = mustAccept;
    }

    public Map<UUID, ResourcePack> getResourcePacks() {
        return resourcePacks;
    }

    public void putResourcePack(ResourcePack... pack) {
        for (ResourcePack p : pack) {
            this.resourcePacks.put(p.getPackId(), p);
        }
    }

    public Map<UUID, ResourcePack> getBehaviourPacks() {
        return behaviourPacks;
    }

    public void putBehaviourPack(ResourcePack... pack) {
        for (ResourcePack p : pack) {
            this.behaviourPacks.put(p.getPackId(), p);
        }
    }

    public boolean isMustAccept() {
        return mustAccept;
    }

    public PlayerRequestResourcePackEvent setMustAccept(boolean mustAccept) {
        this.mustAccept = mustAccept;
        return this;
    }
}
