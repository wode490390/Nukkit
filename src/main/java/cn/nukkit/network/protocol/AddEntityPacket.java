package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.utils.Binary;

import com.google.common.collect.ImmutableMap;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_ENTITY_PACKET;

    public static ImmutableMap<Integer, String> LEGACY_ID_MAP_BC = ImmutableMap.<Integer, String>builder()
            .put(Entity.NPC, "minecraft:npc")
            .put(Entity.PLAYER, "minecraft:player")
            .put(Entity.WITHER_SKELETON, "minecraft:wither_skeleton")
            .put(Entity.HUSK, "minecraft:husk")
            .put(Entity.STRAY, "minecraft:stray")
            .put(Entity.WITCH, "minecraft:witch")
            .put(Entity.ZOMBIE_VILLAGER, "minecraft:zombie_villager")
            .put(Entity.BLAZE, "minecraft:blaze")
            .put(Entity.MAGMA_CUBE, "minecraft:magma_cube")
            .put(Entity.GHAST, "minecraft:ghast")
            .put(Entity.CAVE_SPIDER, "minecraft:cave_spider")
            .put(Entity.SILVERFISH, "minecraft:silverfish")
            .put(Entity.ENDERMAN, "minecraft:enderman")
            .put(Entity.SLIME, "minecraft:slime")
            .put(Entity.ZOMBIE_PIGMAN, "minecraft:zombie_pigman")
            .put(Entity.SPIDER, "minecraft:spider")
            .put(Entity.SKELETON, "minecraft:skeleton")
            .put(Entity.CREEPER, "minecraft:creeper")
            .put(Entity.ZOMBIE, "minecraft:zombie")
            .put(Entity.SKELETON_HORSE, "minecraft:skeleton_horse")
            .put(Entity.MULE, "minecraft:mule")
            .put(Entity.DONKEY, "minecraft:donkey")
            .put(Entity.DOLPHIN, "minecraft:dolphin")
            .put(Entity.TROPICALFISH, "minecraft:tropicalfish")
            .put(Entity.WOLF, "minecraft:wolf")
            .put(Entity.SQUID, "minecraft:squid")
            .put(Entity.DROWNED, "minecraft:drowned")
            .put(Entity.SHEEP, "minecraft:sheep")
            .put(Entity.MOOSHROOM, "minecraft:mooshroom")
            .put(Entity.PANDA, "minecraft:panda")
            .put(Entity.SALMON, "minecraft:salmon")
            .put(Entity.PIG, "minecraft:pig")
            .put(Entity.VILLAGER, "minecraft:villager")
            .put(Entity.COD, "minecraft:cod")
            .put(Entity.PUFFERFISH, "minecraft:pufferfish")
            .put(Entity.COW, "minecraft:cow")
            .put(Entity.CHICKEN, "minecraft:chicken")
            .put(Entity.BALLOON, "minecraft:balloon")
            .put(Entity.LLAMA, "minecraft:llama")
            .put(Entity.IRON_GOLEM, "minecraft:iron_golem")
            .put(Entity.RABBIT, "minecraft:rabbit")
            .put(Entity.SNOW_GOLEM, "minecraft:snow_golem")
            .put(Entity.BAT, "minecraft:bat")
            .put(Entity.OCELOT, "minecraft:ocelot")
            .put(Entity.HORSE, "minecraft:horse")
            .put(Entity.CAT, "minecraft:cat")
            .put(Entity.POLAR_BEAR, "minecraft:polar_bear")
            .put(Entity.ZOMBIE_HORSE, "minecraft:zombie_horse")
            .put(Entity.TURTLE, "minecraft:turtle")
            .put(Entity.PARROT, "minecraft:parrot")
            .put(Entity.GUARDIAN, "minecraft:guardian")
            .put(Entity.ELDER_GUARDIAN, "minecraft:elder_guardian")
            .put(Entity.VINDICATOR, "minecraft:vindicator")
            .put(Entity.WITHER, "minecraft:wither")
            .put(Entity.ENDER_DRAGON, "minecraft:ender_dragon")
            .put(Entity.SHULKER, "minecraft:shulker")
            .put(Entity.ENDERMITE, "minecraft:endermite")
            .put(Entity.MINECART, "minecraft:minecart")
            .put(Entity.HOPPER_MINECART, "minecraft:hopper_minecart")
            .put(Entity.TNT_MINECART, "minecraft:tnt_minecart")
            .put(Entity.CHEST_MINECART, "minecraft:chest_minecart")
            .put(Entity.COMMAND_BLOCK_MINECART, "minecraft:command_block_minecart")
            .put(Entity.ARMOR_STAND, "minecraft:armor_stand")
            .put(Entity.ITEM, "minecraft:item")
            .put(Entity.TNT, "minecraft:tnt")
            .put(Entity.FALLING_BLOCK, "minecraft:falling_block")
            .put(Entity.XP_BOTTLE, "minecraft:xp_bottle")
            .put(Entity.XP_ORB, "minecraft:xp_orb")
            .put(Entity.EYE_OF_ENDER_SIGNAL, "minecraft:eye_of_ender_signal")
            .put(Entity.ENDER_CRYSTAL, "minecraft:ender_crystal")
            .put(Entity.SHULKER_BULLET, "minecraft:shulker_bullet")
            .put(Entity.FISHING_HOOK, "minecraft:fishing_hook")
            .put(Entity.DRAGON_FIREBALL, "minecraft:dragon_fireball")
            .put(Entity.ARROW, "minecraft:arrow")
            .put(Entity.SNOWBALL, "minecraft:snowball")
            .put(Entity.EGG, "minecraft:egg")
            .put(Entity.PAINTING, "minecraft:painting")
            .put(Entity.THROWN_TRIDENT, "minecraft:thrown_trident")
            .put(Entity.FIREBALL, "minecraft:fireball")
            .put(Entity.SPLASH_POTION, "minecraft:splash_potion")
            .put(Entity.ENDER_PEARL, "minecraft:ender_pearl")
            .put(Entity.LEASH_KNOT, "minecraft:leash_knot")
            .put(Entity.WITHER_SKULL, "minecraft:wither_skull")
            .put(Entity.WITHER_SKULL_DANGEROUS, "minecraft:wither_skull_dangerous")
            .put(Entity.BOAT, "minecraft:boat")
            .put(Entity.LIGHTNING_BOLT, "minecraft:lightning_bolt")
            .put(Entity.SMALL_FIREBALL, "minecraft:small_fireball")
            .put(Entity.LLAMA_SPIT, "minecraft:llama_spit")
            .put(Entity.AREA_EFFECT_CLOUD, "minecraft:area_effect_cloud")
            .put(Entity.LINGERING_POTION, "minecraft:lingering_potion")
            .put(Entity.FIREWORKS_ROCKET, "minecraft:fireworks_rocket")
            .put(Entity.EVOCATION_FANG, "minecraft:evocation_fang")
            .put(Entity.EVOCATION_ILLAGER, "minecraft:evocation_illager")
            .put(Entity.VEX, "minecraft:vex")
            .put(Entity.AGENT, "minecraft:agent")
            .put(Entity.ICE_BOMB, "minecraft:ice_bomb")
            .put(Entity.PHANTOM, "minecraft:phantom")
            .put(Entity.TRIPOD_CAMERA, "minecraft:tripod_camera")
            .build();

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId = 0; //TODO
    public long entityRuntimeId = 0;
    public int type = -1;
    public Vector3f position = new Vector3f();
    public Vector3f motion = new Vector3f();
    public float pitch = 0f;
    public float yaw = 0f;
    public float headYaw = 0f;

    public Attribute[] attributes = new Attribute[0];
    public EntityMetadata metadata = new EntityMetadata();
    public EntityLink[] links = new EntityLink[0];

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putString(LEGACY_ID_MAP_BC.containsKey(type) ? LEGACY_ID_MAP_BC.get(type) : ":");
        this.putVector3(this.position);
        this.putVector3Nullable(this.motion);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.headYaw);

        this.putUnsignedVarInt(this.attributes.length);
        for (Attribute attribute : attributes) {
            this.putString(attribute.getId());
            this.putLFloat(attribute.getMinValue());
            this.putLFloat(attribute.getValue());
            this.putLFloat(attribute.getMaxValue());
        }

        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.links.length);
        for (EntityLink link : this.links) {
            this.putEntityLink(link);
        }
    }
}
