package org.hkrdi.eden.ggm.repository.semanticmap.entity;

import javax.persistence.*;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "map_link")
public class MapLink {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_link_id_seq")
    @SequenceGenerator(name = "map_link_id_seq", allocationSize = 1)
    @Column(name = "map_link_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "from_map_word_id", referencedColumnName = "map_word_id")
    private MapWord fromWord;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_map_word_id", referencedColumnName = "map_word_id")
    private MapWord toWord;

    @Column(columnDefinition = "text")
    private String verb;

    @Column(columnDefinition = "text")
    private String conjunction;

    @Column(columnDefinition = "text")
    private String verbalization;

    @Column(columnDefinition = "text")
    private String phrase;

    @Column(columnDefinition = "text")
    private String details;

    public MapLink() {
    }

    public MapLink(MapWord fromWord, MapWord toWord, String verb, String conjunction, String verbalization, String phrase, String details) {
        this.fromWord = fromWord;
        this.toWord = toWord;
        this.verb = verb;
        this.conjunction = conjunction;
        this.verbalization = verbalization;
        this.phrase = phrase;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MapWord getFromWord() {
        return fromWord;
    }

    public void setFromWord(MapWord fromWord) {
        this.fromWord = fromWord;
    }

    public MapWord getToWord() {
        return toWord;
    }

    public void setToWord(MapWord toWord) {
        this.toWord = toWord;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }

    public String getVerbalization() {
        return verbalization;
    }

    public void setVerbalization(String verbalization) {
        this.verbalization = verbalization;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
