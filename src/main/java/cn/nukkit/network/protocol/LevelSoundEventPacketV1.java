package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

/**
 * Useless leftover from a 1.8 refactor, does nothing
 */
@ToString
public class LevelSoundEventPacketV1 extends DataPacket implements ClientboundPacket, ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.LEVEL_SOUND_EVENT_PACKET_V1;

    public int sound;
    public Vector3f position;
    public int extraData = 0;
    public int entityType = 1;
    public boolean isBabyMob = false; //...
    public boolean disableRelativeVolume = false;

    @Override
    public void decode() {
        this.sound = this.getByte();
        this.position = this.getVector3();
        this.extraData = this.getVarInt();
        this.entityType = this.getVarInt();
        this.isBabyMob = this.getBoolean();
        this.disableRelativeVolume = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.sound);
        this.putVector3(this.position);
        this.putVarInt(this.extraData);
        this.putVarInt(this.entityType);
        this.putBoolean(this.isBabyMob);
        this.putBoolean(this.disableRelativeVolume);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
