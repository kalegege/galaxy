/**
 * 
 */
package com.wasu.ptyw.galaxy.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wenguang
 * @date 2015年6月3日
 */
public class MyRuntimeException extends RuntimeException implements MyThrowable{
	private static final long serialVersionUID = -5052234143018555702L;

	private String errorCode;
	
	private List<Object> runtimeParameters;
	
	public MyRuntimeException(String errorCode , String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
	}
	
	public MyRuntimeException(String errorCode , String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public MyRuntimeException(String errorCode , String errorMessage, Object... runtimeParameters) {
		super(errorMessage);
		this.errorCode = errorCode;
		addRuntimeParameters(runtimeParameters);
	}

	public MyRuntimeException(String errorCode , String errorMessage, Object runtimeParameter) {
		super(errorMessage);
		this.errorCode = errorCode;
		addRuntimeParameter(runtimeParameter);
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	/**
	 * 转成非运行时异常
	 * @return
	 */
	public MyException toCheckedException() {
		MyException exception = new MyException(this.errorCode,
				super.getMessage(),this.getCause());
		exception.setStackTrace(this.getStackTrace());
		return exception;
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
				sb.append(o).append("\n");
			}
		}
		return sb.toString();
	}
}
