package org.gsnaker.engine;
/**
 * 框架抛出的所有异常都是此类（Unchecked Exception）
 * @author hoocen
 * @since
 */
public class SnakerException extends RuntimeException{

	private static final long serialVersionUID = 7119356069269870234L;

	public SnakerException() {
		super();
	}

	public SnakerException(String msg, Throwable cause) {
		super(msg);
		super.initCause(cause);
	}

	public SnakerException(String msg) {
		super(msg);
	}

	public SnakerException(Throwable cause) {
		super();
		super.initCause(cause);
	}
}
