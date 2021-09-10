package com.primus.common.finitevalue.model;


import com.primus.common.annotations.RadsPropertySet;
import com.primus.common.model.ModelObject;

import javax.persistence.*;

@Entity
@Table(name="FINITE_VALUES")
public class FiniteValue extends ModelObject {

    String code;
    String description;
    FiniteGroup group ;

    @Column(name  ="VALUE_CODE")
    @Id
    @RadsPropertySet(isPK = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name  ="VALUE_DESC")
    @RadsPropertySet (isBK = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name  ="GROUP_CODE")
    @RadsPropertySet(useBKForJSON = true,useBKForMap = true,usePKForJSON = true)
    public FiniteGroup getGroup() {
        return group;
    }

    @RadsPropertySet(useBKForJSON = true,useBKForMap = true,usePKForJSON = true)
    public void setGroup(FiniteGroup group) {
        this.group = group;
    }
}
