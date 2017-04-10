/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.persist;

/**
 * @author wenguang
 * 
 */
public class DAOException extends Exception {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7738623580577342841L;

	public DAOException() {
		super();
	}

	public DAOException(String msg) {
		super(msg);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

	public DAOException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
