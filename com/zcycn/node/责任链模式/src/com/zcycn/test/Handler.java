package com.zcycn.test;

/**
 * ������
 * @author zhuch
 *
 */
public abstract class Handler {
	/**
	 * �����¸������ߵ�����
	 */
	public Handler nextHandler;
	
	public void handRequest(AbstractRequest request){
		if(request.getRequestLevel()==getHandler()){
			handler(request);
		}else if(nextHandler != null){
			nextHandler.handRequest(request);
		}else{
			System.out.println("===û�ж�����");
		}
	}

	/**
	 * ����Ĵ�����ʵ��
	 */
	public abstract void handler(AbstractRequest request);
	
	/**
	 * �ܹ�����ļ���
	 * @return
	 */
	public abstract int getHandler();
	
}
