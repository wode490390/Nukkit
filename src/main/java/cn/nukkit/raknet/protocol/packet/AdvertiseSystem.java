package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AdvertiseSystem extends Packet {

    public static final byte ID = MessageIdentifiers.ID_ADVERTISE_SYSTEM;

    public String serverName;

    @Override
    public byte getID() {
        return ID;
    }

    @Override
    public void encode() {
        super.encode();
        this.putString(this.serverName);
    }

    @Override
    public void decode() {
        super.decode();
        this.serverName = this.getString();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new AdvertiseSystem();
        }

    }
}
