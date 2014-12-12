package at.ac.uniklu.crosmos.socialanalysis.notes;

import java.util.Date;

/** Notes class, which is the parent class to be the reference to
 *  its child classes: TextNotes, VideoNotes, AudioNotes. It's an abstract
 *  class where it holds the common attributes of the notes such as location
 *  information, timestamp, UID and the type of note.
 *  
 *  @author Arda Akcay <ardaakcay@gmail.com>
 *  @version 1.0
 */

public abstract class Notes {
	
	//*********************
	// Attributes of Notes
	//*********************
	private String UID;
	private double longitude;
	private double latitude;
	private Date timestamp;
	private String noteType;

	
	/** Constructor */
	public Notes(){
		setUID("");
		setLongitude(0);
		setLatitude(0);
		setTimestamp(null);
	}
	
	//*********************
	// Setters and Getters
	//*********************

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
