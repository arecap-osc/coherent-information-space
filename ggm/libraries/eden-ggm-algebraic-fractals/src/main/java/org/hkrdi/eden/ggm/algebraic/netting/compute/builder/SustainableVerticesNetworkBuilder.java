package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.netting.SustainableVerticesNetwork;
import org.hkrdi.eden.ggm.algebraic.netting.compute.index.ModIndex;
import org.hkrdi.eden.ggm.algebraic.netting.compute.transtion.SustainableVerticesTransition;

public class SustainableVerticesNetworkBuilder extends SustainableNetworkBuilder
        implements SustainableVerticesTransition, SustainableVerticesNetwork {

    @Override
    public int getIndex() {
        if(getColumn() >= 0 && getRow() >= 0) { /// mod6 = 3 and 4
            return   getColumn() >= getRow()/2 ? ModIndex.getMod6Index3(2 * getColumn() + getRow() - getRow()/2 - getRow()/2)  - getColumn() + getRow()/2 : ModIndex.getMod6Index4(getColumn() + getRow() - getRow()/2) - 2 * getColumn() - getRow()%2;
        }
        if(getColumn() < 0 && getRow() >= 0) { /// mod6 = 4 and 0
            return  -getColumn() <= getRow()/2 + getRow()%2 ? ModIndex.getMod6Index4(-getColumn() + getRow() - getRow()/2 - getRow()%2) - 2 * getColumn() - getRow() % 2 : ModIndex.getMod6Index0(-2 * getColumn() - getRow() + getRow()/2 + getRow()/2) + getColumn() - getRow()/2;
        }
        if(getColumn() >= 0 && getRow() < 0) { // mod6 = 1 and  2
            return getColumn() < -getRow()/2 ? ModIndex.getMod6Index1(getColumn() - getRow() + getRow()/2) + 2*getColumn() - getRow()%2 : ModIndex.getMod6Index2(2 * getColumn() - getRow() + getRow()/2 + getRow()/2) + getColumn() + getRow()/2;
        }
        if(getColumn() < 0 && getRow() < 0) { //mod6 = 0 and 1
            return -getColumn() >= -getRow()/2 - getRow()%2 ?   ModIndex.getMod6Index0(-2 * getColumn() + getRow() - getRow()/2 - getRow()/2) + getColumn() - getRow()/2 - getRow()%2  : ModIndex.getMod6Index1(-getColumn() - getRow() + getRow()/2 + getRow()%2) + 2 * getColumn() - getRow()%2;
        }
        return 0;
    }

}
