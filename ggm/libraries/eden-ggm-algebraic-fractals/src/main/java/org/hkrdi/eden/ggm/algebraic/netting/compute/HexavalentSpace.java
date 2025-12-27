package org.hkrdi.eden.ggm.algebraic.netting.compute;

import org.hkrdi.eden.ggm.algebraic.netting.compute.index.ModIndex;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;

public interface HexavalentSpace {

    Point getSpaceCenter();

    Point getCenter();

    int getIndex();

    double getSpaceDistance();

    double getDistance(double value);

    BigDecimal getDistanceBigDecimal(double value);

    double getWidth();

    BigDecimal getWidthBigDecimal();

    double getHeight();

    BigDecimal getHeightBigDecimal();

    default  int getBeltIndex(int spaceIndex) {
        for(int i = 0; i <= spaceIndex; i++){
            if(spaceIndex > ModIndex.getMod6Index0(i)) {
                continue;
            }
            return i;
        }
        return 0;
    }

    int getLastIndex(int spaceIndex);

    int getCognitiveOuterSourceIndex(int spaceIndex);

    int getCognitiveOuterSensorIndex(int spaceIndex);

    int getCognitiveOuterDeciderIndex(int spaceIndex);

    int getSocialOuterSourceIndex(int spaceIndex);

    int getSocialOuterSensorIndex(int spaceIndex);

    int getSocialOuterDeciderIndex(int spaceIndex);

}
