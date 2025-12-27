package org.hkrdi.eden.ggm.algebraic.netting.compute;

import org.springframework.data.geo.Point;

public final class GridPartition {

    public static RectangularPartition getRectangularPartition(double spaceDistance, int domain, int limit) {
        RectangularPartition rectangularPartition = new RectangularPartition();
        rectangularPartition.setPartitionCenter(getDomainCenter(spaceDistance, domain, limit));
        rectangularPartition.setPartitionTopLeft(getDomainTopLeft(spaceDistance, domain, limit));
        return rectangularPartition;
    }

    public static Point getDomainTopLeft(double spaceDistance, int domain, int limit) {
        int[] gridDomain = getGridDomain(domain, limit);
        int[] gridSlices = getGridSlices(limit);
        return new Point(- spaceDistance / 2 + spaceDistance / gridSlices[0] * (gridDomain[1] -1 ) ,
                - spaceDistance / 2 + spaceDistance / gridSlices[1] * (gridDomain[0] - 1));
    }

    public static Point getDomainCenter(double spaceDistance, int domain, int limit) {
        Point domainTopLeft = getDomainTopLeft(spaceDistance, domain, limit);
        int[] gridSlices = getGridSlices(limit);
        return new Point(domainTopLeft.getX() + spaceDistance / (2 * gridSlices[0]),
                domainTopLeft.getY() + spaceDistance / (2 * gridSlices[1]));
    }

    public static int[] getGridDomain(int domain, int limit) {
        int [] slices = getGridSlices(limit);
        int quotation = domain / slices[0];
        int remainder = domain % slices[0] ;
        return new int[] { remainder == 0 ? quotation : quotation + 1,
                remainder == 0 ? slices[0] : remainder };
    }

    public static int[] getGridSlices(int limit) {
        int verticallySlice = 1;
        for (int i = 2; i * i < limit; i ++ ) {
            if (limit % i == 0) {
                if ( i > verticallySlice) {
                    verticallySlice = i ;
                }
            }
        }
        return new int[] { verticallySlice, limit / verticallySlice };

    }

}
