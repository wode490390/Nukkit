package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockEntityEnchantTable extends BlockEntitySpawnable implements BlockEntityNameable {

    public static final String TAG_ROTT = "rott"; //float

    public float rott = 0; //???

    public BlockEntityEnchantTable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock().getId() == Block.ENCHANT_TABLE;
    }

    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains(TAG_ROTT)) {
            this.rott = this.namedTag.getFloat(TAG_ROTT);
        }

        super.initBlockEntity();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putFloat(TAG_ROTT, this.rott);
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Enchanting Table";
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains("CustomName");
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.namedTag.remove("CustomName");
            return;
        }

        this.namedTag.putString("CustomName", name);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag c = getDefaultCompound(this, ENCHANT_TABLE)
                .putFloat(TAG_ROTT, rott);

        if (this.hasName()) {
            c.put("CustomName", this.namedTag.get("CustomName"));
        }

        return c;
    }

    /*public int countBookshelf() {
        int count = 0;
        for (int y = 0; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (z == 0 && x == 0) {
                        continue;
                    }
                    if (this.getLevel().getBlock(this.add(x, 0, z)).isTransparent()) {
                        if (this.getLevel().getBlock(this.add(0, 1, 0)).isTransparent()) {
                            //diagonal and straight
                            if (this.getLevel().getBlock(this.add(x << 1, y, z << 1)).getId() == Block.BOOKSHELF) {
                                count++;
                            }

                            if (x != 0 && z != 0) {
                                //one block diagonal and one straight
                                if (this.getLevel().getBlock(this.add(x << 1, y, z)).getId() == Block.BOOKSHELF) {
                                    ++count;
                                }

                                if (this.getLevel().getBlock(this.add(x, y, z << 1)).getId() == Block.BOOKSHELF) {
                                    ++count;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }*/
}
