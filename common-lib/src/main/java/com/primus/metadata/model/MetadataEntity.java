package com.primus.metadata.model;



import com.primus.common.annotations.RadsPropertySet;
import com.primus.common.model.ModelObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "ENTITIES")
public class MetadataEntity extends ModelObject {






    String name;
    boolean hasSub ;
    MetadataEntity parentEntity ;
    Collection<Field> fields ;
    String className;
    String serviceName;
    String validatorName;
    String pkType ;
    Collection<ValidationRule> validationRules ;


    @Id
    @Column(name = "ENTITY_NAME")
    @RadsPropertySet(isPK = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "HAS_SUB")
    public boolean isHasSub() {
        return hasSub;
    }

    public void setHasSub(boolean hasSub) {
        this.hasSub = hasSub;
    }

    @ManyToOne(cascade=CascadeType.DETACH)
    @JoinColumn(name  ="PARENT_ENTITY")
    public MetadataEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(MetadataEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    @RadsPropertySet(excludeFromMap = true, excludeFromXML = true  , excludeFromJSON =  true)
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "entity")
    public Collection<Field> getFields() {
        return fields;
    }

    public void setFields(Collection<Field> fields) {
        this.fields = fields;
    }

    @Column(name = "CLASSNAME")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = "SERVICENAME")
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Column(name = "VALIDATORNAME")
    public String getValidatorName() {
        return validatorName;
    }

    public void setValidatorName(String validatorName) {
        this.validatorName = validatorName;
    }

    @Column(name = "PK_TYPE")
    public String getPkType() {
        return pkType;
    }

    public void setPkType(String pkType) {
        this.pkType = pkType;
    }

    @RadsPropertySet(excludeFromMap = true, excludeFromXML = true  , excludeFromJSON =  true)
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "entity")
    public Collection<ValidationRule> getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(Collection<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }
}
