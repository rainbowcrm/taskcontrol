package com.primus.common;

import com.primus.common.model.transaction.TransactionResult;
import com.primus.generic.BusinessContext;
import com.primus.generic.BusinessModel;
import com.primus.generic.GenericDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IService {

    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionResult update(BusinessModel model, BusinessContext context);

    public GenericDAO getDAO();

    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionResult create(BusinessModel model, BusinessContext context);

    public long getTotalRecordCount(String entity, String whereCondition);

    public List<BusinessModel> listData(String entity, int from, int to, String whereCondition, String orderby);

    public BusinessModel fetchData(String entity, String pk,BusinessContext context);

    public BusinessModel getByBusinessKey(BusinessModel object, BusinessContext context);

    public BusinessModel getFullData(BusinessModel model,BusinessContext context);


    }
