package cn.nukkit.network.protocol;

public class PlaySoundPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAY_SOUND_PACKET;

    public String soundName;
    public float x;
    public float y;
    public float z;
    public float volume;
    public float pitch;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.soundName);
        this.putBlockPosition((int) (this.x * 8), (int) (this.y * 8), (int) (this.z * 8));
        this.putLFloat(this.volume);
        this.putLFloat(this.pitch);
    }
}
