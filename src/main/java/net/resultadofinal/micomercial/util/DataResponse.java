package net.resultadofinal.micomercial.util;

public class DataResponse {
	private boolean status;
	private String msg;
	private Object data;
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public DataResponse(boolean status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}
	public DataResponse(boolean status,Object data, String msg) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
}
