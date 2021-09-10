package com.primus.generic;


import com.primus.common.utils.Utils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public  class GenericDAO {

    @PersistenceContext
    protected  EntityManager em;

    public   String getEntityClassName(String entity)
    {
        return null;
    }

    public  Class getEntityClass(String entity)
    {
        return null;
    }

    public BusinessModel getById( String entity , Object PK) {
        Class className =  getEntityClass(entity) ;
        int companyId = Integer.parseInt(String.valueOf(PK));
        return (BusinessModel)em.find(className,PK);
    }

    public BusinessModel getById( Class className, Object PK) {
        return (BusinessModel)em.find(className,PK);
    }


    @Transactional
    public void create(BusinessModel model) {
        em.merge(model);
    }

    @Transactional
    public void update(BusinessModel model) {
        em.merge(model);
    }

    public void batchUpdate(List<? extends BusinessModel> objects)  throws Exception{

        boolean success = false;
        try {
            for (BusinessModel object : objects) {
                em.merge(object);
            }

            success = true;
        }catch(Exception ex) {

            //throw new DatabaseException(ex,DatabaseException.PERSISTENCE_ERROR);
        }
    }

    public long getTotalRecordCount(String entityName,  String whereCondition ) {

        try  {
            String queryString = " Select count(*) from " + entityName  + " " +  whereCondition;
            Query query =  em.createQuery(queryString);
            List lst = query.getResultList();
            if(!Utils.isNull(lst)) {
                Object obj = lst.get(0);
                if (obj!=null && obj instanceof Long) {
                    return(((Long)obj).longValue());
                } if (obj!=null && obj instanceof Integer) {
                    return(((Integer)obj).intValue());
                }
            }
            return 1;
        }finally{

        }
    }


    public List<BusinessModel> listData(String table, int from , int to , String whereCondition, String orderby ) {
/*        Session session = openSession(false);
        Query query = session.createQuery("from " + table   +  ((Utils.isNull(whereCondition))?"":whereCondition) +
                " " + ((Utils.isNull(orderby))?"": (" order by " + orderby) ) );
        query.setFirstResult(from);
        query.setMaxResults(to-from);
        List<CRMModelObject> ans = query.list();
        supplement(ans);
        closeSession(session,false);*/

        Query query =  em.createQuery("from " + table   +  ((Utils.isNull(whereCondition))?"":whereCondition) +
                " " + ((Utils.isNull(orderby))?"": (" order by " + orderby) ) );
        query.setFirstResult(from);
        query.setMaxResults(to-from);
        List<BusinessModel> ans = query.getResultList();

        return ans;
    }

    public List<BusinessModel> fetchAllActive(String table,  String whereCondition, String orderby ) {

        Query query =  em.createQuery("from " + table   +  ((Utils.isNull(whereCondition))?"":whereCondition) +
                " " + ((Utils.isNull(orderby))?"": (" order by " + orderby) ) );

        List<BusinessModel> ans = query.getResultList();

        return ans;
    }
}
