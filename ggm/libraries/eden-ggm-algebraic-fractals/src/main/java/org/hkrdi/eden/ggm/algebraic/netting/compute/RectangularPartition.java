package org.hkrdi.eden.ggm.algebraic.netting.compute;

import org.springframework.data.geo.Point;

public class RectangularPartition implements NetworkPartition {

    private Point partitionCenter;

    private Point partitionTopLeft;

    public RectangularPartition() {
    }

    public RectangularPartition(Point partitionCenter, Point partitionTopLeft) {
        this.partitionCenter = partitionCenter;
        this.partitionTopLeft = partitionTopLeft;
    }

    @Override
    public Point getPartitionCenter() {
        return partitionCenter;
    }

    public void setPartitionCenter(Point partitionCenter) {
        this.partitionCenter = partitionCenter;
    }

    @Override
    public Point getPartitionTopLeft() {
        return partitionTopLeft;
    }

    public void setPartitionTopLeft(Point partitionTopLeft) {
        this.partitionTopLeft = partitionTopLeft;
    }
}
