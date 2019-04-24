package cn.nukkit.raknet.protocol;

public abstract class OfflineMessage extends Packet {

    /**
     * Magic bytes used to distinguish offline messages from loose garbage.
     */
    private final byte[] MAGIC = new byte[]{
            (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00,
            (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd,
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78
    };

    public byte[] magic = new byte[16];

    public void writeMagic() {
        this.put(MAGIC);
    }

    public void readMagic() {
        this.magic = this.get(16);
    }

    public boolean isValid() {
        return this.magic == MAGIC;
    }

    @Override
    public Packet clean() {
        this.magic = new byte[16];
        return super.clean();
    }

    @Override
    public OfflineMessage clone() throws CloneNotSupportedException {
        OfflineMessage packet = (OfflineMessage) super.clone();
        packet.magic = new byte[16];
        return packet;
    }
}
