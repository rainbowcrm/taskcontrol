package com.primus.generic;



import com.primus.common.annotations.RadsPropertySet;
import com.primus.common.model.ModelObject;
import com.primus.common.utils.Utils;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract  class BusinessModel extends ModelObject {


    @RadsPropertySet(excludeFromMap = true, excludeFromJSON = true, excludeFromXML = true)
    public void setProperty(String property, Object value) throws InvocationTargetException, IllegalAccessException {
        Map<String, Method> allFields = new HashMap();
        Method[] allMethods = this.getClass().getMethods();
        for (int i = 0; i < allMethods.length; ++i) {
            Method currMethod = allMethods[i];
            if (("set" + Utils.initupper(property)).equals(currMethod.getName())) {
                currMethod.invoke(this, value);
            }
        }
    }





    @Transient
    @RadsPropertySet(excludeFromMap = true,excludeFromJSON = true,excludeFromXML = true)
    public Object getProperty(String property) throws InvocationTargetException, IllegalAccessException
    {
        Map<String, Method> allFields = new HashMap();
        Method[] allMethods = this.getClass().getMethods();
        for(int i = 0; i < allMethods.length; ++i) {
            Method currMethod = allMethods[i];
            if (("get" + Utils.initupper(property)).equals(currMethod.getName())) {
                return currMethod.invoke(this);
            }
        }
        return null;

    }

    protected  boolean deleted ;
    protected Date lastUpdateDate;
    protected Date createdDate;
    protected  String createdBy ;
    protected  String lastUpdatedBy ;
    protected long  version;

    protected int id;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  ="ID")
    @RadsPropertySet(isPK = true)
    public int getId() {
        return id;
    }

    @RadsPropertySet(isPK = true)
    public void setId(int id) {
        this.id = id;
    }


    @Column(name  ="CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name  ="IS_DELETED")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Column(name  ="LAST_UPDATED_DATE")
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Column(name  ="CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }


    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name  ="LAST_UPDATED_BY")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }


    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }



    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
