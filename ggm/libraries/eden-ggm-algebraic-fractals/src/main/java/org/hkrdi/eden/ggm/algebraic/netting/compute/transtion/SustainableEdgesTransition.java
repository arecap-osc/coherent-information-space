package org.hkrdi.eden.ggm.algebraic.netting.compute.transtion;

import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.math.MathContext;

public interface SustainableEdgesTransition extends HexavalentTransition{

    default Point getHorizontalTransition() {
        return new Point(getWidthBigDecimal().doubleValue(), 0);
    }

    default Point getVerticalTransition() {
        return new Point(getWidthBigDecimal().divide(new BigDecimal(2), MathContext.DECIMAL128).doubleValue(),
                getHeightBigDecimal().multiply(new BigDecimal(3)).divide(new BigDecimal(4), MathContext.DECIMAL128).doubleValue());
    }
}
