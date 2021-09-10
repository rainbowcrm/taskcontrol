package com.primus.metadata.service;

import com.primus.common.IService;
import com.primus.common.finitevalue.model.FiniteValue;
import com.primus.common.finitevalue.service.FiniteValueService;
import com.primus.generic.BusinessContext;
import com.primus.generic.BusinessModel;
import com.primus.generic.GenericService;
import com.primus.common.ObjectFactory;
import com.primus.metadata.dao.MetadataDAO;
import com.primus.metadata.model.Field;
import com.primus.metadata.model.MetadataEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MetadataService {

    Map<String,MetadataEntity> cachedMetadata = new HashMap<>();


    @Autowired
    MetadataDAO metadataDAO;


    private void populateDropDownContents(Field field,BusinessContext context)
    {
         if (field.getPopulator().contains("FiniteValues"))
         {
             FiniteValueService finiteValueService = (FiniteValueService) ObjectFactory.getInstance().getFiniteValueServiceInstance(context);
             String fvGroup = field.getPopulator().substring(13,field.getPopulator().length());
             List<FiniteValue> fvsByGroup = finiteValueService.getAllFVbyGroup(context,fvGroup);
             List<Map<String,String>> dropDownValues = new ArrayList<>();
             fvsByGroup.forEach( fv -> {
                 Map<String,String> mp = new HashMap();
                 mp.put("Code",fv.getDescription());
                 mp.put("Value",fv.getDescription());
                 dropDownValues.add(mp);
             });
             field.setDropDownValues(dropDownValues);
         } else {

             String parts[] = field.getPopulator().split("[.]");
             GenericService service = ObjectFactory.getInstance().getServiceForEntity(parts[0],context);
             List<BusinessModel> entities =  service.listData(parts[0],0,9999,"",parts[1]);
             if (!CollectionUtils.isEmpty(entities))
             {
                     List<Map<String,String>> dropDownValues = new ArrayList<>();
                     entities.forEach(entity -> {
                         try {
                                String retValue = (String )entity.getProperty(parts[1]);
                                 Map<String,String> mp = new HashMap();
                                 mp.put("Code",retValue);
                                 mp.put("Value",retValue);
                                 dropDownValues.add(mp);

                         }catch (Exception ex) {

                         }
                    });
                 field.setDropDownValues(dropDownValues);
              }
         }
    }


    public Map getPage(String entity, Integer from, Integer to,Map filter,BusinessContext context)
    {

        int fro = from!=null?from.intValue():0;
        int toI= to!=null?to.intValue():12 ;

        IService genericService =  ObjectFactory.getInstance().getServiceInstance("genericService",context) ;
        MetadataEntity metadataEntity =  null;
        if (cachedMetadata.get(entity) == null)
        {
            metadataEntity = metadataDAO.getEntityDetails(entity);
            cachedMetadata.put(entity,metadataEntity);
        }
        else
        {
            metadataEntity = cachedMetadata.get(entity);
        }

        StringBuffer whereCondition = new StringBuffer("");

        Map ans = new LinkedHashMap();
        List<Map> fields = new ArrayList<>();
        List<Map> data = new ArrayList<>();
        if (metadataEntity != null ) {
            metadataEntity.getFields().forEach( field ->{
                if (filter != null && filter.containsKey(field.getJsonTag()))
                {
                    if(!whereCondition.toString().equals(""))
                        whereCondition.append(" and ");
                    else
                        whereCondition.append(" where ");
                    whereCondition.append(field.getJsonTag() + "='" + filter.get(field.getJsonTag()) + "'");
                }
                if (field.getDisplayControl().equalsIgnoreCase("DropDown"))  {
                    populateDropDownContents(field,context);
                    System.out.println(field);
                }
                fields.add(field.toMap(context));
            });

            ans.put("fields", fields);
        }


        List <BusinessModel> entries  = genericService.listData(entity,fro,toI,whereCondition.toString(),null);
        for ( BusinessModel entry : entries )
        {
            data.add(entry.toMap(context));
        }
        ans.put("data",data);
        long totalRec = genericService.getTotalRecordCount(entity,"");
        ans.put("totalRecords",totalRec);
        return ans;


    }

    public Map getListContent(String entity, Integer from, Integer to,BusinessContext context)
    {

        int fro = from!=null?from.intValue():0;
        int toI= to!=null?to.intValue():12 ;
        IService genericService = ObjectFactory.getInstance().getServiceInstance("genericService",context) ;
        Map ans = new LinkedHashMap();
        List<Map> data = new ArrayList<>();
        List <BusinessModel> entries  = genericService.listData(entity,fro,toI,null,null);
        for ( BusinessModel entry : entries )
        {
            data.add(entry.toMap(context));
        }
        ans.put("data",data);
        return ans;


    }

    public  MetadataEntity getMetadata(String entity,BusinessContext context) {
        if (cachedMetadata.get(entity) == null)
        {
            MetadataEntity metadataEntity = metadataDAO.getEntityDetails(entity);
            cachedMetadata.put(entity,metadataEntity);
            return  metadataEntity ;
        }
        else
        {
            return cachedMetadata.get(entity);
        }
    }
}
