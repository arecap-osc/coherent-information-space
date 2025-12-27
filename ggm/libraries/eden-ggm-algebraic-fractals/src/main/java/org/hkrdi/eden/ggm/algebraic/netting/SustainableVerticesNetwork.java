package org.hkrdi.eden.ggm.algebraic.netting;

import org.hkrdi.eden.ggm.algebraic.netting.compute.SustainableSpace;

public interface SustainableVerticesNetwork extends SustainableSpace, VerticesNetwork {

    @Override
    default NettingType getNettingType() {
        return NettingType.SUSTAINABLE_VERTICES;
    }

}
