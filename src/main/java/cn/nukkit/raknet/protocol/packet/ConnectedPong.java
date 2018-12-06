package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ConnectedPong extends Packet {

    public static final byte ID = MessageIdentifiers.ID_CONNECTED_PONG;

    @Override
    public byte getID() {
        return ID;
    }

    public long sendPingTime;
    public long sendPongTime;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.sendPingTime);
        this.putLong(this.sendPongTime);
    }

    @Override
    public void decode() {
        super.decode();
        this.sendPingTime = this.getLong();
        this.sendPongTime = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new ConnectedPong();
        }

    }
}
