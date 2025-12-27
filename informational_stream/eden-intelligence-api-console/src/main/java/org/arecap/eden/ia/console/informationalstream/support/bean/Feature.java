package org.arecap.eden.ia.console.informationalstream.support.bean;


import org.arecap.eden.ia.console.informationalstream.api.bean.FeatureBean;
import org.arecap.eden.ia.console.informationalstream.api.bean.I18nBean;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Audited
@Table(name = "feature")
public class Feature implements FeatureBean<Long, InformationalStream>, I18nBean<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feature_id")
    private Long id;

    @Column
    private Long i18nId;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "informational_stream_id")
    private InformationalStream informationalStream;

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setInformationalStream(InformationalStream informationalStream) {
        this.informationalStream = informationalStream;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public InformationalStream getInformationalStream() {
        return informationalStream;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getI18nId() {
        return i18nId;
    }

    public void setI18nId(Long i18nId) {
        this.i18nId = i18nId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Feature)) return false;
        Feature feature = (Feature) obj;
        return Objects.equals(getId(), feature.getId());
    }

}
