package com.zcycn.test;

/**
 * 具体建造者
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
