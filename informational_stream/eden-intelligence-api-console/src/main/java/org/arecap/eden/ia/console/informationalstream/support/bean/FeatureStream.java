package org.arecap.eden.ia.console.informationalstream.support.bean;


import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.FeatureStreamBean;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Audited
@Table(name = "feature_stream")
public class FeatureStream implements FeatureStreamBean<Long, Signal> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feature_stream_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "downstream", referencedColumnName = "signal_id")
    private Signal downstream;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "upstream", referencedColumnName = "signal_id")
    private Signal upstream;

    @Column
    @Enumerated(EnumType.STRING)
    private StreamTopology topology;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Signal getDownstream() {
        return downstream;
    }

    @Override
    public void setDownstream(Signal downstream) {
        this.downstream = downstream;
    }

    @Override
    public Signal getUpstream() {
        return upstream;
    }

    @Override
    public void setUpstream(Signal upstream) {
        this.upstream = upstream;
    }

    @Override
    public StreamTopology getTopology() {
        return topology;
    }

    @Override
    public void setTopology(StreamTopology topology) {
        this.topology = topology;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FeatureStream)) return false;
        FeatureStream featureStream = (FeatureStream) obj;
        return Objects.equals(getId(), featureStream.getId());
    }

}
