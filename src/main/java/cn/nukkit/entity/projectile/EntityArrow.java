package cn.nukkit.entity.projectile;

//import cn.nukkit.Player;
//import cn.nukkit.Server;
//import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
//import cn.nukkit.entity.EntityHuman;
//import cn.nukkit.event.entity.ProjectileHitEvent;
//import cn.nukkit.event.inventory.InventoryPickupArrowEvent;
//import cn.nukkit.item.Item;
//import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
//import cn.nukkit.network.protocol.EntityEventPacket;
//import cn.nukkit.network.protocol.LevelSoundEventPacket;
//import cn.nukkit.network.protocol.TakeItemEntityPacket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityArrow extends EntityProjectile {

    public static final int NETWORK_ID = ARROW;

    public static final int PICKUP_NONE = 0;
    public static final int PICKUP_ANY = 1;
    public static final int PICKUP_CREATIVE = 2;

    private static String TAG_PICKUP = "pickup"; //TAG_Byte

    public static final int DATA_SOURCE_ID = 17;

    protected int pickupMode = PICKUP_ANY;

    protected double punchKnockback = 0;

    protected int collideTicks = 0;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);
        this.setCritical(critical);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.damage = namedTag.contains("damage") ? namedTag.getDouble("damage") : 2;
        this.pickupMode = this.namedTag.getByte(TAG_PICKUP, PICKUP_ANY, true);
        this.collideTicks = this.namedTag.getShort("life", this.collideTicks);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte(TAG_PICKUP, this.pickupMode);
        this.namedTag.putShort("life", this.collideTicks);
    }

    public boolean isCritical() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_CRITICAL);
    }

    public void setCritical() {
        this.setCritical(true);
    }

    public void setCritical(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CRITICAL, value);
    }

    @Override
    public int getResultDamage() {
        int base = super.getResultDamage();
        if (this.isCritical()) {
            base += ThreadLocalRandom.current().nextInt(base / 2 + 1);
        }
        return base;
    }

    @Override
    protected double getBaseDamage() {
        return 2;
    }

    public double getPunchKnockback() {
        return this.punchKnockback;
    }

    public void setPunchKnockback(double punchKnockback) {
        this.punchKnockback = punchKnockback;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        boolean hasUpdate = super.onUpdate(currentTick);

        if (this.isCollided) {
            this.collideTicks += currentTick;
            if (this.collideTicks > 1200) {
                this.flagForDespawn();
                hasUpdate = true;
            }
        } else {
            this.collideTicks = 0;
        }

        this.timing.stopTiming();

        return hasUpdate;
    }

    /*@Override
    public void onHit(ProjectileHitEvent event) {
        this.setCritical(false);
        this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BOW_HIT);
    }

    @Override
    public void onHitBlock(Block blockHit, MovingObjectPosition hitResult) {
        super.onHitBlock(blockHit, hitResult);
        this.broadcastEntityEvent(EntityEventPacket.ARROW_SHAKE, 7); //7 ticks
    }

    @Override
    public void onHitEntity(Entity entityHit, MovingObjectPosition hitResult) {
        super.onHitEntity(entityHit, hitResult);
        if (this.punchKnockback > 0) {
            double horizontalSpeed = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2));
            if (horizontalSpeed > 0) {
                double multiplier = this.punchKnockback * 0.6 / horizontalSpeed;
                entityHit.setMotion(entityHit.getMotion().add(this.motionX * multiplier, 0.1, this.motionZ * multiplier));
            }
        }
    }*/

    public int getPickupMode() {
        return this.pickupMode;
    }

    public void setPickupMode(int pickupMode) {
        this.pickupMode = pickupMode;
    }

    /*@Override
    public void onCollideWithPlayer(EntityHuman entityPlayer) {
        if (this.blockHit == null) {
            return;
        }
        Player player;
        if (entityPlayer instanceof Player) {
            player = (Player) entityPlayer;
        } else {
            return;
        }
        Item item = Item.get(Item.ARROW, 0, 1);
        if (player.isSurvival() && !player.getInventory().canAddItem(item)) {
            return;
        }
        InventoryPickupArrowEvent ev = new InventoryPickupArrowEvent(player.getInventory(), this);
        if (this.pickupMode == PICKUP_NONE || (this.pickupMode == PICKUP_CREATIVE && !player.isCreative())) {
            ev.setCancelled();
        }
        Server.getInstance().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return;
        }
        TakeItemEntityPacket pk = new TakeItemEntityPacket();
        pk.eid = player.getId();
        pk.target = this.getId();
        this.getServer().broadcastPacket(this.getViewers(), pk);
        player.getInventory().addItem(item);
        this.flagForDespawn();
    }*/
}
