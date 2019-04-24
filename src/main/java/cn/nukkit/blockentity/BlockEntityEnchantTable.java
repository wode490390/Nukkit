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
        return this.getBlock().getId() == Block.ENCHANT_TABLE;
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
        CompoundTag c = getDefaultCompound(this, ENCHANT_TABLE);

        if (this.hasName()) {
            c.put("CustomName", this.namedTag.get("CustomName"));
        }

        return c;
    }
}
