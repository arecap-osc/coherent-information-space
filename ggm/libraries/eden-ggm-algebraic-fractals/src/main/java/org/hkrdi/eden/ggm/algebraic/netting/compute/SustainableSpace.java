package org.hkrdi.eden.ggm.algebraic.netting.compute;

import org.hkrdi.eden.ggm.algebraic.netting.Network;

import java.math.BigDecimal;
import java.math.MathContext;

public interface SustainableSpace extends Network {

    @Override
    default double getDistance(double value) {
        return getDistanceBigDecimal(value).doubleValue();
    }

    @Override
    default double getWidth() {
        return  getWidthBigDecimal().doubleValue();
    }

    default BigDecimal getDistanceBigDecimal(double value) {
        BigDecimal bd = new BigDecimal(value);
        return getDeep() == 0 ? bd :  bd.divide(new BigDecimal(Math.pow(3, getDeep())), MathContext.DECIMAL128);// value / (5 * Math.pow(3, getDeep() - 1));
    }


    default BigDecimal getWidthBigDecimal() {
        return  getDistanceBigDecimal(getSpaceDistance());
    }

    @Override
    default double getHeight() {
        return getHeightBigDecimal().doubleValue();
    }

    default BigDecimal getHeightBigDecimal() {
        return getWidthBigDecimal().multiply(new BigDecimal(2)).divide(new BigDecimal(Math.sqrt(3)), MathContext.DECIMAL128);
    }

}
