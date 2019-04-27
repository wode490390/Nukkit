package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Fuel;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@Log4j2
public class Item implements Cloneable, BlockID, ItemID {

    protected static String UNKNOWN_STR = "Unknown";
    public static Class[] list;

    protected Block block;
    protected final int id;
    protected int meta;
    protected boolean hasMeta = true;
    private byte[] tags = new byte[0];
    private CompoundTag cachedNBT;
    public int count;
    protected int durability = 0;
    protected String name;

    public Item(int id) {
        this(id, 0, 1, UNKNOWN_STR);
    }

    public Item(int id, Integer meta) {
        this(id, meta, 1, UNKNOWN_STR);
    }

    public Item(int id, Integer meta, int count) {
        this(id, meta, count, UNKNOWN_STR);
    }

    public Item(int id, Integer meta, int count, String name) {
        this.id = id & 0xffff;
        if (meta != null && meta >= 0) {
            this.meta = meta & 0xffff;
        } else {
            this.hasMeta = false;
        }
        this.count = count;
        this.name = name;
        /*f (this.block != null && this.id <= 0xff && Block.list[id] != null) { //probably useless
            this.block = Block.get(this.id, this.meta);
            this.name = this.block.getName();
        }*/
    }

    public boolean hasMeta() {
        return hasMeta;
    }

    public boolean canBeActivated() {
        return false;
    }

    public static void init() {
        if (list == null) {
            list = new Class[65535];

            for (int i = 0; i < 256; ++i) {
                if (Block.list[i] != null) {
                    list[i] = Block.list[i];
                }
            }
            for (int i = 256; i < 512; ++i) {
                if (Block.list[i] != null) {
                    list[(0xff - i) & 0xffff] = Block.list[i];
                }
            }

            list[GLOW_STICK] = ItemGlowStick.class; //166

            list[IRON_SHOVEL] = ItemShovelIron.class; //256
            list[IRON_PICKAXE] = ItemPickaxeIron.class; //257
            list[IRON_AXE] = ItemAxeIron.class; //258
            list[FLINT_AND_STEEL] = ItemFlintSteel.class; //259
            list[APPLE] = ItemApple.class; //260
            list[BOW] = ItemBow.class; //261
            list[ARROW] = ItemArrow.class; //262
            list[COAL] = ItemCoal.class; //263
            list[DIAMOND] = ItemDiamond.class; //264
            list[IRON_INGOT] = ItemIngotIron.class; //265
            list[GOLD_INGOT] = ItemIngotGold.class; //266
            list[IRON_SWORD] = ItemSwordIron.class; //267
            list[WOODEN_SWORD] = ItemSwordWood.class; //268
            list[WOODEN_SHOVEL] = ItemShovelWood.class; //269
            list[WOODEN_PICKAXE] = ItemPickaxeWood.class; //270
            list[WOODEN_AXE] = ItemAxeWood.class; //271
            list[STONE_SWORD] = ItemSwordStone.class; //272
            list[STONE_SHOVEL] = ItemShovelStone.class; //273
            list[STONE_PICKAXE] = ItemPickaxeStone.class; //274
            list[STONE_AXE] = ItemAxeStone.class; //275
            list[DIAMOND_SWORD] = ItemSwordDiamond.class; //276
            list[DIAMOND_SHOVEL] = ItemShovelDiamond.class; //277
            list[DIAMOND_PICKAXE] = ItemPickaxeDiamond.class; //278
            list[DIAMOND_AXE] = ItemAxeDiamond.class; //279
            list[STICK] = ItemStick.class; //280
            list[BOWL] = ItemBowl.class; //281
            list[MUSHROOM_STEW] = ItemMushroomStew.class; //282
            list[GOLD_SWORD] = ItemSwordGold.class; //283
            list[GOLD_SHOVEL] = ItemShovelGold.class; //284
            list[GOLD_PICKAXE] = ItemPickaxeGold.class; //285
            list[GOLD_AXE] = ItemAxeGold.class; //286
            list[STRING] = ItemString.class; //287
            list[FEATHER] = ItemFeather.class; //288
            list[GUNPOWDER] = ItemGunpowder.class; //289
            list[WOODEN_HOE] = ItemHoeWood.class; //290
            list[STONE_HOE] = ItemHoeStone.class; //291
            list[IRON_HOE] = ItemHoeIron.class; //292
            list[DIAMOND_HOE] = ItemHoeDiamond.class; //293
            list[GOLD_HOE] = ItemHoeGold.class; //294
            list[WHEAT_SEEDS] = ItemSeedsWheat.class; //295
            list[WHEAT] = ItemWheat.class; //296
            list[BREAD] = ItemBread.class; //297
            list[LEATHER_CAP] = ItemHelmetLeather.class; //298
            list[LEATHER_TUNIC] = ItemChestplateLeather.class; //299
            list[LEATHER_PANTS] = ItemLeggingsLeather.class; //300
            list[LEATHER_BOOTS] = ItemBootsLeather.class; //301
            list[CHAIN_HELMET] = ItemHelmetChain.class; //302
            list[CHAIN_CHESTPLATE] = ItemChestplateChain.class; //303
            list[CHAIN_LEGGINGS] = ItemLeggingsChain.class; //304
            list[CHAIN_BOOTS] = ItemBootsChain.class; //305
            list[IRON_HELMET] = ItemHelmetIron.class; //306
            list[IRON_CHESTPLATE] = ItemChestplateIron.class; //307
            list[IRON_LEGGINGS] = ItemLeggingsIron.class; //308
            list[IRON_BOOTS] = ItemBootsIron.class; //309
            list[DIAMOND_HELMET] = ItemHelmetDiamond.class; //310
            list[DIAMOND_CHESTPLATE] = ItemChestplateDiamond.class; //311
            list[DIAMOND_LEGGINGS] = ItemLeggingsDiamond.class; //312
            list[DIAMOND_BOOTS] = ItemBootsDiamond.class; //313
            list[GOLD_HELMET] = ItemHelmetGold.class; //314
            list[GOLD_CHESTPLATE] = ItemChestplateGold.class; //315
            list[GOLD_LEGGINGS] = ItemLeggingsGold.class; //316
            list[GOLD_BOOTS] = ItemBootsGold.class; //317
            list[FLINT] = ItemFlint.class; //318
            list[RAW_PORKCHOP] = ItemPorkchopRaw.class; //319
            list[COOKED_PORKCHOP] = ItemPorkchopCooked.class; //320
            list[PAINTING] = ItemPainting.class; //321
            list[GOLDEN_APPLE] = ItemAppleGold.class; //322
            list[SIGN] = ItemSign.class; //323
            list[WOODEN_DOOR] = ItemDoorWood.class; //324
            list[BUCKET] = ItemBucket.class; //325

            list[MINECART] = ItemMinecart.class; //328
            list[SADDLE] = ItemSaddle.class; //329
            list[IRON_DOOR] = ItemDoorIron.class; //330
            list[REDSTONE] = ItemRedstone.class; //331
            list[SNOWBALL] = ItemSnowball.class; //332
            list[BOAT] = ItemBoat.class; //333
            list[LEATHER] = ItemLeather.class; //334
            list[KELP] = ItemKelp.class; //335
            list[BRICK] = ItemBrick.class; //336
            list[CLAY] = ItemClay.class; //337
            list[SUGARCANE] = ItemSugarcane.class; //338
            list[PAPER] = ItemPaper.class; //339
            list[BOOK] = ItemBook.class; //340
            list[SLIMEBALL] = ItemSlimeball.class; //341
            list[MINECART_WITH_CHEST] = ItemMinecartChest.class; //342

            list[EGG] = ItemEgg.class; //344
            list[COMPASS] = ItemCompass.class; //345
            list[FISHING_ROD] = ItemFishingRod.class; //346
            list[CLOCK] = ItemClock.class; //347
            list[GLOWSTONE_DUST] = ItemGlowstoneDust.class; //348
            list[RAW_FISH] = ItemFish.class; //349
            list[COOKED_FISH] = ItemFishCooked.class; //350
            list[DYE] = ItemDye.class; //351
            list[BONE] = ItemBone.class; //352
            list[SUGAR] = ItemSugar.class; //353
            list[CAKE] = ItemCake.class; //354
            list[BED] = ItemBed.class; //355
            list[REPEATER] = ItemRedstoneRepeater.class; //356
            list[COOKIE] = ItemCookie.class; //357
            list[MAP] = ItemMap.class; //358
            list[SHEARS] = ItemShears.class; //359
            list[MELON] = ItemMelon.class; //360
            list[PUMPKIN_SEEDS] = ItemSeedsPumpkin.class; //361
            list[MELON_SEEDS] = ItemSeedsMelon.class; //362
            list[RAW_BEEF] = ItemBeefRaw.class; //363
            list[STEAK] = ItemSteak.class; //364
            list[RAW_CHICKEN] = ItemChickenRaw.class; //365
            list[COOKED_CHICKEN] = ItemChickenCooked.class; //366
            list[ROTTEN_FLESH] = ItemRottenFlesh.class; //367
            list[ENDER_PEARL] = ItemEnderPearl.class; //368
            list[BLAZE_ROD] = ItemBlazeRod.class; //369
            list[GHAST_TEAR] = ItemGhastTear.class; //370
            list[GOLD_NUGGET] = ItemNuggetGold.class; //371
            list[NETHER_WART] = ItemNetherWart.class; //372
            list[POTION] = ItemPotion.class; //373
            list[GLASS_BOTTLE] = ItemGlassBottle.class; //374
            list[SPIDER_EYE] = ItemSpiderEye.class; //375
            list[FERMENTED_SPIDER_EYE] = ItemSpiderEyeFermented.class; //376
            list[BLAZE_POWDER] = ItemBlazePowder.class; //377
            list[MAGMA_CREAM] = ItemMagmaCream.class; //378
            list[BREWING_STAND] = ItemBrewingStand.class; //379
            list[CAULDRON] = ItemCauldron.class; //380
            list[ENDER_EYE] = ItemEnderEye.class; //381
            list[GLISTERING_MELON] = ItemMelonGlistering.class; //382
            list[SPAWN_EGG] = ItemSpawnEgg.class; //383
            list[EXPERIENCE_BOTTLE] = ItemExpBottle.class; //384
            list[FIRE_CHARGE] = ItemFireCharge.class; //385
            list[BOOK_AND_QUILL] = ItemBookAndQuill.class; //386
            list[WRITTEN_BOOK] = ItemBookWritten.class; //387
            list[EMERALD] = ItemEmerald.class; //388
            list[ITEM_FRAME] = ItemItemFrame.class; //389
            list[FLOWER_POT] = ItemFlowerPot.class; //390
            list[CARROT] = ItemCarrot.class; //391
            list[POTATO] = ItemPotato.class; //392
            list[BAKED_POTATO] = ItemPotatoBaked.class; //393
            list[POISONOUS_POTATO] = ItemPotatoPoisonous.class; //394
            list[EMPTY_MAP] = ItemEmptyMap.class; //395
            list[GOLDEN_CARROT] = ItemCarrotGolden.class; //396
            list[SKULL] = ItemSkull.class; //397
            list[CARROT_ON_A_STICK] = ItemCarrotOnAStick.class; //398
            list[NETHER_STAR] = ItemNetherStar.class; //399
            list[PUMPKIN_PIE] = ItemPumpkinPie.class; //400
            list[FIREWORKS] = ItemFirework.class; //401
            list[FIREWORKSCHARGE] = ItemFireworkCharge.class; //402
            list[ENCHANTED_BOOK] = ItemBookEnchanted.class; //403
            list[COMPARATOR] = ItemRedstoneComparator.class; //404
            list[NETHER_BRICK] = ItemNetherBrick.class; //405
            list[QUARTZ] = ItemQuartz.class; //406
            list[MINECART_WITH_TNT] = ItemMinecartTNT.class; //407
            list[MINECART_WITH_HOPPER] = ItemMinecartHopper.class; //408
            list[PRISMARINE_SHARD] = ItemPrismarineShard.class; //409
            list[HOPPER] = ItemHopper.class; //410
            list[RAW_RABBIT] = ItemRabbitRaw.class; //411
            list[COOKED_RABBIT] = ItemRabbitCooked.class; //412
            list[RABBIT_STEW] = ItemRabbitStew.class; //413
            list[RABBIT_FOOT] = ItemRabbitFoot.class; //414
            list[RABBIT_HIDE] = ItemRabbitHide.class; //415
            list[LEATHER_HORSE_ARMOR] = ItemHorseArmorLeather.class; //416
            list[IRON_HORSE_ARMOR] = ItemHorseArmorIron.class; //417
            list[GOLD_HORSE_ARMOR] = ItemHorseArmorGold.class; //418
            list[DIAMOND_HORSE_ARMOR] = ItemHorseArmorDiamond.class; //419
            list[LEAD] = ItemLead.class; //420
            list[NAME_TAG] = ItemNameTag.class; //421
            list[PRISMARINE_CRYSTALS] = ItemPrismarineCrystals.class; //422
            list[RAW_MUTTON] = ItemMuttonRaw.class; //423
            list[COOKED_MUTTON] = ItemMuttonCooked.class; //424
            list[ARMOR_STAND] = ItemArmorStand.class; //425
            list[END_CRYSTAL] = ItemEndCrystal.class; //426
            list[SPRUCE_DOOR] = ItemDoorSpruce.class; //427
            list[BIRCH_DOOR] = ItemDoorBirch.class; //428
            list[JUNGLE_DOOR] = ItemDoorJungle.class; //429
            list[ACACIA_DOOR] = ItemDoorAcacia.class; //430
            list[DARK_OAK_DOOR] = ItemDoorDarkOak.class; //431
            list[CHORUS_FRUIT] = ItemChorusFruit.class; //432
            list[POPPED_CHORUS_FRUIT] = ItemChorusFruitPopped.class; //433
            list[BANNER_PATTERN] = ItemBannerPattern.class; //434

            list[DRAGON_BREATH] = ItemDragonBreath.class; //437
            list[SPLASH_POTION] = ItemPotionSplash.class; //438

            list[LINGERING_POTION] = ItemPotionLingering.class; //441
            list[SPARKLER] = ItemSparkler.class; //442 edu
            list[COMMAND_BLOCK_MINECART] = ItemMinecartCommandBlock.class; //443
            list[ELYTRA] = ItemElytra.class; //444
            list[SHULKER_SHELL] = ItemShulkerShell.class; //445
            list[BANNER] = ItemBanner.class; //446
            list[EYE_DROP] = ItemEyeDrop.class; //447 edu
            list[BALLOON] = ItemBalloon.class; //448 edu
            list[SUPER_FERTILIZER] = ItemSuperFertilizer.class; //449 edu
            list[TOTEM] = ItemTotem.class; //450
            list[BLEACH] = ItemBleach.class; //451 edu
            list[IRON_NUGGET] = ItemIronNugget.class; //452
            list[ICE_BOMB] = ItemIceBomb.class; //453 edu
            //list[CHALKBOARD] = ItemChalkboard.class; //454 Edu
            list[TRIDENT] = ItemTrident.class; //455
            //list[PORTFOLIO] = ItemPortfolio.class; //456 Edu
            list[BEETROOT] = ItemBeetroot.class; //457
            list[BEETROOT_SEEDS] = ItemSeedsBeetroot.class; //458
            list[BEETROOT_SOUP] = ItemBeetrootSoup.class; //459
            list[RAW_SALMON] = ItemSalmon.class; //460
            list[CLOWNFISH] = ItemClownfish.class; //461
            list[PUFFERFISH] = ItemPufferfish.class; //462
            list[COOKED_SALMON] = ItemSalmonCooked.class; //463
            list[DRIED_KELP] = ItemDriedKelp.class; //464
            list[NAUTILUS_SHELL] = ItemNautilusShell.class; //465
            list[GOLDEN_APPLE_ENCHANTED] = ItemAppleGoldEnchanted.class; //466
            list[HEART_OF_THE_SEA] = ItemHeartOfTheSea.class; //467
            list[SCUTE] = ItemScute.class; //468
            list[TURTLE_SHELL] = ItemTurtleShell.class; //469
            list[PHANTOM_MEMBRANE] = ItemPhantomMembrane.class; //470
            list[CROSSBOW] = ItemCrossbow.class; //471
            list[SPRUCE_SIGN] = ItemSignSpruce.class; //472
            list[BIRCH_SIGN] = ItemSignBirch.class; //473
            list[JUNGLE_SIGN] = ItemSignJungle.class; //474
            list[ACACIA_SIGN] = ItemSignAcacia.class; //475
            list[DARK_OAK_SIGN] = ItemSignDarkOak.class; //476
            list[SWEET_BERRIES] = ItemSweetBerries.class; //477

            //list[CAMERA] = ItemCamera.class; //498 Edu
            list[COMPOUND] = ItemCompound.class; //499 edu
            list[RECORD_13] = ItemRecord13.class; //500
            list[RECORD_CAT] = ItemRecordCat.class; //501
            list[RECORD_BLOCKS] = ItemRecordBlocks.class; //502
            list[RECORD_CHIRP] = ItemRecordChirp.class; //503
            list[RECORD_FAR] = ItemRecordFar.class; //504
            list[RECORD_MALL] = ItemRecordMall.class; //508
            list[RECORD_MELLOHI] = ItemRecordMellohi.class; //506
            list[RECORD_STAL] = ItemRecordStal.class; //507
            list[RECORD_STRAD] = ItemRecordStrad.class; //508
            list[RECORD_WARD] = ItemRecordWard.class; //509
            list[RECORD_11] = ItemRecord11.class; //510
            list[RECORD_WAIT] = ItemRecordWait.class; //511

            list[SHIELD] = ItemShield.class; //513

            list[CAMPFIRE] = ItemCampfire.class; //720
        }

        initCreativeItems();
    }

    private static final ArrayList<Item> creative = new ArrayList<>();

    private static void initCreativeItems() {
        clearCreativeItems();
        Server server = Server.getInstance();

        String path = server.getDataPath() + "creativeitems.json";
        if (!new File(path).exists()) {
            try {
                Utils.writeFile(path, Server.class.getClassLoader().getResourceAsStream("creativeitems.json"));
            } catch (IOException e) {
                log.throwing(e);
                return;
            }
        }
        List<Map> list = new Config(path, Config.YAML).getMapList("items");

        for (Map map : list) {
            try {
                int rid = ((int) map.get("id"));

                int tid = rid < 0 ? 0xff - rid : rid;
                if (rid <= 0xff && (Block.experimental[tid] && !server.isExperimentalAllowed() || Block.education[tid] && !server.isEducationAllowed())) {
                    continue;
                }

                int id = rid & 0xffff;
                int damage = ((int) map.getOrDefault("damage", 0)) & 0xffff;
                String hex = (String) map.get("nbt_hex");
                byte[] nbt = hex != null ? Utils.parseHexBinary(hex) : new byte[0];

                addCreativeItem(Item.get(id, damage, 1, nbt));
            } catch (Exception e) {
                log.throwing(e);
            }
        }
    }

    public static void clearCreativeItems() {
        Item.creative.clear();
    }

    public static ArrayList<Item> getCreativeItems() {
        return new ArrayList<>(Item.creative);
    }

    public static void addCreativeItem(Item item) {
        Item.creative.add(item.clone());
    }

    public static void removeCreativeItem(Item item) {
        int index = getCreativeItemIndex(item);
        if (index != -1) {
            Item.creative.remove(index);
        }
    }

    public static boolean isCreativeItem(Item item) {
        for (Item aCreative : Item.creative) {
            if (item.equals(aCreative, !item.isTool())) {
                return true;
            }
        }
        return false;
    }

    public static Item getCreativeItem(int index) {
        return (index >= 0 && index < Item.creative.size()) ? Item.creative.get(index) : null;
    }

    public static int getCreativeItemIndex(Item item) {
        for (int i = 0; i < Item.creative.size(); i++) {
            if (item.equals(Item.creative.get(i), !item.isTool())) {
                return i;
            }
        }
        return -1;
    }

    public static Item get(int id) {
        return get(id, 0);
    }

    public static Item get(int id, Integer meta) {
        return get(id, meta, 1);
    }

    public static Item get(int id, Integer meta, int count) {
        return get(id, meta, count, new byte[0]);
    }

    public static Item get(int id, Integer meta, int count, byte[] tags) {
        try {
            Class c = list[id];
            Item item;

            if (c == null) {
                item = new Item(id, meta, count);
            } else if (id < 256) {
                if (meta >= 0) {
                    item = new ItemBlock(Block.get(id, meta), meta, count);
                } else {
                    item = new ItemBlock(Block.get(id), meta, count);
                }
            } else if (id > 0xfff) {
                if (meta >= 0) {
                    item = new ItemBlock(Block.get(0xffff - id + 256, meta), meta, count);
                } else {
                    item = new ItemBlock(Block.get(0xffff - id + 256), meta, count);
                }
            } else {
                item = ((Item) c.getConstructor(Integer.class, int.class).newInstance(meta, count));
            }

            if (tags.length != 0) {
                item.setCompoundTag(tags);
            }

            return item;
        } catch (Exception e) {
            return new Item(id, meta, count).setCompoundTag(tags);
        }
    }

    public static Item fromString(String str) {
        String[] b = str.trim().replace(' ', '_').replace("minecraft:", "").split(":");

        int id = 0;
        int meta = 0;

        Pattern integerPattern = Pattern.compile("^-?[1-9]\\d*$");
        if (integerPattern.matcher(b[0]).matches()) {
            id = Integer.valueOf(b[0]);
        } else {
            try {
                id = Item.class.getField(b[0].toUpperCase()).getInt(null);
            } catch (Exception ignore) {
            }
        }

        id &= 0xffff;
        if (b.length != 1) {
            meta = Integer.valueOf(b[1]) & 0xffff;
        }

        return get(id, meta);
    }

    public static Item fromJson(Map<String, Object> data) {
        String nbt = (String) data.getOrDefault("nbt_hex", "");

        return get(Utils.toInt(data.get("id")) & 0xffff, Utils.toInt(data.getOrDefault("damage", 0)), Utils.toInt(data.getOrDefault("count", 1)), nbt.isEmpty() ? new byte[0] : Utils.parseHexBinary(nbt));
    }

    public static Item[] fromStringMultiple(String str) {
        String[] b = str.split(",");
        Item[] items = new Item[b.length - 1];
        for (int i = 0; i < b.length; i++) {
            items[i] = fromString(b[i]);
        }
        return items;
    }

    /**
     * @param tag CompoundTag
     *
     * @return Item
     */
    public Item setCompoundTag(CompoundTag tag) {
        this.setNamedTag(tag);
        return this;
    }

    /**
     * @param tags byte[]
     *
     * @return Item
     */
    public Item setCompoundTag(byte[] tags) {
        this.tags = tags;
        this.cachedNBT = null;
        return this;
    }

    /**
     * @return byte[]
     */
    public byte[] getCompoundTag() {
        return tags;
    }

    /**
     * @return boolean
     */
    public boolean hasCompoundTag() {
        return this.tags != null && this.tags.length > 0;
    }

    /**
     * @return boolean
     */
    public boolean hasCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();
        return tag.contains("BlockEntityTag") && tag.get("BlockEntityTag") instanceof CompoundTag;

    }

    /**
     * @return Item
     */
    public Item clearCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return this;
        }
        CompoundTag tag = this.getNamedTag();

        if (tag.contains("BlockEntityTag") && tag.get("BlockEntityTag") instanceof CompoundTag) {
            tag.remove("BlockEntityTag");
            this.setNamedTag(tag);
        }

        return this;
    }

    /**
     * @param compoundTag CompoundTag
     *
     * @return Item
     */
    public Item setCustomBlockData(CompoundTag compoundTag) {
        CompoundTag tags = compoundTag.copy();
        tags.setName("BlockEntityTag");

        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        tag.putCompound("BlockEntityTag", tags);
        this.setNamedTag(tag);

        return this;
    }

    /**
     * @return CompoundTag
     */
    public CompoundTag getCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return null;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("BlockEntityTag")) {
            Tag bet = tag.get("BlockEntityTag");
            if (bet instanceof CompoundTag) {
                return (CompoundTag) bet;
            }
        }

        return null;
    }

    /**
     * @return boolean
     */
    public boolean hasEnchantments() {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("ench")) {
            return tag.get("ench") instanceof ListTag;
        }

        return false;
    }

    public boolean hasEnchantment(int id) {
        return this.hasEnchantment(id, -1);
    }

    /**
     * @param id int
     * @param level int
     *
     * @return boolean
     */
    public boolean hasEnchantment(int id, int level) {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("ench") && tag.get("ench") instanceof ListTag) {
            for (CompoundTag entry : tag.getList("ench", CompoundTag.class).getAll()) {
                if (entry.getShort("id") == id && (level == -1 || entry.getShort("lvl") == level)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param id int
     *
     * @return Enchantment
     */
    public Enchantment getEnchantment(int id) {
        return getEnchantment((short) (id & 0xffff));
    }

    public Enchantment getEnchantment(short id) {
        if (!this.hasCompoundTag()) {
            return null;
        }
        CompoundTag tag = this.getNamedTag();
        if (!(tag.get("ench") instanceof ListTag)) {
            return null;
        }

        for (CompoundTag entry : tag.getList("ench", CompoundTag.class).getAll()) {
            if (entry.getShort("id") == id) {
                Enchantment e = Enchantment.getEnchantment(entry.getShort("id"));
                if (e != null) {
                    e.setLevel(entry.getShort("lvl"));
                    return e;
                }
            }
        }

        return null;
    }

    public Item removeEnchantment(int id) {
        return this.removeEnchantment(id, -1);
    }

    /**
     * @param id int
     * @param level int
     *
     * @return Item
     */
    public Item removeEnchantment(int id, int level) {
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        ListTag<CompoundTag> ench;
        if (!tag.contains("ench")) {
            ench = new ListTag<>("ench");
            tag.putList(ench);
        } else {
            ench = tag.getList("ench", CompoundTag.class);
        }

        for (int k = 0; k < ench.size(); k++) {
            CompoundTag entry = ench.get(k);
            if (entry.getShort("id") == id && (level == -1 || entry.getShort("lvl") == level)) {
                ench.remove(entry);
                break;
            }
        }

        this.setNamedTag(tag);

        return this;
    }

    /**
     * @return Item
     */
    public Item removeEnchantments() {
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        tag.putList(new ListTag<>("ench"));

        this.setNamedTag(tag);

        return this;
    }

    /**
     * @param enchantments Enchantment...
     *
     * @return Item
     */
    public Item addEnchantment(Enchantment... enchantments) {
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        ListTag<CompoundTag> ench;
        if (!tag.contains("ench")) {
            ench = new ListTag<>("ench");
            tag.putList(ench);
        } else {
            ench = tag.getList("ench", CompoundTag.class);
        }

        for (Enchantment enchantment : enchantments) {
            boolean found = false;

            for (int k = 0; k < ench.size(); k++) {
                CompoundTag entry = ench.get(k);
                if (entry.getShort("id") == enchantment.getId()) {
                    ench.add(k, new CompoundTag()
                            .putShort("id", enchantment.getId())
                            .putShort("lvl", enchantment.getLevel())
                    );
                    found = true;
                    break;
                }
            }

            if (!found) {
                ench.add(new CompoundTag()
                        .putShort("id", enchantment.getId())
                        .putShort("lvl", enchantment.getLevel())
                );
            }
        }

        this.setNamedTag(tag);

        return this;
    }

    /**
     * @return Enchantment[]
     */
    public Enchantment[] getEnchantments() {
        if (!this.hasEnchantments()) {
            return new Enchantment[0];
        }

        List<Enchantment> enchantments = new ArrayList<>();

        ListTag<CompoundTag> ench = this.getNamedTag().getList("ench", CompoundTag.class);
        for (CompoundTag entry : ench.getAll()) {
            Enchantment e = Enchantment.getEnchantment(entry.getShort("id"));
            if (e != null) {
                e.setLevel(entry.getShort("lvl"));
                enchantments.add(e);
            }
        }

        return enchantments.toArray(new Enchantment[0]);
    }

    /**
     * Returns the level of the enchantment on this item with the specified ID, or 0 if the item does not have the enchantment.
     *
     * @param enchantmentId int
     *
     * @return int
     */
    public int getEnchantmentLevel(int enchantmentId) {
        if (this.hasEnchantments()) {
            for (CompoundTag entry : this.getNamedTag().getList("ench", CompoundTag.class).getAll()) {
                if (entry.getShort("id") == enchantmentId) {
                    return entry.getShort("lvl");
                }
            }
        }
        return 0;
    }

    /**
     * @return boolean
     */
    public boolean hasCustomName() {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();
        if (tag.contains("display")) {
            Tag tag1 = tag.get("display");
            return tag1 instanceof CompoundTag && ((CompoundTag) tag1).contains("Name") && ((CompoundTag) tag1).get("Name") instanceof StringTag;
        }

        return false;
    }

    /**
     * @return String
     */
    public String getCustomName() {
        if (!this.hasCompoundTag()) {
            return "";
        }

        CompoundTag tag = this.getNamedTag();
        if (tag.contains("display")) {
            Tag tag1 = tag.get("display");
            if (tag1 instanceof CompoundTag && ((CompoundTag) tag1).contains("Name") && ((CompoundTag) tag1).get("Name") instanceof StringTag) {
                return ((CompoundTag) tag1).getString("Name");
            }
        }

        return "";
    }

    /**
     * @param name String
     *
     * @return Item
     */
    public Item setCustomName(String name) {
        if (name == null || name.isEmpty()) {
            this.clearCustomName();
        }

        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }
        if (tag.contains("display") && tag.get("display") instanceof CompoundTag) {
            tag.getCompound("display").putString("Name", name);
        } else {
            tag.putCompound("display", new CompoundTag("display")
                    .putString("Name", name)
            );
        }
        this.setNamedTag(tag);
        return this;
    }

    /**
     * @return Item
     */
    public Item clearCustomName() {
        if (!this.hasCompoundTag()) {
            return this;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("display") && tag.get("display") instanceof CompoundTag) {
            tag.getCompound("display").remove("Name");
            if (tag.getCompound("display").isEmpty()) {
                tag.remove("display");
            }

            this.setNamedTag(tag);
        }

        return this;
    }

    /**
     * @return String[]
     */
    public String[] getLore() {
        Tag tag = this.getNamedTagEntry("display");
        ArrayList<String> lines = new ArrayList<>();

        if (tag instanceof CompoundTag) {
            CompoundTag nbt = (CompoundTag) tag;
            ListTag<StringTag> lore = nbt.getList("Lore", StringTag.class);

            if (lore.size() > 0) {
                for (StringTag stringTag : lore.getAll()) {
                    lines.add(stringTag.data);
                }
            }
        }

        return lines.toArray(new String[0]);
    }

    /**
     * @param lines String...
     *
     * @return Item
     */
    public Item setLore(String... lines) {
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }
        ListTag<StringTag> lore = new ListTag<>("Lore");

        for (String line : lines) {
            lore.add(new StringTag("", line));
        }

        if (!tag.contains("display")) {
            tag.putCompound("display", new CompoundTag("display").putList(lore));
        } else {
            tag.getCompound("display").putList(lore);
        }

        this.setNamedTag(tag);
        return this;
    }

    /**
     * @param name String
     *
     * @return Tag
     */
    public Tag getNamedTagEntry(String name) {
        CompoundTag tag = this.getNamedTag();
        if (tag != null) {
            return tag.contains(name) ? tag.get(name) : null;
        }

        return null;
    }

    public void setNamedTagEntry(String name, Tag newTag) {
        CompoundTag tag = this.getNamedTag();
        tag.put(name, newTag);
        this.setNamedTag(tag);
    }

    public void removeNamedTagEntry(String... names) {
        CompoundTag tag = this.getNamedTag();
        for (String name : names) {
            tag.remove(name);
        }
        this.setNamedTag(tag);
    }

    /**
     * Returns a tree of Tag objects representing the Item's NBT. If the item does not have any NBT, an empty CompoundTag
     * object is returned to allow the caller to manipulate and apply back to the item.
     *
     * @return CompoundTag
     */
    public CompoundTag getNamedTag() {
        if (!this.hasCompoundTag()) {
            return null;
        }

        if (this.cachedNBT == null) {
            this.cachedNBT = parseCompoundTag(this.tags);
        }

        if (this.cachedNBT != null) {
            this.cachedNBT.setName("");
        }

        return this.cachedNBT;
    }

    /**
     * Sets the Item's NBT from the supplied CompoundTag object.
     *
     * @param tag CompoundTag
     *
     * @return Item
     */
    public Item setNamedTag(CompoundTag tag) {
        if (tag.isEmpty()) {
            return this.clearNamedTag();
        }
        tag.setName(null);

        this.cachedNBT = tag;
        this.tags = writeCompoundTag(tag);

        return this;
    }

    /**
     * Removes the Item's NBT.
     * @return Item
     */
    public Item clearNamedTag() {
        return this.setCompoundTag(new byte[0]);
    }

    public static CompoundTag parseCompoundTag(byte[] tag) {
        try {
            return NBTIO.read(tag, ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] writeCompoundTag(CompoundTag tag) {
        try {
            tag.setName("");
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @return int
     */
    public int getCount() {
        return this.count;
    }

    /**
     * @param count int
     *
     * @return Item
     */
    public Item setCount(int count) {
        this.count = count;

        return this;
    }

    /**
     * @return boolean
     */
    public boolean isNull() {
        return this.count <= 0 || this.id == AIR;
    }

    /**
     * Returns the name of the item, or the custom name if it is set.
     * @return string
     */
    public final String getName() {
        return this.hasCustomName() ? this.getCustomName() : this.getVanillaName();
    }

    /**
     * Returns the vanilla name of the item, disregarding custom names.
     * @return String
     */
    public String getVanillaName() {
        return this.name;
    }

    /**
     * @return boolean
     */
    public final boolean canBePlaced() {
        return ((this.block != null) && this.block.canBePlaced());
    }

    /**
     * Returns the block corresponding to this Item.
     * @return Block
     */
    public Block getBlock() {
        if (this.block != null) {
            return this.block.clone();
        } else {
            return new BlockAir();
        }
    }

    /**
     * @return int
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return int
     */
    public int getDamage() {
        return this.meta;
    }

    public void setDamage(Integer meta) {
        if (meta != null) {
            this.meta = meta & 0xffff;
        } else {
            this.hasMeta = false;
        }
    }

    /**
     * Returns whether this item can match any item with an equivalent ID with any meta value.
     * Used in crafting recipes which accept multiple variants of the same item, for example crafting tables recipes.
     *
     * @return boolean
     */
    public boolean hasAnyDamageValue() {
        return this.meta == -1;
    }

    /**
     * Returns the highest amount of this item which will fit into one inventory slot.
     * @return int
     */
    public int getMaxStackSize() {
        return 64;
    }

    /**
     * Returns the time in ticks which the item will fuel a furnace for.
     * @return Short
     */
    public final Short getFuelTime() {
        if (!Fuel.duration.containsKey(id)) {
            return null;
        }
        if (this.id != BUCKET || this.meta == 10) {
            return Fuel.duration.get(this.id);
        }
        return null;
    }
    /**
     * Returns how many points of damage this item will deal to an entity when used as a weapon.
     * @return int
     */
    public int getAttackPoints() {
        return 1;
    }

    /**
     * Returns how many armor points can be gained by wearing this item.
     * @return int
     */
    public int getDefensePoints() {
        return 0;
    }

    ///**
    // * Returns what type of block-breaking tool this is. Blocks requiring the same tool type as the item will break
    // * faster (except for blocks requiring no tool, which break at the same speed regardless of the tool used)
    // *
    // * @return int
    // */
    //public int getBlockToolType() {
    //    return BlockToolType.TYPE_NONE;
    //}

    ///**
    // * Returns the harvesting power that this tool has. This affects what blocks it can mine when the tool type matches
    // * the mined block.
    // * This should return 1 for non-tiered tools, and the tool tier for tiered tools.
    // *
    // * @see Block.getToolHarvestLevel()
    // *
    // * @return int
    // */
    //public int getBlockToolHarvestLevel() {
    //    return 0;
    //}

    public float getMiningEfficiency(Block block) {
        return 1;
    }

    public boolean useOn(Entity entity) {
        return false;
    }

    public boolean useOn(Block block) {
        return false;
    }

    public boolean isTool() {
        return false;
    }

    public int getMaxDurability() {
        return -1;
    }

    public int getTier() {
        return 0;
    }

    public boolean isPickaxe() {
        return false;
    }

    public boolean isAxe() {
        return false;
    }

    public boolean isSword() {
        return false;
    }

    public boolean isShovel() {
        return false;
    }

    public boolean isHoe() {
        return false;
    }

    public boolean isShears() {
        return false;
    }

    public boolean isArmor() {
        return false;
    }

    public boolean isHelmet() {
        return false;
    }

    public boolean isChestplate() {
        return false;
    }

    public boolean isLeggings() {
        return false;
    }

    public boolean isBoots() {
        return false;
    }

    public int getEnchantAbility() {
        return 0;
    }

    public int getAttackDamage() {
        return 1;
    }

    public int getArmorPoints() {
        return 0;
    }

    public int getToughness() {
        return 0;
    }

    public boolean isUnbreakable() {
        return false;
    }

    public int getDestroySpeed(Block block, Player player) {
        return 1;
    }

    /**
     * Called when a player uses this item on a block.
     *
     * @param level Level
     * @param player Player
     * @param block Block
     * @param target Block
     * @param face BlockFace
     * @param fx double
     * @param fy double
     * @param fz double
     *
     * @return boolean
     */
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        return false;
    }

    /**
     * Called when a player uses the item on air, for example throwing a projectile.
     * Returns whether the item was changed, for example count decrease or durability change.
     *
     * @param player player
     * @param directionVector direction
     *
     * @return item changed
     */
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return false;
    }

    /**
     * Called when a player is using this item and releases it. Used to handle bow shoot actions.
     * Returns whether the item was changed, for example count decrease or durability change.
     *
     * @param player Player
     *
     * @return boolean
     */
    public boolean onReleaseUsing(Player player) {
        return false;
    }

    /**
     * Called when this item is used to destroy a block. Usually used to update durability.
     *
     * @param block Block
     *
     * @return boolean
     */
    public boolean onDestroyBlock(Block block) {
        return false;
    }

    /**
     * Called when this item is used to attack an entity. Usually used to update durability.
     *
     * @param victim Entity
     *
     * @return boolean
     */
    public boolean onAttackEntity(Entity victim) {
        return false;
    }

    /**
     * Returns the number of ticks a player must wait before activating this item again.
     *
     * @return int
     */
    public int getCooldownTicks() {
        return 0;
    }

    public boolean isExperimental() {
        return false;
    }

    public boolean isEducation() {
        return false;
    }

    @Override
    public final boolean equals(Object item) {
        return item instanceof Item && this.equals((Item) item, true);
    }

    public final boolean equals(Item item, boolean checkDamage) {
        return equals(item, checkDamage, true);
    }

    /**
     * Compares an Item to this Item and check if they match.
     *
     * @param item Item
     * @param checkDamage Whether to verify that the damage values match.
     * @param checkCompound Whether to verify that the items' NBT match.
     *
     * @return bool
     */
    public final boolean equals(Item item, boolean checkDamage, boolean checkCompound) {
        if (item != null && this.getId() == item.getId() && (!checkDamage || this.getDamage() == item.getDamage())) {
            if (checkCompound) {
                if (Arrays.equals(this.getCompoundTag(), item.getCompoundTag())) {
                    return true;
                } else if (this.hasCompoundTag() && item.hasCompoundTag()) {
                    return this.getNamedTag().equals(item.getNamedTag());
                }
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the specified item stack has the same ID, damage, NBT and count as this item stack.
     *
     * @param other Item
     *
     * @return boolean equal
     */
    public final boolean equalsExact(Item other) {
        return this.equals(other, true, true) && this.count == other.count;
    }

    @Deprecated
    public final boolean deepEquals(Item item) {
        return equals(item, true);
    }

    @Deprecated
    public final boolean deepEquals(Item item, boolean checkDamage) {
        return equals(item, checkDamage, true);
    }

    @Deprecated
    public final boolean deepEquals(Item item, boolean checkDamage, boolean checkCompound) {
        return equals(item, checkDamage, checkCompound);
    }

    /**
     * @return String
     */
    @Override
    final public String toString() {
        return "Item " + this.name + " (" + this.id + ":" + (!this.hasMeta ? "?" : this.meta) + ")x" + this.count + (this.hasCompoundTag() ? " tags:0x" + Binary.bytesToHexString(this.getCompoundTag()) : "");
    }

    @Override
    public Item clone() {
        try {
            Item item = (Item) super.clone();
            item.tags = this.tags.clone();
            return item;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
