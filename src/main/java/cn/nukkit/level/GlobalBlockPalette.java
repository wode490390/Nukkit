package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import com.google.common.io.ByteStreams;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalBlockPalette implements GlobalBlockPaletteInterface {

    private static GlobalBlockPaletteInterface instance = new GlobalBlockPalette();

    public static void setInstance(GlobalBlockPaletteInterface instance) {
        GlobalBlockPalette.instance = instance;
    }

    private final Int2IntMap legacyToRuntimeId = new Int2IntOpenHashMap();
    private final Int2IntMap runtimeIdToLegacy = new Int2IntOpenHashMap();
    private final AtomicInteger runtimeIdAllocator = new AtomicInteger(0);
    public final byte[] BLOCK_PALETTE;

    public GlobalBlockPalette() {
        legacyToRuntimeId.defaultReturnValue(-1);
        runtimeIdToLegacy.defaultReturnValue(-1);

        ListTag<CompoundTag> tag;
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("runtime_block_states.dat")) {
            if (stream == null) {
                throw new AssertionError("Unable to locate block state nbt");
            }
            //noinspection unchecked
            tag = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream)), ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette", e);
        }

        for (CompoundTag state : tag.getAll()) {
            int runtimeId = runtimeIdAllocator.getAndIncrement();
            if (!state.contains("LegacyStates")) continue;

            List<CompoundTag> legacyStates = state.getList("LegacyStates", CompoundTag.class).getAll();

            // Resolve to first legacy id
            CompoundTag firstState = legacyStates.get(0);
            runtimeIdToLegacy.put(runtimeId, firstState.getInt("id") << 6 | firstState.getShort("val"));

            for (CompoundTag legacyState : legacyStates) {
                int legacyId = legacyState.getInt("id") << 6 | legacyState.getShort("val");
                legacyToRuntimeId.put(legacyId, runtimeId);
            }
            state.remove("meta"); // No point in sending this since the client doesn't use it.
        }

        try {
            BLOCK_PALETTE = NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new AssertionError("Unable to write block palette", e);
        }
    }

    @Override
    public int getOrCreateRuntimeId0(int protocol, int id, int meta) {
        int legacyId = id << 6 | meta;
        int runtimeId = legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            runtimeId = legacyToRuntimeId.get(id << 6);
            if (runtimeId == -1) {
                throw new NoSuchElementException("Unmapped block registered id:" + id + " meta:" + meta);
            }
        }
        return runtimeId;
    }

    @Override
    public int getOrCreateRuntimeId0(int protocol, int legacyId) throws NoSuchElementException {
        return getOrCreateRuntimeId0(legacyId >> 4, legacyId & 0xf);
    }

    //外部直接调用这两个方法

    public static int getOrCreateRuntimeId(int protocol, int id, int meta) {
        return instance.getOrCreateRuntimeId0(protocol, id, meta);
    }

    public static int getOrCreateRuntimeId(int protocol, int legacyId) throws NoSuchElementException {
        return instance.getOrCreateRuntimeId0(protocol, legacyId);
    }

}
