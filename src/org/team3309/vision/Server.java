package org.team3309.vision;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

public class Server {
	
	private static Server instance = null;
	
	private Handler handler;
	
	public static Server getInstance(){
		return new Server();
	}
	
	private Server(){
		try {
			ServerSocket server = new ServerSocket(3309);
			handler = new Handler(server.accept());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean isReady(){
		return instance != null;
	}
	
	public void setData(DataWrapper data){
		handler.setData(data);
	}

	
	private static class Handler extends Thread{
		
		private PrintStream out;
		private DataWrapper toSend = new DataWrapper();
		
		public Handler(Socket skt){
			try {
				out = new PrintStream(skt.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			start();
		}
		
		public void run(){
			while(true){
				try {
					out.println(new Gson().toJson(toSend));
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void setData(DataWrapper data){
			toSend = data;
		}
	}
	
}
