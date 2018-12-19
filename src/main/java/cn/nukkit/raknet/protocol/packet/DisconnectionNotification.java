package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DisconnectionNotification extends Packet {

    public static final byte ID = MessageIdentifiers.ID_DISCONNECTION_NOTIFICATION;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new DisconnectionNotification();
        }

    }
}
