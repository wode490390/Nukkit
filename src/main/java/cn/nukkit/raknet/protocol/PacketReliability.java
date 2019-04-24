package cn.nukkit.raknet.protocol;

public abstract class PacketReliability {

    /*
     * From https://github.com/OculusVR/RakNet/blob/master/Source/PacketPriority.h
     *
     * Default: 0b010 (2) or 0b011 (3)
     */

    public static final int UNRELIABLE = 0;
    public static final int UNRELIABLE_SEQUENCED = 1;
    public static final int RELIABLE = 2;
    public static final int RELIABLE_ORDERED = 3;
    public static final int RELIABLE_SEQUENCED = 4;
    public static final int UNRELIABLE_WITH_ACK_RECEIPT = 5;
    public static final int RELIABLE_WITH_ACK_RECEIPT = 6;
    public static final int RELIABLE_ORDERED_WITH_ACK_RECEIPT = 7;

    public static boolean isReliable(int reliability) {
        return (
            reliability == RELIABLE ||
            reliability == RELIABLE_ORDERED ||
            reliability == RELIABLE_SEQUENCED ||
            reliability == RELIABLE_WITH_ACK_RECEIPT ||
            reliability == RELIABLE_ORDERED_WITH_ACK_RECEIPT
        );
    }

    public static boolean isSequenced(int reliability) {
        return (
            reliability == UNRELIABLE_SEQUENCED ||
            reliability == RELIABLE_SEQUENCED
        );
    }

    public static boolean isOrdered(int reliability) {
        return (
            reliability == RELIABLE_ORDERED ||
            reliability == RELIABLE_ORDERED_WITH_ACK_RECEIPT
        );
    }

    public static boolean isSequencedOrOrdered(int reliability) {
        return (
            reliability == UNRELIABLE_SEQUENCED ||
            reliability == RELIABLE_ORDERED ||
            reliability == RELIABLE_SEQUENCED ||
            reliability == RELIABLE_ORDERED_WITH_ACK_RECEIPT
        );
    }
}
