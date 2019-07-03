package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.PlayerPermissions;
import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class AdventureSettingsPacket extends DataPacket implements ClientboundPacket, ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADVENTURE_SETTINGS_PACKET;

    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_OPERATOR = 1;
    public static final int PERMISSION_HOST = 2;
    public static final int PERMISSION_AUTOMATION = 3;
    public static final int PERMISSION_ADMIN = 4;

    /**
     * This constant is used to identify flags that should be set on the second field. In a sensible world, these
     * flags would all be set on the same packet field, but as of MCPE 1.2, the new abilities flags have for some
     * reason been assigned a separate field.
     */
    public static final int BITFLAG_SECOND_SET = 1 << 16;

    public static final int WORLD_IMMUTABLE = 1;
    public static final int NO_PVP = 1 << 1;
    public static final int NO_MVP = 1 << 2;
    public static final int UNUSED = 1 << 3;
    public static final int SHOW_NAME_TAGS = 1 << 4;
    public static final int AUTO_JUMP = 1 << 5;
    public static final int ALLOW_FLIGHT = 1 << 6;
    public static final int NO_CLIP = 1 << 7;
    public static final int WORLD_BUILDER = 1 << 8;
    public static final int FLYING = 1 << 9;
    public static final int MUTED = 1 << 10;

    public static final int BUILD_AND_MINE = 1 | BITFLAG_SECOND_SET;
    public static final int DOORS_AND_SWITCHES = (1 << 1) | BITFLAG_SECOND_SET;
    public static final int OPEN_CONTAINERS = (1 << 2) | BITFLAG_SECOND_SET;
    public static final int ATTACK_PLAYERS = (1 << 3) | BITFLAG_SECOND_SET;
    public static final int ATTACK_MOBS = (1 << 4) | BITFLAG_SECOND_SET;
    public static final int OPERATOR = (1 << 5) | BITFLAG_SECOND_SET;
    public static final int TELEPORT = (1 << 7) | BITFLAG_SECOND_SET;
    public static final int BUILD = (1 << 8) | BITFLAG_SECOND_SET;
    public static final int DEAULT_LEVEL_PERMISSIONS = (1 << 9) | BITFLAG_SECOND_SET;

    public long playerFlags = 0;

    public long commandPermission = PERMISSION_NORMAL;

    public long worldFlags = -1;

    public long playerPermission = PlayerPermissions.MEMBER;

    public long customFlags = 0; //...

    public long entityUniqueId; //This is a little-endian long, NOT a var-long. (WTF Mojang)

    @Override
    public void decode() {
        this.playerFlags = getUnsignedVarInt();
        this.commandPermission = getUnsignedVarInt();
        this.worldFlags = getUnsignedVarInt();
        this.playerPermission = getUnsignedVarInt();
        this.customFlags = getUnsignedVarInt();
        this.entityUniqueId = getLLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.playerFlags);
        this.putUnsignedVarInt(this.commandPermission);
        this.putUnsignedVarInt(this.worldFlags);
        this.putUnsignedVarInt(this.playerPermission);
        this.putUnsignedVarInt(this.customFlags);
        this.putLLong(this.entityUniqueId);
    }

    public boolean getFlag(int flag) {
        if ((flag & BITFLAG_SECOND_SET) != 0) {
            return (this.worldFlags & flag) != 0;
        }

        return (this.playerFlags & flag) != 0;
    }

    public void setFlag(int flag, boolean value) {
        boolean flags = (flag & BITFLAG_SECOND_SET) != 0;

        if (value) {
            if (flags) {
                this.worldFlags |= flag;
            } else {
                this.playerFlags |= flag;
            }
        } else {
            if (flags) {
                this.worldFlags &= ~flag;
            } else {
                this.playerFlags &= ~flag;
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
