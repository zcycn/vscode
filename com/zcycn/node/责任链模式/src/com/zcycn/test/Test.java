package com.zcycn.test;

public class Test {
	
	public static void main(String[] args) {
		Request1 r1 = new Request1();
		Handler1 h1 = new Handler1();
		Handler1 h2 = new Handler1();
		Handler1 h3 = new Handler1();
		h1.nextHandler = h2;
		h2.nextHandler = h3;
		h1.handRequest(r1);
	}
	
}
