package at.ac.uniklu.crosmos.socialanalysis;

public class AudioNotes extends Notes {

	//--------------------------------
	//Attributes specific to AudioNotes
	//--------------------------------
	
	private String audioFilePath;
	final private Boolean isEditable = false;
	
	public AudioNotes() {
		super();
		setAudioFilePath("");
	}

	public String getAudioFilePath() {
		return audioFilePath;
	}

	public void setAudioFilePath(String audioFilePath) {
		this.audioFilePath = audioFilePath;
	}
	
	public Boolean isEditable() {
		return isEditable;
	}

}
