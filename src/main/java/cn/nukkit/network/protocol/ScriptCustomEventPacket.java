package cn.nukkit.network.protocol;

public class ScriptCustomEventPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SCRIPT_CUSTOM_EVENT_PACKET;

    public String eventName;
    public String eventData; //json data

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
        this.putString(this.eventName);
        this.putString(this.eventData);
    }

}