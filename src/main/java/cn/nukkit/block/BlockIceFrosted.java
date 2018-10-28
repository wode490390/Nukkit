package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.BlockColor;
import java.util.ArrayList;
import java.util.List;

public class BlockIceFrosted extends BlockIce {

    protected int age;

    public BlockIceFrosted() {
        this.age = 0;
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
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockWaterStill(), true);
        List<Block> nearFrosted = new ArrayList<Block>();
        int floorX = this.getFloorX();
        int coordX1 = floorX - 1;
        int coordX2 = floorX + 1;
        int floorZ = this.getFloorZ();
        int coordZ1 = floorZ - 1;
        int coordZ2 = floorZ + 1;
        int floorY = this.getFloorY();
        for (int coordX = coordX1; coordX < coordX2 + 1; coordX++) {
            for (int coordZ = coordZ1; coordZ < coordZ2 + 1; coordZ++) {
                Block nearBlock = this.getLevel().getBlock(coordX, floorY, coordZ);
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

    protected int getAge() {
        return this.age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            //if (this.getLevel().getBlockLightAt((int) this.x, (int) this.y, (int) this.z) > 11) {
                List<Block> nearFrosted = new ArrayList<Block>();
                int floorX = this.getFloorX();
                int coordX1 = floorX - 1;
                int coordX2 = floorX + 1;
                int floorZ = this.getFloorZ();
                int coordZ1 = floorZ - 1;
                int coordZ2 = floorZ + 1;
                int floorY = this.getFloorY();
                for (int coordX = coordX1; coordX < coordX2 + 1; coordX++) {
                    for (int coordZ = coordZ1; coordZ < coordZ2 + 1; coordZ++) {
                        Block nearBlock = this.getLevel().getBlock(coordX, floorY, coordZ);
                        if (nearBlock instanceof BlockIceFrosted) {
                            nearFrosted.add(nearBlock);
                        }

                    }
                }
                int age = this.getDamage();
                if ((new NukkitRandom()).nextRange(0, 2) == 0 || nearFrosted.size() < 4) {
                    if (age >= 1) {
                        age--;
                        this.setDamage(age);
                    }
                }
                if (age + 1 > 3) {
                    this.getLevel().setBlock(this, new BlockWaterStill(), true);
                    if (nearFrosted.size() < 2) {
                        for (Block iceBlock : nearFrosted) {
                            this.getLevel().setBlock(iceBlock, new BlockWaterStill(), true);
                        }
                    } else {
                        for (Block iceBlock : nearFrosted) {
                            iceBlock.setDamage(iceBlock.getDamage() + 1);
                        }
                    }
                } else {
                    this.setDamage(age + 1);
                }
                return Level.BLOCK_UPDATE_RANDOM;
            //}
        }
        return 0;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockAir());
    }

    @Override
    public boolean canSilkTouch() {
        return false;
    }
}
