package com.primus.generic;


import com.primus.ErrorCodes;
import com.primus.common.IService;
import com.primus.common.IValidator;
import com.primus.common.finitevalue.model.FiniteGroup;
import com.primus.common.finitevalue.model.FiniteValue;
import com.primus.common.finitevalue.service.FiniteValueService;
import com.primus.common.ObjectFactory;
import com.primus.common.model.RadsError;
import com.primus.common.model.transaction.TransactionResult;
import com.primus.metadata.model.MetadataEntity;
import com.primus.metadata.model.ValidationRule;
import com.primus.metadata.service.MetadataService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;

@Component
public class GenericValidator implements IValidator {
    public  ResourceBundle  resourceBundle = null;

    private RadsError getErrorforCode(Locale locale , String errorCode, String ... params) {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("ErrorMessages");
        }
        String property = (String)resourceBundle.getObject(String.valueOf(errorCode));
        String converted = MessageFormat.format(property, params);
        RadsError error = new RadsError(String.valueOf(errorCode),converted);
        return error;
    }

    @Autowired
    MetadataService metadataService ;

    @Override
    public TransactionResult basicValidation(BusinessModel model, BusinessContext context)  throws Exception {
        TransactionResult result  = new TransactionResult();
        MetadataEntity metadataEntity = metadataService.getMetadata(context.getCurrentEntity(),context) ;
        String serviceClass = metadataEntity.getServiceName() ;
        IService currentService  = ObjectFactory.getInstance().getServiceInstance(serviceClass,context);
        if (CollectionUtils.isEmpty(metadataEntity.getValidationRules())) {
               return new TransactionResult(TransactionResult.Result.SUCCESS);
           }
           for (ValidationRule validationRule : metadataEntity.getValidationRules()) {
               Object fieldValue = model.getProperty(validationRule.getField());
               if (validationRule.getValidationType().equals(ValidationRule.ValidationType.MANDATORY)) {
                   if ( fieldValue == null || StringUtils.isEmpty(fieldValue) )
                   {
                         result.addError(
                                 getErrorforCode(context.getLocale(),ErrorCodes.FIELD_MANDATORY,validationRule.getParams()));
                         result.setResult(TransactionResult.Result.FAILURE);
                   }
               }
               if (validationRule.getValidationType().equals(ValidationRule.ValidationType.UNIQUE)) {
                   List<BusinessModel> fetchedValues=  currentService.listData(context.getCurrentEntity(),0,99999,
                           " where " + validationRule.getField() + "='" + fieldValue + "'",null);
                   if ( !CollectionUtils.isEmpty(fetchedValues) && fetchedValues.get(0).getId() != model.getId())
                   {
                       result.addError(
                               getErrorforCode(context.getLocale(), ErrorCodes.FIELD_NOTUNIQUE,validationRule.getParams()));
                       result.setResult(TransactionResult.Result.FAILURE);
                   }
               }
               if (validationRule.getValidationType().equals(ValidationRule.ValidationType.FV) && fieldValue != null) {
                   FiniteGroup fvGroup = new FiniteGroup();
                   String fvGroupCode =  validationRule.getFvGroup() ;
                   fvGroup.setGroupCode(fvGroupCode);
                   FiniteValue fvalue = (FiniteValue)  fieldValue ;
                   fvalue.setGroup(fvGroup);
                   FiniteValueService finiteValueService = (FiniteValueService) ObjectFactory.getInstance().getFiniteValueServiceInstance(context);
                   FiniteValue value = finiteValueService.getFiniteValue(context,fvalue);
                   if ( value == null )
                   {
                       result.addError(
                               getErrorforCode(context.getLocale(),ErrorCodes.VALUE_NOTFOUND,validationRule.getParams()));
                       result.setResult(TransactionResult.Result.FAILURE);
                   }else
                   {
                       model.setProperty(validationRule.getField(),value);

                   }
               }
               if (validationRule.getValidationType().equals(ValidationRule.ValidationType.FK) && fieldValue != null) {
                   String referredEntity = validationRule.getReferredEntity() ;
                   String referredField = validationRule.getReferredField() ;
                   String entityClass = ObjectFactory.getInstance().getEntityClass(referredEntity,context);
                   GenericService  genericService = ObjectFactory.getInstance().getServiceForEntity(referredEntity,context);
                   String parentEntity = context.getCurrentEntity() ;
                   context.setCurrentEntity(entityClass);
                   BusinessModel subObject = genericService.getFullData((BusinessModel) fieldValue,context);
                   context.setCurrentEntity(parentEntity);
                   if ( subObject == null )
                   {
                       result.addError(
                               getErrorforCode(context.getLocale(),ErrorCodes.VALUE_NOTFOUND,validationRule.getParams()));
                       result.setResult(TransactionResult.Result.FAILURE);
                   }else
                   {
                       model.setProperty(validationRule.getField(),subObject);

                   }

               }
       }
           return result ;
    }

    @Override
    public TransactionResult advancedValidation(BusinessModel model, BusinessContext context) throws Exception {
        MetadataEntity metadataEntity = metadataService.getMetadata(context.getCurrentEntity(),context) ;
        if (CollectionUtils.isEmpty(metadataEntity.getValidationRules()))
        {
            return new TransactionResult(TransactionResult.Result.SUCCESS);
        }
        return new TransactionResult(TransactionResult.Result.SUCCESS);
    }
}
