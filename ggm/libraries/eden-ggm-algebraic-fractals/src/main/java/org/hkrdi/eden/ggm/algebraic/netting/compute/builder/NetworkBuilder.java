package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.netting.compute.transtion.HexavalentTransition;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;

public interface NetworkBuilder extends HexavalentTransition, ConnectorBuilder {

    default void build() {
        Double centerXDelta = new BigDecimal(getSpaceCenter().getX()).doubleValue();
        Double centerYDelta = new BigDecimal(getSpaceCenter().getY()).doubleValue();
        Point center = new Point(getCenter().getX()+centerXDelta, getCenter().getY() + centerYDelta);
        setCenter(center);
        int v = 0;
        int meridianIdx = 0;
        int parallelIdx = 0;

        while (isInPartitionVertical(center)) {
            parallelIdx = 0;
//            parallelIdx = meridianIdx == 0 ? 1 : 0;
            while (isInPartitionHorizontal(center)) {

                setGrid(parallelIdx, meridianIdx);

                buildHexavalentLogic(center);

                if(centerYDelta.compareTo(getCenter().getY()) != 0) {

                    setCenter(new Point(center.getX(),
                            - (center.getY() - 2 * centerYDelta)));
                    setGrid( parallelIdx, -meridianIdx);
                    if(isInPartitionHorizontal(getCenter()) &&
                            isInPartitionVertical(getCenter())) {
                        buildHexavalentLogic(getCenter());
                    }

                    if(centerXDelta.compareTo(getCenter().getX()) != 0) {

                        setCenter(new Point(- (center.getX() - 2 * centerXDelta),
                                - (center.getY() - 2 * centerYDelta)));
                        setGrid( -(meridianIdx % 2) -parallelIdx, - meridianIdx);

                        if(isInPartitionHorizontal(getCenter()) &&
                                isInPartitionVertical(getCenter())) {
                            buildHexavalentLogic(getCenter());
                        }
                    }
                }
                if(centerXDelta.compareTo(getCenter().getX()) != 0) {

                    setGrid(-(meridianIdx % 2)  -parallelIdx, meridianIdx );
                    setCenter(new Point(- (center.getX() - 2 * centerXDelta)  ,
                            center.getY()));
                    if(isInPartitionHorizontal(getCenter()) &&
                            isInPartitionVertical(getCenter())) {
                        buildHexavalentLogic(getCenter());
                    }
                }
                setCenter(new Point(center.getX() + getHorizontalTransition().getX(),
                        center.getY()));
                center = getCenter();
                parallelIdx++;
            }
            v++;
            meridianIdx++;
            setCenter(new Point( v % 2 == 0 ? centerXDelta : centerXDelta + getVerticalTransition().getX(),
                    center.getY() + getVerticalTransition().getY()));
            center = getCenter();
        }
    }

    void buildHexavalentLogic(Point center);
}
