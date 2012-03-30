package org.team3309.vision;

public class ImageServer{
	
	private static ImageServer instance;

	public static ImageServer getInstance(){
		if(instance == null)
			instance = new ImageServer();
		return instance;
	}
	
	private ImageServer(){
		
	}

}
