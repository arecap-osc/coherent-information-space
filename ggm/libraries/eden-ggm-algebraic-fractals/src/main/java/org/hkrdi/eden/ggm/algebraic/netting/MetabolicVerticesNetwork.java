package org.hkrdi.eden.ggm.algebraic.netting;

import org.hkrdi.eden.ggm.algebraic.netting.compute.MetabolicSpace;

public interface MetabolicVerticesNetwork extends MetabolicSpace, VerticesNetwork {

    @Override
    default NettingType getNettingType() {
        return NettingType.METABOLIC_VERTICES;
    }


}
