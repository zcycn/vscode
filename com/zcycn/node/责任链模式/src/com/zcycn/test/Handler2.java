package com.zcycn.test;

/**
 * 具体的处理者
 * @author zhuch
 *
 */
public class Handler2 extends Handler{

	public void handler(AbstractRequest request) {
		// TODO Auto-generated method stub
		System.out.println("===handle2" + request.getContent());
	}

	public int getHandler() {
		// TODO Auto-generated method stub
		return 2;
	}

}
