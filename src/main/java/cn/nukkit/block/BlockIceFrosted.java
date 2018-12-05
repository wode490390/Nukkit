package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Level;
import cn.nukkit.utils.BlockColor;

import java.util.ArrayList;
import java.util.List;

public class BlockIceFrosted extends BlockTransparentMeta {

    public BlockIceFrosted() {
        this(0);
    }

    public BlockIceFrosted(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return FROSTED_ICE;
    }

    @Override
    public String getName() {
        return "Frosted Ice";
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getFrictionFactor() {
        return 0.98;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockWaterStill(), true);
        List<Block> nearFrosted = new ArrayList<Block>();
        int coordX1 = (int) this.x - 1;
        int coordX2 = (int) this.x + 1;
        int coordZ1 = (int) this.z - 1;
        int coordZ2 = (int) this.z + 1;
        for (int coordX = coordX1; coordX < coordX2 + 1; coordX++) {
            for (int coordZ = coordZ1; coordZ < coordZ2 + 1; coordZ++) {
                Block nearBlock = this.getLevel().getBlock(coordX, (int) this.y, coordZ);
                if (nearBlock instanceof BlockIceFrosted) {
                    nearFrosted.add(nearBlock);
                }

            }
        }
        if (nearFrosted.size() < 2) {
            for (Block iceBlock : nearFrosted) {
                this.getLevel().setBlock(iceBlock, new BlockWaterStill(), true);
            }
        }
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (this.getLevel().getBlockLightAt((int) this.x, (int) this.y, (int) this.z) >= 12) {
                this.getLevel().setBlock(this, new BlockWater(), true);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        //TODO: melt
        return 0;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockAir());
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ICE_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
