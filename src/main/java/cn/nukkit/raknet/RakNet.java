package cn.nukkit.raknet;

/**
 * author: MagicDroidX
 * Nukkit Project
 * UDP network library that follows the RakNet protocol for Nukkit Project
 * This is not affiliated with Jenkins Software LLC nor RakNet.
 */
public abstract class RakNet {

    public static final String VERSION = "1.1.0";
    public static final byte PROTOCOL = 9;
    /**
     * Default vanilla Raknet protocol version that this library implements. Things using RakNet can override this
     * protocol version with something different.
     */
    public static final byte DEFAULT_PROTOCOL_VERSION = 6;

    public static final byte PRIORITY_NORMAL = 0;
    public static final byte PRIORITY_IMMEDIATE = 1;

    public static final byte FLAG_NEED_ACK = 0b00001000;

    /*
     * Regular RakNet uses 10 by default. MCPE uses 20. Configure this value as appropriate.
     */
    public static final int SYSTEM_ADDRESS_COUNT = 20;
}
