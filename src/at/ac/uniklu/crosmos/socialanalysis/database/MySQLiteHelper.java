package at.ac.uniklu.crosmos.socialanalysis.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_NOTES = "notes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_DEVICEID = "deviceID";

	private static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
	      + TABLE_NOTES + "(" + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_TEXT + " text not null, " 
	      + COLUMN_LONGITUDE + " real not null, "
	      + COLUMN_LATITUDE + " real not null, "
	      + COLUMN_TIMESTAMP + " integer not null, "
	      + COLUMN_DEVICEID + " text not null);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
	    onCreate(db);
	}
}
