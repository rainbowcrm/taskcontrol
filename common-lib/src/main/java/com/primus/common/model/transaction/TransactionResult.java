package com.primus.common.model.transaction;

import com.primus.common.model.ModelObject;
import com.primus.common.model.RadsError;
import com.primus.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;



public class TransactionResult {

	public enum Result {
		SUCCESS  , FAILURE ;
	} 
	
	protected Result result ;
	protected List<RadsError> errors ;
	protected ModelObject object ;
	
	public List<RadsError> getErrors() {
		return errors;
	}

	public void setErrors(List<RadsError> errors) {
		this.errors = errors;
	}
	
	
	public void addError(RadsError err) {
		if(errors == null) 
			errors = new ArrayList<RadsError>();
		errors.add(err);
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	public ModelObject getObject() {
		return object;
	}

	public void setObject(ModelObject object) {
		this.object = object;
	}
	
	public boolean hasErrors() {
		return !Utils.isNullList(errors);
	}

	public TransactionResult(Result result, List<RadsError> errors,
			ModelObject object) {
		super();
		this.result = result;
		this.errors = errors;
		this.object = object;
	}

	public TransactionResult(Result result, List<RadsError> errors) {
		super();
		this.result = result;
		this.errors = errors;
	}

	public TransactionResult() {
		super();
		result = Result.SUCCESS;
	}

	public TransactionResult(Result result) {
		super();
		this.result = result;
	}
	
	
	


}
