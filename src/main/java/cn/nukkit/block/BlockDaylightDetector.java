package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/11/22 by CreeperFace.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockDaylightDetector extends BlockTransparentMeta {

    public BlockDaylightDetector() {
        this(0);
    }

    public BlockDaylightDetector(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DAYLIGHT_DETECTOR;
    }

    @Override
    public String getName() {
        return "Daylight Detector";
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        this.getLevel().setBlock(this, new BlockDaylightDetectorInverted());
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }

    //This function is a suggestion that can be renamed or deleted
    protected boolean invertDetect() {
        return false;
    }

    //todo redstone
}
