package org.hkrdi.eden.ggm.algebraic.netting.factory;

import org.hkrdi.eden.ggm.algebraic.TrivalentLogic;
import org.hkrdi.eden.ggm.algebraic.TrivalentLogicType;
import org.hkrdi.eden.ggm.algebraic.Vertex;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface TrivalentLogicFactory {

    List<TrivalentLogic> getTrivalentLogic();

    default TrivalentLogic getTrivalentLogic(TrivalentLogicType type, Vertex source, Vertex sensor, Vertex decider) {
        return Optional.of(getTrivalentLogic(type)).orElseGet(() -> {
            TrivalentLogic trivalentLogic = new TrivalentLogic(type, source, sensor, decider);
            getTrivalentLogic().add(trivalentLogic);
            return trivalentLogic;
        });
    }

    default TrivalentLogic getTrivalentLogic(TrivalentLogicType type) {
        List<TrivalentLogic> candidates = getTrivalentLogic().stream().filter(tl -> tl.getType() == type).collect(Collectors.toList());
        return candidates.size() == 1 ? candidates.get(0) : null;
    }

    default TrivalentLogic getCognitiveOuter() {
        List<TrivalentLogic> candidates = getTrivalentLogic().stream().filter(tl -> tl.getType() == TrivalentLogicType.COGNITIVE_OUTER).collect(Collectors.toList());
        return candidates.size() == 1 ? candidates.get(0) : null;
    }

    default TrivalentLogic getSocialOuter() {
        List<TrivalentLogic> candidates = getTrivalentLogic().stream().filter(tl -> tl.getType() == TrivalentLogicType.SOCIAL_OUTER).collect(Collectors.toList());
        return candidates.size() == 1 ? candidates.get(0) : null;
    }

    default TrivalentLogic getCognitiveInner() {
        List<TrivalentLogic> candidates = getTrivalentLogic().stream().filter(tl -> tl.getType() == TrivalentLogicType.COGNITIVE_INNER).collect(Collectors.toList());
        return candidates.size() == 1 ? candidates.get(0) : null;
    }

    default TrivalentLogic getSocialInner() {
        List<TrivalentLogic> candidates = getTrivalentLogic().stream().filter(tl -> tl.getType() == TrivalentLogicType.SOCIAL_INNER).collect(Collectors.toList());
        return candidates.size() == 1 ? candidates.get(0) : null;
    }
}
