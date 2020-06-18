package cn.nukkit.level;

import java.util.NoSuchElementException;

public interface GlobalBlockPaletteInterface {

    int getOrCreateRuntimeId0(int protocol, int id, int meta);

    int getOrCreateRuntimeId0(int protocol, int legacyId) throws NoSuchElementException;

}
