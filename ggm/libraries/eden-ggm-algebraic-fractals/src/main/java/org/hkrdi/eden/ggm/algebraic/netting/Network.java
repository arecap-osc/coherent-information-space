package org.hkrdi.eden.ggm.algebraic.netting;

import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.netting.compute.HexavalentSpace;

import java.util.List;

public interface Network extends HexavalentSpace {

    int getDeep();

    NettingType getNettingType();

    List<HexavalentLogic> getHexavalentNetting();

    //List<HexavalentLogic> getNetting(double logicDistance, int deep, NettingType nettingType, Point center, Point topRight);

}
