package com.huaijv.forkids4teacher.model;

public class TipsItem {
	
	private String title;
	private String time;
	private String content;
	
	public TipsItem() {}
	
	public TipsItem ( String title,String time,String content){
		this.title=title;
		this.time=time;
		this.content=content;
	}
	public String getTitle(){
		return this.title;
	}
	
	public String getTime(){
		return this.time;
	}
	public String getContent(){
		return this.content;
	}

}
