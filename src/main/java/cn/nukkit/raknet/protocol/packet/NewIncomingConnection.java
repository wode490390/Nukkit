package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.RakNet;
import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

import java.net.InetSocketAddress;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class NewIncomingConnection extends Packet {

    public static final byte ID = MessageIdentifiers.ID_NEW_INCOMING_CONNECTION;

    @Override
    public byte getID() {
        return ID;
    }

    public String address;
    public int port;
    public final InetSocketAddress[] systemAddresses = new InetSocketAddress[RakNet.SYSTEM_ADDRESS_COUNT];

    public long sendPingTime;
    public long sendPongTime;

    @Override
    public void encode() {
        super.encode();
        this.putAddress(this.address);
        for (InetSocketAddress address : systemAddresses) {
            this.putAddress(address);
        }
        this.putLong(this.sendPingTime);
        this.putLong(this.sendPongTime);
    }

    @Override
    public void decode() {
        super.decode();
        InetSocketAddress addr = this.getAddress();
        this.address = addr.getHostString();
        this.port = addr.getPort();

        for (int i = 0; i < RakNet.SYSTEM_ADDRESS_COUNT; ++i) {
            this.systemAddresses[i] = this.getAddress();
        }

        this.sendPingTime = this.getLong();
        this.sendPongTime = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new NewIncomingConnection();
        }

    }
}
