package at.ac.uniklu.crosmos.socialanalysis.gui;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.provider.Settings;
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
import at.ac.uniklu.crosmos.socialanalysis.R;
import at.ac.uniklu.crosmos.socialanalysis.notes.AudioNotes;
import at.ac.uniklu.crosmos.socialanalysis.notes.Notes;
import at.ac.uniklu.crosmos.socialanalysis.notes.TextNotes;
import at.ac.uniklu.crosmos.socialanalysis.notes.VideoNotes;

/** Main activity for the SocialAnalysis app.
 *  The only activity, which is defined and is present in the SocialAnalysis app.
 *  The activity where the user can take notes and send them to the server.
 *  
 *  @author Arda Akcay <ardaakcay@gmail.com>
 *  @version 1.0
 */
public class MainActivity extends Activity {
	
	//**********************
	// GUI-related variables
	//**********************
	private Button buttonSend;
	private Spinner spinner;
	private EditText editTextBottom;
	private ImageView imageView;
	private ListView listView;
	
	//***************************
	// Notes-related variables
	//***************************
	private NotesListAdapter nAdapter;
	private ArrayAdapter<String> spAdapter;
	private ArrayList<Notes> notesList;
	
	//***************************
	// Location-related variables
	//***************************
	private LocationManager locationManager;
	private LocationListener locationListener;
	private String locationProvider;
	private double latitude = 0.0;
	private double longitude = 0.0;
	
	//**************************************************
	// Other global variables, which are specific to GUI
	//**************************************************
	private String[] noteType = {"Type>","Audio>","Video>"};
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
		
		// The list of notes, which are kept in the ListView
		nAdapter = new NotesListAdapter(this);
		nAdapter.setData(notesList);
		nAdapter.notifyDataSetChanged();
		listView.setAdapter(nAdapter);
		
	    // Spinner selection box for selection of the type of notes
		spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, noteType);
	    spinner.setAdapter(spAdapter);
    	
	    // Location services registration and obtaining the last known location
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    locationProvider = locationManager.getBestProvider(new Criteria(), false);
	    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
	    
	    // Toggling the listeners needed for the main activity
    	toggleListeners();
    	
    	// If last location is known, then update the location information with that
    	// in order to save some time while obtaining the current location information
    	if (lastKnownLocation != null) {
    	      locationListener.onLocationChanged(lastKnownLocation);
    	} 

	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    // Every time that the app is activated, check if the location service is enabled
	    // If not, show the AlertDialog to drag the user.
	    checkIfLocationServiceIsActivated();
	    
	    // Request location updates from each provider, which is available in every 5 seconds and 1 meter
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, locationListener);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
	    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 1, locationListener);
	    
	    // EditText field at the bottom of activity, which has no focus in the beginning
 		editTextBottom.setCursorVisible(false);
 		editTextBottom.setFocusable(false);
 	    editTextBottom.setFocusableInTouchMode(false);
 	    
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// Stop getting location updates when the user has no focus on the app
		locationManager.removeUpdates(locationListener);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Stop getting location updates when the app finally is destroyed
		locationManager.removeUpdates(locationListener);
		
		// Destroy the ArrayList of Notes
		notesList.clear();
	}


	@Override
	protected void onPause() {
	    super.onPause();
	    
	    // Stop getting location updates when the user has no focus on the app
	    locationManager.removeUpdates(locationListener);
	}
	
	/** Add the related note to the server */
	private void addNoteToServer() {
		
	}
	
	/** Remove the related note from the server */
	private void removeNoteFromServer() {
		
	}
	
	/** Enabling all the listeners needed for the main activity.
	 *  @return void
	 **/
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
		        else {
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
            		newTxtNote.setLongitude(longitude);
            		newTxtNote.setLatitude(latitude);
            		notesList.add(newTxtNote);
            		
            		editTextBottom.setText("");
                	editTextBottom.setCursorVisible(false);
                	editTextBottom.setFocusable(false);
                	editTextBottom.setFocusableInTouchMode(false);
            	}
            	
            	else if(positionOfSpinner == 1) {
            		AudioNotes newAudioNote = new AudioNotes();
            		notesList.add(newAudioNote);
            	}
            	
            	else if(positionOfSpinner == 2) {
            		VideoNotes newVideoNote = new VideoNotes();
            		notesList.add(newVideoNote);
            	}
            
            	listView.setFocusable(false);
            	listView.setFocusableInTouchMode(false);
            	
            	nAdapter.notifyDataSetChanged();
            	addNoteToServer();
            }
            
        });
		
		// When EditText at the bottom has been touched, give the focus on it.
		editTextBottom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextBottom.setCursorVisible(true);
				editTextBottom.setFocusable(true);
				editTextBottom.setFocusableInTouchMode(true);
				
			}
	    });
		
		// Listens when an item from spinner gets selected
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {   
			  @Override
		      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				  	positionOfSpinner = arg2;
				  	
		            if(positionOfSpinner == 0) {
	            		editTextBottom.setCursorVisible(true);
		            	editTextBottom.setFocusable(true);
		            	editTextBottom.setFocusableInTouchMode(true);
		            	
		            	editTextBottom.setVisibility(View.VISIBLE);
		            	imageView.setVisibility(View.INVISIBLE);
		            	
		            	showAsToast("Type mode: ON");
		            }
		            	
		            if(positionOfSpinner == 1 || positionOfSpinner == 2) {
		            	editTextBottom.setCursorVisible(false);
		            	editTextBottom.setFocusable(false);
		            	editTextBottom.setFocusableInTouchMode(false);
		            	
		            	editTextBottom.setVisibility(View.INVISIBLE);
		            	imageView.setVisibility(View.VISIBLE);
		            	
		            	showAsToast("Recording mode: ON");
		            }
		            	
		            editTextBottom.setText("");
		      }
			  
		      @Override
		      public void onNothingSelected(AdapterView<?> arg0) {
		      }
		});
		
		// Listener for location updates
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		    	latitude = location.getLatitude();
		    	longitude = location.getLongitude();
		    	showAsToast("Longitude: "+longitude+"\nLatitude: "+latitude);
		    }
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};
		
	}
	
	/** Checking if the location services are enabled or not. 
	 *  If no, then drag the user the location settings menu.
	 *  @return void
	 **/
	private void checkIfLocationServiceIsActivated() {
		
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("Warning!");	 
			alertDialogBuilder.setMessage("In order to use our services, we kindly ask you to enable location services. " +
					"You will be re-directed to Location Settings of your smartphone, when you click OK below. You cannot use our services " +
					"without activating location services of your smartphone.");
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
			});				
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}
	
	/** Hide the soft keyboard from the activity 
	 *  @param edtTxt EditText view where the virtual keyboard is supposed to be hidden.
	 *  @return void
	 **/
	private void hideSoftKeyboard(EditText edtTxt) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtTxt.getWindowToken(), 0);
		
	}
	
	/** Show the soft keyboard in the activity
	 *  @param edtTxt EditText view where the virtual keyboard is supposed to be exposed.
	 *  @return void
	 **/
	private void showSoftKeyboard(EditText edtTxt) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
	}
	
	/** Show the related text as Toast to MainActivity 
	 *  @param message The message that will be shown in the Toast view
	 *  @return void
	 **/
	private void showAsToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
