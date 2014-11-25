package at.ac.uniklu.crosmos.socialanalysis;

public class VideoNotes extends Notes {

	//--------------------------------
	//Attributes specific to VideoNotes
	//--------------------------------
	
	private String videoFilePath;
	final private Boolean isEditable = false;
	
	public VideoNotes() {
		super();
		setVideoFilePath("");
	}
	
	public String getVideoFilePath() {
		return videoFilePath;
	}
	
	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}
	
	public Boolean isEditable() {
		return isEditable;
	}

}
