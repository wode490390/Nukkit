package cn.nukkit.network.protocol;

public class BookEditPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BOOK_EDIT_PACKET;

    public static final int TYPE_REPLACE_PAGE = 0;
    public static final int TYPE_ADD_PAGE = 1;
    public static final int TYPE_DELETE_PAGE = 2;
    public static final int TYPE_SWAP_PAGES = 3;
    public static final int TYPE_SIGN_BOOK = 4;

    public int type;
    public int inventorySlot;
    public int pageNumber;
    public int secondaryPageNumber;

    public String text;
    public String photoName;

    public String title;
    public String author;
    public String xuid;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.type);
        this.putByte((byte) this.inventorySlot);

        switch (this.type) {
            case TYPE_REPLACE_PAGE:
            case TYPE_ADD_PAGE:
                this.putByte((byte) this.pageNumber);
                this.putString(this.text);
                this.putString(this.photoName);
                break;
            case TYPE_DELETE_PAGE:
                this.putByte((byte) this.pageNumber);
                break;
            case TYPE_SWAP_PAGES:
                this.putByte((byte) this.pageNumber);
                this.putByte((byte) this.secondaryPageNumber);
                break;
            case TYPE_SIGN_BOOK:
                this.putString(this.title);
                this.putString(this.author);
                this.putString(this.xuid);
                break;
            default:
                throw new RuntimeException("Unknown book edit type " + this.type + "!");
        }
    }
}
