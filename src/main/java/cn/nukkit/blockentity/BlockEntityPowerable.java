package cn.nukkit.blockentity;

public interface BlockEntityPowerable {

    void setPowered();

    void setPowered(boolean powered);

    boolean isPowered();

    String TAG_POWERED = "powered";
}
