package com.zcycn.test;

/**
 * ���彨����
 * @author zhuch
 *
 */
public class BuildImpl implements Build{
	
	private Room room = new Room();

	public void makeWindow() {
		room.setWindow("build window");
	}

	public Room build() {
		return room;
	}

}
