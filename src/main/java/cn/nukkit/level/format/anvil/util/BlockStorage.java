package cn.nukkit.level.format.anvil.util;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public class BlockStorage {

    private static final int SECTION_SIZE = 4096;
    private final byte[] blockIds;
    private final NibbleArray blockData;
    private final NibbleArray extraData;

    public BlockStorage() {
        blockIds = new byte[SECTION_SIZE];
        blockData = new NibbleArray(SECTION_SIZE);
        extraData = new NibbleArray(SECTION_SIZE);
    }

    private BlockStorage(byte[] blockIds, NibbleArray blockData, NibbleArray extraData) {
        this.blockIds = blockIds;
        this.blockData = blockData;
        this.extraData = extraData;
    }

    private static int getIndex(int x, int y, int z) {
        int index = (x << 8) + (z << 4) + y; // XZY = Bedrock format
        Preconditions.checkArgument(index >= 0 && index < SECTION_SIZE, "Invalid index");
        return index;
    }

    public int getBlockData(int x, int y, int z) {
        return blockData.get(getIndex(x, y, z)) & 0xf;
    }

    public int getExtraData(int x, int y, int z) {
        return extraData.get(getIndex(x, y, z)) & 0xf;
    }

    public int getBlockId(int x, int y, int z) {
        int id = blockIds[getIndex(x, y, z)];
        return this.getExtraData(x, y, z) > 0 ? 0xff - id : id;
    }

    public void setBlockId(int x, int y, int z, int id) {
        blockIds[getIndex(x, y, z)] = (byte) (id & 0xff);
        if (id > 0xff) this.setExtraData(x, y, z, 1);
    }

    public void setBlockData(int x, int y, int z, int data) {
        blockData.set(getIndex(x, y, z), (byte) data);
    }

    public void setExtraData(int x, int y, int z, int data) {
        extraData.set(getIndex(x, y, z), (byte) data);
    }

    public int getFullBlock(int x, int y, int z) {
        return getFullBlock(getIndex(x, y, z));
    }

    public void setFullBlock(int x, int y, int z, int value) {
        this.setFullBlock(getIndex(x, y, z), (short) value);
    }

    public int getAndSetFullBlock(int x, int y, int z, int value) {
        return getAndSetFullBlock(getIndex(x, y, z), (short) value);
    }

    private int getAndSetFullBlock(int index, short value) {
        Preconditions.checkArgument(value < 0x1fff, "Invalid full block"); //Future needs to be expanded to 0x3fff, similarly hereinafter
        int oldBlock = blockIds[index];
        byte oldData = blockData.get(index);
        byte oldExtra = extraData.get(index);
        int newBlock = (value & 0x1ff0) >> 4;
        byte newExtra = 0;
        if (newBlock > 0xff) {
            newExtra = 1;
            newBlock &= 0xff;
        }
        byte newData = (byte) (value & 0xf);
        if (oldBlock != newBlock) blockIds[index] = (byte) newBlock;
        if (oldData != newData) blockData.set(index, newData);
        if (oldExtra != newExtra) extraData.set(index, newExtra);
        if (oldExtra > 0) oldBlock += 256;
        return (oldBlock << 4) | oldData;
    }

    private int getFullBlock(int index) {
        int block = blockIds[index];
        if (extraData.get(index) > 0) block += 256;
        byte data = blockData.get(index);
        return (block << 4) | data;
    }

    private void setFullBlock(int index, short value) {
        Preconditions.checkArgument(value < 0x1fff, "Invalid full block");
        int block = (value & 0x1ff0) >> 4;
        if (block > 0xff) {
            extraData.set(index, 1);
            block &= 0xff;
        }
        byte data = (byte) (value & 0xf);

        blockIds[index] = (byte) block;
        blockData.set(index, data);
    }

    public byte[] getBlockIds() {
        return Arrays.copyOf(blockIds, blockIds.length);
    }

    public byte[] getBlockData() {
        return blockData.getData();
    }

    public byte[] getBlockExtraData() {
        return extraData.getData();
    }

    public BlockStorage copy() {
        return new BlockStorage(blockIds.clone(), blockData.copy(), extraData.copy());
    }
}
