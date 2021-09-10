package com.primus.metadata.model;


import com.primus.common.annotations.RadsPropertySet;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="ENTITY_VALIDATIONS")
public class ValidationRule {

    public enum ValidationType {
        MANDATORY, UNIQUE, FV, FK ,  RANGED  , NON_ZERO_POSITIVE, ONLY_PRESENT_FUTUREDATE , ONLY_PASTDATE
    }

    protected Date lastUpdateDate;
    protected Date createdDate;
    protected String createdBy;
    protected String lastUpdatedBy;


    protected int id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @RadsPropertySet(isPK = true)
    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    @Column(name = "LAST_UPDATED_DATE")
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }


    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "LAST_UPDATED_BY")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }


    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }


    MetadataEntity entity;
    ValidationType validationType ;
    String field;
    String referredEntity;
    String referredField;
    String params;
    String fvGroup ;

    @ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name  ="ENTITY_NAME")
    @RadsPropertySet(excludeFromXML = true,excludeFromMap = true,excludeFromJSON = true)
    public MetadataEntity getEntity() {
        return entity;
    }

    public void setEntity(MetadataEntity entity) {
        this.entity = entity;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "VALIDATION_TYPE")
    public ValidationType getValidationType() {
        return validationType;
    }

    public void setValidationType(ValidationType validationType) {
        this.validationType = validationType;
    }

    @Column(name = "FIELD_NAME")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Column(name = "REF_ENTITY")
    public String getReferredEntity() {
        return referredEntity;
    }

    public void setReferredEntity(String referredEntity) {
        this.referredEntity = referredEntity;
    }

    @Column(name = "REF_FIELD")
    public String getReferredField() {
        return referredField;
    }

    public void setReferredField(String referredField) {
        this.referredField = referredField;
    }

    @Column(name = "PARAMS")
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Column(name = "FV_GROUP")
    public String getFvGroup() {
        return fvGroup;
    }

    public void setFvGroup(String fvGroup) {
        this.fvGroup = fvGroup;
    }
}
