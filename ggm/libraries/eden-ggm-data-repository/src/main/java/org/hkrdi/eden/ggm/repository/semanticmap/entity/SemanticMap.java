package org.hkrdi.eden.ggm.repository.semanticmap.entity;

import javax.persistence.*;

import org.hibernate.envers.Audited;

import java.io.Serializable;

@Entity
@Audited
@Table(name = "semantic_map" )
public class SemanticMap implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semantic_map_id_seq")
    @SequenceGenerator(name = "semantic_map_id_seq", allocationSize = 1)
    @Column(name = "semantic_map_id")
    private Long id;

    @Column(columnDefinition = "text")
    private String label;

    @Column(columnDefinition = "text")
    private String brief;

    @Column(columnDefinition = "text")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
