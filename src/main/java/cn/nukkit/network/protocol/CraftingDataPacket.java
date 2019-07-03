package cn.nukkit.network.protocol;

import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentEntry;
import cn.nukkit.item.enchantment.EnchantmentList;
import cn.nukkit.utils.BinaryStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class CraftingDataPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CRAFTING_DATA_PACKET;

    public static final int ENTRY_SHAPELESS = 0;
    public static final int ENTRY_SHAPED = 1;
    public static final int ENTRY_FURNACE = 2;
    public static final int ENTRY_FURNACE_DATA = 3;
    public static final int ENTRY_ENCHANT_LIST = 4, ENTRY_MULTI = 4;
    public static final int ENTRY_SHULKER_BOX = 5; //TODO
    public static final int ENTRY_SHAPELESS_CHEMISTRY = 6; //TODO
    public static final int ENTRY_SHAPED_CHEMISTRY = 7; //TODO

    public static final String CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TAG_STONECUTTER = "stonecutter";
    public static final String CRAFTING_TAG_FURNACE = "furnace";
    public static final String CRAFTING_TAG_CAMPFIRE = "campfire";
    public static final String CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    public static final String CRAFTING_TAG_SMOKER = "smoker";

    public List<Object> entries = new ArrayList<>();
    public boolean cleanRecipes = false;

    private static int writeEntry(Object entry, BinaryStream stream) {
        if (entry instanceof ShapelessRecipe) {
            return writeShapelessRecipe(((ShapelessRecipe) entry), stream);
        } else if (entry instanceof ShapedRecipe) {
            return writeShapedRecipe(((ShapedRecipe) entry), stream);
        } else if (entry instanceof FurnaceRecipe) {
            return writeFurnaceRecipe(((FurnaceRecipe) entry), stream);
        } else if (entry instanceof EnchantmentList) {
            return writeEnchantList(((EnchantmentList) entry), stream);
        }
        return -1;
    }

    private static int writeShapelessRecipe(ShapelessRecipe recipe, BinaryStream stream) {
        stream.putUnsignedVarInt(recipe.getIngredientCount());
        for (Item item : recipe.getIngredientList()) {
            stream.putSlot(item);
        }

        stream.putUnsignedVarInt(1); //TODO: results
        stream.putSlot(recipe.getResult());
        stream.putUUID(recipe.getId());
        stream.putString(CRAFTING_TAG_CRAFTING_TABLE);

        return CraftingDataPacket.ENTRY_SHAPELESS;
    }

    private static int writeShapedRecipe(ShapedRecipe recipe, BinaryStream stream) {
        stream.putVarInt(recipe.getWidth());
        stream.putVarInt(recipe.getHeight());

        for (int z = 0; z < recipe.getHeight(); ++z) {
            for (int x = 0; x < recipe.getWidth(); ++x) {
                stream.putSlot(recipe.getIngredient(x, z));
            }
        }

        stream.putUnsignedVarInt(1); //TODO: results
        stream.putSlot(recipe.getResult());

        stream.putUUID(recipe.getId());
        stream.putString(CRAFTING_TAG_CRAFTING_TABLE);

        return CraftingDataPacket.ENTRY_SHAPED;
    }

    private static int writeFurnaceRecipe(FurnaceRecipe recipe, BinaryStream stream) {
        stream.putVarInt(recipe.getInput().getId());
        int result = CraftingDataPacket.ENTRY_FURNACE;
        if (recipe.getInput().hasMeta()) { //Data recipe
            stream.putVarInt(recipe.getInput().getDamage());
            result =  CraftingDataPacket.ENTRY_FURNACE_DATA;
        }
        stream.putSlot(recipe.getResult());
        stream.putString(CRAFTING_TAG_FURNACE);
        return result;
    }

    private static int writeEnchantList(EnchantmentList list, BinaryStream stream) {
        stream.putByte((byte) list.getSize());
        for (int i = 0; i < list.getSize(); ++i) {
            EnchantmentEntry entry = list.getSlot(i);
            stream.putUnsignedVarInt(entry.getCost());
            stream.putUnsignedVarInt(entry.getEnchantments().length);
            for (Enchantment enchantment : entry.getEnchantments()) {
                stream.putUnsignedVarInt(enchantment.getId());
                stream.putUnsignedVarInt(enchantment.getLevel());
            }
            stream.putString(entry.getRandomName());
        }
        return CraftingDataPacket.ENTRY_ENCHANT_LIST;
    }

    public void addShapelessRecipe(ShapelessRecipe... recipe) {
        Collections.addAll(this.entries, recipe);
    }

    public void addShapedRecipe(ShapedRecipe... recipe) {
        Collections.addAll(this.entries, recipe);
    }

    public void addFurnaceRecipe(FurnaceRecipe... recipe) {
        Collections.addAll(this.entries, recipe);
    }

    public void addEnchantList(EnchantmentList... list) {
        Collections.addAll(this.entries, list);
    }

    @Override
    public DataPacket clean() {
        this.entries = new ArrayList<>();
        return super.clean();
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.entries.size());

        BinaryStream writer = new BinaryStream();

        for (Object entry : this.entries) {
            int entryType = writeEntry(entry, writer);
            if (entryType >= 0) {
                this.putVarInt(entryType);
                this.put(writer.getBuffer());
            } else {
                this.putVarInt(-1);
            }

            writer.reset();
        }

        this.putBoolean(this.cleanRecipes);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
