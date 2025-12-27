package org.arecap.eden.ia.console.informationalstream.support.bean;

import org.arecap.eden.ia.console.informationalstream.api.bean.I18nBean;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamBean;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Locale;
import java.util.Objects;

@Entity
@Audited
@Table(name = "informational_stream")
public class InformationalStream implements InformationalStreamBean<Long>, I18nBean<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "informational_stream_id")
    private Long id;

    @Column
    private String name;

    @Column
    private Locale locale;

    @Column
    private Long i18nId;


    @Column(columnDefinition = "text")
    private String functionalDescription;


    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void getName(String name) {
        this.name = name;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setI18nId(Long i18nId) {
        this.i18nId = i18nId;
    }

    @Override
    public void getFunctionalDescription(String functionalDescription) {
        this.functionalDescription = functionalDescription;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public Long getI18nId() {
        return i18nId;
    }

    @Override
    public String getFunctionalDescription() {
        return functionalDescription;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunctionalDescription(String functionalDescription) {
        this.functionalDescription = functionalDescription;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InformationalStream)) return false;
        InformationalStream informationalStream = (InformationalStream) obj;
        return Objects.equals(getId(), informationalStream.getId());
    }

}
