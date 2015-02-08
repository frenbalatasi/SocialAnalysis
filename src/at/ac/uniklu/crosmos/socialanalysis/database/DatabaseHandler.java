package at.ac.uniklu.crosmos.socialanalysis.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import at.ac.uniklu.crosmos.socialanalysis.notes.TextNotes;

public class DatabaseHandler {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TEXT,
			MySQLiteHelper.COLUMN_LONGITUDE,
			MySQLiteHelper.COLUMN_LATITUDE,
			MySQLiteHelper.COLUMN_TIMESTAMP,
			MySQLiteHelper.COLUMN_DEVICEID
	};
	
	public DatabaseHandler(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
	    dbHelper.close();
	}
	
	public TextNotes createTextNote(TextNotes textNote) {
		ContentValues values = new ContentValues();
		
	    values.put(MySQLiteHelper.COLUMN_TEXT, textNote.getText());
	    values.put(MySQLiteHelper.COLUMN_LONGITUDE, textNote.getLongitude());
	    values.put(MySQLiteHelper.COLUMN_LATITUDE, textNote.getLatitude());
	    values.put(MySQLiteHelper.COLUMN_TIMESTAMP, textNote.getTimestamp());
	    values.put(MySQLiteHelper.COLUMN_DEVICEID, textNote.getDeviceID());
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null, values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    TextNotes newTxtNote = cursorToNote(cursor);
	    cursor.close();
	    return newTxtNote;
	}

	public void deleteTextNote(TextNotes textNote) {
	    database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_TIMESTAMP
	        + " = " + textNote.getTimestamp(), null);
	}
	
	public ArrayList<TextNotes> getAllTextNotes() {
	    ArrayList<TextNotes> txtnotesList = new ArrayList<TextNotes>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	TextNotes txtNote = cursorToNote(cursor);
	    	txtnotesList.add(txtNote);
	    	cursor.moveToNext();
	    }

	    cursor.close();
	    return txtnotesList;
	}
	
	private TextNotes cursorToNote(Cursor cursor) {
	    TextNotes txtNote = new TextNotes();
	    
	    txtNote.setText(cursor.getString(1));
	    txtNote.setLongitude(cursor.getDouble(2));
	    txtNote.setLatitude(cursor.getDouble(3));
	    txtNote.setTimestamp(cursor.getLong(4));
	    txtNote.setDeviceID(cursor.getString(5));
	    
	    return txtNote;
	}

}
