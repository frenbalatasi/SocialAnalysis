package at.ac.uniklu.crosmos.socialanalysis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Spinner spinner;
	private EditText editTextBottom;
	private ImageView imageView;
	private ListView listView;
	private NotesListAdapter nAdapter;
	private ArrayAdapter<String> spAdapter;
	private ArrayList<Notes> notesList;
	private Button buttonSend;
	
	
	private String[] noteType = {
	      "Type>",
	      "Audio>",
	      "Video>",
	};
	
	private int positionOfSpinner = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Instantiation of GUI elements
		spinner = (Spinner)findViewById(R.id.spinnerBottom);
		editTextBottom = (EditText)findViewById(R.id.editTextBottom);
		imageView = (ImageView)findViewById(R.id.imageBottom);
		listView = (ListView)findViewById(R.id.listViewNotes);
		buttonSend = (Button)findViewById(R.id.buttonSend);
		notesList = new ArrayList<Notes>();
		
		nAdapter = new NotesListAdapter(this);
		nAdapter.setData(notesList);
		nAdapter.notifyDataSetChanged();
		listView.setAdapter(nAdapter);
		
		spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, noteType);
	    spinner.setAdapter(spAdapter);
		
		editTextBottom.setFocusable(false);
    	editTextBottom.setFocusableInTouchMode(false);
    	
    	toggleListeners();

	}
	
	/** Add the related note to the server */
	private void addNoteToServer() {
		
	}
	
	/** Remove the related note from the server */
	private void removeNoteFromServer() {
		
	}
	
	/** Enabling all the listeners needed */
	private void toggleListeners() {
		
		// ClickListener for Remove Button
		nAdapter.setRemoveButtonClickListener(new NotesListAdapter.OnRemoveButtonClickListener() {			
			@Override
			public void onRemoveButtonClick(int position) {
				notesList.remove(position);
				nAdapter.notifyDataSetChanged();
				removeNoteFromServer();
			}
		});
		
		// ClickListener for text part of the note
		nAdapter.setTextViewClickListener(new NotesListAdapter.OnTextViewClickListener() {			
			@Override
			public void onTextViewClick(int position) {
				showAsToast("Position of note: "+(++position));
			}
		});
		
		// ClickListener for Play Button
		nAdapter.setImageViewClickListener(new NotesListAdapter.OnImageViewClickListener() {			
			@Override
			public void onImageViewClick(int position) {
				showAsToast("Position of note: "+(++position));
			}
		});
		
		// Show the keyboard when EditText at the bottom has a focus on it
		editTextBottom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus) {
		        	showSoftKeyboard(editTextBottom);
		        }
		        else if(!hasFocus) {
		        	hideSoftKeyboard(editTextBottom);
		        }
		    }
		});
		
		// ClickListener for Send Button
		buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	if(positionOfSpinner == 0) {
            		TextNotes newTxtNote = new TextNotes();
            		newTxtNote.setText(editTextBottom.getText().toString());
            		notesList.add(newTxtNote);
            	}
            	
            	else if(positionOfSpinner == 1) {
            		AudioNotes newAudioNote = new AudioNotes();
            		nAdapter.addData(newAudioNote);
            	}
            	
            	else if(positionOfSpinner == 2) {
            		VideoNotes newVideoNote = new VideoNotes();
            		nAdapter.addData(newVideoNote);
            	}
            	
            	editTextBottom.setFocusable(false);
            	editTextBottom.setFocusableInTouchMode(false);
            	nAdapter.notifyDataSetChanged();
            	addNoteToServer();
            }
        });
		
		// When EditText at the bottom has been touched, give the focus on it.
		editTextBottom.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            v.setFocusable(true);
	            v.setFocusableInTouchMode(true);
	            return false;
	        }
	    });
		
		// Listens when an item from spinner gets selected
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {   
			  @Override
		      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		            positionOfSpinner = arg2;
		            
		            if(positionOfSpinner == 0) {
		            	editTextBottom.setFocusable(true);
		            	editTextBottom.setFocusableInTouchMode(true);
		            	editTextBottom.setVisibility(View.VISIBLE);
		            	imageView.setVisibility(View.INVISIBLE);
		            }
		            	
		            if(positionOfSpinner == 1 || positionOfSpinner == 2) {
		            	editTextBottom.setFocusable(false);
		            	editTextBottom.setFocusableInTouchMode(false);
		            	editTextBottom.setVisibility(View.INVISIBLE);
		            	imageView.setVisibility(View.VISIBLE);
		            }
		            	
		            showAsToast("You have selected "+noteType[+positionOfSpinner]);
		            editTextBottom.setText("");
		      }
			  
		      @Override
		      public void onNothingSelected(AdapterView<?> arg0) {
		      }
		});
		
	}
	
	/** Hide the soft keyboard from the activity */
	private void hideSoftKeyboard(EditText edtTxt) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtTxt.getWindowToken(), 0);
		
	}
	
	/** Show the soft keyboard in the activity */
	private void showSoftKeyboard(EditText edtTxt) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	/** Show the related text as Toast to MainActivity */
	private void showAsToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
