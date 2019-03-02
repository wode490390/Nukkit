package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.randomitem.Fishing;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.level.particle.WaterParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by PetteriM1
 */
public class EntityFishingHook extends EntityProjectile {

    public static final int NETWORK_ID = 77;

    public static final int WAIT_CHANCE = 120;
    public static final int CHANCE = 40;

    public boolean chance = false;
    public int waitChance = WAIT_CHANCE * 2;
    public boolean attracted = false;
    public int attractTimer = 0;
    public boolean caught = false;
    public int coughtTimer = 0;

    public Vector3 fish;

    public Item rod;

    public EntityFishingHook(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityFishingHook(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.2f;
    }

    @Override
    public float getLength() {
        return 0.2f;
    }

    @Override
    public float getHeight() {
        return 0.2f;
    }

    @Override
    public float getGravity() {
        return 0.08f;
    }

    @Override
    public float getDrag() {
        return 0.05f;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        boolean hasUpdate = super.onUpdate(currentTick);
        if (hasUpdate) {
            return false;
        }

        if (this.isInsideOfWater()) {
            this.motionX = 0;
            this.motionY -= getGravity() * -0.03;
            this.motionZ = 0;
            hasUpdate = true;
        } else if (this.isCollided && this.keepMovement) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.keepMovement = false;
            hasUpdate = true;
        } else if (this.isOnGround() || this.isInsideOfSolid()) {
            this.motionX = 0;
            this.motionY = getGravity();
            this.motionZ = 0;
            hasUpdate = true;
        }

        if (this.isInsideOfWater()) {
            if (!this.attracted) {
                if (this.waitChance > 0) {
                    --this.waitChance;
                }
                if (this.waitChance == 0) {
                    if (ThreadLocalRandom.current().nextInt(100) < 90) {
                        this.attractTimer = ThreadLocalRandom.current().nextInt(20, 60);
                        this.spawnFish();
                        this.caught = false;
                        this.attracted = true;
                    } else {
                        this.waitChance = WAIT_CHANCE;
                    }
                }
            } else if (!this.caught) {
                if (this.attractFish()) {
                    this.coughtTimer = ThreadLocalRandom.current().nextInt(30, 50);
                    this.fishBites();
                    this.caught = true;
                }
            } else {
                if (this.coughtTimer > 0) {
                    --this.coughtTimer;
                }
                if (this.coughtTimer == 0) {
                    this.attracted = false;
                    this.caught = false;
                    this.waitChance = WAIT_CHANCE * 3;
                }
            }
        }

        return hasUpdate;
    }

    public int getWaterHeight() {
        for (int y = this.getFloorY(); y < 256; y++) {
            int id = this.level.getBlockIdAt(this.getFloorX(), y, this.getFloorZ());
            if (id == Block.AIR) {
                return y;
            }
        }
        return this.getFloorY();
    }

    public void fishBites() {
        EntityEventPacket pk = new EntityEventPacket();
        pk.entityRuntimeId = this.getId();
        pk.event = EntityEventPacket.FISH_HOOK_HOOK;
        Server.broadcastPacket(this.level.getPlayers().values(), pk);

        EntityEventPacket bubblePk = new EntityEventPacket();
        bubblePk.entityRuntimeId = this.getId();
        bubblePk.event = EntityEventPacket.FISH_HOOK_BUBBLE;
        Server.broadcastPacket(this.level.getPlayers().values(), bubblePk);

        EntityEventPacket teasePk = new EntityEventPacket();
        teasePk.entityRuntimeId = this.getId();
        teasePk.event = EntityEventPacket.FISH_HOOK_TEASE;
        Server.broadcastPacket(this.level.getPlayers().values(), teasePk);

        for (int i = 0; i < 5; i++) {
            this.level.addParticle(new BubbleParticle(this.setComponents(
                    this.x + ThreadLocalRandom.current().nextDouble() * 0.5 - 0.25,
                    this.getWaterHeight(),
                    this.z + ThreadLocalRandom.current().nextDouble() * 0.5 - 0.25
            )));
        }
    }

    public void spawnFish() {
        this.fish = new Vector3(
                this.x + (ThreadLocalRandom.current().nextDouble() * 1.2 + 1) * (ThreadLocalRandom.current().nextBoolean() ? -1 : 1),
                this.getWaterHeight(),
                this.z + (ThreadLocalRandom.current().nextDouble() * 1.2 + 1) * (ThreadLocalRandom.current().nextBoolean() ? -1 : 1)
        );
    }

    public boolean attractFish() {
        double multiply = 0.1;
        this.fish.setComponents(
                this.fish.x + (this.x - this.fish.x) * multiply,
                this.fish.y,
                this.fish.z + (this.z - this.fish.z) * multiply
        );
        if (ThreadLocalRandom.current().nextInt(100) < 85) {
            this.level.addParticle(new WaterParticle(this.fish));
        }
        double dist = Math.abs(Math.sqrt(this.x * this.x + this.z * this.z) - Math.sqrt(this.fish.x * this.fish.x + this.fish.z * this.fish.z));
        if (dist < 0.15) {
            return true;
        }
        return false;
    }

    public void reelLine() {
        if (this.shootingEntity instanceof Player && this.caught) {
            Item item = Fishing.getFishingResult(this.rod);
            int experience = ThreadLocalRandom.current().nextInt(1, 4);
            Vector3 motion;

            if (this.shootingEntity != null) {
                motion = this.shootingEntity.subtract(this).multiply(0.1);
                motion.y += Math.sqrt(this.shootingEntity.distance(this)) * 0.08;
            } else {
                motion = new Vector3();
            }

            CompoundTag itemTag = NBTIO.putItemHelper(item);
            itemTag.setName("Item");

            EntityItem itemEntity = new EntityItem(
                    this.level.getChunk((int) this.x >> 4, (int) this.z >> 4, true),
                    new CompoundTag()
                            .putList(new ListTag<DoubleTag>("Pos")
                                    .add(new DoubleTag("", this.getX()))
                                    .add(new DoubleTag("", this.getWaterHeight()))
                                    .add(new DoubleTag("", this.getZ())))
                            .putList(new ListTag<DoubleTag>("Motion")
                                    .add(new DoubleTag("", motion.x))
                                    .add(new DoubleTag("", motion.y))
                                    .add(new DoubleTag("", motion.z)))
                            .putList(new ListTag<FloatTag>("Rotation")
                                    .add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360))
                                    .add(new FloatTag("", 0)))
                            .putShort("Health", 5).putCompound("Item", itemTag).putShort("PickupDelay", 1));

            if (this.shootingEntity != null && this.shootingEntity instanceof Player) {
                itemEntity.setOwner(this.shootingEntity.getName());
            }
            itemEntity.spawnToAll();

            Player player = (Player) this.shootingEntity;
            if (experience > 0) {
                player.addExperience(experience);
            }
        }
        if (this.shootingEntity instanceof Player) {
            EntityEventPacket pk = new EntityEventPacket();
            pk.entityRuntimeId = this.getId();
            pk.event = EntityEventPacket.FISH_HOOK_TEASE;
            Server.broadcastPacket(this.level.getPlayers().values(), pk);
        }
        if (!this.closed) {
            this.kill();
            this.close();
        }
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityRuntimeId = this.getId();
        pk.entityUniqueId = this.getId();
        pk.type = NETWORK_ID;
        pk.position = new Vector3(this.x, this.y, this.z).asVector3f();
        pk.motion = new Vector3(this.motionX, this.motionY, this.motionZ).asVector3f();
        pk.yaw = (float) this.yaw;
        pk.pitch = (float) this.pitch;

        long ownerId = -1;
        if (this.shootingEntity != null) {
            ownerId = this.shootingEntity.getId();
        }
        pk.metadata = this.dataProperties.putLong(DATA_OWNER_EID, ownerId);
        player.dataPacket(pk);
        super.spawnTo(player);
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.server.getPluginManager().callEvent(new ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)));
        float damage = this.getResultDamage();

        EntityDamageEvent ev;
        if (this.shootingEntity == null) {
            ev = new EntityDamageByEntityEvent(this, entity, DamageCause.PROJECTILE, damage);
        } else {
            ev = new EntityDamageByChildEntityEvent(this.shootingEntity, this, entity, DamageCause.PROJECTILE, damage);
        }

        entity.attack(ev);
    }
}
