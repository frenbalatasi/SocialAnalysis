package at.ac.uniklu.crosmos.socialanalysis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
		setSpinnerView();
		
		
		// ClickListener for Remove Button
		nAdapter.setRemoveButtonClickListener(new NotesListAdapter.OnRemoveButtonClickListener() {			
			@Override
			public void onRemoveButtonClick(int position) {
				nAdapter.deleteData(position);
				nAdapter.notifyDataSetChanged();
				removeNoteFromServer();
			}
		});
		
		nAdapter.setTextViewClickListener(new NotesListAdapter.OnTextViewClickListener() {			
			@Override
			public void onTextViewClick(int position) {
				showAsToast("Panpa klikledin lan! :D");
			}
		});
		
		// ClickListener for Send Button
		buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	if(positionOfSpinner == 0) {
            		TextNotes newTxtNote = new TextNotes();
            		newTxtNote.setText(editTextBottom.getText().toString());
            		nAdapter.addData(newTxtNote);
            	}
            	
            	else if(positionOfSpinner == 1) {
            		AudioNotes newAudioNote = new AudioNotes();
            		nAdapter.addData(newAudioNote);
            	}
            	
            	else if(positionOfSpinner == 2) {
            		VideoNotes newVideoNote = new VideoNotes();
            		nAdapter.addData(newVideoNote);
            	}
            	
            	nAdapter.notifyDataSetChanged();
            	addNoteToServer();
            }
        });

	}
	
	/** Add the related note to the server */
	private void addNoteToServer() {
		
	}
	
	/** Remove the related note from the server */
	private void removeNoteFromServer() {
		
	}
	
	/** Set the SpinnerView for the right bottom of the MainActivity */
	private void setSpinnerView() {
		spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, noteType);
	    spinner.setAdapter(spAdapter);
	    spinner.setOnItemSelectedListener(
              new AdapterView.OnItemSelectedListener() {
                  
            	  @Override
                  public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		                positionOfSpinner = spinner.getSelectedItemPosition();
		                
		                if(positionOfSpinner == 0) {
		                	editTextBottom.setVisibility(View.VISIBLE);
		                	imageView.setVisibility(View.INVISIBLE);
		                }
		                	
		                if(positionOfSpinner == 1 || positionOfSpinner == 2) {
		                	editTextBottom.setVisibility(View.INVISIBLE);
		                	editTextBottom.setText("");
		                	imageView.setVisibility(View.VISIBLE);
		                }
		                	
		                showAsToast("You have selected "+noteType[+positionOfSpinner]);
                  }
            	  
                  @Override
                  public void onNothingSelected(AdapterView<?> arg0) {
                      // TODO Auto-generated method stub
                  }
				
              }
	    );
	}
	
	/** Show the related text as Toast to MainActivity */
	private void showAsToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
