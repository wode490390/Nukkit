package cn.nukkit.block;

public interface BlockID {

    int AIR = 0;
    int STONE = 1;
    int GRASS = 2;
    int DIRT = 3;
    int COBBLESTONE = 4, COBBLE = 4;
    int PLANKS = 5, PLANK = 5, WOODEN_PLANK = 5, WOODEN_PLANKS = 5;
    int SAPLING = 6, SAPLINGS = 6;
    int BEDROCK = 7;
    int FLOWING_WATER = 8, WATER = 8;//
    int STILL_WATER = 9;//WATER = 9, 
    int FLOWING_LAVA = 10, LAVA = 10;//
    int STILL_LAVA = 11;//LAVA = 11, 
    int SAND = 12;
    int GRAVEL = 13;
    int GOLD_ORE = 14;
    int IRON_ORE = 15;
    int COAL_ORE = 16;
    int LOG = 17, WOOD = 17, TRUNK = 17;
    int LEAVES = 18, LEAVE = 18;
    int SPONGE = 19;
    int GLASS = 20;
    int LAPIS_ORE = 21;
    int LAPIS_BLOCK = 22;
    int DISPENSER = 23;
    int SANDSTONE = 24;
    int NOTEBLOCK = 25;
    int BED_BLOCK = 26;//BED = 26, 
    int GOLDEN_RAIL = 27, POWERED_RAIL = 27;
    int DETECTOR_RAIL = 28;
    int STICKY_PISTON = 29;
    int WEB = 30 = COBWEB = 30;
    int TALLGRASS = 31, TALL_GRASS = 31;
    int DEADBUSH = 32, BUSH = 32, DEAD_BUSH = 32;
    int PISTON = 33;
    int PISTON_ARM_COLLISION = 34, PISTON_HEAD = 34;
    int WOOL = 35;
    int ELEMENT_0 = 36; //edu
    int YELLOW_FLOWER = 37, DANDELION = 37;
    int RED_FLOWER = 38, POPPY = 38, ROSE = 38, FLOWER = 38;
    int BROWN_MUSHROOM = 39;
    int RED_MUSHROOM = 40;
    int GOLD_BLOCK = 41;
    int IRON_BLOCK = 42;
    int DOUBLE_STONE_SLAB = 43, DOUBLE_SLAB = 43, DOUBLE_SLABS = 43;
    int STONE_SLAB = 44, SLAB = 44, SLABS = 44;
    int BRICK_BLOCK = 45, BRICKS = 45, BRICKS_BLOCK = 45;
    int TNT = 46;
    int BOOKSHELF = 47;
    int MOSSY_COBBLESTONE = 48, MOSS_STONE = 48, MOSSY_STONE = 48;
    int OBSIDIAN = 49;
    int TORCH = 50;
    int FIRE = 51;
    int MOB_SPAWNER = 52, MONSTER_SPAWNER = 52;
    int OAK_STAIRS = 53, WOOD_STAIRS = 53, WOODEN_STAIRS = 53, OAK_WOOD_STAIRS = 53, OAK_WOODEN_STAIRS = 53;
    int CHEST = 54;
    int REDSTONE_WIRE = 55;
    int DIAMOND_ORE = 56;
    int DIAMOND_BLOCK = 57;
    int CRAFTING_TABLE = 58, WORKBENCH = 58;
    int WHEAT_BLOCK = 59;//WHEAT = 59, 
    int FARMLAND = 60;
    int FURNACE = 61;
    int BURNING_FURNACE = 62, LIT_FURNACE = 62;
    int STANDING_SIGN = 63, SIGN_POST = 63;
    int DOOR_BLOCK = 64, WOODEN_DOOR_BLOCK = 64, WOOD_DOOR_BLOCK = 64;//WOODEN_DOOR = 64, 
    int LADDER = 65;
    int RAIL = 66;
    int STONE_STAIRS = 67, COBBLE_STAIRS = 67, COBBLESTONE_STAIRS = 67;
    int WALL_SIGN = 68;
    int LEVER = 69;
    int STONE_PRESSURE_PLATE = 70;
    int IRON_DOOR_BLOCK = 71;//IRON_DOOR = 71, 
    int WOODEN_PRESSURE_PLATE = 72;
    int REDSTONE_ORE = 73;
    int LIT_REDSTONE_ORE = 74, GLOWING_REDSTONE_ORE = 74;
    int UNLIT_REDSTONE_TORCH = 75;
    int REDSTONE_TORCH = 76;
    int STONE_BUTTON = 77;
    int SNOW_LAYER = 78, SNOW = 78;
    int ICE = 79;
    int SNOW = 80, SNOW_BLOCK = 80;
    int CACTUS = 81;
    int CLAY_BLOCK = 82;//CLAY = 82, 
    int REEDS = 83, SUGARCANE_BLOCK = 83;
    int JUKEBOX = 84;
    int FENCE = 85;
    int PUMPKIN = 86;
    int NETHERRACK = 87;
    int SOUL_SAND = 88;
    int GLOWSTONE = 89, GLOWSTONE_BLOCK = 89;
    int PORTAL = 90, NETHER_PORTAL = 90;
    int LIT_PUMPKIN = 91, JACK_O_LANTERN = 91;
    int CAKE_BLOCK = 92;//CAKE = 92, 
    int UNPOWERED_REPEATER = 93;
    int POWERED_REPEATER = 94;
    int INVISIBLE_BEDROCK = 95;
    int TRAPDOOR = 96;
    int MONSTER_EGG = 97;
    int STONEBRICK = 98, STONE_BRICKS = 98, STONE_BRICK = 98;
    int BROWN_MUSHROOM_BLOCK = 99;
    int RED_MUSHROOM_BLOCK = 100;
    int IRON_BARS = 101, IRON_BAR = 101;
    int GLASS_PANE = 102, GLASS_PANEL = 102;
    int MELON_BLOCK = 103;
    int PUMPKIN_STEM = 104;
    int MELON_STEM = 105;
    int VINE = 106, VINES = 106;
    int FENCE_GATE = 107, FENCE_GATE_OAK = 107;
    int BRICK_STAIRS = 108;
    int STONE_BRICK_STAIRS = 109;
    int MYCELIUM = 110;
    int WATERLILY = 111, WATER_LILY = 111, LILY_PAD = 111;
    int NETHER_BRICKS = 112, NETHER_BRICK_BLOCK = 112;//NETHER_BRICK = 112, 
    int NETHER_BRICK_FENCE = 113;
    int NETHER_BRICKS_STAIRS = 114;
    int NETHER_WART_BLOCK = 115;//NETHER_WART = 115, 
    int ENCHANTING_TABLE = 116, ENCHANT_TABLE = 116, ENCHANTMENT_TABLE = 116;
    int BREWING_STAND_BLOCK = 117, BREWING_BLOCK = 117;//BREWING_STAND = 117, 
    int CAULDRON_BLOCK = 118;//CAULDRON = 118, 
    int END_PORTAL = 119;
    int END_PORTAL_FRAME = 120;
    int END_STONE = 121;
    int DRAGON_EGG = 122;
    int REDSTONE_LAMP = 123;
    int LIT_REDSTONE_LAMP = 124;
    int DROPPER = 125;
    int ACTIVATOR_RAIL = 126;
    int COCOA = 127, COCOA_BLOCK = 127;
    int SANDSTONE_STAIRS = 128;
    int EMERALD_ORE = 129;
    int ENDER_CHEST = 130;
    int TRIPWIRE_HOOK = 131;
    int TRIPWIRE = 132;
    int EMERALD_BLOCK = 133;
    int SPRUCE_STAIRS = 134, SPRUCE_WOOD_STAIRS = 134, SPRUCE_WOODEN_STAIRS = 134;
    int BIRCH_STAIRS = 135, BIRCH_WOOD_STAIRS = 135, BIRCH_WOODEN_STAIRS = 135;
    int JUNGLE_STAIRS = 136, JUNGLE_WOOD_STAIRS = 136, JUNGLE_WOODEN_STAIRS = 136;
    int COMMAND_BLOCK = 137;
    int BEACON = 138;
    int COBBLESTONE_WALL = 139, COBBLE_WALL = 139, STONE_WALL = 139;
    int FLOWER_POT_BLOCK = 140;//FLOWER_POT = 140, 
    int CARROTS = 141, CARROT_BLOCK = 141;
    int POTATOES = 142, POTATO_BLOCK = 142;
    int WOODEN_BUTTON = 143;
    int SKULL_BLOCK = 144;//SKULL = 144, 
    int ANVIL = 145;
    int TRAPPED_CHEST = 146;
    int LIGHT_WEIGHTED_PRESSURE_PLATE = 147;
    int HEAVY_WEIGHTED_PRESSURE_PLATE = 148;
    int UNPOWERED_COMPARATOR = 149;
    int POWERED_COMPARATOR = 150;
    int DAYLIGHT_DETECTOR = 151;
    int REDSTONE_BLOCK = 152;
    int QUARTZ_ORE = 153;
    int HOPPER_BLOCK = 154;//HOPPER = 154, 
    int QUARTZ_BLOCK = 155;
    int QUARTZ_STAIRS = 156;
    int DOUBLE_WOODEN_SLAB = 157, DOUBLE_WOOD_SLAB = 157, DOUBLE_WOOD_SLABS = 157, DOUBLE_WOODEN_SLABS = 157;
    int WOODEN_SLAB = 158, WOOD_SLAB = 158, WOOD_SLABS = 158, WOODEN_SLABS = 158;
    int STAINED_HARDENED_CLAY = 159, STAINED_TERRACOTTA = 159;
    int STAINED_GLASS_PANE = 160;
    int LEAVES2 = 161, LEAVE2 = 161;
    int LOG2 = 162, WOOD2 = 162, TRUNK2 = 162;
    int ACACIA_STAIRS = 163, ACACIA_WOOD_STAIRS = 163, ACACIA_WOODEN_STAIRS = 163;
    int DARK_OAK_STAIRS = 164, DARK_OAK_WOOD_STAIRS = 164, DARK_OAK_WOODEN_STAIRS = 164;
    int SLIME_BLOCK = 165;//SLIME = 165, 
    int GLOW_STICK = 166; //edu
    int IRON_TRAPDOOR = 167;
    int PRISMARINE = 168;
    int SEA_LANTERN = 169;
    int HAY_BALE = 170;
    int CARPET = 171;
    int HARDENED_CLAY = 172, TERRACOTTA = 172;
    int COAL_BLOCK = 173;
    int PACKED_ICE = 174;
    int DOUBLE_PLANT = 175;
    int STANDING_BANNER = 176;
    int WALL_BANNER = 177;
    int DAYLIGHT_DETECTOR_INVERTED = 178;
    int RED_SANDSTONE = 179;
    int RED_SANDSTONE_STAIRS = 180;
    int DOUBLE_STONE_SLAB2 = 181, DOUBLE_RED_SANDSTONE_SLAB = 181;
    int STONE_SLAB2 = 182, RED_SANDSTONE_SLAB = 182;
    int SPRUCE_FENCE_GATE = 183, FENCE_GATE_SPRUCE = 183;
    int BIRCH_FENCE_GATE = 184, FENCE_GATE_BIRCH = 184;
    int JUNGLE_FENCE_GATE = 185, FENCE_GATE_JUNGLE = 185;
    int DARK_OAK_FENCE_GATE = 186, FENCE_GATE_DARK_OAK = 186;
    int ACACIA_FENCE_GATE = 187, FENCE_GATE_ACACIA = 187;
    int REPEATING_COMMAND_BLOCK = 188;
    int CHAIN_COMMAND_BLOCK = 189;
    int HARD_GLASS_PANE = 190; //edu
    int HARD_STAINED_GLASS_PANE = 191; //edu
    int CHEMICAL_HEAT = 192; //edu
    int SPRUCE_DOOR_BLOCK = 193;//SPRUCE_DOOR = 193, 
    int BIRCH_DOOR_BLOCK = 194;//BIRCH_DOOR = 194, 
    int JUNGLE_DOOR_BLOCK = 195;//JUNGLE_DOOR = 195, 
    int ACACIA_DOOR_BLOCK = 196;//ACACIA_DOOR = 196, 
    int DARK_OAK_DOOR_BLOCK = 197;//DARK_OAK_DOOR = 197, 
    int GRASS_PATH = 198;
    int FRAME = 199, ITEM_FRAME_BLOCK = 199;
    int CHORUS_FLOWER = 200;
    int PURPUR_BLOCK = 201;
    int COLORED_TORCH_RG = 202; //edu
    int PURPUR_STAIRS = 203;
    int COLORED_TORCH_BP = 204; //edu
    int UNDYED_SHULKER_BOX = 205;
    int END_BRICKS = 206;
    int FROSTED_ICE = 207;
    int END_ROD = 208;
    int END_GATEWAY = 209;
    //int ALLOW = 210; //Edu
    //int DENY = 211; //Edu
    //int BORDER_BLOCK = 212; //Edu
    int MAGMA = 213;
    int NETHER_WART_BLOCK = 214, BLOCK_NETHER_WART_BLOCK = 214;
    int RED_NETHER_BRICK = 215;
    int BONE_BLOCK = 216;

    int SHULKER_BOX = 218;
    int PURPLE_GLAZED_TERRACOTTA = 219;
    int WHITE_GLAZED_TERRACOTTA = 220;
    int ORANGE_GLAZED_TERRACOTTA = 221;
    int MAGENTA_GLAZED_TERRACOTTA = 222;
    int LIGHT_BLUE_GLAZED_TERRACOTTA = 223;
    int YELLOW_GLAZED_TERRACOTTA = 224;
    int LIME_GLAZED_TERRACOTTA = 225;
    int PINK_GLAZED_TERRACOTTA = 226;
    int GRAY_GLAZED_TERRACOTTA = 227;
    int SILVER_GLAZED_TERRACOTTA = 228;
    int CYAN_GLAZED_TERRACOTTA = 229;
    //int CHALKBOARD_BLOCK = 230;//CHALKBOARD = 230, //Edu
    int BLUE_GLAZED_TERRACOTTA = 231;
    int BROWN_GLAZED_TERRACOTTA = 232;
    int GREEN_GLAZED_TERRACOTTA = 233;
    int RED_GLAZED_TERRACOTTA = 234;
    int BLACK_GLAZED_TERRACOTTA = 235;
    int CONCRETE = 236;
    int CONCRETE_POWDER = 237;
    int CHEMISTRY_TABLE = 238; //edu
    int UNDERWATER_TORCH = 239; //edu
    int CHORUS_PLANT = 240;
    int STAINED_GLASS = 241;
    //int CAMERA_BLOCK = 242;//CAMERA = 242, //Edu
    int PODZOL = 243;
    int BEETROOT_BLOCK = 244;//BEETROOT = 244, 
    int STONECUTTER = 245;
    int GLOWINGOBSIDIAN = 246, GLOWING_OBSIDIAN = 246;
    int NETHERREACTOR = 247, NETHER_REACTOR_CORE = 247;
    int INFO_UPDATE = 248;
    int INFO_UPDATE2 = 249;
    int MOVING_BLOCK = 250;
    int OBSERVER = 251;
    int STRUCTURE_BLOCK = 252;
    int HARD_GLASS = 253; //edu
    int HARD_STAINED_GLASS = 254; //edu
    int RESERVED6 = 255;

    /* HACK ID */

    int PRISMARINE_STAIRS = 257; //-2
    int DARK_PRISMARINE_STAIRS = 258; //-3
    int PRISMARINE_BRICKS_STAIRS = 259; //-4
    int STRIPPED_SPRUCE_LOG = 260; //-5
    int STRIPPED_BIRCH_LOG = 261; //-6
    int STRIPPED_JUNGLE_LOG = 262; //-7
    int STRIPPED_ACACIA_LOG = 263; //-8
    int STRIPPED_DARK_OAK_LOG = 264; //-9
    int STRIPPED_OAK_LOG = 265; //-10
    int BLUE_ICE = 266; //-11
    int ELEMENT_1 = 267; //-12 //edu
    int ELEMENT_2 = 268; //-13 //edu
    int ELEMENT_3 = 269; //-14 //edu
    int ELEMENT_4 = 270; //-15 //edu
    int ELEMENT_5 = 271; //-16 //edu
    int ELEMENT_6 = 272; //-17 //edu
    int ELEMENT_7 = 273; //-18 //edu
    int ELEMENT_8 = 274; //-19 //edu
    int ELEMENT_9 = 275; //-20 //edu
    int ELEMENT_10 = 276; //-21 //edu
    int ELEMENT_11 = 277; //-22 //edu
    int ELEMENT_12 = 278; //-23 //edu
    int ELEMENT_13 = 279; //-24 //edu
    int ELEMENT_14 = 280; //-25 //edu
    int ELEMENT_15 = 281; //-26 //edu
    int ELEMENT_16 = 282; //-27 //edu
    int ELEMENT_17 = 283; //-28 //edu
    int ELEMENT_18 = 284; //-29 //edu
    int ELEMENT_19 = 285; //-30 //edu
    int ELEMENT_20 = 286; //-31 //edu
    int ELEMENT_21 = 287; //-32 //edu
    int ELEMENT_22 = 288; //-33 //edu
    int ELEMENT_23 = 289; //-34 //edu
    int ELEMENT_24 = 290; //-35 //edu
    int ELEMENT_25 = 291; //-36 //edu
    int ELEMENT_26 = 292; //-37 //edu
    int ELEMENT_27 = 293; //-38 //edu
    int ELEMENT_28 = 294; //-39 //edu
    int ELEMENT_29 = 295; //-40 //edu
    int ELEMENT_30 = 296; //-41 //edu
    int ELEMENT_31 = 297; //-42 //edu
    int ELEMENT_32 = 298; //-43 //edu
    int ELEMENT_33 = 299; //-44 //edu
    int ELEMENT_34 = 300; //-45 //edu
    int ELEMENT_35 = 301; //-46 //edu
    int ELEMENT_36 = 302; //-47 //edu
    int ELEMENT_37 = 303; //-48 //edu
    int ELEMENT_38 = 304; //-49 //edu
    int ELEMENT_39 = 305; //-50 //edu
    int ELEMENT_40 = 306; //-51 //edu
    int ELEMENT_41 = 307; //-52 //edu
    int ELEMENT_42 = 308; //-53 //edu
    int ELEMENT_43 = 309; //-54 //edu
    int ELEMENT_44 = 310; //-55 //edu
    int ELEMENT_45 = 311; //-56 //edu
    int ELEMENT_46 = 312; //-57 //edu
    int ELEMENT_47 = 313; //-58 //edu
    int ELEMENT_48 = 314; //-59 //edu
    int ELEMENT_49 = 315; //-60 //edu
    int ELEMENT_50 = 316; //-61 //edu
    int ELEMENT_51 = 317; //-62 //edu
    int ELEMENT_52 = 318; //-63 //edu
    int ELEMENT_53 = 319; //-64 //edu
    int ELEMENT_54 = 320; //-65 //edu
    int ELEMENT_55 = 321; //-66 //edu
    int ELEMENT_56 = 322; //-67 //edu
    int ELEMENT_57 = 323; //-68 //edu
    int ELEMENT_58 = 324; //-69 //edu
    int ELEMENT_59 = 325; //-70 //edu
    int ELEMENT_60 = 326; //-71 //edu
    int ELEMENT_61 = 327; //-72 //edu
    int ELEMENT_62 = 328; //-73 //edu
    int ELEMENT_63 = 329; //-74 //edu
    int ELEMENT_64 = 330; //-75 //edu
    int ELEMENT_65 = 331; //-76 //edu
    int ELEMENT_66 = 332; //-77 //edu
    int ELEMENT_67 = 333; //-78 //edu
    int ELEMENT_68 = 334; //-79 //edu
    int ELEMENT_69 = 335; //-80 //edu
    int ELEMENT_70 = 336; //-81 //edu
    int ELEMENT_71 = 337; //-82 //edu
    int ELEMENT_72 = 338; //-83 //edu
    int ELEMENT_73 = 339; //-84 //edu
    int ELEMENT_74 = 340; //-85 //edu
    int ELEMENT_75 = 341; //-86 //edu
    int ELEMENT_76 = 342; //-87 //edu
    int ELEMENT_77 = 343; //-88 //edu
    int ELEMENT_78 = 344; //-89 //edu
    int ELEMENT_79 = 345; //-90 //edu
    int ELEMENT_80 = 346; //-91 //edu
    int ELEMENT_81 = 347; //-92 //edu
    int ELEMENT_82 = 348; //-93 //edu
    int ELEMENT_83 = 349; //-94 //edu
    int ELEMENT_84 = 350; //-95 //edu
    int ELEMENT_85 = 351; //-96 //edu
    int ELEMENT_86 = 352; //-97 //edu
    int ELEMENT_87 = 353; //-98 //edu
    int ELEMENT_88 = 354; //-99 //edu
    int ELEMENT_89 = 355; //-100 //edu
    int ELEMENT_90 = 356; //-101 //edu
    int ELEMENT_91 = 357; //-102 //edu
    int ELEMENT_92 = 358; //-103 //edu
    int ELEMENT_93 = 359; //-104 //edu
    int ELEMENT_94 = 360; //-105 //edu
    int ELEMENT_95 = 361; //-106 //edu
    int ELEMENT_96 = 362; //-107 //edu
    int ELEMENT_97 = 363; //-108 //edu
    int ELEMENT_98 = 364; //-109 //edu
    int ELEMENT_99 = 365; //-110 //edu
    int ELEMENT_100 = 366; //-111 //edu
    int ELEMENT_101 = 367; //-112 //edu
    int ELEMENT_102 = 368; //-113 //edu
    int ELEMENT_103 = 369; //-114 //edu
    int ELEMENT_104 = 370; //-115 //edu
    int ELEMENT_105 = 371; //-116 //edu
    int ELEMENT_106 = 372; //-117 //edu
    int ELEMENT_107 = 373; //-118 //edu
    int ELEMENT_108 = 374; //-119 //edu
    int ELEMENT_109 = 375; //-120 //edu
    int ELEMENT_110 = 376; //-121 //edu
    int ELEMENT_111 = 377; //-122 //edu
    int ELEMENT_112 = 378; //-123 //edu
    int ELEMENT_113 = 379; //-124 //edu
    int ELEMENT_114 = 380; //-125 //edu
    int ELEMENT_115 = 381; //-126 //edu
    int ELEMENT_116 = 382; //-127 //edu
    int ELEMENT_117 = 383; //-128 //edu
    int ELEMENT_118 = 384; //-129 //edu
    int SEAGRASS = 385; //-130
    int CORAL = 386; //-131
    int CORAL_BLOCK = 387; //-132
    int CORAL_FAN = 388; //-133
    int CORAL_FAN_DEAD = 389; //-134
    int CORAL_FAN_HANG = 390; //-135
    int CORAL_FAN_HANG2 = 391; //-136
    int CORAL_FAN_HANG3 = 392; //-137
    int KELP = 393; //-138
    int DRIED_KELP_BLOCK = 394; //-139
    int ACACIA_BUTTON = 395; //-140
    int BIRCH_BUTTON = 396; //-141
    int DARK_OAK_BUTTON = 397; //-142
    int JUNGLE_BUTTON = 398; //-143
    int SPRUCE_BUTTON = 399; //-144
    int ACACIA_TRAPDOOR = 400; //-145
    int BIRCH_TRAPDOOR = 401; //-146
    int DARK_OAK_TRAPDOOR = 402; //-147
    int JUNGLE_TRAPDOOR = 403; //-148
    int SPRUCE_TRAPDOOR = 404; //-149
    int ACACIA_PRESSURE_PLATE = 405; //-150
    int BIRCH_PRESSURE_PLATE = 406; //-151
    int DARK_OAK_PRESSURE_PLATE = 407; //-152
    int JUNGLE_PRESSURE_PLATE = 408; //-153
    int SPRUCE_PRESSURE_PLATE = 409; //-154
    int CARVED_PUMPKIN = 410; //-155
    int SEA_PICKLE = 411; //-156
    int CONDUIT = 412; //-157

    int TURTLE_EGG = 414; //-159
    int BUBBLE_COLUMN = 415; //-160
    int BARRIER = 416; //-161

    int BAMBOO = 418; //-163
    int BAMBOO_SAPLING = 419; //-164
    int SCAFFOLDING = 420; //-165
}
