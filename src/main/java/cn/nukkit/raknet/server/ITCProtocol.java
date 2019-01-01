package cn.nukkit.raknet.server;

public interface ITCProtocol {

    byte[] MAGIC = new byte[]{
            (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00,
            (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd,
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78
    };

    /*
     * These internal "packets" DO NOT exist in the RakNet protocol. They are used by the RakLib API to communicate
     * messages between the RakLib thread and the implementation's thread.
     *
     * Internal Packet:
     * byte (packet ID)
     * byte[] (payload)
     */

    /*
     * ENCAPSULATED payload:
     * byte (identifier length)
     * byte[] (identifier)
     * byte (flags, last 3 bits, priority)
     * payload (binary internal EncapsulatedPacket)
     */
    byte PACKET_ENCAPSULATED = 0x01;

    /*
     * OPEN_SESSION payload:
     * byte (identifier length)
     * byte[] (identifier)
     * byte (address length)
     * byte[] (address)
     * short (port)
     * long (clientID)
     */
    byte PACKET_OPEN_SESSION = 0x02;

    /*
     * CLOSE_SESSION payload:
     * byte (identifier length)
     * byte[] (identifier)
     * string (reason)
     */
    byte PACKET_CLOSE_SESSION = 0x03;

    /*
     * INVALID_SESSION payload:
     * byte (identifier length)
     * byte[] (identifier)
     */
    byte PACKET_INVALID_SESSION = 0x04;

    /* SEND_QUEUE payload:
     * byte (identifier length)
     * byte[] (identifier)
     */
    byte PACKET_SEND_QUEUE = 0x05;

    /*
     * ACK_NOTIFICATION payload:
     * byte (identifier length)
     * byte[] (identifier)
     * int (identifierACK)
     */
    byte PACKET_ACK_NOTIFICATION = 0x06;

    /*
     * SET_OPTION payload:
     * byte (option name length)
     * byte[] (option name)
     * byte[] (option value)
     */
    byte PACKET_SET_OPTION = 0x07;

    /*
     * RAW payload:
     * byte (address length)
     * byte[] (address from/to)
     * short (port)
     * byte[] (payload)
     */
    byte PACKET_RAW = 0x08;

    /*
     * BLOCK_ADDRESS payload:
     * byte (address length)
     * byte[] (address)
     * int (timeout)
     */
    byte PACKET_BLOCK_ADDRESS = 0x09;

    /*
     * UNBLOCK_ADDRESS payload:
     * byte (adress length)
     * byte[] (address)
     */
    byte PACKET_UNBLOCK_ADDRESS = 0x10;

    /*
     * REPORT_PING payload:
     * byte (identifier length)
     * byte[] (identifier)
     * int32 (measured latency in MS)
     */
    byte PACKET_REPORT_PING = 0x11;

    /*
     * RAW_FILTER payload:
     * byte[] (pattern)
     */
    byte PACKET_RAW_FILTER = 0x12;

    /*
     * No payload
     *
     * Sends the disconnect message, removes sessions correctly, closes sockets.
     */
    byte PACKET_SHUTDOWN = 0x7e;

    /*
     * No payload
     *
     * Leaves everything as-is and halts, other Threads can be in a post-crash condition.
     */
    byte PACKET_EMERGENCY_SHUTDOWN = 0x7f;
}
