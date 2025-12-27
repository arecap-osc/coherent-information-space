package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.netting.SustainableEdgesNetwork;
import org.hkrdi.eden.ggm.algebraic.netting.compute.index.ModIndex;
import org.hkrdi.eden.ggm.algebraic.netting.compute.transtion.SustainableEdgesTransition;

public class SustainableEdgesNetworkBuilder  extends SustainableNetworkBuilder
        implements SustainableEdgesTransition, SustainableEdgesNetwork {

    @Override
    public int getIndex() {
        if(getColumn() >= 0 && getRow() >= 0) { /// mod6 = 2 and 3
            return   getColumn()  > getRow()/2 ? ModIndex.getMod6Index2(getColumn() + getRow() - getRow()/2) + getRow() : (ModIndex.getMod6Index3(getRow()) + getRow()/2 - getColumn());
        }
        if(getColumn() < 0 && getRow() >= 0) { /// mod6 = 4 and 5
            return  -getColumn() <= getRow()/2 + getRow()%2 ? ModIndex.getMod6Index4(getRow()) - getRow()/2 - getColumn() - getRow()%2 : ModIndex.getMod6Index5(-getColumn() + getRow() - getRow()/2 - getRow()%2) - getRow();
        }
        if(getColumn() >= 0 && getRow() < 0) { // mod6 = 1 and  2
            return getColumn()  <= -getRow()/2 ? ModIndex.getMod6Index1(-getRow()) + getColumn() + getRow()/2  : ModIndex.getMod6Index2(getColumn() - getRow() + getRow()/2) + getRow() ;
        }
        if(getColumn() < 0 && getRow() < 0) { //mod6 = 5 and 1
            return -getColumn() >= -getRow()/2 - getRow()%2 ? (ModIndex.getMod6Index5(-getColumn() - getRow()/2) - getRow()) : (ModIndex.getMod6Index1(-getRow()) + getColumn() + getRow()/2);
        }
        return 0;
    }

}
