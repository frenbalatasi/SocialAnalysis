package at.ac.uniklu.crosmos.socialanalysis.notes;

public class VideoNotes extends Notes {

	//**********************************
	// Attributes specific to VideoNotes
	//**********************************
	
	private String videoFilePath;
	final private Boolean isEditable = false;
	
	/** Constructor */
	public VideoNotes() {
		super();
		setVideoFilePath("");
		setNoteType("video");
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
