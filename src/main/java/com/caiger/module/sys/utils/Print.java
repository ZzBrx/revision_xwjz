package com.caiger.module.sys.utils;

public class Print {
	public static void main(String[] args) {
		 Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				
				
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
	}
}