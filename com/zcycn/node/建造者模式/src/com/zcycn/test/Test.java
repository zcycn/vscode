package com.zcycn.test;

/*
 * 建造者模式：构建与表示进行分离
 * 角色：抽象建造者
 * 		具体建造者
 * 		设计者  指挥建造者生成产品
 * 		产品
 */
public class Test {
	
	public static void main(String[] args) {
		Build build = new BuildImpl();
		Designer designer = new Designer();
		Room room = designer.build(build);
		System.out.println(room.getWindow());
	}
	
}
