package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.RakNet;
import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class OpenConnectionRequest1 extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_OPEN_CONNECTION_REQUEST_1;

    @Override
    public byte getID() {
        return ID;
    }

    public byte protocol = RakNet.PROTOCOL; //RakNet.DEFAULT_PROTOCOL_VERSION
    public short mtuSize;

    @Override
    public void encode() {
        super.encode();
        this.writeMagic();
        this.putByte(this.protocol);
        this.put(new byte[this.mtuSize - 18]);
    }

    @Override
    public void decode() {
        super.decode();
        this.readMagic();
        this.protocol = this.getByte();
        this.mtuSize = (short) this.buffer.length;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new OpenConnectionRequest1();
        }

    }
}
