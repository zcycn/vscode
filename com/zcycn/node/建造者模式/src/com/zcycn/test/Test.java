package com.zcycn.test;

/*
 * ������ģʽ���������ʾ���з���
 * ��ɫ����������
 * 		���彨����
 * 		�����  ָ�ӽ��������ɲ�Ʒ
 * 		��Ʒ
 */
public class Test {
	
	public static void main(String[] args) {
		Build build = new BuildImpl();
		Designer designer = new Designer();
		Room room = designer.build(build);
		System.out.println(room.getWindow());
	}
	
}
