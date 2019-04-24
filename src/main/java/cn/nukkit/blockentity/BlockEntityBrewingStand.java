package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockBrewingStand;
import cn.nukkit.event.inventory.BrewEvent;
import cn.nukkit.event.inventory.StartBrewEvent;
import cn.nukkit.inventory.BrewingInventory;
import cn.nukkit.inventory.BrewingRecipe;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import com.google.common.collect.ImmutableList;
import java.util.HashSet;
import java.util.List;

public class BlockEntityBrewingStand extends BlockEntitySpawnable implements InventoryHolder, BlockEntityContainer, BlockEntityNameable {

    protected BrewingInventory inventory;

    public static final int MAX_BREW_TIME = 400;

    public int brewTime = MAX_BREW_TIME;
    public int fuelTotal;
    public int fuelAmount;

    public static final List<Integer> INGREDIENTS = new ImmutableList.Builder<Integer>()
            .add(Item.NETHER_WART)
            .add(Item.GOLD_NUGGET)
            .add(Item.GHAST_TEAR)
            .add(Item.GLOWSTONE_DUST)
            .add(Item.REDSTONE_DUST)
            .add(Item.GUNPOWDER)
            .add(Item.MAGMA_CREAM)
            .add(Item.BLAZE_POWDER)
            .add(Item.GOLDEN_CARROT)
            .add(Item.SPIDER_EYE)
            .add(Item.FERMENTED_SPIDER_EYE)
            .add(Item.GLISTERING_MELON)
            .add(Item.SUGAR)
            .add(Item.RAW_FISH)
            .build();

    public BlockEntityBrewingStand(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new BrewingInventory(this);

        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        }

        for (int i = 0; i < getSize(); i++) {
            inventory.setItem(i, this.getItem(i));
        }

        if (!this.namedTag.contains("CookTime") || this.namedTag.getShort("CookTime") > MAX_BREW_TIME) {
            this.brewTime = MAX_BREW_TIME;
        } else {
            this.brewTime = this.namedTag.getShort("CookTime");
        }

        this.fuelAmount = this.namedTag.getShort("FuelAmount");
        this.fuelTotal = this.namedTag.getShort("FuelTotal");

        if (this.brewTime < MAX_BREW_TIME) {
            this.scheduleUpdate();
        }

        super.initBlockEntity();
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Brewing Stand";
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains("CustomName");
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.namedTag.remove("CustomName");
            return;
        }

        this.namedTag.putString("CustomName", name);
    }

    @Override
    public void close() {
        if (!this.closed) {
            for (Player player : new HashSet<>(getInventory().getViewers())) {
                player.removeWindow(this.getInventory());
            }
            super.close();
        }
    }

    @Override
    public void onBreak() {
        for (Item content : this.inventory.getContents().values()) {
            this.level.dropItem(this, content);
        }
    }

    @Override
    public void saveNBT() {
        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < getSize(); index++) {
            this.setItem(index, this.inventory.getItem(index));
        }

        this.namedTag.putShort("CookTime", this.brewTime);
        this.namedTag.putShort("FuelAmount", this.fuelAmount);
        this.namedTag.putShort("FuelTotal", this.fuelTotal);
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.getBlock().getId() == Block.BREWING_STAND_BLOCK;
    }

    @Override
    public int getSize() {
        return 4;
    }

    protected int getSlotIndex(int index) {
        ListTag<CompoundTag> list = this.namedTag.getList("Items", CompoundTag.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getByte("Slot") == index) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Item getItem(int index) {
        int i = this.getSlotIndex(index);
        if (i < 0) {
            return new ItemBlock(new BlockAir(), 0, 0);
        } else {
            CompoundTag data = (CompoundTag) this.namedTag.getList("Items").get(i);
            return NBTIO.getItemHelper(data);
        }
    }

    @Override
    public void setItem(int index, Item item) {
        int i = this.getSlotIndex(index);

        CompoundTag d = NBTIO.putItemHelper(item, index);

        if (item.getId() == Item.AIR || item.getCount() <= 0) {
            if (i >= 0) {
                this.namedTag.getList("Items").getAll().remove(i);
            }
        } else if (i < 0) {
            (this.namedTag.getList("Items", CompoundTag.class)).add(d);
        } else {
            (this.namedTag.getList("Items", CompoundTag.class)).add(i, d);
        }
    }

    @Override
    public BrewingInventory getInventory() {
        return this.inventory;
    }

    protected boolean checkIngredient(Item ingredient) {
        return INGREDIENTS.contains(ingredient.getId());
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }

        boolean ret = false;

        Item ingredient = this.inventory.getIngredient();
        boolean canBrew = false;

        Item fuel = this.getInventory().getFuel();
        if (this.fuelAmount <= 0 && fuel.getId() == Item.BLAZE_POWDER && fuel.getCount() > 0) {
            fuel.count--;
            this.fuelAmount = 20;
            this.fuelTotal = 20;

            this.inventory.setFuel(fuel);
            this.sendFuel();
        }

        if (this.fuelAmount > 0) {
            for (int i = 1; i <= 3; i++) {
                if (this.inventory.getItem(i).getId() == Item.POTION) {
                    canBrew = true;
                }
            }

            if (this.brewTime <= MAX_BREW_TIME && canBrew && ingredient.getCount() > 0) {
                if (!this.checkIngredient(ingredient)) {
                    canBrew = false;
                }
            } else {
                canBrew = false;
            }
        }

        if (canBrew) {
            if (this.brewTime == MAX_BREW_TIME) {
                this.sendBrewTime();
                StartBrewEvent e = new StartBrewEvent(this);
                this.server.getPluginManager().callEvent(e);

                if (e.isCancelled()) {
                    return false;
                }
            }

            this.brewTime--;

            if (this.brewTime <= 0) { //20 seconds
                BrewEvent e = new BrewEvent(this);
                this.server.getPluginManager().callEvent(e);

                if (!e.isCancelled()) {
                    for (int i = 1; i <= 3; i++) {
                        Item potion = this.inventory.getItem(i);
                        BrewingRecipe recipe = Server.getInstance().getCraftingManager().matchBrewingRecipe(ingredient, potion);

                        if (recipe != null) {
                            this.inventory.setItem(i, recipe.getResult());
                        }
                    }
                    this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_POTION_BREWED);

                    ingredient.count--;
                    this.inventory.setIngredient(ingredient);

                    this.fuelAmount--;
                    this.sendFuel();
                }

                this.brewTime = MAX_BREW_TIME;
            }

            ret = true;
        } else {
            this.brewTime = MAX_BREW_TIME;
        }

        //this.sendBrewTime();
        lastUpdate = System.currentTimeMillis();

        return ret;
    }

    protected void sendFuel() {
        ContainerSetDataPacket pk = new ContainerSetDataPacket();

        for (Player p : this.inventory.getViewers()) {
            int windowId = p.getWindowId(this.inventory);
            if (windowId > 0) {
                pk.windowId = windowId;

                pk.property = ContainerSetDataPacket.PROPERTY_BREWING_STAND_FUEL_AMOUNT;
                pk.value = this.fuelAmount;
                p.dataPacket(pk);

                pk.property = ContainerSetDataPacket.PROPERTY_BREWING_STAND_FUEL_TOTAL;
                pk.value = this.fuelTotal;
                p.dataPacket(pk);
            }
        }
    }

    protected void sendBrewTime() {
        ContainerSetDataPacket pk = new ContainerSetDataPacket();
        pk.property = ContainerSetDataPacket.PROPERTY_BREWING_STAND_BREW_TIME;
        pk.value = this.brewTime;

        for (Player p : this.inventory.getViewers()) {
            int windowId = p.getWindowId(this.inventory);
            if (windowId > 0) {
                pk.windowId = windowId;

                p.dataPacket(pk);
            }
        }
    }

    public void updateBlock() {
        Block block = this.getLevelBlock();

        if (!(block instanceof BlockBrewingStand)) {
            return;
        }

        int meta = 0;

        for (int i = 1; i <= 3; ++i) {
            Item potion = this.inventory.getItem(i);

            if (potion.getId() == Item.POTION && potion.getCount() > 0) {
                meta |= 1 << (i - 1);
            }
        }

        block.setDamage(meta);
        this.level.setBlock(block, block, false, false);
    }

    public int getFuel() {
        return this.fuelAmount;
    }

    public void setFuel(int fuel) {
        this.fuelAmount = fuel;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag nbt = getDefaultCompound(this, BREWING_STAND)
                .putShort("FuelTotal", this.fuelTotal)
                .putShort("FuelAmount", this.fuelAmount);

        if (this.brewTime < MAX_BREW_TIME) {
            nbt.putShort("CookTime", this.brewTime);
        }

        if (this.hasName()) {
            nbt.put("CustomName", this.namedTag.get("CustomName"));
        }

        return nbt;
    }
}
