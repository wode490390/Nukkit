package cn.nukkit.level.format.anvil.util;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public class BlockStorage {

    private static final int SECTION_SIZE = 4096;
    private final byte[] blockIds;
    private final NibbleArray blockData;

    public BlockStorage() {
        blockIds = new byte[SECTION_SIZE];
        blockData = new NibbleArray(SECTION_SIZE);
    }

    private BlockStorage(byte[] blockIds, NibbleArray blockData) {
        this.blockIds = blockIds;
        this.blockData = blockData;
    }

    private static int getIndex(int x, int y, int z) {
        int index = (x << 8) + (z << 4) + y; // XZY = Bedrock format
        Preconditions.checkArgument(index >= 0 && index < SECTION_SIZE, "Invalid index");
        return index;
    }

    public int getBlockData(int x, int y, int z) {
        return blockData.get(getIndex(x, y, z)) & 0xf;
    }

    public int getBlockId(int x, int y, int z) {
        int id = blockIds[getIndex(x, y, z)];
        return id < 0 ? 255 - id : id;
    }

    public void setBlockId(int x, int y, int z, int id) {
        blockIds[getIndex(x, y, z)] = (byte) (id > 0xff ? 255 - id : id);
    }

    public void setBlockData(int x, int y, int z, int data) {
        blockData.set(getIndex(x, y, z), (byte) data);
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
        int newBlock = (value & 0x1ff0) >> 4;
        if (newBlock > 0xff) newBlock = 255 - newBlock;
        byte newData = (byte) (value & 0xf);
        if (oldBlock != newBlock) blockIds[index] = (byte) newBlock;
        if (oldData != newData) blockData.set(index, newData);
        if (oldBlock < 0) oldBlock = 255 - oldBlock;
        return (oldBlock << 4) | oldData;
    }

    private int getFullBlock(int index) {
        int block = blockIds[index];
        if (block < 0) block = 255 - block;
        byte data = blockData.get(index);
        return (block << 4) | data;
    }

    private void setFullBlock(int index, short value) {
        Preconditions.checkArgument(value < 0x1fff, "Invalid full block");
        int block = (value & 0x1ff0) >> 4;
        if (block > 0xff) block = 255 - block;
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

    public BlockStorage copy() {
        return new BlockStorage(blockIds.clone(), blockData.copy());
    }
}
