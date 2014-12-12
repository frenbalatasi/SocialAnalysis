package at.ac.uniklu.crosmos.socialanalysis.gui;

import java.util.ArrayList;

import at.ac.uniklu.crosmos.socialanalysis.R;
import at.ac.uniklu.crosmos.socialanalysis.notes.Notes;
import at.ac.uniklu.crosmos.socialanalysis.notes.TextNotes;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/** Custom adapter, which extends BaseAdapter of Android SDK
 *  in order to show the user in the custom ListView derived in the main activity.
 *  For the text-based notes, text field is shown to the user. For the other types of notes, 
 *  play button is shown to the user. With remove button provided right hand side,
 *  users can delete their notes. For the detailed information regarding the position 
 *  of the ListView, please refer to MainActivity.java
 *  
 *  @author Arda Akcay <ardaakcay@gmail.com>
 *  @version 1.0
 */

public class NotesListAdapter extends BaseAdapter{
	
	//************************************
	// GUI elements for the ListView, 
	// which is shown in the main activity 
	// when the user takes a note
	//************************************
	private LayoutInflater mInflater;	
	private ArrayList<Notes> notesList;
	private OnRemoveButtonClickListener rbListener;
	private OnTextViewClickListener etListener;
	private OnImageViewClickListener pbListener;
	
	/** Constructor of NotesListAdapter */
	public NotesListAdapter(Context context) { 
        mInflater = LayoutInflater.from(context);        
    }
	
	/** Setting the list of notes for the first time 
	 *  @param notes The ArrayList of Notes class
	 *  @return void
	 **/
	public void setData(ArrayList<Notes> notes) {
		notesList = notes;
	}
	
	/** Adding new notes to the ListView 
	 *  @param newNote Notes object, which is to be added to ListView
	 *  @return void
	 * */
	public void addData(Notes newNote){
		notesList.add(newNote);
	}
	
	/** Deleting notes from the ListView 
	 * @param position Integer value, which denotes the position of the Notes object in the ListView to be deleted
	 * @return void 
	 **/
	public void deleteData(int position){
		notesList.remove(position);
	}
	
	/** Setting up the ClickListener for the remove button 
	 * @param listener OnRemoveButtonClickListener object
	 * @return void
	 **/
	public void setRemoveButtonClickListener(OnRemoveButtonClickListener listener) {
		rbListener = listener;
	}
	
	/** Setting up the ClickListener for the text field of a text note 
	 *  @param listener OnTextViewClickListener object
	 *  @return void
	 **/
	public void setTextViewClickListener(OnTextViewClickListener listener) {
		etListener = listener;
	}
	
	/** Setting up the ClickListener for the play button 
	 *  @param listener OnImageViewClickListener object
	 *  @return void
	 **/
	public void setImageViewClickListener(OnImageViewClickListener listener) {
		pbListener = listener;
	}
	
	public int getCount() {
		return (notesList == null) ? 0 : notesList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {			
			convertView	= mInflater.inflate(R.layout.list_custom_notes, null);
			holder = new ViewHolder();
			
			holder.textNotes = (TextView) convertView.findViewById(R.id.textNotes);
			holder.playButton = (ImageView) convertView.findViewById(R.id.playButton);
			holder.removeButton= (ImageView) convertView.findViewById(R.id.buttonRemove);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// If the note that is taken is from Soft-Keyboard
		if(notesList.get(position).getNoteType().equals("type")) {
			holder.playButton.setVisibility(View.INVISIBLE);
			holder.textNotes.setVisibility(View.VISIBLE);
			
			TextNotes txtNote = (TextNotes) notesList.get(position);
			holder.textNotes.setText(txtNote.getText());
		}
		
		// If the note that is taken is from Audio or Video
		else if(notesList.get(position).getNoteType().equals("audio") || notesList.get(position).getNoteType().equals("video")) {
			holder.textNotes.setVisibility(View.INVISIBLE);
			holder.playButton.setVisibility(View.VISIBLE);
		}

		// ClickListener for the remove button
		holder.removeButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (rbListener != null) {
					rbListener.onRemoveButtonClick(position);
				}
			}
		});
		
		// ClickListener for text field of a text note
		holder.textNotes.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (etListener != null) {
					etListener.onTextViewClick(position);
				}
			}
		});
		
		// ClickListener for the play button
		holder.playButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (etListener != null) {
					pbListener.onImageViewClick(position);
				}
			}
		});
		
        return convertView;
	}

	/** GUI elements of the adapter */
	static class ViewHolder {
		TextView textNotes;
		ImageView removeButton;
		ImageView playButton;
	}
	
	/** Mandatory ClickListener for remove button for the implementation in the main activity */
	public interface OnRemoveButtonClickListener {
		public abstract void onRemoveButtonClick(int position);
	}
	
	/** Mandatory ClickListener for text field of a text note for the implementation in the main activity */
	public interface OnTextViewClickListener {
		public abstract void onTextViewClick(int position);
	}
	
	/** Mandatory ClickListener for play button for the implementation in the main activity */
	public interface OnImageViewClickListener {
		public abstract void onImageViewClick(int position);
	}
}