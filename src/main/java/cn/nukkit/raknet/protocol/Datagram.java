package cn.nukkit.raknet.protocol;

import cn.nukkit.utils.Binary;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class Datagram extends Packet {

    public static final byte BITFLAG_VALID = (byte) 0x80;
    public static final byte BITFLAG_ACK = (byte) 0x40;
    public static final byte BITFLAG_NAK = (byte) 0x20; // hasBAndAS for ACKs

    /*
     * These flags can be set on regular datagrams, but they are useless as per the public version of RakNet
     * (the receiving client will not use them or pay any attention to them).
     */
    public static final byte BITFLAG_PACKET_PAIR = (byte) 0x10;
    public static final byte BITFLAG_CONTINUOUS_SEND = (byte) 0x08;
    public static final byte BITFLAG_NEEDS_B_AND_AS = (byte) 0x04;

    public byte headerFlags = 0;

    public ConcurrentLinkedQueue<Object> packets = new ConcurrentLinkedQueue<>();

    public Integer seqNumber;

    @Override
    public void encode() {
        this.putByte((byte) (BITFLAG_VALID | this.headerFlags));
        this.putLTriad(this.seqNumber);
        for (Object packet : this.packets) {
            this.put(packet instanceof EncapsulatedPacket ? ((EncapsulatedPacket) packet).toBinary() : (byte[]) packet);
        }
    }

    public int length() {
        int length = 4;
        for (Object packet : this.packets) {
            length += packet instanceof EncapsulatedPacket ? ((EncapsulatedPacket) packet).getTotalLength() : ((byte[]) packet).length;
        }

        return length;
    }

    @Override
    public void decode() {
        this.headerFlags = this.getByte();
        this.seqNumber = this.getLTriad();

        while (!this.feof()) {
            byte[] data = Binary.subBytes(this.buffer, this.offset);
            EncapsulatedPacket packet = EncapsulatedPacket.fromBinary(data, false);
            this.offset += packet.getOffset();
            if (packet.buffer.length == 0) {
                break;
            }
            this.packets.add(packet);
        }
    }

    @Override
    public Packet clean() {
        this.packets = new ConcurrentLinkedQueue<>();
        this.seqNumber = null;

        return super.clean();
    }

    @Override
    public Datagram clone() throws CloneNotSupportedException {
        Datagram packet = (Datagram) super.clone();
        packet.packets = new ConcurrentLinkedQueue<>(this.packets);
        return packet;
    }
}
