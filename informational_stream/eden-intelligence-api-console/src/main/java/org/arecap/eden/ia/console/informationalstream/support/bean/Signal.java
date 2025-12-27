package org.arecap.eden.ia.console.informationalstream.support.bean;


import org.arecap.eden.ia.console.boot.BeanUtil;
import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;
import org.arecap.eden.ia.console.informationalstream.api.bean.FeatureSignalBean;
import org.arecap.eden.ia.console.informationalstream.api.bean.I18nBean;
import org.arecap.eden.ia.console.informationalstream.api.bean.SignalBean;
import org.arecap.eden.ia.console.informationalstream.support.service.SignalService;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
@Audited
@Table(name = "signal")
public class Signal implements SignalBean<Long, Signal>, FeatureSignalBean<Long, Feature>, I18nBean<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "signal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "informational_stream_id")
    private InformationalStream informationalStream;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "feature_id")
    private Feature feature;

    @Column
    @Enumerated(EnumType.STRING)
    private StreamApplicationType streamApplicationType;

    @Column
    private Locale locale;

    @Column
    private String featurePart;

    @Column
    private String details;

    @Column
    private Long i18nId;

    @Column
    private Long congruenceId;

    @Column
    private Long similarityId;


    @Transient
    public Set<Signal> getI18n() {
        return BeanUtil.getBean(SignalService.class).getI18nBeans(this);
    }

    @Override
    @Transient
    public Set<Signal> getCongruence() {
        return BeanUtil.getBean(SignalService.class).getCongruenceSignals(this);
    }

    @Override
    @Transient
    public Set<Signal> getSimilarity() {
        return BeanUtil.getBean(SignalService.class).getSimilaritySignals(this);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public InformationalStream getInformationalStream() {
        return informationalStream;
    }

    public void setInformationalStream(InformationalStream informationalStream) {
        this.informationalStream = informationalStream;
    }

    @Override
    public Feature getFeature() {
        return feature;
    }

    @Override
    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @Override
    public StreamApplicationType getStreamApplicationType() {
        return streamApplicationType;
    }

    @Override
    public void setStreamApplicationType(StreamApplicationType streamApplicationType) {
        this.streamApplicationType = streamApplicationType;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getFeaturePart() {
        return featurePart;
    }

    @Override
    public void setFeaturePart(String featurePart) {
        this.featurePart = featurePart;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    public Long getI18nId() {
        return i18nId;
    }

    public void setI18nId(Long i18nId) {
        this.i18nId = i18nId;
    }

    public Long getCongruenceId() {
        return congruenceId;
    }

    public void setCongruenceId(Long congruenceId) {
        this.congruenceId = congruenceId;
    }

    public Long getSimilarityId() {
        return similarityId;
    }

    public void setSimilarityId(Long similarityId) {
        this.similarityId = similarityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Signal)) return false;
        Signal signal = (Signal) obj;
        return Objects.equals(getId(), signal.getId());
    }

}
