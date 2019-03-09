package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockRedstoneEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

/**
 * Created by CreeperFace on 27. 11. 2016.
 */
public abstract class BlockButton extends BlockFlowableMeta implements BlockFaceable {

    protected BlockButton(int meta) {
        super(meta);
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (target.isTransparent()) {
            return false;
        }

        this.setDamage(face.getIndex());
        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (this.isActivated()) {
            return false;
        }

        this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 0, 15));
        this.setDamage(this.getDamage() ^ 0x8);
        this.level.setBlock(this, this, true, false);
        this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.RANDOM_CLICK);
        this.level.scheduleUpdate(this, 30);
        Vector3 pos = getLocation();

        level.updateAroundRedstone(pos, null);
        level.updateAroundRedstone(pos.getSide(getFacing().getOpposite()), null);
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(getFacing().getOpposite()).isTransparent()) {
                this.level.useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (this.isActivated()) {
                this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 15, 0));

                this.setDamage(this.getDamage() ^ 0x8);
                this.level.setBlock(this, this, true, false);
                this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.RANDOM_CLICK);

                Vector3 pos = getLocation();
                level.updateAroundRedstone(pos, null);
                level.updateAroundRedstone(pos.getSide(getFacing().getOpposite()), null);
            }

            return Level.BLOCK_UPDATE_SCHEDULED;
        }

        return 0;
    }

    public boolean isActivated() {
        return ((this.getDamage() & 0x8) == 0x8);
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getWeakPower(BlockFace side) {
        return isActivated() ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace side) {
        return !isActivated() ? 0 : (getFacing() == side ? 15 : 0);
    }

    public BlockFace getFacing() {
        int side = isActivated() ? getDamage() ^ 0x8 : getDamage();
        return BlockFace.fromIndex(side);
    }

    @Override
    public boolean onBreak(Item item) {
        if (isActivated()) {
            this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 15, 0));
        }

        return super.onBreak(item);
    }

    @Override
    public Item toItem() {
        return Item.get(this.getId(), 0, 1);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
