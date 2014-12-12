package at.ac.uniklu.crosmos.socialanalysis.notes;

public class AudioNotes extends Notes {

	//**********************************
	// Attributes specific to AudioNotes
	//**********************************
	
	private String audioFilePath;
	final private Boolean isEditable = false;
	
	/** Constructor */
	public AudioNotes() {
		super();
		setAudioFilePath("");
		setNoteType("audio");
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
