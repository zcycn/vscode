package com.zcycn.test;

/**
 * �����
 * @author zhuch
 *
 */
public class Designer {
	public Room build(Build build){
		build.makeWindow();
		return build.build();
	}
}
