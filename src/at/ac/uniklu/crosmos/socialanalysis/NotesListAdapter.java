package at.ac.uniklu.crosmos.socialanalysis;

import java.util.ArrayList;
import java.util.List;

import at.ac.uniklu.crosmos.socialanalysis.R;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NotesListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;	
	private ArrayList<Notes> notesList;
	private OnRemoveButtonClickListener rbListener;
	private OnTextViewClickListener etListener;
	
	public NotesListAdapter(Context context) { 
        mInflater = LayoutInflater.from(context);        
    }
	
	/** Setting the list of notes for the first time */
	public void setData(ArrayList<Notes> notes) {
		notesList = notes;
	}
	
	/** Adding new notes to the ListView */
	public void addData(Notes newNote){
		notesList.add(newNote);
	}
	
	/** Deleting notes from the ListView */
	public void deleteData(int position){
		notesList.remove(position);
	}
	
	/** Setting up the ClickListener for Remove Button */
	public void setRemoveButtonClickListener(OnRemoveButtonClickListener listener) {
		rbListener = listener;
	}
	
	public void setTextViewClickListener(OnTextViewClickListener listener) {
		etListener = listener;
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
			convertView	= mInflater.inflate(R.layout.list_item_device, null);
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

		// ClickListener for Remove Button
		holder.removeButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (rbListener != null) {
					rbListener.onRemoveButtonClick(position);
				}
			}
		});
		
		holder.textNotes.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (etListener != null) {
					etListener.onTextViewClick(position);
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
	
	/** Mandatory ClickListener for Remove Button */
	public interface OnRemoveButtonClickListener {
		public abstract void onRemoveButtonClick(int position);
	}
	
	public interface OnTextViewClickListener {
		public abstract void onTextViewClick(int position);
	}
}