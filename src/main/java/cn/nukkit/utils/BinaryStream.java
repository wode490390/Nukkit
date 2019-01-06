package cn.nukkit.utils;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BinaryStream {

    public int offset;
    public byte[] buffer;
    private int count;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public BinaryStream() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public BinaryStream(byte[] buffer) {
        this(buffer, 0);
    }

    public BinaryStream(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
        this.count = buffer.length;
    }

    public BinaryStream reset() {
        this.offset = 0;
        this.count = 0;
        return this;
    }

    public final void superReset() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.count = buffer == null ? -1 : buffer.length;
    }

    public void setBuffer(byte[] buffer, int offset) {
        this.setBuffer(buffer);
        this.setOffset(offset);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public byte[] getBuffer() {
        return Arrays.copyOf(buffer, count);
    }

    public int getCount() {
        return count;
    }

    public byte[] get() {
        return this.get(this.count - this.offset);
    }

    public byte[] get(int len) {
        if (len < 0) {
            this.offset = this.count - 1;
            return new byte[0];
        }
        len = Math.min(len, this.getCount() - this.offset);
        this.offset += len;
        return Arrays.copyOfRange(this.buffer, this.offset - len, this.offset);
    }

    public void put(byte[] bytes) {
        if (bytes == null) {
            return;
        }

        this.ensureCapacity(this.count + bytes.length);

        System.arraycopy(bytes, 0, this.buffer, this.count, bytes.length);
        this.count += bytes.length;
    }

    public long getLong() {
        return Binary.readLong(this.get(8));
    }

    public void putLong(long l) {
        this.put(Binary.writeLong(l));
    }

    public int getInt() {
        return Binary.readInt(this.get(4));
    }

    public void putInt(int i) {
        this.put(Binary.writeInt(i));
    }

    public long getLLong() {
        return Binary.readLLong(this.get(8));
    }

    public void putLLong(long l) {
        this.put(Binary.writeLLong(l));
    }

    public int getLInt() {
        return Binary.readLInt(this.get(4));
    }

    public void putLInt(int i) {
        this.put(Binary.writeLInt(i));
    }

    public int getShort() {
        return Binary.readShort(this.get(2));
    }

    public void putShort(int s) {
        this.put(Binary.writeShort(s));
    }

    public int getLShort() {
        return Binary.readLShort(this.get(2));
    }

    public void putLShort(int s) {
        this.put(Binary.writeLShort(s));
    }

    public float getFloat() {
        return getFloat(-1);
    }

    public float getFloat(int accuracy) {
        return Binary.readFloat(this.get(4), accuracy);
    }

    public void putFloat(float v) {
        this.put(Binary.writeFloat(v));
    }

    public float getLFloat() {
        return getLFloat(-1);
    }

    public float getLFloat(int accuracy) {
        return Binary.readLFloat(this.get(4), accuracy);
    }

    public void putLFloat(float v) {
        this.put(Binary.writeLFloat(v));
    }

    public int getTriad() {
        return Binary.readTriad(this.get(3));
    }

    public void putTriad(int triad) {
        this.put(Binary.writeTriad(triad));
    }

    public int getLTriad() {
        return Binary.readLTriad(this.get(3));
    }

    public void putLTriad(int triad) {
        this.put(Binary.writeLTriad(triad));
    }

    public boolean getBoolean() {
        return this.getByte() != 0x00;
    }

    public void putBoolean(boolean bool) {
        this.putByte((byte) (bool ? 1 : 0));
    }

    public int getByte() {
        return this.get(1)[0] & 0xff;
    }

    public void putByte(byte b) {
        this.put(new byte[]{b});
    }

    /**
     * Reads a list of Attributes from the stream.
     *
     * @return Attribute[]
     */
    public Attribute[] getAttributeList() throws Exception {
        List<Attribute> list = new ArrayList<>();
        long count = this.getUnsignedVarInt();

        for (int i = 0; i < count; ++i) {
            float min = this.getLFloat();
            float max = this.getLFloat();
            float current = this.getLFloat();
            float defaultValue = this.getLFloat();
            String id = this.getString();

            Attribute attr = Attribute.getAttribute(id);
            if (attr != null) {
                attr.setMinValue(min);
                attr.setMaxValue(max);
                attr.setValue(current);
                attr.setDefaultValue(defaultValue);

                list.add(attr);
            } else {
                throw new Exception("Unknown attribute type \"" + id + "\"");
            }
        }

        return list.stream().toArray(Attribute[]::new);
    }

    /**
     * Writes a list of Attributes to the packet buffer using the standard format.
     */
    public void putAttributeList(Attribute[] attributes) {
        this.putUnsignedVarInt(attributes.length);
        for (Attribute attribute : attributes) {
            this.putLFloat(attribute.getMinValue());
            this.putLFloat(attribute.getMaxValue());
            this.putLFloat(attribute.getValue());
            this.putLFloat(attribute.getDefaultValue());
            this.putString(attribute.getId());
        }
    }

    public void putUUID(UUID uuid) {
        this.put(Binary.writeUUID(uuid));
    }

    public UUID getUUID() {
        return Binary.readUUID(this.get(16));
    }

    public void putSkin(Skin skin) {
        this.putString(skin.getSkinId());
        this.putByteArray(skin.getSkinData());
        this.putByteArray(skin.getCapeData());
        this.putString(skin.getGeometryName());
        this.putString(skin.getGeometryData());
    }

    public Skin getSkin() {
        Skin skin = new Skin();
        skin.setSkinId(this.getString());
        skin.setSkinData(this.getByteArray());
        skin.setCapeData(this.getByteArray());
        skin.setGeometryName(this.getString());
        skin.setGeometryData(this.getString());
        return skin;
    }

    public Item getSlot() {
        int id = this.getVarInt();

        if (id <= 0) {
            return Item.get(0, 0, 0);
        }
        int auxValue = this.getVarInt();
        int data = auxValue >> 8;
        if (data == Short.MAX_VALUE) {
            data = -1;
        }
        int cnt = auxValue & 0xff;

        int nbtLen = this.getLShort();
        byte[] nbt = new byte[0];
        if (nbtLen > 0) {
            nbt = this.get(nbtLen);
        }

        //TODO
        int canPlaceOn = this.getVarInt();
        if (canPlaceOn > 0) {
            for (int i = 0; i < canPlaceOn; ++i) {
                this.getString();
            }
        }

        //TODO
        int canDestroy = this.getVarInt();
        if (canDestroy > 0) {
            for (int i = 0; i < canDestroy; ++i) {
                this.getString();
            }
        }

        return Item.get(
                id, data, cnt, nbt
        );
    }

    public void putSlot(Item item) {
        if (item == null || item.getId() == 0) {
            this.putVarInt(0);
            return;
        }

        this.putVarInt(item.getId());
        int auxValue = (((item.hasMeta() ? item.getDamage() : -1) & 0x7fff) << 8) | item.getCount();
        this.putVarInt(auxValue);
        byte[] nbt = item.getCompoundTag();
        this.putLShort(nbt.length);
        this.put(nbt);
        this.putVarInt(0); //TODO CanPlaceOn entry count
        this.putVarInt(0); //TODO CanDestroy entry count
    }

    public byte[] getByteArray() {
        return this.get((int) this.getUnsignedVarInt());
    }

    public void putByteArray(byte[] b) {
        this.putUnsignedVarInt(b.length);
        this.put(b);
    }

    public String getString() {
        return new String(this.getByteArray(), StandardCharsets.UTF_8);
    }

    public void putString(String string) {
        byte[] b = string.getBytes(StandardCharsets.UTF_8);
        this.putByteArray(b);
    }

    public long getUnsignedVarInt() {
        return VarInt.readUnsignedVarInt(this);
    }

    public void putUnsignedVarInt(long v) {
        VarInt.writeUnsignedVarInt(this, v);
    }

    public int getVarInt() {
        return VarInt.readVarInt(this);
    }

    public void putVarInt(int v) {
        VarInt.writeVarInt(this, v);
    }

    public long getVarLong() {
        return VarInt.readVarLong(this);
    }

    public void putVarLong(long v) {
        VarInt.writeVarLong(this, v);
    }

    public long getUnsignedVarLong() {
        return VarInt.readUnsignedVarLong(this);
    }

    public void putUnsignedVarLong(long v) {
        VarInt.writeUnsignedVarLong(this, v);
    }

    public BlockVector3 getBlockPosition() {
        return new BlockVector3(this.getVarInt(), (int) this.getUnsignedVarInt(), this.getVarInt());
    }

    public BlockVector3 getSignedBlockPosition() {
        return new BlockVector3(getVarInt(), getVarInt(), getVarInt());
    }

    public void putSignedBlockPosition(BlockVector3 v) {
        this.putVarInt(v.x);
        this.putVarInt(v.y);
        this.putVarInt(v.z);
    }

    public void putBlockVector3(BlockVector3 v) {
        this.putBlockPosition(v.x, v.y, v.z);
    }

    public void putBlockPosition(int x, int y, int z) {
        this.putVarInt(x);
        this.putUnsignedVarInt(y);
        this.putVarInt(z);
    }

    public Vector3f getVector3() {
        return new Vector3f(this.getLFloat(4), this.getLFloat(4), this.getLFloat(4));
    }

    public void putVector3(Vector3f v) {
        this.putVector3(v.x, v.y, v.z);
    }

    public void putVector3(float x, float y, float z) {
        this.putLFloat(x);
        this.putLFloat(y);
        this.putLFloat(z);
    }

    public void putGameRules(GameRules gameRules) {
        Map<GameRule, GameRules.Value> rules = gameRules.getGameRules();
        this.putUnsignedVarInt(rules.size());
        rules.forEach((gameRule, value) -> {
            putString(gameRule.getName().toLowerCase());
            value.write(this);
        });
    }

    /**
     * Reads and returns an EntityUniqueID
     *
     * @return int
     */
    public long getEntityUniqueId() {
        return this.getVarLong();
    }

    /**
     * Writes an EntityUniqueID
     */
    public void putEntityUniqueId(long eid) {
        this.putVarLong(eid);
    }

    /**
     * Reads and returns an EntityRuntimeID
     */
    public long getEntityRuntimeId() {
        return this.getUnsignedVarLong();
    }

    /**
     * Writes an EntityUniqueID
     */
    public void putEntityRuntimeId(long eid) {
        this.putUnsignedVarLong(eid);
    }

    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getVarInt());
    }

    public void putBlockFace(BlockFace face) {
        this.putVarInt(face.getIndex());
    }

    public boolean feof() {
        return this.offset < 0 || this.offset >= this.buffer.length;
    }

    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buffer.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buffer.length;
        int newCapacity = oldCapacity << 1;

        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        this.buffer = Arrays.copyOf(buffer, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public CommandOriginData getCommandOriginData() {
        CommandOriginData result = new CommandOriginData();

        result.type = (int) this.getUnsignedVarInt();
        result.uuid = this.getUUID();
        result.requestId = this.getString();

        if (result.type == CommandOriginData.ORIGIN_DEV_CONSOLE || result.type == CommandOriginData.ORIGIN_TEST) {
            result.varlong1 = this.getVarLong();
        }

        return result;
    }

    public void putCommandOriginData(CommandOriginData data) {
        this.putUnsignedVarInt(data.type);
        this.putUUID(data.uuid);
        this.putString(data.requestId);

        if (data.type == CommandOriginData.ORIGIN_DEV_CONSOLE || data.type == CommandOriginData.ORIGIN_TEST) {
            this.putVarLong(data.varlong1);
        }
    }

    public void putEntityLink(EntityLink link) {
        this.putEntityUniqueId(link.fromEntityUniqueId);
        this.putEntityUniqueId(link.toEntityUniqueId);
        this.putByte((byte) link.type);
        this.putBoolean(link.immediate);
    }

    public EntityLink getEntityLink() {
        long fromEntityUniqueId = this.getEntityUniqueId();
        long toEntityUniqueId = this.getEntityUniqueId();
        byte type = (byte) this.getByte();
        boolean immediate = this.getBoolean();
        return new EntityLink(fromEntityUniqueId, toEntityUniqueId, type, immediate);
    }

    public void putVector3Nullable() {
        this.putLFloat(0);
        this.putLFloat(0);
        this.putLFloat(0);
    }

    public void putVector3Nullable(Vector3f vector) {
        this.putVector3(vector);
    }

    public void putByteRotation(float rotation) {
        this.putByte((byte) (rotation / (360d / 256d)));
    }

    public float getByteRotation() {
        return (float) (this.getByte() * (360d / 256d));
    }
}
