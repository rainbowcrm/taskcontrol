package com.primus.common.finitevalue.model;


import com.primus.common.annotations.RadsPropertySet;
import com.primus.common.model.ModelObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="FINITE_GROUPS")
public class FiniteGroup extends ModelObject {
    String groupCode;
    String groupDesc ;

    @Column(name  ="GROUP_CODE")
    @Id
    @RadsPropertySet(isPK = true )
    public String getGroupCode() {
        return groupCode;
    }

    @RadsPropertySet(isPK = true )
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Column(name  ="GROUP_DESC")
    @RadsPropertySet(isBK = true )
    public String getGroupDesc() {
        return groupDesc;
    }

    @RadsPropertySet(isBK = true )
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
