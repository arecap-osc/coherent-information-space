package org.hkrdi.eden.ggm.repository.semanticmap.entity;

import javax.persistence.*;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "map_word" )
public class MapWord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_word_id_seq")
    @SequenceGenerator(name = "map_word_id_seq", allocationSize = 1)
    @Column(name = "map_word_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "semantic_map_id")
    private SemanticMap semanticMap;

    private String letter;

    @Column(columnDefinition = "text")
    private String word;

    @Column(columnDefinition = "text")
    private String details;

    private Long x;

    private Long y;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SemanticMap getSemanticMap() {
        return semanticMap;
    }

    public void setSemanticMap(SemanticMap semanticMap) {
        this.semanticMap = semanticMap;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }
}
