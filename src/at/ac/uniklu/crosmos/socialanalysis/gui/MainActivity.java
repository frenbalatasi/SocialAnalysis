package at.ac.uniklu.crosmos.socialanalysis.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.*;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.EditTextPreference;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
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
	private ProgressDialog progressDlg;
	
	//***************************
	// Notes-related variables
	//***************************
	private NotesListAdapter nAdapter;
	private ArrayAdapter<String> spAdapter;
	private List<Notes> notesList;
	private TextNotes newTxtNote;
	
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
	private String[] noteTypes = Notes.typesOfNotes();
	private int positionOfSpinner = 0;
	private Boolean sendingFailed;
	
	//**************************
	// Server-related variables
	//**************************
	private TextNotesRepository repository;
	private RestAdapter adapter;
	
	//*****************************************************
	// Unique ID for the device, which app is installed.
	// Since app is compatible starting from Android 4.2.2,
	// using unique ID as ANDROID_ID is not a problem.
	//*****************************************************
	private String androidID;
	
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
		progressDlg = new ProgressDialog(MainActivity.this);
		
		progressDlg.setMessage("Connecting...");
		progressDlg.setCancelable(false);
		
		// The list of notes, which are kept in the ListView
		nAdapter = new NotesListAdapter(this);
		nAdapter.setData(notesList);
		listView.setAdapter(nAdapter);
		nAdapter.notifyDataSetChanged();
		
	    // Spinner selection box for selection of the type of notes
		spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, noteTypes);
	    spinner.setAdapter(spAdapter);
    	
	    // Location services registration and obtaining the last known location
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    locationProvider = locationManager.getBestProvider(new Criteria(), false);
	    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
	    
    	// If last location is known, then update the location information with that
    	// in order to save some time while obtaining the current location information
    	if (lastKnownLocation != null) {
    	      locationListener.onLocationChanged(lastKnownLocation);
    	}
    	
    	 // Grab the RestAdapter instance.
	 	adapter = new RestAdapter(getApplicationContext(), "http://192.168.0.100:3000/api");

	    // Instantiate our repository for text notes.
	 	repository = adapter.createRepository(TextNotesRepository.class);
    	
	 	// ANDROID_ID
	 	androidID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    // Toggling the listeners needed for the main activity
    	toggleListeners();
	    
	    // Clear the elements of ListView for each time that app starts
	    notesList.clear();
	    
	    // Start to retrieve the notes from server
	    retrieveNotesFromServer();
	    
	    // Every time that the app is activated, check if the 
	    // location service is enabled. If not, show the 
	    // AlertDialog to drag the user.
	    checkIfLocationServiceIsActivated();
	    
	    // Request location updates from each provider, which is available in every 5 seconds and 1 meter
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, locationListener);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
	    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 1, locationListener);
	    
	    // EditText field at the bottom of activity, which has no focus in the beginning
 		editTextBottom.setCursorVisible(false);
 		editTextBottom.setFocusable(false);
 	    editTextBottom.setFocusableInTouchMode(false);
 	    
 	    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// Stop getting location updates when the user has no focus on the app
		locationManager.removeUpdates(locationListener);
		
		// Disable the listeners
	    disableListeners();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Stop getting location updates when the app finally is destroyed
		locationManager.removeUpdates(locationListener);
		
		// Disable the listeners
	    disableListeners();
	}
	
	@Override
	public void onBackPressed() {
		// AlertDialog before quitting the app
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.warning);	 
		alertDialogBuilder.setMessage(R.string.textQuit);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					MainActivity.super.onBackPressed();
				}
		});
		
		alertDialogBuilder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    
	    // Stop getting location updates when the user has no focus on the app
	    locationManager.removeUpdates(locationListener);
	    
	    // Disable the listeners
	    disableListeners();
	}
	
	//***************************************************************************
	//***************************************************************************
	// TO-DO
	// Settings in the upper-right corner
	//***************************************************************************
	//***************************************************************************
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//***************************************************************************
	//***************************************************************************
	// TO-DO
	// Settings in the upper-right corner
	//***************************************************************************
	//***************************************************************************

	/** Add the related note to the server */
	private void addNoteToServer() {
		
	}
	
	/** Remove the related note from the server */
	private void removeNoteFromServer() {
		
	}
	
	/** Repository class for TextNotes */
	public static class TextNotesRepository extends ModelRepository<TextNotes> {
        public TextNotesRepository() {
            super("note", TextNotes.class);
        }
    }
	
	/** Retrieve the existing notes from the server */
	private void retrieveNotesFromServer() {
		progressDlg.show();
		
		repository.findAll(new ModelRepository.FindAllCallback<TextNotes>() {
			@Override
			public void onSuccess(List<TextNotes> textNotes) {
				progressDlg.dismiss();
				notesList.addAll((ArrayList<TextNotes>) textNotes);
				nAdapter.notifyDataSetChanged();
			}
			
			@Override
            public void onError(Throwable t) {
				progressDlg.dismiss();
                showAsToast("Notes couldn't be retrieved!\nCheck your network connection...");
            }
        });
    }
	
	/** Disabling all the listeners needed for the main activity.
	 *  @return void
	 **/
	private void disableListeners() {
		
	}
	
	/** Enabling all the listeners needed for the main activity.
	 *  @return void
	 **/
	private void toggleListeners() {
		
		// ClickListener for Remove Button
		nAdapter.setRemoveButtonClickListener(new NotesListAdapter.OnRemoveButtonClickListener() {			
			@Override
			public void onRemoveButtonClick(int position) {
				listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
				
				final int pos = position;
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
				alertDialogBuilder.setTitle(R.string.warning);
				alertDialogBuilder.setMessage(getString(R.string.textDelete)+" " +((TextNotes) notesList.get(pos)).getText());
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
					@SuppressWarnings("deprecation")
					public void onClick(DialogInterface dialog,int id) {
						progressDlg.show();
						TextNotes noteToBeDeleted = (TextNotes) notesList.get(pos);
						
						noteToBeDeleted.destroy(new Model.Callback() {
						    @Override
						    public void onSuccess() {
						    	progressDlg.dismiss();
						    	notesList.remove(pos);
								nAdapter.notifyDataSetChanged();
						    }
						    @Override
						    public void onError(Throwable t) {
						    	progressDlg.dismiss();
						    	showAsToast("Deleting failed!\nCheck your network connection...");
						    }
						});
					}
				});
				
				alertDialogBuilder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
		
		// ClickListener for text part of the note
		nAdapter.setTextViewClickListener(new NotesListAdapter.OnTextViewClickListener() {			
			@Override
			public void onTextViewClick(int position) {
				TextNotes txtNote = (TextNotes) notesList.get(position);
				showAsToast(txtNote.getText());
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
		    	listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		    	
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
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
            	listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            	
            	// If TextNote is chosen
            	if(positionOfSpinner == 0) {	
            		final String txtNoteTxt = editTextBottom.getText().toString();
            		
            		if(!(txtNoteTxt.isEmpty())) {
            			progressDlg.show();
            			
            			newTxtNote = new TextNotes();
                		newTxtNote.setText(txtNoteTxt);
                		newTxtNote.setLongitude(longitude);
                		newTxtNote.setLatitude(latitude);
                		newTxtNote.setTimestamp(new Date().getTime());
                		newTxtNote.setDeviceID(androidID);
                		
                		Map<String,?> parameters = ImmutableMap.of(
                                "text", newTxtNote.getText(),
                                "latitude", newTxtNote.getLatitude(),
                                "longitude",newTxtNote.getLongitude(),
                                "timestamp",newTxtNote.getTimestamp(),
                                "deviceID", newTxtNote.getDeviceID());
                		
                		newTxtNote = repository.createModel(parameters);
                		
                		newTxtNote.save(new Model.Callback() {
                		    @Override
                		    public void onSuccess() {
                		    	progressDlg.dismiss();
                		    	notesList.add(newTxtNote);
                		    	nAdapter.notifyDataSetChanged();
                		    }
                		 
                		    @Override
                		    public void onError(Throwable t) {
                		    	progressDlg.dismiss();
                		    	editTextBottom.setText(txtNoteTxt);
                		    	showAsToast("Sending failed!\nCheck your network connection...");
                		    }
                		});
            		}
            		
            		else {
            			listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            			showAsToast("Type something to send.");
            		}
            		
            		editTextBottom.setText("");
                	editTextBottom.setCursorVisible(false);
                	editTextBottom.setFocusable(false);
                	editTextBottom.setFocusableInTouchMode(false);
            	}
            	
            	// If AudioNote is chosen
            	else if(positionOfSpinner == 1) {
            		AudioNotes newAudioNote = new AudioNotes();
            		notesList.add(newAudioNote);
            	}
            	
            	// If VideoNote is chosen
            	else if(positionOfSpinner == 2) {
            		VideoNotes newVideoNote = new VideoNotes();
            		notesList.add(newVideoNote);
            	}
            	
            	listView.setFocusable(false);
            	listView.setFocusableInTouchMode(false);
            }
            
        });
		
		// When EditText at the bottom has been touched, give the focus on it.
		editTextBottom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				
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
		    }
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};
		
	}
	
	/** Checking if the location services are enabled or not. 
	 *  If no, then drag the user to the location settings menu of the device.
	 *  @return void
	 **/
	private void checkIfLocationServiceIsActivated() {
		
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.warning);	 
			alertDialogBuilder.setMessage(R.string.textWarning);
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
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
