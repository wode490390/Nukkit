package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

public class ItemBookWritten extends Item {

    protected boolean isWritten = true;

    public ItemBookWritten() {
        this(0);
    }

    public ItemBookWritten(Integer meta) {
        this(meta, 1);
    }

    public ItemBookWritten(Integer meta, int count) {
        super(WRITTEN_BOOK, meta, count, "Book");
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    public String getAuthor() {
        return this.getNamedTag().getString("author", "");
    }

    public String getTitle() {
        return this.getNamedTag().getString("title", "Book");
    }

    public String[] getPages() {
        ListTag<CompoundTag> tag = (ListTag<CompoundTag>) this.getNamedTag().getList("pages");
        if (tag == null) {
            return new String[0];
        }
        String[] pages = new String[tag.size()];
        int i = 0;
        for (CompoundTag pageCompound : tag.getAll()) {
            pages[i] = pageCompound.getString("text");
            i++;
        }
        return pages;
    }
}
