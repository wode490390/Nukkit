package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@ToString
public class EntityEventPacket extends DataPacket {

    public static final int NETWORK_ID = ProtocolInfo.ENTITY_EVENT_PACKET;

    public static final int HURT_ANIMATION = 2;
    public static final int DEATH_ANIMATION = 3;
    public static final int ARM_SWING = 4;

    public static final int TAME_FAIL = 6;
    public static final int TAME_SUCCESS = 7;
    public static final int SHAKE_WET = 8;
    public static final int USE_ITEM = 9;
    public static final int EAT_GRASS_ANIMATION = 10;
    public static final int FISH_HOOK_BUBBLE = 11;
    public static final int FISH_HOOK_POSITION = 12;
    public static final int FISH_HOOK_HOOK = 13;
    public static final int FISH_HOOK_TEASE = 14;
    public static final int SQUID_INK_CLOUD = 15;
    public static final int ZOMBIE_VILLAGER_CURE = 16;
    public static final int AMBIENT_SOUND = 17;
    public static final int RESPAWN = 18;
    public static final int IRON_GOLEM_OFFER_FLOWER = 19;
    public static final int IRON_GOLEM_WITHDRAW_FLOWER = 20;
    public static final int LOVE_PARTICLES = 21; //breeding
    public static final int VILLAGER_HURT = 22;
    public static final int VILLAGER_STOP_TRADING = 23;
    public static final int WITCH_SPELL_PARTICLES = 24;
    public static final int FIREWORK_PARTICLES = 25;

    public static final int SILVERFISH_SPAWN_ANIMATION = 27;
    public static final int GUARDIAN_ATTACK_ANIMATION = 28;
    public static final int WITCH_DRINK_POTION = 29;
    public static final int WITCH_THROW_POTION = 30;
    public static final int MINECART_TNT_PRIME_FUSE = 31;

    public static final int PLAYER_ADD_XP_LEVELS = 34;
    public static final int ELDER_GUARDIAN_CURSE = 35;
    public static final int AGENT_ARM_SWING = 36;
    public static final int ENDER_DRAGON_DEATH = 37;
    public static final int DUST_PARTICLES = 38; //not sure what this is
    public static final int ARROW_SHAKE = 39;

    public static final int EATING_ITEM = 57;

    public static final int BABY_ANIMAL_FEED = 60; //green particles, like bonemeal on crops
    public static final int DEATH_SMOKE_CLOUD = 61;
    public static final int COMPLETE_TRADE = 62;
    public static final int REMOVE_LEASH = 63; //data 1 = cut leash
    public static final int CARAVAN = 64;
    public static final int CONSUME_TOTEM = 65;
    public static final int PLAYER_CHECK_TREASURE_HUNTER_ACHIEVEMENT = 66; //mojang...
    public static final int ENTITY_SPAWN = 67; //used for MinecraftEventing stuff, not needed
    public static final int DRAGON_PUKE = 68; //they call this puke particles
    public static final int ITEM_ENTITY_MERGE = 69;

    public static final int BALLOON_POP = 71;
    public static final int FIND_TREASURE_BRIBE = 72;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityRuntimeId;
    public int event;
    public int data = 0;

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.event = this.getByte();
        this.data = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte((byte) this.event);
        this.putVarInt((byte) this.data);
    }
}
