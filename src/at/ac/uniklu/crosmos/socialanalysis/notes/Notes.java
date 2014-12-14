package at.ac.uniklu.crosmos.socialanalysis.notes;

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
	private long timestamp;
	private String noteType;

	
	/** Constructor */
	public Notes(){
		setUID("");
		setLongitude(0);
		setLatitude(0);
		setTimestamp(0);
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
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
