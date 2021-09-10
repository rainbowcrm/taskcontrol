package com.primus.common;

import com.primus.common.model.transaction.TransactionResult;
import com.primus.generic.BusinessContext;
import com.primus.generic.BusinessModel;

public interface IValidator {


    public default TransactionResult basicValidation(BusinessModel model, BusinessContext context) throws Exception
    {
        return new TransactionResult(TransactionResult.Result.SUCCESS);
    }

    public  default  TransactionResult advancedValidation(BusinessModel model, BusinessContext context) throws Exception
    {
        return new TransactionResult(TransactionResult.Result.SUCCESS);
        
    }
}


