package com.zcycn.test;

/**
 * ����Ĵ�����
 * @author zhuch
 *
 */
public class Handler3 extends Handler{

	public void handler(AbstractRequest request) {
		// TODO Auto-generated method stub
		System.out.println("===handle3" + request.getContent());
	}

	public int getHandler() {
		// TODO Auto-generated method stub
		return 3;
	}

}
