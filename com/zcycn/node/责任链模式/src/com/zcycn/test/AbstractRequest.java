package com.zcycn.test;

/**
 * �������
 * @author zhuch
 *
 */
public abstract class AbstractRequest {
	
	private Object object;
	
	public Object getContent(){
		return object;
	}
	
	public abstract int getRequestLevel();
	
}
