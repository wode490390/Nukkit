package cn.nukkit.raknet.protocol;

import cn.nukkit.utils.BinaryStream;
//import cn.nukkit.utils.IPv6Converter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class Packet extends BinaryStream implements Cloneable {

    public static final byte ID = -1;

    public Long sendTime;

    public abstract byte getID();

    @Override
    public String getString() {        
        return new String(this.get(this.getShort()), StandardCharsets.UTF_8);        
    }

    protected InetSocketAddress getAddress() {
        byte version = (byte) this.getByte();
        if (version == 4) {
            String addr = ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff);
            int port = this.getShort();
            return new InetSocketAddress(addr, port);/*
        } else if (version == 6) {
            //http://man7.org/linux/man-pages/man7/ipv6.7.html
            this.getLShort(); //Family, AF_INET6
            int port = this.getShort();
            this.getInt(); //flow info
            String addr = IPv6Converter.splitKey(this.get(16));
            this.getInt(); //scope ID
            return new InetSocketAddress(addr, port);*/
        } else {
            //throw new RuntimeException("IP version " + version + " is not supported");
            return null;
        }
    }

    @Override
    public void putString(String str) {        
        byte[] b = str.getBytes(StandardCharsets.UTF_8);        
        this.putShort(b.length);        
        this.put(b);        
    }

    protected void putAddress(String addr, int port) {
        this.putAddress(addr, port, (byte) 4);
    }

    protected void putAddress(String addr, int port, byte version) {
        this.putByte(version);
        if (version == 4) {
            for (String b : addr.split("\\.")) {
                this.putByte((byte) ((~Integer.valueOf(b)) & 0xff));
            }
            this.putShort(port);/*
        } else if (version == 6) {
            this.put(Binary.writeLShort(10));
            this.putShort(port);
            this.putInt(0);
            this.put(IPv6Converter.toByte(addr));
            this.putInt(0);*/
        } else {
            //throw new RuntimeException("IP version " + version + " is not supported");
        }
    }

    protected void putAddress(InetSocketAddress address) {
        this.putAddress(address.getHostString(), address.getPort());
    }

    public void encode() {
        this.buffer = new byte[]{ID};
    }

    public void decode() {
        this.getByte(); //PID
    }

    public Packet clean() {
        this.buffer = null;
        this.offset = 0;
        this.sendTime = null;

        return this;
    }

    @Override
    public Packet clone() throws CloneNotSupportedException {
        Packet packet = (Packet) super.clone();
        packet.buffer = this.buffer.clone();
        return packet;
    }

    /**
     * A factory to create new packet instances
     */
    public interface PacketFactory {
        /**
         * Creates the packet
         */
        Packet create();
    }
}
