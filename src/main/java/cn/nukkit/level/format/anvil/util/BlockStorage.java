package cn.nukkit.level.format.anvil.util;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public class BlockStorage {

    private static final int SECTION_SIZE = 4096;
    private final short[] blockIds;
    private final NibbleArray blockData;

    public BlockStorage() {
        blockIds = new short[SECTION_SIZE];
        blockData = new NibbleArray(SECTION_SIZE);
    }

    private BlockStorage(short[] blockIds, NibbleArray blockData) {
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
        return blockIds[getIndex(x, y, z)] & 0x1ff; //Future needs to be expanded to 0x3ff
    }

    public void setBlockId(int x, int y, int z, int id) {
        blockIds[getIndex(x, y, z)] = (short) (id & 0x1ff); //Future needs to be expanded to 0x3ff
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
        Preconditions.checkArgument(value < 0x1fff, "Invalid full block"); //Future needs to be expanded to 0x3fff
        short oldBlock = blockIds[index];
        byte oldData = blockData.get(index);
        short newBlock = (value & 0x1ff0) >> 4; //Future needs to be expanded to 0x3ff0
        byte newData = (byte) (value & 0xf);
        if (oldBlock != newBlock) {
            blockIds[index] = newBlock;
        }
        if (oldData != newData) {
            blockData.set(index, newData);
        }
        return ((oldBlock & 0x1ff) << 4) | oldData; //Future needs to be expanded to 0x3ff
    }

    private int getFullBlock(int index) {
        short block = blockIds[index];
        byte data = blockData.get(index);
        return ((block & 0x1ff) << 4) | data; //Future needs to be expanded to 0x3ff
    }

    private void setFullBlock(int index, short value) {
        Preconditions.checkArgument(value < 0x1fff, "Invalid full block"); //Future needs to be expanded to 0x3fff
        short block = (value & 0x1ff0) >> 4; //Future needs to be expanded to 0x3ff0
        byte data = (byte) (value & 0xf);

        blockIds[index] = block;
        blockData.set(index, data);
    }

    public short[] getBlockIds() {
        return Arrays.copyOf(blockIds, blockIds.length);
    }

    public byte[] getBlockData() {
        return blockData.getData();
    }

    public BlockStorage copy() {
        return new BlockStorage(blockIds.clone(), blockData.copy());
    }
}
