package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 2015/12/2 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockDeadBush extends BlockFlowable {

    public BlockDeadBush() {

    }

    @Override
    public String getName() {
        return "Dead Bush";
    }

    @Override
    public int getId() {
        return DEAD_BUSH;
    }
    
    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = this.down();
        if (down.getId() == SAND || down.getId() == TERRACOTTA || down.getId() == STAINED_TERRACOTTA || down.getId() == PODZOL || down.getId() == DIRT) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }


    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.down().isTransparent()) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            return new Item[]{
                    this.toItem()
            };
        } else {
            return new Item[]{
                    Item.get(Item.STICK, 0, ThreadLocalRandom.current().nextInt(3))
            };
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
