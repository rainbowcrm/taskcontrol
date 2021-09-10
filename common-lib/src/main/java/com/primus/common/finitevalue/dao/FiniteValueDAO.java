package com.primus.common.finitevalue.dao;

import com.primus.common.finitevalue.model.FiniteValue;
import com.primus.generic.BusinessModel;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

@Component
public class FiniteValueDAO {

    @PersistenceContext
    protected EntityManager em;

    public FiniteValue getByCode(String code) {
        return (FiniteValue)em.find(FiniteValue.class,code);
    }

    public List<FiniteValue> getAllFVbyGroup(String groupCode)
    {
        Query query =  em.createQuery("from FiniteValue  where group.groupCode = ?"   );
        query.setParameter(1,groupCode);
        List<FiniteValue> ans = query.getResultList();

        return ans;

    }

    public FiniteValue getFVByDescription(String groupCode,String description)
    {
        Query query =  em.createQuery("from FiniteValue  where group.groupCode = ? and  description = ?"   );
        query.setParameter(1,groupCode);
        query.setParameter(2,description);
        List<FiniteValue> ans = query.getResultList();
        if (!CollectionUtils.isEmpty(ans))
        {
            return ans.get(0);
        }
        return null;

    }


}
