package cn.nukkit.network.protocol;

import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.PlayerPermissions;

/**
 * Created on 15-10-13.
 */
public class StartGamePacket extends DataPacket {

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

    private static String runtimeIdTable = null;

    public long entityUniqueId = 0;
    public long entityRuntimeId = 0;
    public int playerGamemode = 0;

    public Vector3f playerPosition = new Vector3f();

    public float pitch = 0;
    public float yaw = 0;

    public int seed = 0;
    public byte dimension = 0;
    public int generator = 1; //default infinite - 0 old, 1 infinite, 2 flat
    public int worldGamemode = 0;
    public int difficulty = 1;
    public int spawnX = 0;
    public int spawnY = 0;
    public int spawnZ = 0;
    public boolean hasAchievementsDisabled = true;
    public int time = -1; //-1 = not stopped, any positive value = stopped at that time
    public boolean eduMode = false;
    public boolean hasEduFeaturesEnabled = false;
    public float rainLevel = 0;
    public float lightningLevel = 0;
    public boolean hasConfirmedPlatformLockedContent = false;
    public boolean isMultiplayerGame = true;
    public boolean hasLANBroadcast = true;
    public int xboxLiveBroadcastMode = GAME_PUBLISH_SETTING_PUBLIC;
    public int platformBroadcastMode = GAME_PUBLISH_SETTING_PUBLIC;
    public boolean commandsEnabled = false;
    public boolean isTexturePacksRequired = false;
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
    public String worldName = "";
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
/* TODO:
        if (this.runtimeIdTable == null) {
            //this is a really nasty hack, but it'll do for now
            stream = new NetworkBinaryStream();
            data = json_decode(file_get_contents("\RESOURCE_PATH\" . "runtimeid_table.json"), true);
            stream.putUnsignedVarInt(data.count);
            for (Object v : data){
                stream.putString(v["name"]);
                stream.putLShort(v["data"]);
            }
            this.runtimeIdTable = stream.buffer;
        }
        this->put(self::$runtimeIdTable);*/

        this.put(GlobalBlockPalette.getCompiledTable());
        this.putString(this.multiplayerCorrelationId);
    }

}
