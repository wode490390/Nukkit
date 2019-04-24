package cn.nukkit.blockentity;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.NumberTag;

/**
 * @author CreeperFace
 */
public class BlockEntityPistonArm extends BlockEntitySpawnable {

    public float progress = 1;
    public float lastProgress = 0;
    public BlockFace facing;
    public boolean extending = false;
    public boolean sticky = false;
    public byte state = 1;
    public byte newState = 1;
    public Vector3 attachedBlock;
    public boolean isMovable = true;
    public boolean powered = false;

    public BlockEntityPistonArm(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (namedTag.contains("Progress")) {
            this.progress = namedTag.getFloat("Progress");
        }
        if (namedTag.contains("LastProgress")) {
            this.lastProgress = namedTag.getInt("LastProgress");
        }
        if (namedTag.contains("Sticky")) {
            this.sticky = namedTag.getBoolean("Sticky");
        }
        if (namedTag.contains("Extending")) {
            this.extending = namedTag.getBoolean("Extending");
        }
        if (namedTag.contains("powered")) {
            this.powered = namedTag.getBoolean("powered");
        }
        if (namedTag.contains("AttachedBlocks")) {
            ListTag<?> blocks = namedTag.getList("AttachedBlocks", IntTag.class);
            if (blocks != null && blocks.size() > 0) {
                this.attachedBlock = new Vector3((double) ((NumberTag<Integer>) blocks.get(0)).getData(), (double) ((NumberTag<Integer>) blocks.get(1)).getData(), (double) ((NumberTag<Integer>) blocks.get(2)).getData());
            }
        } else {
            namedTag.putList(new ListTag("AttachedBlocks"));
        }

        super.initBlockEntity();
    }

    private void pushEntities() {
        float lastProgress = this.getExtendedProgress(this.lastProgress);
        double x = lastProgress * this.facing.getXOffset();
        double y = lastProgress * this.facing.getYOffset();
        double z = lastProgress * this.facing.getZOffset();
        AxisAlignedBB bb = new SimpleAxisAlignedBB(x, y, z, x + 1.0d, y + 1.0d, z + 1.0d);
        Entity[] entities = this.level.getCollidingEntities(bb);
        if (entities.length != 0) {
            //TODO
        }
    }

    private float getExtendedProgress(float progress) {
        return this.extending ? progress - 1.0f : 1.0f - progress;
    }

    @Override
    public boolean isBlockEntityValid() {
        return true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("isMovable", this.isMovable);
        this.namedTag.putByte("State", this.state);
        this.namedTag.putByte("NewState", this.newState);
        this.namedTag.putFloat("Progress", this.progress);
        this.namedTag.putFloat("LastProgress", this.lastProgress);
        this.namedTag.putBoolean("powered", this.powered);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return getDefaultCompound(this, PISTON_ARM)
                .putFloat("Progress", this.progress)
                .putBoolean("isMovable", this.isMovable)
                .putByte("State", this.state)
                .putFloat("LastProgress", this.lastProgress)
                .putByte("NewState", this.newState)
                .putList(new ListTag<>("BreakBlocks"))
                .putList(new ListTag<>("AttachedBlocks"))
                .putBoolean("Sticky", this.sticky);
    }
}
