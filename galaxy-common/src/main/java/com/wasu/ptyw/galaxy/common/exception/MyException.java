/**
 * 
 */
package com.wasu.ptyw.galaxy.common.exception;

import java.util.ArrayList;
import java.util.List;

import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;

/**
 * 
 * @author wenguang
 * @date 2015年6月3日
 */
public class MyException extends Exception implements MyThrowable{
	private static final long serialVersionUID = -6806232760499057638L;

	private String errorCode;
	
	private ResultCode resultCode;
	
	private List<Object> runtimeParameters;
	
	public MyException(String errorMessage,Throwable cause , Object... runtimeParameters ) {
		super(errorMessage,cause);
		addRuntimeParameters(runtimeParameters);
	}
	
	public MyException(String errorMessage,Throwable cause , Object runtimeParameter) {
		super(errorMessage,cause);
		addRuntimeParameter(runtimeParameter);
	}
	
	public MyException(String errorMessage,Throwable cause) {
		super(errorMessage,cause);
	}
	
	public MyException(String errorMessage) {
		super(errorMessage,null);
	}
	
	public MyException(Throwable cause) {
		super("", cause);
	}
	
	public MyException(String errorCode , String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
	}
	
	public MyException(String errorCode , String errorMessage, Throwable cause , Object runtimeParameter ) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
		addRuntimeParameter(runtimeParameter);
	}
	
	public MyException(String errorCode , String errorMessage, Throwable cause , Object... runtimeParameters ) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
		addRuntimeParameters(runtimeParameters);
	}
	
	public MyException(ResultCode errorCode, Throwable cause, Object... runtimeParameters) {
		super(errorCode.getMessage(), cause);
		this.resultCode = errorCode;
		this.errorCode = errorCode.getCode();
		addRuntimeParameters(runtimeParameters);
	}
	
	public MyException(String errorCode , String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}
	
	public MyException(String errorCode , String errorMessage, Object runtimeParameter) {
		super(errorMessage);
		this.errorCode = errorCode;
		addRuntimeParameter(runtimeParameter);
	}
	
	public MyException(String errorCode , String errorMessage, Object... runtimeParameters) {
		super(errorMessage);
		this.errorCode = errorCode;
		addRuntimeParameters(runtimeParameters);
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	public ResultCode getResultCode() {
		return resultCode;
	}

	public List<Object> getRuntimeParameters() {
		return runtimeParameters;
	}

	public void setRuntimeParameters(List<Object> runtimeParameters) {
		this.runtimeParameters = runtimeParameters;
	}

	public void addRuntimeParameter(Object o){
		if(runtimeParameters == null){
			runtimeParameters = new ArrayList<Object>();
		}
		runtimeParameters.add(o);
	}
	
	public void addRuntimeParameters(Object... objs){
		if(runtimeParameters == null){
			runtimeParameters = new ArrayList<Object>();
		}
		for(Object o : objs){
			runtimeParameters.add(o);
		}
	}
	
	public String getErrorDetails(){
		StringBuffer sb = new StringBuffer();
		sb.append("ErrorCode:").append(errorCode).append("\n");
		sb.append("ErrorMessage:").append(this.getMessage()).append("\n");
		if(runtimeParameters != null && runtimeParameters.size() > 0){
			sb.append("RuntimeParams:").append("\n");
			for(Object o : runtimeParameters){
				sb.append(String.valueOf(o)).append("\n");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return getErrorDetails();
	}
	
	/**
	 * 转成非运行时异常
	 */
	public MyRuntimeException toRuntimeException() {
		MyRuntimeException exception = new MyRuntimeException(this.errorCode,
				super.getMessage(),this.getCause());
		exception.setStackTrace(this.getStackTrace());
		return exception;
	}
}
