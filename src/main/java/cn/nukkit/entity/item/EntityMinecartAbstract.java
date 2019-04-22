package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.api.API;
import cn.nukkit.api.API.Definition;
import cn.nukkit.api.API.Usage;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.block.BlockRailActivator;
import cn.nukkit.block.BlockRailPowered;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.vehicle.VehicleMoveEvent;
import cn.nukkit.event.vehicle.VehicleUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MinecartType;
import cn.nukkit.utils.Rail;
import cn.nukkit.utils.Rail.Orientation;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by: larryTheCoder on 2017/6/26.
 * <p>
 * Nukkit Project,
 * Minecart and Riding Project,
 * Package cn.nukkit.entity.item in project Nukkit.
 */
public abstract class EntityMinecartAbstract extends EntityVehicle {

    private String entityName;
    private static final int[][][] matrix = new int[][][]{
            {{0, 0, -1}, {0, 0, 1}},
            {{-1, 0, 0}, {1, 0, 0}},
            {{-1, -1, 0}, {1, 0, 0}},
            {{-1, 0, 0}, {1, -1, 0}},
            {{0, 0, -1}, {0, -1, 1}},
            {{0, -1, -1}, {0, 0, 1}},
            {{0, 0, 1}, {1, 0, 0}},
            {{0, 0, 1}, {-1, 0, 0}},
            {{0, 0, -1}, {-1, 0, 0}},
            {{0, 0, -1}, {1, 0, 0}}
    };
    private double currentSpeed = 0;
    private Block blockInside;
    // Plugins modifiers
    private boolean slowWhenEmpty = true;
    private double derailedX = 0.5;
    private double derailedY = 0.5;
    private double derailedZ = 0.5;
    private double flyingX = 0.95;
    private double flyingY = 0.95;
    private double flyingZ = 0.95;
    private double maxSpeed = 0.4;
    private final boolean devs = false; // Avoid maintained features into production

    public abstract MinecartType getType();

    public abstract boolean isRideable();

    public EntityMinecartAbstract(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.setMaxHealth(40);
        this.setHealth(40);
    }

    @Override
    public float getHeight() {
        return 0.7f;
    }

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    protected float getDrag() {
        return 0.1f;
    }

    public void setName(String name) {
        this.entityName = name;
    }

    @Override
    public String getName() {
        return this.entityName;
    }

    @Override
    public float getBaseOffset() {
        return 0.35f;
    }

    @Override
    public boolean hasCustomName() {
        return this.entityName != null;
    }

    @Override
    public boolean canDoInteraction() {
        return this.passengers.isEmpty() && this.getDisplayBlock() == null;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.prepareDataProperty();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (!this.isAlive()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
                this.close();
            }
            return this.deadTicks < 10;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0) {
            return false;
        }

        this.lastUpdate = currentTick;

        if (this.isAlive()) {
            super.onUpdate(currentTick);

            // The damage token
            if (this.getHealth() < 20) {
                this.setHealth(this.getHealth() + 1);
            }

            // Entity variables
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.motionY -= 0.03999999910593033;
            int dx = MathHelper.floor(this.x);
            int dy = MathHelper.floor(this.y);
            int dz = MathHelper.floor(this.z);

            // Some hack to check rails
            if (Rail.isRailBlock(this.level.getBlockIdAt(dx, dy - 1, dz))) {
                --dy;
            }

            Block block = this.level.getBlock(new Vector3(dx, dy, dz));

            // Ensure that the block is a rail
            if (Rail.isRailBlock(block)) {
                this.processMovement(dx, dy, dz, (BlockRail) block);
                if (block instanceof BlockRailActivator) {
                    // Activate the minecart/TNT
                    this.activate(dx, dy, dz, (block.getDamage() & 0x8) != 0);
                }
            } else {
                this.setFalling();
            }
            this.checkBlockCollision();

            // Minecart head
            this.pitch = 0;
            double diffX = this.lastX - this.x;
            double diffZ = this.lastZ - this.z;
            double yawToChange = this.yaw;
            if (Math.pow(diffX, 2) + Math.pow(diffZ, 2) > 0.001) {
                yawToChange = (Math.atan2(diffZ, diffX) * 180 / Math.PI);
            }

            // Reverse yaw if yaw is below 0
            if (yawToChange < 0) {
                // -90-(-90)-(-90) = 90
                yawToChange -= yawToChange - yawToChange;
            }

            this.setRotation(yawToChange, this.pitch);

            Location from = new Location(this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch, this.level);
            Location to = new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.level);

            this.getServer().getPluginManager().callEvent(new VehicleUpdateEvent(this));

            if (!from.equals(to)) {
                this.getServer().getPluginManager().callEvent(new VehicleMoveEvent(this, from, to));
            }

            // Collisions
            for (Entity entity : this.level.getNearbyEntities(this.boundingBox.grow(0.2, 0, 0.2), this)) {
                if (!this.passengers.contains(entity) && entity instanceof EntityMinecartAbstract) {
                    entity.applyEntityCollision(this);
                }
            }

            Iterator<Entity> linkedIterator = this.passengers.iterator();

            while (linkedIterator.hasNext()) {
                Entity linked = linkedIterator.next();

                if (!linked.isAlive()) {
                    if (linked.riding == this) {
                        linked.riding = null;
                    }

                    linkedIterator.remove();
                }
            }

            // No need to onGround or Motion diff! This always have an update
            return true;
        }

        return false;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (this.invulnerable) {
            return false;
        } else {
            source.setDamage(source.getDamage() * 15);

            boolean attack = super.attack(source);

            if (this.isAlive()) {
                this.performHurtAnimation();
            }

            return attack;
        }
    }

    public void dropItem() {
        this.level.dropItem(this, Item.get(Item.MINECART));
    }

    @Override
    public void kill() {
        super.kill();

        if (this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
            this.dropItem();
        }
    }

    @Override
    public void close() {
        super.close();

        for (Entity entity : this.passengers) {
            if (entity instanceof Player) {
                entity.riding = null;
            }
        }

        SmokeParticle particle = new SmokeParticle(this);
        this.level.addParticle(particle);
    }

    @Override
    public boolean onInteract(Player p, Item item) {
        if (!this.passengers.isEmpty() && isRideable()) {
            return false;
        }

        // Simple
        return this.blockInside == null && this.mountEntity(p);
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (entity != this.riding) {
            if (entity instanceof EntityLiving
                    && !(entity instanceof EntityHuman)
                    && Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2) > 0.01
                    && this.passengers.isEmpty()
                    && entity.riding == null
                    && this.blockInside == null) {
                if (this.riding == null && this.devs) {
                    this.mountEntity(entity);// TODO: rewrite (weird riding)
                }
            }

            double motiveX = entity.x - this.x;
            double motiveZ = entity.z - this.z;
            double square = Math.pow(motiveX, 2) + Math.pow(motiveZ, 2);

            if (square >= 9.999999747378752E-5D) {
                square = Math.sqrt(square);
                motiveX /= square;
                motiveZ /= square;
                double next = 1 / square;

                if (next > 1) {
                    next = 1;
                }

                motiveX *= next;
                motiveZ *= next;
                motiveX *= 0.10000000149011612;
                motiveZ *= 0.10000000149011612;
                motiveX *= 1 + this.entityCollisionReduction;
                motiveZ *= 1 + this.entityCollisionReduction;
                motiveX *= 0.5;
                motiveZ *= 0.5;
                if (entity instanceof EntityMinecartAbstract) {
                    EntityMinecartAbstract mine = (EntityMinecartAbstract) entity;
                    double desinityX = mine.x - this.x;
                    double desinityZ = mine.z - this.z;
                    Vector3 vector = new Vector3(desinityX, 0, desinityZ).normalize();
                    Vector3 vec = new Vector3(MathHelper.cos((float) this.yaw * 0.017453292f), 0, MathHelper.sin((float) this.yaw * 0.017453292f)).normalize();
                    double desinityXZ = Math.abs(vector.dot(vec));

                    if (desinityXZ < 0.800000011920929) {
                        return;
                    }

                    double motX = mine.motionX + this.motionX;
                    double motZ = mine.motionZ + this.motionZ;

                    if (mine.getType().getId() == 2 && this.getType().getId() != 2) {
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.motionX += mine.motionX - motiveX;
                        this.motionZ += mine.motionZ - motiveZ;
                        mine.motionX *= 0.949999988079071;
                        mine.motionZ *= 0.949999988079071;
                    } else if (mine.getType().getId() != 2 && this.getType().getId() == 2) {
                        mine.motionX *= 0.20000000298023224;
                        mine.motionZ *= 0.20000000298023224;
                        this.motionX += mine.motionX + motiveX;
                        this.motionZ += mine.motionZ + motiveZ;
                        this.motionX *= 0.949999988079071;
                        this.motionZ *= 0.949999988079071;
                    } else {
                        motX /= 2;
                        motZ /= 2;
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.motionX += motX - motiveX;
                        this.motionZ += motZ - motiveZ;
                        mine.motionX *= 0.20000000298023224;
                        mine.motionZ *= 0.20000000298023224;
                        mine.motionX += motX + motiveX;
                        mine.motionZ += motZ + motiveZ;
                    }
                } else {
                    this.motionX -= motiveX;
                    this.motionZ -= motiveZ;
                }
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.saveEntityData();
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    protected void activate(int x, int y, int z, boolean flag) {

    }

    private boolean hasUpdated = false;

    private void setFalling() {
        this.motionX = NukkitMath.clamp(this.motionX, -this.getMaxSpeed(), this.getMaxSpeed());
        this.motionZ = NukkitMath.clamp(this.motionZ, -this.getMaxSpeed(), this.getMaxSpeed());

        if (!this.hasUpdated) {
            for (Entity linked : this.passengers) {
                linked.setSeatPosition(getMountedOffset(linked).add(0, 0.35f));
                this.updatePassengerPosition(linked);
            }

            this.hasUpdated = true;
        }

        if (this.onGround) {
            this.motionX *= this.derailedX;
            this.motionY *= this.derailedY;
            this.motionZ *= this.derailedZ;
        }

        move(this.motionX, this.motionY, this.motionZ);
        if (!this.onGround) {
            this.motionX *= this.flyingX;
            this.motionY *= this.flyingY;
            this.motionZ *= this.flyingZ;
        }
    }

    private void processMovement(int dx, int dy, int dz, BlockRail block) {
        this.fallDistance = 0;
        Vector3 vector = this.getNextRail(this.x, this.y, this.z);

        this.y = dy;
        boolean isPowered = false;
        boolean isSlowed = false;

        if (block instanceof BlockRailPowered) {
            isPowered = block.isActive();
            isSlowed = !block.isActive();
        }

        switch (Orientation.byMetadata(block.getRealMeta())) {
            case ASCENDING_NORTH:
                motionX -= 0.0078125;
                this.y += 1;
                break;
            case ASCENDING_SOUTH:
                motionX += 0.0078125;
                this.y += 1;
                break;
            case ASCENDING_EAST:
                motionZ += 0.0078125;
                this.y += 1;
                break;
            case ASCENDING_WEST:
                motionZ -= 0.0078125;
                this.y += 1;
                break;
        }

        int[][] facing = matrix[block.getRealMeta()];
        double facing1 = facing[1][0] - facing[0][0];
        double facing2 = facing[1][2] - facing[0][2];
        double speedOnTurns = Math.sqrt(Math.pow(facing1, 2) + Math.pow(facing2, 2));
        double realFacing = this.motionX * facing1 + this.motionZ * facing2;

        if (realFacing < 0) {
            facing1 = -facing1;
            facing2 = -facing2;
        }

        double squareOfFame = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2));

        if (squareOfFame > 2) {
            squareOfFame = 2;
        }

        this.motionX = squareOfFame * facing1 / speedOnTurns;
        this.motionZ = squareOfFame * facing2 / speedOnTurns;
        double expectedSpeed;
        double playerYawNeg; // PlayerYawNegative
        double playerYawPos; // PlayerYawPositive
        double motion;

        Entity linked = this.getPassenger();

        if (linked instanceof EntityLiving) {
            expectedSpeed = this.currentSpeed;
            if (expectedSpeed > 0) {
                // This is a trajectory (Angle of elevation)
                playerYawNeg = -Math.sin(linked.yaw * Math.PI / 180f);
                playerYawPos = Math.cos(linked.yaw * Math.PI / 180f);
                motion = Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2);
                if (motion < 0.01) {
                    this.motionX += playerYawNeg * 0.1;
                    this.motionZ += playerYawPos * 0.1;

                    isSlowed = false;
                }
            }
        }

        //http://minecraft.gamepedia.com/Powered_Rail#Rail
        if (isSlowed) {
            expectedSpeed = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2));
            if (expectedSpeed < 0.03) {
                this.motionX *= 0;
                this.motionY *= 0;
                this.motionZ *= 0;
            } else {
                this.motionX *= 0.5;
                this.motionY *= 0;
                this.motionZ *= 0.5;
            }
        }

        playerYawNeg = dx + 0.5 + facing[0][0] * 0.5;
        playerYawPos = dz + 0.5 + facing[0][2] * 0.5;
        motion = dx + 0.5 + facing[1][0] * 0.5;
        double wallOfFame = dz + 0.5 + facing[1][2] * 0.5;

        facing1 = motion - playerYawNeg;
        facing2 = wallOfFame - playerYawPos;
        double motX;
        double motZ;

        if (facing1 == 0) {
            this.x = dx + 0.5;
            expectedSpeed = this.z - dz;
        } else if (facing2 == 0) {
            this.z = dz + 0.5;
            expectedSpeed = this.x - dx;
        } else {
            motX = this.x - playerYawNeg;
            motZ = this.z - playerYawPos;
            expectedSpeed = (motX * facing1 + motZ * facing2) * 2;
        }

        this.x = playerYawNeg + facing1 * expectedSpeed;
        this.z = playerYawPos + facing2 * expectedSpeed;
        this.setPosition(new Vector3(this.x, this.y, this.z)); // Hehe, my minstake :3

        motX = this.motionX;
        motZ = this.motionZ;
        if (!this.passengers.isEmpty()) {
            motX *= 0.75;
            motZ *= 0.75;
        }
        motX = NukkitMath.clamp(motX, -this.getMaxSpeed(), this.getMaxSpeed());
        motZ = NukkitMath.clamp(motZ, -this.getMaxSpeed(), this.getMaxSpeed());

        move(motX, 0, motZ);
        if (facing[0][1] != 0 && MathHelper.floor(this.x) - dx == facing[0][0] && MathHelper.floor(this.z) - dz == facing[0][2]) {
            this.setPosition(new Vector3(this.x, this.y + facing[0][1], this.z));
        } else if (facing[1][1] != 0 && MathHelper.floor(this.x) - dx == facing[1][0] && MathHelper.floor(this.z) - dz == facing[1][2]) {
            this.setPosition(new Vector3(this.x, this.y + facing[1][1], this.z));
        }

        this.applyDrag();
        Vector3 vector1 = getNextRail(x, y, z);

        if (vector1 != null && vector != null) {
            double d14 = (vector.y - vector1.y) * 0.05;

            squareOfFame = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2));
            if (squareOfFame > 0) {
                this.motionX = this.motionX / squareOfFame * (squareOfFame + d14);
                this.motionZ = this.motionZ / squareOfFame * (squareOfFame + d14);
            }

            this.setPosition(new Vector3(this.x, vector1.y, this.z));
        }

        int floorX = MathHelper.floor(this.x);
        int floorZ = MathHelper.floor(this.z);

        if (floorX != dx || floorZ != dz) {
            squareOfFame = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2));
            this.motionX = squareOfFame * (floorX - dx);
            this.motionZ = squareOfFame * (floorZ - dz);
        }

        if (isPowered) {
            double newMovie = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2));

            if (newMovie > 0.01) {
                double nextMovie = 0.06;

                this.motionX += this.motionX / newMovie * nextMovie;
                this.motionZ += this.motionZ / newMovie * nextMovie;
            } else if (block.getOrientation() == Orientation.STRAIGHT_NORTH_SOUTH) {
                if (this.level.getBlock(new Vector3(dx - 1, dy, dz)).isNormalBlock()) {
                    this.motionX = 0.02;
                } else if (this.level.getBlock(new Vector3(dx + 1, dy, dz)).isNormalBlock()) {
                    this.motionX = -0.02;
                }
            } else if (block.getOrientation() == Orientation.STRAIGHT_EAST_WEST) {
                if (this.level.getBlock(new Vector3(dx, dy, dz - 1)).isNormalBlock()) {
                    this.motionZ = 0.02;
                } else if (this.level.getBlock(new Vector3(dx, dy, dz + 1)).isNormalBlock()) {
                    this.motionZ = -0.02;
                }
            }
        }
    }

    private void applyDrag() {
        if (!this.passengers.isEmpty() || !this.slowWhenEmpty) {
            this.motionX *= 0.996999979019165;
            this.motionY *= 0;
            this.motionZ *= 0.996999979019165;
        } else {
            this.motionX *= 0.9599999785423279;
            this.motionY *= 0;
            this.motionZ *= 0.9599999785423279;
        }
    }

    private Vector3 getNextRail(double dx, double dy, double dz) {
        int checkX = MathHelper.floor(dx);
        int checkY = MathHelper.floor(dy);
        int checkZ = MathHelper.floor(dz);

        if (Rail.isRailBlock(this.level.getBlockIdAt(checkX, checkY - 1, checkZ))) {
            --checkY;
        }

        Block block = this.level.getBlock(new Vector3(checkX, checkY, checkZ));

        if (Rail.isRailBlock(block)) {
            int[][] facing = matrix[((BlockRail) block).getRealMeta()];
            double rail;
            // Genisys mistake (Doesn't check surrounding more exactly)
            double nextOne = checkX + 0.5 + facing[0][0] * 0.5;
            double nextTwo = checkY + 0.5 + facing[0][1] * 0.5;
            double nextThree = checkZ + 0.5 + facing[0][2] * 0.5;
            double nextFour = checkX + 0.5 + facing[1][0] * 0.5;
            double nextFive = checkY + 0.5 + facing[1][1] * 0.5;
            double nextSix = checkZ + 0.5 + facing[1][2] * 0.5;
            double nextSeven = nextFour - nextOne;
            double nextEight = (nextFive - nextTwo) * 2;
            double nextMax = nextSix - nextThree;

            if (nextSeven == 0) {
                rail = dz - checkZ;
            } else if (nextMax == 0) {
                rail = dx - checkX;
            } else {
                double whatOne = dx - nextOne;
                double whatTwo = dz - nextThree;

                rail = (whatOne * nextSeven + whatTwo * nextMax) * 2;
            }

            dx = nextOne + nextSeven * rail;
            dy = nextTwo + nextEight * rail;
            dz = nextThree + nextMax * rail;
            if (nextEight < 0) {
                ++dy;
            }

            if (nextEight > 0) {
                dy += 0.5;
            }

            return new Vector3(dx, dy, dz);
        }
        return null;
    }

    /**
     * Used to multiply the minecart current speed
     *
     * @param speed The speed of the minecart that will be calculated
     */
    public void setCurrentSpeed(double speed) {
        this.currentSpeed = speed;
    }

    private void prepareDataProperty() {
        this.setRollingAmplitude(0);
        this.setRollingDirection(1);
        if (this.namedTag.contains("CustomDisplayTile")) {
            if (this.namedTag.getBoolean("CustomDisplayTile")) {
                int display = this.namedTag.getInt("DisplayTile");
                int offSet = this.namedTag.getInt("DisplayOffset");
                this.setDataProperty(new ByteEntityData(DATA_HAS_DISPLAY, 1));
                this.setDataProperty(new IntEntityData(DATA_DISPLAY_ITEM, display));
                this.setDataProperty(new IntEntityData(DATA_DISPLAY_OFFSET, offSet));
            }
        } else {
            int display = this.blockInside == null ? 0 : this.blockInside.getId() | this.blockInside.getDamage() << 16;
            if (display == 0) {
                this.setDataProperty(new ByteEntityData(DATA_HAS_DISPLAY, 0));
                return;
            }
            this.setDataProperty(new ByteEntityData(DATA_HAS_DISPLAY, 1));
            this.setDataProperty(new IntEntityData(DATA_DISPLAY_ITEM, display));
            this.setDataProperty(new IntEntityData(DATA_DISPLAY_OFFSET, 6));
        }
    }

    private void saveEntityData() {
        boolean hasDisplay = super.getDataPropertyByte(DATA_HAS_DISPLAY) == 1 || this.blockInside != null;
        int display;
        int offSet;
        this.namedTag.putBoolean("CustomDisplayTile", hasDisplay);
        if (hasDisplay) {
            display = blockInside.getId() | blockInside.getDamage() << 16;
            offSet = getDataPropertyInt(DATA_DISPLAY_OFFSET);
            this.namedTag.putInt("DisplayTile", display);
            this.namedTag.putInt("DisplayOffset", offSet);
        }
    }

    /**
     * Set the minecart display block
     *
     * @param block The block that will changed. Set {@code null} for BlockAir
     * @return {@code true} if the block is normal block
     */
    public boolean setDisplayBlock(Block block){
        return this.setDisplayBlock(block, true);
    }

    /**
     * Set the minecart display block
     *
     * @param block The block that will changed. Set {@code null} for BlockAir
     * @param update Do update for the block. (This state changes if you want to show the block)
     * @return {@code true} if the block is normal block
     */
    @API(usage = Usage.MAINTAINED, definition = Definition.UNIVERSAL)
    public boolean setDisplayBlock(Block block, boolean update) {
        if(!update){
            if (block.isNormalBlock()) {
                this.blockInside = block;
            } else {
                this.blockInside = null;
            }
            return true;
        }
        if (block != null) {
            if (block.isNormalBlock()) {
                this.blockInside = block;
                int display = this.blockInside.getId() | this.blockInside.getDamage() << 16;
                this.setDataProperty(new ByteEntityData(DATA_HAS_DISPLAY, 1));
                this.setDataProperty(new IntEntityData(DATA_DISPLAY_ITEM, display));
                this.setDisplayBlockOffset(6);
            }
        } else {
            // Set block to air (default).
            this.blockInside = null;
            this.setDataProperty(new ByteEntityData(DATA_HAS_DISPLAY, 0));
            this.setDataProperty(new IntEntityData(DATA_DISPLAY_ITEM, 0));
            this.setDisplayBlockOffset(0);
        }
        return true;
    }

    /**
     * Get the minecart display block
     *
     * @return Block of minecart display block
     */
    @API(usage = Usage.STABLE, definition = Definition.UNIVERSAL)
    public Block getDisplayBlock() {
        return this.blockInside;
    }

    /**
     * Set the block offset.
     *
     * @param offset The offset
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.PLATFORM_NATIVE)
    public void setDisplayBlockOffset(int offset) {
        this.setDataProperty(new IntEntityData(DATA_DISPLAY_OFFSET, offset));
    }

    /**
     * Get the block display offset
     *
     * @return integer
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.UNIVERSAL)
    public int getDisplayBlockOffset() {
        return super.getDataPropertyInt(DATA_DISPLAY_OFFSET);
    }

    /**
     * Is the minecart can be slowed when empty?
     *
     * @return boolean
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.UNIVERSAL)
    public boolean isSlowWhenEmpty() {
        return this.slowWhenEmpty;
    }

    /**
     * Set the minecart slowdown flag
     *
     * @param slow The slowdown flag
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.UNIVERSAL)
    public void setSlowWhenEmpty(boolean slow) {
        this.slowWhenEmpty = slow;
    }

    public Vector3 getFlyingVelocityMod() {
        return new Vector3(this.flyingX, this.flyingY, this.flyingZ);
    }

    public void setFlyingVelocityMod(Vector3 flying) {
        Objects.requireNonNull(flying, "Flying velocity modifiers cannot be null");
        this.flyingX = flying.getX();
        this.flyingY = flying.getY();
        this.flyingZ = flying.getZ();
    }

    public Vector3 getDerailedVelocityMod() {
        return new Vector3(this.derailedX, this.derailedY, this.derailedZ);
    }

    public void setDerailedVelocityMod(Vector3 derailed) {
        Objects.requireNonNull(derailed, "Derailed velocity modifiers cannot be null");
        this.derailedX = derailed.getX();
        this.derailedY = derailed.getY();
        this.derailedZ = derailed.getZ();
    }

    public void setMaximumSpeed(double speed) {
        this.maxSpeed = speed;
    }
}
