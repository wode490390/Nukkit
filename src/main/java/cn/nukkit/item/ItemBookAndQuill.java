package cn.nukkit.item;

public class ItemBookAndQuill extends Item {

    public ItemBookAndQuill() {
        this(0, 1);
    }

    public ItemBookAndQuill(Integer meta) {
        this(meta, 1);
    }

    public ItemBookAndQuill(Integer meta, int count) {
        super(BOOK_AND_QUILL, meta, count, "Book and Quill");
    }
}
