package cn.nukkit.network.protocol;

import cn.nukkit.resourcepacks.ResourcePack;
import lombok.ToString;

@ToString
public class ResourcePacksInfoPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;

    public boolean mustAccept = false; //if true, forces client to use selected resource packs
    public boolean hasScripts = false; //if true, causes disconnect for any platform that doesn't support scripts yet
    public ResourcePack[] behaviourPackEntries = new ResourcePack[0];
    public ResourcePack[] resourcePackEntries = new ResourcePack[0];

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.mustAccept);
        this.encodePacks(this.resourcePackEntries);
        this.encodePacks(this.behaviourPackEntries);
        this.putBoolean(this.hasScripts);
    }

    private void encodePacks(ResourcePack[] packs) {
        this.putLShort(packs.length);
        for (ResourcePack entry : packs) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            this.putLLong(entry.getPackSize());
            this.putString(""); //TODO: encryption key
            this.putString(""); //TODO: subpack name
            this.putString(""); //TODO: content identity
            this.putBoolean(false); //TODO: has scripts (seems useless for resource packs)
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
