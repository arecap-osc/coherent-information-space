package org.hkrdi.eden.ggm.algebraic.netting;

import org.hkrdi.eden.ggm.algebraic.netting.compute.SustainableSpace;

public interface SustainableEdgesNetwork extends SustainableSpace, EdgesNetwork {

    @Override
    default NettingType getNettingType() {
        return NettingType.SUSTAINABLE_EDGES;
    }

}
