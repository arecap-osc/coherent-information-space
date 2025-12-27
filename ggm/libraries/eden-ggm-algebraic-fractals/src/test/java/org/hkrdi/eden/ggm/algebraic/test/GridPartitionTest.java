package org.hkrdi.eden.ggm.algebraic.test;


import org.hkrdi.eden.ggm.algebraic.netting.compute.GridPartition;
import org.hkrdi.eden.ggm.algebraic.netting.compute.RectangularPartition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
public class GridPartitionTest {

    @Test
    public void testGridPartitionDomain() {
        int[] domain = GridPartition.getGridDomain(18, 30);
        Assert.isTrue(domain[0]== 4 && domain[1]==3, "Wrong computation");
        domain = GridPartition.getGridDomain(17, 30);
        Assert.isTrue(domain[0]== 4 && domain[1]==2, "Wrong computation");
        domain = GridPartition.getGridDomain(11, 30);
        Assert.isTrue(domain[0]== 3 && domain[1]==1, "Wrong computation");
        domain = GridPartition.getGridDomain(15, 30);
        Assert.isTrue(domain[0]== 3 && domain[1]==5, "Wrong computation");
        domain = GridPartition.getGridDomain(14, 15);
        Assert.isTrue(domain[0]== 5 && domain[1]==2, "Wrong computation");
        domain = GridPartition.getGridDomain(12, 15);
        Assert.isTrue(domain[0]== 4 && domain[1]==3, "Wrong computation");
        domain = GridPartition.getGridDomain(5, 15);
        Assert.isTrue(domain[0]== 2 && domain[1]==2, "Wrong computation");
        domain = GridPartition.getGridDomain(27, 30);
        Assert.isTrue(domain[0]== 6 && domain[1]==2, "Wrong computation");
    }

    @Test
    public void testGridDomain() {
        Point gridDomain = GridPartition.getDomainTopLeft(300, 1, 30);
        Point gridCenter = GridPartition.getDomainCenter(300, 1, 30);
        Assert.isTrue(gridDomain.getX() == -150 && gridDomain.getY() == -150, "Wrong computation");
        Assert.isTrue(gridCenter.getX() == -120 && gridCenter.getY() == -125, "Wrong computation");
        gridDomain = GridPartition.getDomainTopLeft(300, 2, 30);
        gridCenter = GridPartition.getDomainCenter(300, 2, 30);
        Assert.isTrue(gridDomain.getX() == -90 && gridDomain.getY() == -150, "Wrong computation");
        Assert.isTrue(gridCenter.getX() == -60 && gridCenter.getY() == -125, "Wrong computation");
        gridDomain = GridPartition.getDomainTopLeft(300, 30, 30);
        gridCenter = GridPartition.getDomainCenter(300, 30, 30);
        Assert.isTrue(gridDomain.getX() == 90 && gridDomain.getY() == 100, "Wrong computation");
        Assert.isTrue(gridCenter.getX() == 120 && gridCenter.getY() == 125, "Wrong computation");
        gridDomain = GridPartition.getDomainTopLeft(300, 17, 30);
        gridCenter = GridPartition.getDomainCenter(300, 17, 30);
        Assert.isTrue(gridDomain.getX() == -90 && gridDomain.getY() == 0, "Wrong computation");
        Assert.isTrue(gridCenter.getX() == -60 && gridCenter.getY() == 25, "Wrong computation");
        gridDomain = GridPartition.getDomainTopLeft(300, 28, 30);
        gridCenter = GridPartition.getDomainCenter(300, 28, 30);
        Assert.isTrue(gridDomain.getX() == -30 && gridDomain.getY() == 100, "Wrong computation");
        Assert.isTrue(gridCenter.getX() == 0 && gridCenter.getY() == 125, "Wrong computation");
    }

    @Test
    public void testRectangularPartition() {
        RectangularPartition rectangularPartition = GridPartition.getRectangularPartition(300, 1, 30);
        Assert.isTrue(rectangularPartition.getPartitionTopLeft().getX() == -150 && rectangularPartition.getPartitionTopLeft().getY() == -150, "Wrong computation");
        Assert.isTrue(rectangularPartition.getPartitionCenter().getX() == -120 && rectangularPartition.getPartitionCenter().getY() == -125, "Wrong computation");
        rectangularPartition = GridPartition.getRectangularPartition(300, 2, 30);
        Assert.isTrue(rectangularPartition.getPartitionTopLeft().getX() == -90 && rectangularPartition.getPartitionTopLeft().getY() == -150, "Wrong computation");
        Assert.isTrue(rectangularPartition.getPartitionCenter().getX() == -60 && rectangularPartition.getPartitionCenter().getY() == -125, "Wrong computation");
        rectangularPartition = GridPartition.getRectangularPartition(300, 30, 30);
        Assert.isTrue(rectangularPartition.getPartitionTopLeft().getX() == 90 && rectangularPartition.getPartitionTopLeft().getY() == 100, "Wrong computation");
        Assert.isTrue(rectangularPartition.getPartitionCenter().getX() == 120 && rectangularPartition.getPartitionCenter().getY() == 125, "Wrong computation");
        rectangularPartition = GridPartition.getRectangularPartition(300, 17, 30);
        Assert.isTrue(rectangularPartition.getPartitionTopLeft().getX() == -90 && rectangularPartition.getPartitionTopLeft().getY() == 0, "Wrong computation");
        Assert.isTrue(rectangularPartition.getPartitionCenter().getX() == -60 && rectangularPartition.getPartitionCenter().getY() == 25, "Wrong computation");
        rectangularPartition = GridPartition.getRectangularPartition(300, 28, 30);
        Assert.isTrue(rectangularPartition.getPartitionTopLeft().getX() == -30 && rectangularPartition.getPartitionTopLeft().getY() == 100, "Wrong computation");
        Assert.isTrue(rectangularPartition.getPartitionCenter().getX() == 0 && rectangularPartition.getPartitionCenter().getY() == 125, "Wrong computation");
    }

}
