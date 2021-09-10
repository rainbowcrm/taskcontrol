package com.primus.generic;

import com.primus.common.IService;
import com.primus.common.IValidator;
import com.primus.common.ObjectFactory;
import com.primus.common.model.RadsError;
import com.primus.common.model.transaction.TransactionResult;
import com.primus.common.utils.Utils;
import com.primus.metadata.model.MetadataEntity;
import com.primus.metadata.service.MetadataService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class GenericService implements IService {

    @Autowired
    MetadataService metadataService;

    @Autowired
    GenericDAO genericDAO;

    protected TransactionResult validate(BusinessModel model, BusinessContext context)
    {
        try {
        IValidator currentValidator = getValidator(context.getCurrentEntity(),context);
        TransactionResult result = currentValidator.basicValidation(model, context);
        if (result.hasErrors()) {
            return result;
        }

        result = currentValidator.advancedValidation(model, context);
        if (result.hasErrors()) {
            return result;
        }
        return result;
        }catch (Exception ex)
        {
            ex.printStackTrace();
            TransactionResult result = new TransactionResult(TransactionResult.Result.FAILURE);
            result.addError(new RadsError("","system error! please contact Admin"));
            return result;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionResult create(BusinessModel model, BusinessContext context) {

        TransactionResult result = validate(model,context);
        if (result.hasErrors()) {
            return result;
        }
        model.setCreatedDate(new java.util.Date());
        model.setCreatedBy(context.getUser());
        model.setLastUpdatedBy(context.getUser());
        model.setLastUpdateDate(new java.util.Date());
        genericDAO.create(model);
        //Logger.logDebug("Object Created= " + object.toJSON() + "\n Context=" + productContext.toString(), this.getClass());
        return new TransactionResult();
    }


    public GenericDAO getDAO() {
        return genericDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionResult update(BusinessModel model, BusinessContext context) {
        TransactionResult result = validate(model,context);
        if (result.hasErrors()) {
            return result;
        }
        model.setLastUpdatedBy(context.getUser());
        model.setLastUpdateDate(new java.util.Date());
        genericDAO.update(model);
        return new TransactionResult();
    }

    public long getTotalRecordCount(String entity, String whereCondition) {
        return genericDAO.getTotalRecordCount(entity, whereCondition);
    }

    public List<BusinessModel> listData(String entity, int from, int to, String whereCondition, String orderby) {
        GenericDAO dao = getDAO();
        return dao.listData(entity, from, to, whereCondition, null);
    }

    public BusinessModel fetchData(String entity, String pk,BusinessContext context) {
        GenericDAO dao = getDAO();
        MetadataEntity metadataEntity = metadataService.getMetadata(entity,context);
        try {
            if (metadataEntity.getPkType().equalsIgnoreCase("ID") || metadataEntity.getPkType().equalsIgnoreCase("INTEGER"))
                return dao.getById(Class.forName(metadataEntity.getClassName()), Integer.parseInt(pk));
            else
                return dao.getById(Class.forName(metadataEntity.getClassName()), pk);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public BusinessModel getByBusinessKey(BusinessModel object, BusinessContext context) {
        Map<String, Object> keys =  object.getBK();
        StringBuffer condition = new StringBuffer();
        if (keys != null ) {
            condition.append(" where ");
            Iterator it = keys.keySet().iterator();
            while (it.hasNext()) {
                String key = (String)it.next() ;
                Object val = keys.get(key);
                if(val == null ) continue ;
                if (val instanceof Integer || val instanceof Long  || val instanceof Double || val instanceof Float)
                    condition  = condition.append( Utils.initlower(key) + " = " + val ) ;
                else
                    condition  = condition.append( Utils.initlower(key) + " = '" + val +"'" ) ;
                if (it.hasNext()) {
                    condition.append(" and ") ;
                }
            }
        }
        if (condition.toString().equals(" where ")) return null;
        List<? extends BusinessModel> objects = listData(context.getCurrentEntity(), 0, 2, condition.toString(),null);
        if (!CollectionUtils.isEmpty(objects))
            return objects.get(0);
        else
            return  null;
    }

    public BusinessModel getFullData(BusinessModel model,BusinessContext context) {
        GenericDAO dao = getDAO();
        try {
            if(model.getId() > 0)
            {
               return dao.getById(model.getClass(),model.getId());

            }else{
                return getByBusinessKey(model,context);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    protected IValidator getValidator(String entity,BusinessContext context)
    {
        MetadataEntity metadataEntity = metadataService.getMetadata(entity,context) ;
        String validator = metadataEntity.getValidatorName();
        IValidator iValidator = (IValidator) ObjectFactory.getInstance().getValidatorInstance(validator,context);
        return iValidator;
    }
}