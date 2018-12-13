package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.EntityID;
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
            .put(EntityID.NPC, "minecraft:npc")
            .put(EntityID.PLAYER, "minecraft:player")
            .put(EntityID.WITHER_SKELETON, "minecraft:wither_skeleton")
            .put(EntityID.HUSK, "minecraft:husk")
            .put(EntityID.STRAY, "minecraft:stray")
            .put(EntityID.WITCH, "minecraft:witch")
            .put(EntityID.ZOMBIE_VILLAGER, "minecraft:zombie_villager")
            .put(EntityID.BLAZE, "minecraft:blaze")
            .put(EntityID.MAGMA_CUBE, "minecraft:magma_cube")
            .put(EntityID.GHAST, "minecraft:ghast")
            .put(EntityID.CAVE_SPIDER, "minecraft:cave_spider")
            .put(EntityID.SILVERFISH, "minecraft:silverfish")
            .put(EntityID.ENDERMAN, "minecraft:enderman")
            .put(EntityID.SLIME, "minecraft:slime")
            .put(EntityID.ZOMBIE_PIGMAN, "minecraft:zombie_pigman")
            .put(EntityID.SPIDER, "minecraft:spider")
            .put(EntityID.SKELETON, "minecraft:skeleton")
            .put(EntityID.CREEPER, "minecraft:creeper")
            .put(EntityID.ZOMBIE, "minecraft:zombie")
            .put(EntityID.SKELETON_HORSE, "minecraft:skeleton_horse")
            .put(EntityID.MULE, "minecraft:mule")
            .put(EntityID.DONKEY, "minecraft:donkey")
            .put(EntityID.DOLPHIN, "minecraft:dolphin")
            .put(EntityID.TROPICALFISH, "minecraft:tropicalfish")
            .put(EntityID.WOLF, "minecraft:wolf")
            .put(EntityID.SQUID, "minecraft:squid")
            .put(EntityID.DROWNED, "minecraft:drowned")
            .put(EntityID.SHEEP, "minecraft:sheep")
            .put(EntityID.MOOSHROOM, "minecraft:mooshroom")
            .put(EntityID.PANDA, "minecraft:panda")
            .put(EntityID.SALMON, "minecraft:salmon")
            .put(EntityID.PIG, "minecraft:pig")
            .put(EntityID.VILLAGER, "minecraft:villager")
            .put(EntityID.COD, "minecraft:cod")
            .put(EntityID.PUFFERFISH, "minecraft:pufferfish")
            .put(EntityID.COW, "minecraft:cow")
            .put(EntityID.CHICKEN, "minecraft:chicken")
            .put(EntityID.BALLOON, "minecraft:balloon")
            .put(EntityID.LLAMA, "minecraft:llama")
            .put(EntityID.IRON_GOLEM, "minecraft:iron_golem")
            .put(EntityID.RABBIT, "minecraft:rabbit")
            .put(EntityID.SNOW_GOLEM, "minecraft:snow_golem")
            .put(EntityID.BAT, "minecraft:bat")
            .put(EntityID.OCELOT, "minecraft:ocelot")
            .put(EntityID.HORSE, "minecraft:horse")
            .put(EntityID.CAT, "minecraft:cat")
            .put(EntityID.POLAR_BEAR, "minecraft:polar_bear")
            .put(EntityID.ZOMBIE_HORSE, "minecraft:zombie_horse")
            .put(EntityID.TURTLE, "minecraft:turtle")
            .put(EntityID.PARROT, "minecraft:parrot")
            .put(EntityID.GUARDIAN, "minecraft:guardian")
            .put(EntityID.ELDER_GUARDIAN, "minecraft:elder_guardian")
            .put(EntityID.VINDICATOR, "minecraft:vindicator")
            .put(EntityID.WITHER, "minecraft:wither")
            .put(EntityID.ENDER_DRAGON, "minecraft:ender_dragon")
            .put(EntityID.SHULKER, "minecraft:shulker")
            .put(EntityID.ENDERMITE, "minecraft:endermite")
            .put(EntityID.MINECART, "minecraft:minecart")
            .put(EntityID.HOPPER_MINECART, "minecraft:hopper_minecart")
            .put(EntityID.TNT_MINECART, "minecraft:tnt_minecart")
            .put(EntityID.CHEST_MINECART, "minecraft:chest_minecart")
            .put(EntityID.COMMAND_BLOCK_MINECART, "minecraft:command_block_minecart")
            .put(EntityID.ARMOR_STAND, "minecraft:armor_stand")
            .put(EntityID.ITEM, "minecraft:item")
            .put(EntityID.TNT, "minecraft:tnt")
            .put(EntityID.FALLING_BLOCK, "minecraft:falling_block")
            .put(EntityID.XP_BOTTLE, "minecraft:xp_bottle")
            .put(EntityID.XP_ORB, "minecraft:xp_orb")
            .put(EntityID.EYE_OF_ENDER_SIGNAL, "minecraft:eye_of_ender_signal")
            .put(EntityID.ENDER_CRYSTAL, "minecraft:ender_crystal")
            .put(EntityID.SHULKER_BULLET, "minecraft:shulker_bullet")
            .put(EntityID.FISHING_HOOK, "minecraft:fishing_hook")
            .put(EntityID.DRAGON_FIREBALL, "minecraft:dragon_fireball")
            .put(EntityID.ARROW, "minecraft:arrow")
            .put(EntityID.SNOWBALL, "minecraft:snowball")
            .put(EntityID.EGG, "minecraft:egg")
            .put(EntityID.PAINTING, "minecraft:painting")
            .put(EntityID.THROWN_TRIDENT, "minecraft:thrown_trident")
            .put(EntityID.FIREBALL, "minecraft:fireball")
            .put(EntityID.SPLASH_POTION, "minecraft:splash_potion")
            .put(EntityID.ENDER_PEARL, "minecraft:ender_pearl")
            .put(EntityID.LEASH_KNOT, "minecraft:leash_knot")
            .put(EntityID.WITHER_SKULL, "minecraft:wither_skull")
            .put(EntityID.WITHER_SKULL_DANGEROUS, "minecraft:wither_skull_dangerous")
            .put(EntityID.BOAT, "minecraft:boat")
            .put(EntityID.LIGHTNING_BOLT, "minecraft:lightning_bolt")
            .put(EntityID.SMALL_FIREBALL, "minecraft:small_fireball")
            .put(EntityID.LLAMA_SPIT, "minecraft:llama_spit")
            .put(EntityID.AREA_EFFECT_CLOUD, "minecraft:area_effect_cloud")
            .put(EntityID.LINGERING_POTION, "minecraft:lingering_potion")
            .put(EntityID.FIREWORKS_ROCKET, "minecraft:fireworks_rocket")
            .put(EntityID.EVOCATION_FANG, "minecraft:evocation_fang")
            .put(EntityID.EVOCATION_ILLAGER, "minecraft:evocation_illager")
            .put(EntityID.VEX, "minecraft:vex")
            .put(EntityID.AGENT, "minecraft:agent")
            .put(EntityID.ICE_BOMB, "minecraft:ice_bomb")
            .put(EntityID.PHANTOM, "minecraft:phantom")
            .put(EntityID.TRIPOD_CAMERA, "minecraft:tripod_camera")
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
