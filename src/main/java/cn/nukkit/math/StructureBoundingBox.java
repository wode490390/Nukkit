package cn.nukkit.math;

public class StructureBoundingBox {

    private BlockVector3 min;
    private BlockVector3 max;

    public StructureBoundingBox(BlockVector3 min, BlockVector3 max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Checks whether the given point is inside a block that intersects this box.
     *
     * @param vec the point to check
     * @return true if this box intersects the block containing {@code vec}
     */
    public boolean isVectorInside(BlockVector3 vec) {
        return vec.getX() >= this.min.getX() && vec.getX() <= this.max.getX() && vec.getY() >= this.min.getY() && vec.getY() <= this.max.getY() && vec.getZ() >= this.min.getZ() && vec.getZ() <= this.max.getZ();
    }

    /**
     * Whether this box intersects the given box.
     *
     * @param boundingBox the box to check intersection with
     * @return true if the given box intersects this box; false otherwise
     */
    public boolean intersectsWith(StructureBoundingBox boundingBox) {
        return boundingBox.getMin().getX() <= this.max.getX() && boundingBox.getMax().getX() >= this.min.getX() && boundingBox.getMin().getY() <= this.max.getY() && boundingBox.getMax().getY() >= this.min.getY() && boundingBox.getMin().getZ() <= this.max.getZ() && boundingBox.getMax().getZ() >= this.min.getZ();
    }

    /**
     * Whether this box intersects the given vertically-infinite box.
     *
     * @param minX the minimum X coordinate
     * @param minZ the minimum Z coordinate
     * @param maxX the maximum X coordinate
     * @param maxZ the maximum Z coordinate
     * @return true if the given box intersects this box; false otherwise
     */
    public boolean intersectsWith(int minX, int minZ, int maxX, int maxZ) {
        return minX <= this.max.getX() && maxX >= this.min.getX() && minZ <= this.max.getZ() && maxZ >= this.min.getZ();
    }

    /**
     * Changes this bounding box to the bounding box of the union of itself and another bounding
     * box.
     *
     * @param boundingBox the other bounding box to contain
     */
    public void expandTo(StructureBoundingBox boundingBox) {
        this.min = new BlockVector3(Math.min(this.min.getX(), boundingBox.getMin().getX()), Math.min(this.min.getY(), boundingBox.getMin().getY()), Math.min(this.min.getZ(), boundingBox.getMin().getZ()));
        this.max = new BlockVector3(Math.max(this.max.getX(), boundingBox.getMax().getX()), Math.max(this.max.getY(), boundingBox.getMax().getY()), Math.max(this.max.getZ(), boundingBox.getMax().getZ()));
    }

    public void offset(BlockVector3 offset) {
        this.min = this.min.add(offset);
        this.max = this.max.add(offset);
    }

    public BlockVector3 getMin() {
        return this.min;
    }

    public BlockVector3 getMax() {
        return this.max;
    }
}
