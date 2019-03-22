package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;

import com.google.common.collect.ImmutableMap;

public interface EntityJockey {

    ImmutableMap<Integer, Boolean> ZOMBIE_JOCKEY = ImmutableMap.<Integer, Boolean>builder()
            .put(Entity.ZOMBIE, true)
            .put(Entity.ZOMBIE_VILLAGER, true)
            .put(Entity.ZOMBIE_PIGMAN, true)
            .put(Entity.HUSK, true)
            .put(Entity.DROWNED, true)
            .put(Entity.CHICKEN, true)
            .put(Entity.OCELOT, true)
            .put(Entity.CAT, true)
            .put(Entity.WOLF, true)
            .put(Entity.PANDA, true)
            .put(Entity.SPIDER, false)
            .put(Entity.CAVE_SPIDER, false)
            .put(Entity.HORSE, false)
            .put(Entity.MULE, false)
            .put(Entity.DONKEY, false)
            .put(Entity.LLAMA, false)
            .put(Entity.ZOMBIE_HORSE, false)
            .put(Entity.SKELETON_HORSE, false)
            .put(Entity.COW, false)
            .put(Entity.MOOSHROOM, false)
            .put(Entity.SHEEP, false)
            .put(Entity.PIG, false)
            .put(Entity.POLAR_BEAR, false)
            .build();

    ImmutableMap<Integer, Boolean> SKELETON_JOCKEY = ImmutableMap.<Integer, Boolean>builder()
            .put(Entity.SPIDER, false)
            .put(Entity.CAVE_SPIDER, false)
            .build();

    boolean canRideTo(EntityCreature creature);
}
