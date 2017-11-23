package org.gsnaker.engine.model;

/**
 * 工作元素
 * @author hoocen
 * @since 1.0
 */
public abstract class WorkModel extends NodeModel{

	private static final long serialVersionUID = -1950053160042513356L;
	/**
	 * form
	 */
	private String form;
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
}
