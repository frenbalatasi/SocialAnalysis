package at.ac.uniklu.crosmos.socialanalysis.notes;

import com.strongloop.android.loopback.Model;

/** Notes class, which is the parent class to be the reference to
 *  its child classes: TextNotes, VideoNotes, AudioNotes. It's an abstract
 *  class where it holds the common attributes of the notes such as location
 *  information, timestamp, UID and the type of note.
 *  
 *  @author Arda Akcay <ardaakcay@gmail.com>
 *  @version 1.0
 */

public class Notes extends Model {
	
	//*********************
	// Attributes of Notes
	//*********************
	private double longitude;
	private double latitude;
	private long timestamp;
	private String deviceID;
	private String noteType;
	
	/** Constructor */
	public Notes(){
		setLongitude(0);
		setLatitude(0);
		setTimestamp(0);
		setDeviceID("");
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	protected void setNoteType(String noteType)
	{
		this.noteType = noteType;
	}
	
	public String getNoteType(){
		return noteType;
	}
	
	public static String[] typesOfNotes() {
		final String[] typesOfNotes = {"Type>"};
		return typesOfNotes;
	}
}
