package cn.nukkit.raknet.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@Log4j2
public class UDPServerSocket extends ChannelInboundHandlerAdapter {

    protected Bootstrap bootstrap;
    protected Channel channel;

    private String bindAddress;
    private int bindPort;

    protected ConcurrentLinkedQueue<DatagramPacket> packets = new ConcurrentLinkedQueue<>();

    public UDPServerSocket() {
        this(19132, "0.0.0.0");
    }

    public UDPServerSocket(int port) {
        this(port, "0.0.0.0");
    }

    public UDPServerSocket(int port, String interfaz) {
        this.bindAddress = interfaz;
        this.bindPort = port;
        try {
            if (Epoll.isAvailable()) {
                bootstrap = new Bootstrap()
                        .channel(EpollDatagramChannel.class)
                        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                        .handler(this)
                        .group(new EpollEventLoopGroup());
                log.info("Epoll is available. EpollEventLoop will be used.");
            } else {
                bootstrap = new Bootstrap()
                        .group(new NioEventLoopGroup())
                        .channel(NioDatagramChannel.class)
                        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                        .handler(this);
                log.info("Epoll is unavailable. Reverting to NioEventLoop.");
            }
            channel = bootstrap.bind(this.bindAddress, this.bindPort).sync().channel();
        } catch (Exception e) {
            log.fatal("**** FAILED TO BIND TO " + this.bindAddress + ":" + this.bindPort + "!");
            log.fatal("Perhaps a server is already running on that port?");
            System.exit(1);
        }
    }

    public String getBindAddress() {
        return this.bindAddress;
    }

    public int getBindPort() {
        return this.bindPort;
    }

    public void close() {
        bootstrap.config().group().shutdownGracefully();
        if (channel != null) {
            channel.close().syncUninterruptibly();
        }
    }

    public void clearPacketQueue() {
        this.packets.clear();
    }

    public DatagramPacket readPacket() throws IOException {
        return this.packets.poll();
    }

    public int writePacket(byte[] data, String dest, int port) throws IOException {
        return this.writePacket(data, new InetSocketAddress(dest, port));
    }

    public int writePacket(byte[] data, InetSocketAddress dest) throws IOException {
        channel.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(data), dest));
        return data.length;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.packets.add((DatagramPacket) msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn(cause.getMessage(), cause);
    }
}
