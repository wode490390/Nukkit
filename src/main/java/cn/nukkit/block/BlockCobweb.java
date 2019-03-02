package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/12/2 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockCobweb extends BlockFlowable {

    public BlockCobweb() {

    }

    @Override
    public String getName() {
        return "Cobweb";
    }

    @Override
    public int getId() {
        return COBWEB;
    }

    @Override
    public double getHardness() {
        return 4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SWORD;//TODO: shears
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            return new Item[]{
                    this.toItem()
            };
        } else if (item.isSword()) {
            return new Item[]{
                    Item.get(Item.STRING)
            };
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLOTH_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean diffusesSkyLight() {
        return true;
    }
}
