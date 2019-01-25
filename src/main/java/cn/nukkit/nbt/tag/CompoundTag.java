package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class CompoundTag extends Tag implements Cloneable {

    private final Map<String, Tag> tags = new HashMap<>();

    public CompoundTag() {
        this("");
    }

    public CompoundTag(String name) {
        super(name);
    }

    @Override
    public void write(NBTOutputStream dos) throws IOException {
        for (Tag tag : this.tags.values()) {
            Tag.writeNamedTag(tag, dos);
        }
        dos.writeByte(Tag.TAG_End);
    }

    @Override
    public void load(NBTInputStream dis) throws IOException {
        this.tags.clear();
        Tag tag;
        while ((tag = Tag.readNamedTag(dis)).getId() != Tag.TAG_End) {
            this.tags.put(tag.getName(), tag);
        }
    }

    public Collection<Tag> getAllTags() {
        return this.tags.values();
    }

    @Override
    public byte getId() {
        return TAG_Compound;
    }

    public CompoundTag put(String name, Tag tag) {
        this.tags.put(name, tag.setName(name));
        return this;
    }

    public CompoundTag putByte(String name, int value) {
        this.tags.put(name, new ByteTag(name, value));
        return this;
    }

    public CompoundTag putShort(String name, int value) {
        this.tags.put(name, new ShortTag(name, value));
        return this;
    }

    public CompoundTag putInt(String name, int value) {
        this.tags.put(name, new IntTag(name, value));
        return this;
    }

    public CompoundTag putLong(String name, long value) {
        this.tags.put(name, new LongTag(name, value));
        return this;
    }

    public CompoundTag putFloat(String name, float value) {
        this.tags.put(name, new FloatTag(name, value));
        return this;
    }

    public CompoundTag putDouble(String name, double value) {
        this.tags.put(name, new DoubleTag(name, value));
        return this;
    }

    public CompoundTag putString(String name, String value) {
        this.tags.put(name, new StringTag(name, value));
        return this;
    }

    public CompoundTag putByteArray(String name, byte[] value) {
        this.tags.put(name, new ByteArrayTag(name, value));
        return this;
    }

    public CompoundTag putIntArray(String name, int[] value) {
        this.tags.put(name, new IntArrayTag(name, value));
        return this;
    }

    public CompoundTag putList(ListTag<? extends Tag> listTag) {
        this.tags.put(listTag.getName(), listTag);
        return this;
    }

    public CompoundTag putCompound(String name, CompoundTag value) {
        this.tags.put(name, value.setName(name));
        return this;
    }

    public CompoundTag putBoolean(String string, boolean val) {
        putByte(string, val ? 1 : 0);
        return this;
    }

    public Tag get(String name) {
        return this.tags.get(name);
    }

    public boolean contains(String name) {
        return this.tags.containsKey(name);
    }

    public CompoundTag remove(String... names) {
        for (String name : names) {
            this.tags.remove(name);
        }
        return this;
    }

    public <T extends Tag> T removeAndGet(String name) {
        return (T) this.tags.remove(name);
    }

    public int getByte(String name) {
        return this.getByte(name, 0);
    }

    public int getByte(String name, int defaultValue) {
        return this.getByte(name, defaultValue, false);
    }

    public int getByte(String name, int defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return (byte) defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof ByteTag) && badTagDefault) {
            return (byte) defaultValue;
        }
        return (byte) ((NumberTag) tag).getData().intValue();
    }

    public int getShort(String name) {
        return this.getShort(name, 0);
    }

    public int getShort(String name, int defaultValue) {
        return this.getShort(name, defaultValue, false);
    }

    public int getShort(String name, int defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return (short) defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof ShortTag) && badTagDefault) {
            return (short) defaultValue;
        }
        return (short) ((NumberTag) tag).getData().intValue();
    }

    public int getInt(String name) {
        return this.getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        return this.getInt(name, defaultValue, false);
    }

    public int getInt(String name, int defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof IntTag) && badTagDefault) {
            return defaultValue;
        }
        return ((NumberTag) tag).getData().intValue();
    }

    public long getLong(String name) {
        return this.getLong(name, 0);
    }

    public long getLong(String name, long defaultValue) {
        return this.getLong(name, defaultValue, false);
    }

    public long getLong(String name, long defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof LongTag) && badTagDefault) {
            return defaultValue;
        }
        return ((NumberTag) tag).getData().longValue();
    }

    public float getFloat(String name) {
        return this.getFloat(name, 0);
    }

    public float getFloat(String name, xxxx defaultValue) {
        return this.getFloat(name, defaultValue, false);
    }

    public float getFloat(String name, float defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof FloatTag) && badTagDefault) {
            return defaultValue;
        }
        return ((NumberTag) tag).getData().floatValue();
    }

    public double getDouble(String name) {
        return this.getDouble(name, 0);
    }

    public double getDouble(String name, double defaultValue) {
        return this.getDouble(name, defaultValue, false);
    }

    public double getDouble(String name, double defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof DoubleTag) && badTagDefault) {
            return defaultValue;
        }
        return ((NumberTag) tag).getData().doubleValue();
    }

    public String getString(String name) {
        return this.getString(name, "");
    }

    public String getString(String name, String defaultValue) {
        return this.getString(name, defaultValue, false);
    }

    public String getString(String name, String defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (tag instanceof NumberTag) {
            return String.valueOf(((NumberTag) tag).getData());
        } else if (!(tag instanceof StringTag) && badTagDefault) {
            return defaultValue;
        }
        return ((StringTag) tag).data;
    }

    public byte[] getByteArray(String name) {
        return this.getByteArray(name, new byte[0]);
    }

    public byte[] getByteArray(String name, byte[] defaultValue) {
        return this.getByteArray(name, defaultValue, false);
    }

    public byte[] getByteArray(String name, byte[] defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof ByteArrayTag) && badTagDefault) {
            return defaultValue;
        }
        return ((ByteArrayTag) tag).data;
    }

    public int[] getIntArray(String name) {
        return this.getIntArray(name, new int[0]);
    }

    public int[] getIntArray(String name, int[] defaultValue) {
        return this.getIntArray(name, defaultValue, false);
    }

    public int[] getIntArray(String name, int[] defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof IntArrayTag) && badTagDefault) {
            return defaultValue;
        }
        return ((IntArrayTag) tag).data;
    }

    public CompoundTag getCompound(String name) {
        return this.getCompound(name, new CompoundTag(name));
    }

    public CompoundTag getCompound(String name, CompoundTag defaultValue) {
        return this.getCompound(name, defaultValue, false);
    }

    public CompoundTag getCompound(String name, CompoundTag defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        Tag tag = this.tags.get(name);
        if (!(tag instanceof CompoundTag) && badTagDefault) {
            return defaultValue;
        }
        return (CompoundTag) tag;
    }

    @SuppressWarnings("unchecked")
    public ListTag<? extends Tag> getList(String name) {
        if (!this.tags.containsKey(name)) {
            return new ListTag<>(name);
        }
        return (ListTag<? extends Tag>) this.tags.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag> ListTag<T> getList(String name, Class<T> type) {
        if (!this.tags.containsKey(name)) {
            return new ListTag<>(name);
        }
        return (ListTag<T>) this.tags.get(name);
    }

    public Map<String, Tag> getTags() {
        return new HashMap<>(this.tags);
    }

    public boolean getBoolean(String name) {
        return this.getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return this.getBoolean(name, defaultValue, false);
    }

    public boolean getBoolean(String name, boolean defaultValue, boolean badTagDefault) {
        if (!this.tags.containsKey(name)) {
            return defaultValue;
        }
        return this.getByte(name, defaultValue ? 1 : 0, badTagDefault) != 0;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n\t");
        this.tags.forEach((key, tag) -> joiner.add(key + " : " + tag.toString()));
        return "CompoundTag '" + this.getName() + "' (" + this.tags.size() + " entries) {\n\t" + joiner.toString() + "\n}";
    }

    public void print(String prefix, PrintStream out) {
        super.print(prefix, out);
        out.println(prefix + "{");
        String orgPrefix = prefix;
        prefix += "   ";
        for (Tag tag : this.tags.values()) {
            tag.print(prefix, out);
        }
        out.println(orgPrefix + "}");
    }

    public boolean isEmpty() {
        return this.tags.isEmpty();
    }

    public CompoundTag copy() {
        CompoundTag tag = new CompoundTag(getName());
        for (String key : this.tags.keySet()) {
            tag.put(key, this.tags.get(key).copy());
        }
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            CompoundTag o = (CompoundTag) obj;
            return this.tags.entrySet().equals(o.tags.entrySet());
        }
        return false;
    }

    /**
     * Check existence of NBT tag
     *
     * @param name - NBT tag Id.
     * @return - true, if tag exists
     */
    public boolean exist(String name) {
        return this.tags.containsKey(name);
    }

    public int size() {
        return this.tags.size();
    }

    @Override
    public CompoundTag clone() {
        CompoundTag nbt = new CompoundTag();
        this.getTags().forEach((key, value) -> nbt.put(key, value.copy()));
        return nbt;
    }
}
