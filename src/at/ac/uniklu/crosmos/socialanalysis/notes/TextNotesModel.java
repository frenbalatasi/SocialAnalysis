package at.ac.uniklu.crosmos.socialanalysis.notes;

import com.strongloop.android.loopback.Model;

public class TextNotesModel extends Model {

	//*********************************
	// Attributes specific to TextNotes
	//*********************************
	
	private String text;
	
	/** Constructor */
	public TextNotesModel() {
		super();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
