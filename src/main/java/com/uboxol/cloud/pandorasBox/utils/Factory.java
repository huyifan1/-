package com.uboxol.cloud.pandorasBox.utils;

public class Factory {
	
	public static String getStatus(int status) {
		switch(status){
		 case 1: return "已付款";
		
		case 2:  return "未付款";
		}
		return null;
	} 
	
	public static void main(String[] args) {
		
		System.out.println(Factory.getStatus(1));
	}

}
