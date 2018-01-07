package com.zcycn.test;

/**
 * 处理者
 * @author zhuch
 *
 */
public abstract class Handler {
	/**
	 * 持有下个处理者的引用
	 */
	public Handler nextHandler;
	
	public void handRequest(AbstractRequest request){
		if(request.getRequestLevel()==getHandler()){
			handler(request);
		}else if(nextHandler != null){
			nextHandler.handRequest(request);
		}else{
			System.out.println("===没有对象处理");
		}
	}

	/**
	 * 具体的处理者实现
	 */
	public abstract void handler(AbstractRequest request);
	
	/**
	 * 能够处理的级别
	 * @return
	 */
	public abstract int getHandler();
	
}
