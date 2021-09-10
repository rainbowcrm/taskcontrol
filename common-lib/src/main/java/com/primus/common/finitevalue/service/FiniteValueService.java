package com.primus.common.finitevalue.service;

import com.primus.common.finitevalue.dao.FiniteValueDAO;
import com.primus.common.finitevalue.model.FiniteValue;
import com.primus.generic.BusinessContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FiniteValueService {

    @Autowired
    FiniteValueDAO finiteValueDAO ;


    public List<FiniteValue> getAllFVbyGroup(BusinessContext context, String groupCode)
    {
        return  finiteValueDAO.getAllFVbyGroup(groupCode);
    }

    public FiniteValue getFiniteValue(BusinessContext context,FiniteValue value)
    {
        if (StringUtils.isNotEmpty(value.getCode()))
        {
           return  finiteValueDAO.getByCode(value.getCode()) ;
        }
        if( StringUtils.isNotEmpty( value.getDescription()))
        {
            return finiteValueDAO.getFVByDescription(value.getGroup().getGroupCode() ,value.getDescription() );
        }
        return null;

    }



}
