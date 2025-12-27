package org.hkrdi.eden.ggm.algebraic.netting.compute.transtion;

import org.hkrdi.eden.ggm.algebraic.netting.compute.HexavalentSpace;
import org.springframework.data.geo.Point;

public interface HexavalentTransition extends HexavalentSpace {


    void setGrid(int col, int row);

    void setCenter(Point center);

    Point getHorizontalTransition();

    Point getVerticalTransition();

    int getRow();

    int getColumn();

//    default int getRow() {
//        return (int) Math.round(getCenter().getY() / getVerticalTransition().getY());
//    }
//
//    default int getColumn() {
//        int col =  (int) Math.round(getCenter().getX() / getHorizontalTransition().getX());
//        return  getRow() % 2 == 0 ? col : col - 1;
//    }


    //    default Point getPositivePartitionTopLeft() {
//        return new Point(Math.max(Math.abs(getPartitionTopLeft().getX()),
//                Math.abs(2*getPartitionCenter().getX() - getPartitionTopLeft().getX())) ,
//                Math.max(Math.abs(getPartitionTopLeft().getY()),
//                        Math.abs(2*getPartitionCenter().getY() - getPartitionTopLeft().getY())));
//    }
//
//    default Point getHexavalentTopLeftCenter() {
//        return new Point(getHorizontalTransition().getX() * getRow(),
//                getVerticalTransition().getY() * getColumn());
//    }
//
//    default boolean isInPartition(Point point) {
//        Point positivePartition = getPartitionTopLeft();
//        return point.getX() + getWidth() / 2 <=
//                positivePartition.getX() + 2 * Math.abs( getPartitionCenter().getX() - getPartitionTopLeft().getX()) &&
//                point.getY() + getHeight() / 2 <=
//                        positivePartition.getY() + 2 * Math.abs( getPartitionCenter().getY() - getPartitionTopLeft().getY());
//    }

    default boolean isInPartitionHorizontal(Point point) {
        return point.getX() - getSpaceCenter().getX()  <=
                 getSpaceDistance() * 2;
    }

    default boolean isInPartitionVertical(Point point) {
        return point.getY() - getSpaceCenter().getY()  <=
                getSpaceDistance() * 2;
    }

}
