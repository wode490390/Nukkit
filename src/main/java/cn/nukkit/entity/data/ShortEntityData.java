package cn.nukkit.entity.data;

import cn.nukkit.entity.Entity;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ShortEntityData extends EntityData<Integer> {

    public int data;

    public ShortEntityData(int id, int data) {
        super(id);
        this.data = data;
    }

    @Override
    public Integer getData() {
        return data;
    }

    @Override
    public void setData(Integer data) {
        if (data == null) {
            this.data = 0;
        } else {
            this.data = data;
        }
    }

    @Override
    public int getType() {
        return Entity.DATA_TYPE_SHORT;
    }
}
