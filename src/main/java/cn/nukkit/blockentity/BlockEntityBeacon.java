package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.inventory.BeaconInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import java.util.Map;

/**
 * author: Rover656
 */
public class BlockEntityBeacon extends BlockEntitySpawnable {

    public BlockEntityBeacon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (!namedTag.contains("Lock")) {
            namedTag.putString("Lock", "");
        }

        if (!namedTag.contains("Levels")) {
            namedTag.putInt("Levels", 0);
        }

        if (!namedTag.contains("Primary")) {
            namedTag.putInt("Primary", 0);
        }

        if (!namedTag.contains("Secondary")) {
            namedTag.putInt("Secondary", 0);
        }

        this.scheduleUpdate();

        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        int blockID = this.getBlock().getId();
        return blockID == Block.BEACON;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return getDefaultCompound(this, BEACON)
                .putString("Lock", this.namedTag.getString("Lock"))
                .putInt("Levels", this.namedTag.getInt("Levels"))
                .putInt("Primary", this.namedTag.getInt("Primary"))
                .putInt("Secondary", this.namedTag.getInt("Secondary"));
    }

    private long currentTick = 0;

    @Override
    public boolean onUpdate() {
        //Only apply effects every 4 secs
        if (currentTick++ % 80 != 0) {
            return true;
        }

        int oldPowerLevel = this.getPowerLevel();
        //Get the power level based on the pyramid
        this.setPowerLevel(this.calculatePowerLevel());
        int newPowerLevel = this.getPowerLevel();

        //Skip beacons that do not have a pyramid or sky access
        if (newPowerLevel < 1 || !this.hasSkyAccess()) {
            if (oldPowerLevel > 0) {
                this.getLevel().addSound(this, Sound.BEACON_DEACTIVATE);
            }
            return true;
        } else if (oldPowerLevel < 1) {
            this.getLevel().addSound(this, Sound.BEACON_ACTIVATE);
        } else {
            this.getLevel().addSound(this, Sound.BEACON_AMBIENT);
        }

        //Get all players in game
        Map<Long, Player> players = this.level.getPlayers();

        //Calculate vars for beacon power
        Integer range = 10 + this.getPowerLevel() * 10;
        Integer duration = 9 + this.getPowerLevel() * 2;

        for(Map.Entry<Long, Player> entry : players.entrySet()) {
            Player p = entry.getValue();

            //If the player is in range
            if (p.distance(this) < range) {
                Effect e;

                if (getPrimaryPower() != 0) {
                    //Apply the primary power
                    e = Effect.getEffect(getPrimaryPower());

                    //Set duration
                    e.setDuration(duration * 20);

                    //If secondary is selected as the primary too, apply 2 amplification
                    if (getSecondaryPower() == getPrimaryPower()) {
                        e.setAmplifier(2);
                    } else {
                        e.setAmplifier(1);
                    }

                    //Hide particles
                    e.setVisible(false);

                    //Add the effect
                    p.addEffect(e);
                }

                //If we have a secondary power as regen, apply it
                if (getSecondaryPower() == Effect.REGENERATION) {
                    //Get the regen effect
                    e = Effect.getEffect(Effect.REGENERATION);

                    //Set duration
                    e.setDuration(duration * 20);

                    //Regen I
                    e.setAmplifier(1);

                    //Hide particles
                    e.setVisible(false);

                    //Add effect
                    p.addEffect(e);
                }
            }
        }

        return true;
    }

    private static final int POWER_LEVEL_MAX = 4;

    private boolean hasSkyAccess() {
        int tileX = this.getFloorX();
        int tileY = this.getFloorY();
        int tileZ = this.getFloorZ();

        //Check every block from our y coord to the top of the world
        for (int y = tileY + 1; y <= 255; y++) {
            int testBlockId = level.getBlockIdAt(tileX, y, tileZ);
            if (!Block.transparent[testBlockId]) {
                //There is no sky access
                return false;
            }
        }

        return true;
    }

    private int calculatePowerLevel() {
        int tileX = this.getFloorX();
        int tileY = this.getFloorY();
        int tileZ = this.getFloorZ();

        //The power level that we're testing for
        for (int powerLevel = 1; powerLevel <= POWER_LEVEL_MAX; powerLevel++) {
            int queryY = tileY - powerLevel; //Layer below the beacon block

            for (int queryX = tileX - powerLevel; queryX <= tileX + powerLevel; queryX++) {
                for (int queryZ = tileZ - powerLevel; queryZ <= tileZ + powerLevel; queryZ++) {

                    int testBlockId = level.getBlockIdAt(queryX, queryY, queryZ);
                    if (testBlockId != Block.IRON_BLOCK && testBlockId != Block.GOLD_BLOCK && testBlockId != Block.EMERALD_BLOCK && testBlockId != Block.DIAMOND_BLOCK) {
                        return powerLevel - 1;
                    }

                }
            }
        }

        return POWER_LEVEL_MAX;
    }

    public int getPowerLevel() {
        return this.namedTag.getInt("Level");
    }

    public void setPowerLevel(int level) {
        int currentLevel = this.getPowerLevel();
        if (level != currentLevel) {
            this.namedTag.putInt("Level", level);
            this.setDirty();
            this.spawnToAll();
        }
    }

    public int getPrimaryPower() {
        return this.namedTag.getInt("Primary");
    }

    public void setPrimaryPower(int power) {
        int currentPower = this.getPrimaryPower();
        if (power != currentPower) {
            this.namedTag.putInt("Primary", power);
            this.setDirty();
            this.spawnToAll();
        }
    }

    public int getSecondaryPower() {
        return this.namedTag.getInt("Secondary");
    }

    public void setSecondaryPower(int power) {
        int currentPower = this.getSecondaryPower();
        if (power != currentPower) {
            this.namedTag.putInt("Secondary", power);
            this.setDirty();
            this.spawnToAll();
        }
    }

    @Override
    public boolean updateCompoundTag(CompoundTag nbt, Player player) {
        if (!nbt.getString("id").equals(BlockEntity.BEACON)) {
            return false;
        }

        this.setPrimaryPower(nbt.getInt("primary"));
        this.setSecondaryPower(nbt.getInt("secondary"));
        this.getLevel().addSound(this, Sound.BEACON_POWER);

        BeaconInventory inv = (BeaconInventory) player.getWindowById(Player.BEACON_WINDOW_ID);
        inv.setItem(0, new ItemBlock(Block.get(Block.AIR)));

        return true;
    }
}
