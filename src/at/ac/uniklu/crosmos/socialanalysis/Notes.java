package at.ac.uniklu.crosmos.socialanalysis;

import java.util.Date;

public class Notes {
	
	//*********************
	// Attributes of Notes
	//*********************
	private String UID;
	private int location;
	private Date timestamp;
	private String noteType;

	
	/** Constructor */
	public Notes(){
		setUID("");
		setLocation(0);
		setTimestamp(null);
	}
	
	//*********************
	// Setters and Getters
	//*********************

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	protected void setNoteType(String noteType)
	{
		this.noteType = noteType;
	}
	
	public String getNoteType(){
		return noteType;
	}

}
