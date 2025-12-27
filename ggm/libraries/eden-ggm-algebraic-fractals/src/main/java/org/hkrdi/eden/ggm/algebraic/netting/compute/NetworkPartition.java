package org.hkrdi.eden.ggm.algebraic.netting.compute;

import org.springframework.data.geo.Point;

public interface NetworkPartition  {

    Point getPartitionCenter();

    Point getPartitionTopLeft();


}
