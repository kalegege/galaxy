/**
 * 
 */
package com.wasu.ptyw.galaxy.common.dataobject;

/**
 * AO返回的结果类
 * 
 * @author wenguang
 * 
 */
public class Result<V> {

	/**
	 * 执行成功与否
	 */
	protected boolean success = false;

	/**
	 * 消息编码
	 */
	private String code;

	/**
	 * 消息内容
	 */
	private String message;

	/**
	 * 返回的实体类
	 */
	private V value;

	public Result() {
		this(false);
	}

	public Result(boolean success) {
		this.success = success;
	}

	public Result(boolean success, V value) {
		this.success = success;
		this.value = value;
	}

	public Result<V> setMessage(String code, String message) {
		this.code = code;
		this.message = message;
		return this;
	}

	public Result<V> setErrorMessage(String code, String message) {
		this.code = code;
		this.message = message;
		this.setSuccess(false);
		return this;
	}

	public Result<V> setMessage(String message) {
		this.message = message;
		return this;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
		this.success = true;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 转换成字符串的表示。
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Result {\n");
		buffer.append("    success    = ").append(success).append(",\n");
		if (!success) {
			buffer.append("    code     = ").append(getCode());
			buffer.append("    message     = ").append(getMessage());
		}
		return buffer.toString();
	}
}
