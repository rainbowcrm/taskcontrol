package com.primus.common;

import com.primus.common.finitevalue.service.FiniteValueService;
import com.primus.generic.BusinessContext;
import com.primus.generic.GenericService;
import com.primus.generic.GenericValidator;
import com.primus.metadata.model.MetadataEntity;
import com.primus.metadata.service.MetadataService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ObjectFactory implements ApplicationContextAware {

    @Autowired
    GenericService genericService ;



    @Autowired
    GenericValidator genericValidator ;

    @Autowired
    FiniteValueService finiteValueService ;

    @Autowired
    MetadataService metadataService;

    public String getEntityClass(String entityName, BusinessContext context)
    {
        MetadataEntity metadataEntity = metadataService.getMetadata(entityName,context);
        return metadataEntity.getClassName() ;
    }

    public GenericService getServiceForEntity(String entity,BusinessContext context)
    {
            return  genericService;
    }



    public FiniteValueService getFiniteValueServiceInstance(BusinessContext context)
    {
        return finiteValueService ;
    }

    public IService getServiceInstance(String obj , BusinessContext context)
    {
        if("genericService".equalsIgnoreCase(obj))
            return genericService ;



        return genericService;
    }

    public IValidator getValidatorInstance(String obj , BusinessContext context)
    {

        if("genericValidator".equalsIgnoreCase(obj))
            return genericValidator ;

        return genericValidator;
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ObjectFactory.applicationContext = applicationContext;

    }
    public static ObjectFactory getInstance() {
        return applicationContext.getBean(ObjectFactory.class);
    }
}
