package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

public class BlockEntityBanner extends BlockEntitySpawnable {

    public static final String PATTERN_BOTTOM_STRIPE = "bs";
    public static final String PATTERN_TOP_STRIPE = "ts";
    public static final String PATTERN_LEFT_STRIPE = "ls";
    public static final String PATTERN_RIGHT_STRIPE = "rs";
    public static final String PATTERN_CENTER_STRIPE = "cs";
    public static final String PATTERN_MIDDLE_STRIPE = "ms";
    public static final String PATTERN_DOWN_RIGHT_STRIPE = "drs";
    public static final String PATTERN_DOWN_LEFT_STRIPE = "dls";
    public static final String PATTERN_SMALL_STRIPES = "ss";
    public static final String PATTERN_DIAGONAL_CROSS = "cr";
    public static final String PATTERN_SQUARE_CROSS = "sc";
    public static final String PATTERN_LEFT_OF_DIAGONAL = "ld";
    public static final String PATTERN_RIGHT_OF_UPSIDE_DOWN_DIAGONAL = "rud";
    public static final String PATTERN_LEFT_OF_UPSIDE_DOWN_DIAGONAL = "lud";
    public static final String PATTERN_RIGHT_OF_DIAGONAL = "rd";
    public static final String PATTERN_VERTICAL_HALF_LEFT = "vh";
    public static final String PATTERN_VERTICAL_HALF_RIGHT = "vhr";
    public static final String PATTERN_HORIZONTAL_HALF_TOP = "hh";
    public static final String PATTERN_HORIZONTAL_HALF_BOTTOM = "hhb";
    public static final String PATTERN_BOTTOM_LEFT_CORNER = "bl";
    public static final String PATTERN_BOTTOM_RIGHT_CORNER = "br";
    public static final String PATTERN_TOP_LEFT_CORNER = "tl";
    public static final String PATTERN_TOP_RIGHT_CORNER = "tr";
    public static final String PATTERN_BOTTOM_TRIANGLE = "bt";
    public static final String PATTERN_TOP_TRIANGLE = "tt";
    public static final String PATTERN_BOTTOM_TRIANGLE_SAWTOOTH = "bts";
    public static final String PATTERN_TOP_TRIANGLE_SAWTOOTH = "tts";
    public static final String PATTERN_MIDDLE_CIRCLE = "mc";
    public static final String PATTERN_MIDDLE_RHOMBUS = "mr";
    public static final String PATTERN_BORDER = "bo";
    public static final String PATTERN_CURLY_BORDER = "cbo";
    public static final String PATTERN_BRICK = "bri";
    public static final String PATTERN_GRADIENT = "gra";
    public static final String PATTERN_GRADIENT_UPSIDE_DOWN = "gru";
    public static final String PATTERN_CREEPER = "cre";
    public static final String PATTERN_SKULL = "sku";
    public static final String PATTERN_FLOWER = "flo";
    public static final String PATTERN_MOJANG = "moj";

    public static final int COLOR_BLACK = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_BROWN = 3;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_PURPLE = 5;
    public static final int COLOR_CYAN = 6;
    public static final int COLOR_LIGHT_GRAY = 7;
    public static final int COLOR_GRAY = 8;
    public static final int COLOR_PINK = 9;
    public static final int COLOR_LIME = 10;
    public static final int COLOR_YELLOW = 11;
    public static final int COLOR_LIGHT_BLUE = 12;
    public static final int COLOR_MAGENTA = 13;
    public static final int COLOR_ORANGE = 14;
    public static final int COLOR_WHITE = 15;

    public BlockEntityBanner(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        int id = this.getBlock().getId();
        return id == Block.STANDING_BANNER || id == Block.WALL_BANNER;
    }

    @Override
    public String getName() {
        return "Banner";
    }

    public void addAdditionalSpawnData(CompoundTag nbt) {
        nbt.putList(this.namedTag.getList("Patterns"));
        nbt.putInt("Base", this.namedTag.getInt("Base"));
    }

    /**
     * Returns the color of the banner base.
     *
     * @return int
     */
    public int getBaseColor() {
        return this.namedTag.getInt("Base");
    }

    /**
     * Sets the color of the banner base.
     * @param color
     */
    public void setBaseColor(int color) {
        this.namedTag.putInt("Base", color & 0x0f);
        this.onChanged();
    }

    public int addPattern(Pattern pattern) {
        return addPattern(pattern.pattern, pattern.color);
    }

    /**
     * Applies a new pattern on the banner with the given color.
     *
     * @param pattern
     * @param color
     * 
     * @return int ID of pattern.
     */
    public int addPattern(String pattern, int color) {
        int patternId = this.namedTag.getList("Patterns").size();

        this.namedTag.getList("Patterns", CompoundTag.class).add(patternId, new CompoundTag()
                .putInt("Color", color & 0x0f)
                .putString("Pattern", pattern)
        );

        this.onChanged();
        return patternId;
    }

    /**
     * Returns whether a pattern with the given ID exists on the banner or not.
     * 
     * @param patternId
     * 
     * @return 
     */
    public boolean patternExists(int patternId) {
        ListTag<?> list = this.namedTag.getList("Patterns");

        return list.size() > patternId && list.get(patternId) != null;
    }

    /**
     * Returns the data of a pattern with the given ID.
     * 
     * @param patternId
     * 
     * @return 
     */
    public Pattern getPatternData(int patternId) {
        if (!this.patternExists(patternId)) {
            return null;
        }

        CompoundTag data = this.namedTag.getList("Patterns", CompoundTag.class).get(patternId);


        return new Pattern(data.getInt("Color"), data.getString("Pattern"));
    }

    /**
     * Changes the pattern of a previously existing pattern.
     *
     * @param patternId
     * @param pattern
     * @param color
     * 
     * @return bool indicating success.
     */
    public boolean changePattern(int patternId, String pattern, int color) {
        if (!this.patternExists(patternId)) {
            return false;
        }
        this.namedTag.getList("Patterns", CompoundTag.class).get(patternId)
                .putInt("Color", color & 0x0F)
                .putString("Pattern", pattern);

        this.onChanged();
        return true;
    }

    /**
     * Deletes a pattern from the banner with the given ID.
     *
     * @param patternId
     * 
     * @return bool indicating whether the pattern existed or not.
     */
    public boolean deletePattern(int patternId) {
        if (!this.patternExists(patternId)) {
            return false;
        }
        this.namedTag.getList("Patterns").remove(patternId);

        this.onChanged();
        return true;
    }

    /**
     * Deletes the top most pattern of the banner.
     *
     * @return bool indicating whether the banner was empty or not.
     */
    public boolean deleteTopPattern() {
        ListTag<?> list = this.namedTag.getList("Patterns");

        if (list.size() <= 0) {
            return false;
        }

        list.remove(list.size() - 1);

        this.onChanged();
        return true;
    }

    /**
     * Deletes the bottom pattern of the banner.
     *
     * @return bool indicating whether the banner was empty or not.
     */
    public boolean deleteBottomPattern() {
        ListTag<?> list = this.namedTag.getList("Patterns");

        if (list.size() <= 0) {
            return false;
        }

        list.remove(0);
        this.onChanged();
        return true;
    }

    /**
     * Returns the total count of patterns on this banner.
     *
     * @return int
     */
    public int getPatternCount() {
        return this.namedTag.getList("Patterns").size();
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag()
                .putString("id", BlockEntity.BANNER)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z)
                .putInt("Base", this.getBaseColor());
    }

    public static class Pattern {

        public int color;
        public String pattern;

        public Pattern(int color, String pattern) {
            this.color = color;
            this.pattern = pattern;
        }
    }
}
