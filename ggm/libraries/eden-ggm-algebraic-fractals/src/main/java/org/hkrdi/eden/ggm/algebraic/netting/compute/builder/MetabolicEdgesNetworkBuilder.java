package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.netting.MetabolicEdgesNetwork;
import org.hkrdi.eden.ggm.algebraic.netting.compute.index.ModIndex;
import org.hkrdi.eden.ggm.algebraic.netting.compute.transtion.MetabolicEdgesTransition;

public class MetabolicEdgesNetworkBuilder extends MetabolicNetworkBuilder
        implements MetabolicEdgesTransition, MetabolicEdgesNetwork {

    @Override
    public int getIndex() {
        if(getColumn() >= 0 && getRow() >= 0) { /// mod6 = 4 done and 5 done
            return   getColumn() >= getRow()/2 ? ModIndex.getMod6Index4(2 * getColumn() + getRow() - getRow()/2 - getRow()/2)  - getColumn() + getRow()/2 : ModIndex.getMod6Index5(getColumn() + getRow() - getRow()/2) - 2 * getColumn() - getRow()%2;
        }
        if(getColumn() < 0 && getRow() >= 0) { /// mod6 = 5 done and 1 done
            return  -getColumn() <= getRow()/2 + getRow()%2 ? ModIndex.getMod6Index5(-getColumn() + getRow() - getRow()/2 - getRow()%2) - 2 * getColumn() - getRow() % 2 : ModIndex.getMod6Index1(-2 * getColumn() - getRow() + getRow()/2 + getRow()/2) + getColumn() - getRow()/2;
        }
        if(getColumn() >= 0 && getRow() < 0) { // mod6 = 2 done and  3 done
            return getColumn() < -getRow()/2 ? ModIndex.getMod6Index2(getColumn() - getRow() + getRow()/2) + 2*getColumn() - getRow()%2 : ModIndex.getMod6Index3(2 * getColumn() - getRow() + getRow()/2 + getRow()/2) + getColumn() + getRow()/2;
        }
        if(getColumn() < 0 && getRow() < 0) { //mod6 = 1 done and 2 done
            return -getColumn() >= -getRow()/2 - getRow()%2 ?   ModIndex.getMod6Index1(-2 * getColumn() + getRow() - getRow()/2 - getRow()/2) + getColumn() - getRow()/2 - getRow()%2  : ModIndex.getMod6Index2(-getColumn() - getRow() + getRow()/2 + getRow()%2) + 2 * getColumn() - getRow()%2;
        }
        return 0;
    }


}
