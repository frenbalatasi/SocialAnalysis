package at.ac.uniklu.crosmos.socialanalysis.notes;

public class TextNotes extends Notes {

	//*********************************
	// Attributes specific to TextNotes
	//*********************************
	
	private String text;
	final private Boolean isEditable = true;
	
	/** Constructor */
	public TextNotes() {
		super();
		setText("");
		setNoteType("type");
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Boolean isEditable() {
		return isEditable;
	}
}
