package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;

public class ItemLead extends Item {

    public ItemLead() {
        this(0, 1);
    }

    public ItemLead(Integer meta) {
        this(meta, 1);
    }

    public ItemLead(Integer meta, int count) {
        super(LEAD, meta, count, "Lead");
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        if (target instanceof BlockFence) {
            //TODO
            return true;
        }
        return false;
    }
}
