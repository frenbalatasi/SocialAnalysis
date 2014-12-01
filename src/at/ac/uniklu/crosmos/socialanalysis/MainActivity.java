package at.ac.uniklu.crosmos.socialanalysis;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Spinner spnr;
	private EditText editText;
	private ImageView imageView;
	
	private String[] noteType = {
	      "Type>",
	      "Audio>",
	      "Video>",
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		spnr = (Spinner)findViewById(R.id.spinnerBottom);
		editText = (EditText)findViewById(R.id.editTextBottom);
		imageView = (ImageView)findViewById(R.id.imageBottom);
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	        this, android.R.layout.simple_spinner_dropdown_item, noteType);
	    spnr.setAdapter(adapter);
	    spnr.setOnItemSelectedListener(
	              new AdapterView.OnItemSelectedListener() {
	                  
	            	  @Override
	                  public void onItemSelected(AdapterView<?> arg0, View arg1,
	                          int arg2, long arg3) {
	                    int position = spnr.getSelectedItemPosition();
	                    
	                    if(position == 0) {
	                    	editText.setVisibility(View.VISIBLE);
	                    	imageView.setVisibility(View.INVISIBLE);
	                    }
	                    	
	                    if(position == 1 || position == 2) {
	                    	editText.setVisibility(View.INVISIBLE);
	                    	editText.setText("");
	                    	imageView.setVisibility(View.VISIBLE);
	                    }
	                    	
	                    showAsToast("You have selected "+noteType[+position]);
	                      // TODO Auto-generated method stub
	                  }
	                  @Override
	                  public void onNothingSelected(AdapterView<?> arg0) {
	                      // TODO Auto-generated method stub
	                  }
					
	              }
	          );
		

	}
	
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	private void showAsToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
