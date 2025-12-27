package org.hkrdi.eden.ggm.algebraic.netting;

import org.hkrdi.eden.ggm.algebraic.netting.compute.MetabolicSpace;

public interface MetabolicEdgesNetwork extends MetabolicSpace, EdgesNetwork  {

    @Override
    default NettingType getNettingType() {
        return NettingType.METABOLIC_EDGES;
    }

}
