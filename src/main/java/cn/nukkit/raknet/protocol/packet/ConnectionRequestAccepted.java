package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.RakNet;
import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

import java.net.InetSocketAddress;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ConnectionRequestAccepted extends Packet {

    public static final byte ID = MessageIdentifiers.ID_CONNECTION_REQUEST_ACCEPTED;

    @Override
    public byte getID() {
        return ID;
    }

    public String address;
    public int port;
    public final InetSocketAddress[] systemAddresses = new InetSocketAddress[]{
            new InetSocketAddress("127.0.0.1", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0),
            new InetSocketAddress("0.0.0.0", 0)
    };

    public long sendPingTime;
    public long sendPongTime;

    @Override
    public void encode() {
        super.encode();
        this.putAddress(new InetSocketAddress(this.address, this.port));
        this.putShort(0);

        for (int i = 0; i < RakNet.SYSTEM_ADDRESS_COUNT; ++i) {
            this.putAddress(this.systemAddresses[i]);
        }

        this.putLong(this.sendPingTime);
        this.putLong(this.sendPongTime);
    }

    @Override
    public void decode() {
        super.decode();
        this.address = this.getAddress();
        this.getShort(); //TODO: check this

        for (int i = 0; i < RakNet.SYSTEM_ADDRESS_COUNT; ++i) {
            this.systemAddresses[i] = this.getAddress();
        }

        this.sendPingTime = this.getLong();
        this.sendPongTime = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new ConnectionRequestAccepted();
        }

    }
}
