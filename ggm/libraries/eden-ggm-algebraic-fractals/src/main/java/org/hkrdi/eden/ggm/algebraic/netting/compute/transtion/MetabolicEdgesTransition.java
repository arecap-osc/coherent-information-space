package org.hkrdi.eden.ggm.algebraic.netting.compute.transtion;

import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.math.MathContext;

public interface MetabolicEdgesTransition extends HexavalentTransition{

    default Point getHorizontalTransition() {
        return new Point(new BigDecimal(3).multiply(getWidthBigDecimal()).divide( new BigDecimal(2), MathContext.DECIMAL128).doubleValue(), 0);
    }

    default Point getVerticalTransition() {
        return new Point(new BigDecimal(3).multiply( getWidthBigDecimal()).divide(new BigDecimal(4), MathContext.DECIMAL128).doubleValue(),
                getHeightBigDecimal().divide(new BigDecimal(2), MathContext.DECIMAL128).doubleValue());
    }
}
