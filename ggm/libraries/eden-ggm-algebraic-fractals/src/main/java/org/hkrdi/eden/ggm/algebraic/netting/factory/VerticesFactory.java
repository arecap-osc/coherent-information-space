package org.hkrdi.eden.ggm.algebraic.netting.factory;

import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.hkrdi.eden.ggm.algebraic.VertexType;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface VerticesFactory {


    List<Vertex> getVertices();

    default Vertex getVertex(int index, VertexType type, Point point) {
        return Optional.of(getVertex(index)).orElseGet(() -> {
            Vertex vertex = new Vertex(index, type, point);
            getVertices().add(vertex);
            return vertex;
        });
    }

    default Vertex getVertex(int index) {
        List<Vertex> candidates = getVertices().stream().filter(v -> v.getIndex() == index).collect(Collectors.toList());
        return candidates.size() == 1 ? candidates.get(0) : null;
    }

    default List<Vertex> getSources() {
        return getVertices().stream().filter(v -> v.getType() == VertexType.SOURCE).collect(Collectors.toList());
    }

    default List<Vertex> getSensors() {
        return getVertices().stream().filter(v -> v.getType() == VertexType.SENSOR).collect(Collectors.toList());
    }

    default List<Vertex> getDeciders() {
        return getVertices().stream().filter(v -> v.getType() == VertexType.DECIDER).collect(Collectors.toList());
    }

}
