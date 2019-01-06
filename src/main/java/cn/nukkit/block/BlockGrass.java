package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.object.ObjectTallGrass;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockGrass extends BlockDirt {

    public BlockGrass() {
    }

    @Override
    public int getId() {
        return GRASS;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == Item.DYE && item.getDamage() == 0x0F) {
            item.count--;
            ObjectTallGrass.growGrass(this.getLevel(), this, new NukkitRandom(), 15, 10);
            return true;
        } else if (item.isHoe()) {
            item.useOn(this);
            this.getLevel().setBlock(this, new BlockFarmland());
            return true;
        } else if (item.isShovel()) {
            item.useOn(this);
            this.getLevel().setBlock(this, new BlockGrassPath());
            return true;
        }

        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            Block above = this.up();
            int light = this.getLevel().getFullLight(above);

            if (light < 4 && Block.lightFilter[above.getId()] >= 3) {
                BlockSpreadEvent ev = new BlockSpreadEvent(this, this, new BlockDirt());
                Server.getInstance().getPluginManager().callEvent(ev);

                if (!ev.isCancelled()) {
                    this.getLevel().setBlock(this, ev.getNewState(), false, false);
                }
            } else if (light >= 9) {
                for (int i = 0; i < 4; ++i) {
                    int x = ThreadLocalRandom.current().nextInt(this.getFloorX() - 1, this.getFloorX() + 1);
                    int y = ThreadLocalRandom.current().nextInt(this.getFloorY() - 3, this.getFloorY() + 1);
                    int z = ThreadLocalRandom.current().nextInt(this.getFloorZ() - 1, this.getFloorZ() + 1);

                    Block block = this.getLevel().getBlock(new Vector3(x, y, z));

                    if (block.getId() == Block.DIRT && block.getDamage() == 0 && this.level.getFullLight(above) >= 4 && Block.lightFilter[above.getId()] < 3) {
                        BlockSpreadEvent ev = new BlockSpreadEvent(block, this, new BlockGrass());
                        Server.getInstance().getPluginManager().callEvent(ev);
                        if (!ev.isCancelled()) {
                            this.getLevel().setBlock(block, ev.getNewState());
                        }
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRASS_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
