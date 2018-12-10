package cn.nukkit.raknet.protocol;

import cn.nukkit.raknet.RakNet;

public abstract class OfflineMessage extends Packet {

    public byte[] magic = new byte[16];

    public void writeMagic() {
        this.put(RakNet.MAGIC);
    }

    public void readMagic() {
        this.magic = this.get(16);
    }

    public boolean isValid() {
        return this.magic == RakNet.MAGIC;
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
