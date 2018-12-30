package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.BlockEventPacket;

/**
 * Created by Snake1999 on 2016/1/17.
 * Package cn.nukkit.block in project nukkit.
 */
public class BlockNoteblock extends BlockSolid {

    private int strength = 0;

    public BlockNoteblock() {

    }

    @Override
    public String getName() {
        return "Note Block";
    }

    @Override
    public int getId() {
        return NOTEBLOCK;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public double getHardness() {
        return 0.8D;
    }

    @Override
    public double getResistance() {
        return 4D;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void increaseStrength() {
        if (this.getStrength() < 24) {
            this.setStrength(this.getStrength() + 1);
        } else {
            this.setStrength(0);
        }
    }

    public Instrument getInstrument() {
        Block below = this.down();
        switch (below.getId()) {
            case LOG:
            case LOG2:
            //case STRIPPED_SPRUCE_LOG:
            //case STRIPPED_BIRCH_LOG:
            //case STRIPPED_JUNGLE_LOG:
            //case STRIPPED_ACACIA_LOG:
            //case STRIPPED_DARK_OAK_LOG:
            //case STRIPPED_OAK_LOG:
            case PLANKS:
            case DOUBLE_WOODEN_SLAB:
            case WOODEN_SLAB:
            case WOOD_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case ACACIA_WOOD_STAIRS:
            case DARK_OAK_WOOD_STAIRS:
            case FENCE:
            case FENCE_GATE:
            case FENCE_GATE_SPRUCE:
            case FENCE_GATE_BIRCH:
            case FENCE_GATE_JUNGLE:
            case FENCE_GATE_DARK_OAK:
            case FENCE_GATE_ACACIA:
            case DOOR_BLOCK:
            case SPRUCE_DOOR_BLOCK:
            case BIRCH_DOOR_BLOCK:
            case JUNGLE_DOOR_BLOCK:
            case ACACIA_DOOR_BLOCK:
            case DARK_OAK_DOOR_BLOCK:
            case WOODEN_PRESSURE_PLATE:
            //case ACACIA_PRESSURE_PLATE:
            //case BIRCH_PRESSURE_PLATE:
            //case DARK_OAK_PRESSURE_PLATE:
            //case JUNGLE_PRESSURE_PLATE:
            //case SPRUCE_PRESSURE_PLATE:
            case TRAPDOOR:
            //case ACACIA_TRAPDOOR:
            //case BIRCH_TRAPDOOR:
            //case DARK_OAK_TRAPDOOR:
            //case JUNGLE_TRAPDOOR:
            //case SPRUCE_TRAPDOOR:
            case SIGN_POST:
            case WALL_SIGN:
            case NOTEBLOCK:
            case BOOKSHELF:
            case CHEST:
            case TRAPPED_CHEST:
            case CRAFTING_TABLE:
            case JUKEBOX:
            case BROWN_MUSHROOM_BLOCK:
            case RED_MUSHROOM_BLOCK:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case STANDING_BANNER:
            case WALL_BANNER:
                return Instrument.BASS;
            case SAND:
            case SOUL_SAND:
            case GRAVEL:
            case CONCRETE_POWDER:
                return Instrument.DRUM;
            case GLASS:
            case GLASS_PANEL:
            case STAINED_GLASS_PANE:
            case STAINED_GLASS:
            //case HARD_GLASS_PANE:
            //case HARD_STAINED_GLASS_PANE:
            //case HARD_GLASS:
            //case HARD_STAINED_GLASS:
            case GLOWSTONE_BLOCK:
            case BEACON:
            case SEA_LANTERN:
                return Instrument.STICKS;
            case STONE:
            case SANDSTONE:
            case RED_SANDSTONE:
            case COBBLESTONE:
            case MOSSY_STONE:
            case BRICKS:
            case STONE_BRICKS:
            case NETHER_BRICK_BLOCK:
            case RED_NETHER_BRICK:
            case QUARTZ_BLOCK:
            case DOUBLE_SLAB:
            case SLAB:
            case DOUBLE_RED_SANDSTONE_SLAB:
            case RED_SANDSTONE_SLAB:
            case COBBLE_STAIRS:
            case BRICK_STAIRS:
            case STONE_BRICK_STAIRS:
            case NETHER_BRICKS_STAIRS:
            case SANDSTONE_STAIRS:
            case QUARTZ_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case PURPUR_STAIRS:
            //case PRISMARINE_STAIRS:
            //case DARK_PRISMARINE_STAIRS:
            //case PRISMARINE_BRICKS_STAIRS:
            case COBBLE_WALL:
            case NETHER_BRICK_FENCE:
            case BEDROCK:
            case GOLD_ORE:
            case IRON_ORE:
            case COAL_ORE:
            case LAPIS_ORE:
            case DIAMOND_ORE:
            case REDSTONE_ORE:
            case GLOWING_REDSTONE_ORE:
            case EMERALD_ORE:
            case DROPPER:
            case DISPENSER:
            case FURNACE:
            case BURNING_FURNACE:
            case OBSIDIAN:
            case GLOWING_OBSIDIAN:
            case MONSTER_SPAWNER:
            case STONE_PRESSURE_PLATE:
            case NETHERRACK:
            case QUARTZ_ORE:
            case ENCHANTING_TABLE:
            case END_PORTAL_FRAME:
            case END_STONE:
            case END_BRICKS:
            case ENDER_CHEST:
            case STAINED_TERRACOTTA:
            case TERRACOTTA:
            case PRISMARINE:
            case COAL_BLOCK:
            case PURPUR_BLOCK:
            case MAGMA:
            case BONE_BLOCK:
            case CONCRETE:
            case STONECUTTER:
            case OBSERVER:
                return Instrument.BASS_DRUM;
            default:
                return Instrument.PIANO;
        }
    }

    public void emitSound() {
        Instrument instrument = getInstrument();

        BlockEventPacket pk = new BlockEventPacket();
        pk.x = this.getFloorX();
        pk.y = this.getFloorY();
        pk.z = this.getFloorZ();
        pk.eventType = instrument.ordinal();
        pk.eventData = this.getStrength();
        this.getLevel().addChunkPacket(this.getFloorX() >> 4, this.getFloorZ() >> 4, pk);

        float pitch = 0.5f;
        switch (this.getStrength()) {
            case 1:
                pitch = (float) Math.pow(2, -11.0 / 12.0);
                break;
            case 2:
                pitch = (float) Math.pow(2, -10.0 / 12.0);
                break;
            case 3:
                pitch = (float) Math.pow(2, -9.0 / 12.0);
                break;
            case 4:
                pitch = (float) Math.pow(2, -8.0 / 12.0);
                break;
            case 5:
                pitch = (float) Math.pow(2, -7.0 / 12.0);
                break;
            case 6:
                pitch = (float) Math.pow(2, -6.0 / 12.0);
                break;
            case 7:
                pitch = (float) Math.pow(2, -5.0 / 12.0);
                break;
            case 8:
                pitch = (float) Math.pow(2, -4.0 / 12.0);
                break;
            case 9:
                pitch = (float) Math.pow(2, -3.0 / 12.0);
                break;
            case 10:
                pitch = (float) Math.pow(2, -2.0 / 12.0);
                break;
            case 11:
                pitch = (float) Math.pow(2, -1.0 / 12.0);
                break;
            case 12:
                pitch = 1;
                break;
            case 13:
                pitch = (float) Math.pow(2, 1.0 / 12.0);
                break;
            case 14:
                pitch = (float) Math.pow(2, 2.0 / 12.0);
                break;
            case 15:
                pitch = (float) Math.pow(2, 3.0 / 12.0);
                break;
            case 16:
                pitch = (float) Math.pow(2, 4.0 / 12.0);
                break;
            case 17:
                pitch = (float) Math.pow(2, 5.0 / 12.0);
                break;
            case 18:
                pitch = (float) Math.pow(2, 6.0 / 12.0);
                break;
            case 19:
                pitch = (float) Math.pow(2, 7.0 / 12.0);
                break;
            case 20:
                pitch = (float) Math.pow(2, 8.0 / 12.0);
                break;
            case 21:
                pitch = (float) Math.pow(2, 9.0 / 12.0);
                break;
            case 22:
                pitch = (float) Math.pow(2, 10.0 / 12.0);
                break;
            case 23:
                pitch = (float) Math.pow(2, 11.0 / 12.0);
                break;
            case 24:
                pitch = 2;
                break;
        }
        this.getLevel().addSound(this, instrument.getSound(), 1, pitch);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block up = this.up();
        if (up.getId() == Block.AIR) {
            this.increaseStrength();
            this.emitSound();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_REDSTONE) {
            this.increaseStrength();
            if (this.up().getId() == Block.AIR) {
                this.emitSound();
            }
        }
        return 0;
    }

    public enum Instrument {
        PIANO(Sound.NOTE_HARP),
        BASS_DRUM(Sound.NOTE_BD),
        STICKS(Sound.NOTE_HAT),
        DRUM(Sound.NOTE_SNARE),
        BASS(Sound.NOTE_BASS);
        //PLING(Sound.NOTE_PLING),
        //BASS_ATTACK(Sound.NOTE_BASSATTACK),

        private final Sound sound;

        Instrument(Sound sound) {
            this.sound = sound;
        }

        public Sound getSound() {
            return sound;
        }
    }
}
