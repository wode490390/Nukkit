package cn.nukkit.network.protocol;

import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.PlayerPermissions;
import lombok.ToString;

/**
 * Created on 15-10-13.
 */
@ToString
public class StartGamePacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.START_GAME_PACKET;

    public static final int GAME_PUBLISH_SETTING_NO_MULTI_PLAY = 0;
    public static final int GAME_PUBLISH_SETTING_INVITE_ONLY = 1;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_ONLY = 2;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_OF_FRIENDS = 3;
    public static final int GAME_PUBLISH_SETTING_PUBLIC = 4;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public int playerGamemode;

    public Vector3f playerPosition;

    public float pitch;
    public float yaw;

    public int seed;
    public byte dimension;
    public int generator = 1; //default infinite - 0 old, 1 infinite, 2 flat
    public int worldGamemode;
    public int difficulty;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public boolean hasAchievementsDisabled = true;
    public int time = -1; //-1 = not stopped, any positive value = stopped at that time
    public boolean eduMode = false;
    public boolean hasEduFeaturesEnabled = false;
    public float rainLevel;
    public float lightningLevel;
    public boolean hasConfirmedPlatformLockedContent = false;
    public boolean isMultiplayerGame = true;
    public boolean hasLANBroadcast = true;
    public int xboxLiveBroadcastMode = GAME_PUBLISH_SETTING_PUBLIC;
    public int platformBroadcastMode = GAME_PUBLISH_SETTING_PUBLIC;
    public boolean commandsEnabled;
    public boolean isTexturePacksRequired = true;
    public GameRules gameRules;
    public boolean hasBonusChestEnabled = false;
    public boolean hasStartWithMapEnabled = false;
    public int defaultPlayerPermission = PlayerPermissions.MEMBER; //TODO

    public int serverChunkTickRange = 4; //TODO (leave as default for now)

    public boolean hasLockedBehaviorPack = false;
    public boolean hasLockedResourcePack = false;
    public boolean isFromLockedWorldTemplate = false;
    public boolean useMsaGamertagsOnly = false;
    public boolean isFromWorldTemplate = false;
    public boolean isWorldTemplateOptionLocked = false;

    public String levelId = ""; //base64 string, usually the same as world folder name in vanilla
    public String worldName;
    public String premiumWorldTemplateId = "";
    public boolean isTrial = false;
    public long currentTick = 0; //only used if isTrial is true
    public int enchantmentSeed = 0;
    public String multiplayerCorrelationId = ""; //TODO: this should be filled with a UUID of some sort

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putVarInt(this.playerGamemode);

        this.putVector3(this.playerPosition);

        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);

        //Level settings
        this.putVarInt(this.seed);
        this.putVarInt(this.dimension);
        this.putVarInt(this.generator);
        this.putVarInt(this.worldGamemode);
        this.putVarInt(this.difficulty);
        this.putBlockPosition(this.spawnX, this.spawnY, this.spawnZ);
        this.putBoolean(this.hasAchievementsDisabled);
        this.putVarInt(this.time);
        this.putBoolean(this.eduMode);
        this.putBoolean(this.hasEduFeaturesEnabled);
        this.putLFloat(this.rainLevel);
        this.putLFloat(this.lightningLevel);
        this.putBoolean(this.hasConfirmedPlatformLockedContent);
        this.putBoolean(this.isMultiplayerGame);
        this.putBoolean(this.hasLANBroadcast);
        this.putVarInt(this.xboxLiveBroadcastMode);
        this.putVarInt(this.platformBroadcastMode);
        this.putBoolean(this.commandsEnabled);
        this.putBoolean(this.isTexturePacksRequired);
        this.putGameRules(this.gameRules);
        this.putBoolean(this.hasBonusChestEnabled);
        this.putBoolean(this.hasStartWithMapEnabled);
        this.putVarInt(this.defaultPlayerPermission);
        this.putLInt(this.serverChunkTickRange);
        this.putBoolean(this.hasLockedBehaviorPack);
        this.putBoolean(this.hasLockedResourcePack);
        this.putBoolean(this.isFromLockedWorldTemplate);
        this.putBoolean(this.useMsaGamertagsOnly);
        this.putBoolean(this.isFromWorldTemplate);
        this.putBoolean(this.isWorldTemplateOptionLocked);

        this.putString(this.levelId);
        this.putString(this.worldName);
        this.putString(this.premiumWorldTemplateId);
        this.putBoolean(this.isTrial);
        this.putLLong(this.currentTick);

        this.putVarInt(this.enchantmentSeed);

        this.put(GlobalBlockPalette.getCompiledTable());
        this.putString(this.multiplayerCorrelationId);
    }
}
