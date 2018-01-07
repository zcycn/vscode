package com.zcycn.test;

/**
 * 处理对象
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
