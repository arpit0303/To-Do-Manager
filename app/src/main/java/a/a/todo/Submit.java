package a.a.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Submit extends ActionBarActivity {

    TextView dateTime;
    EditText eventName;
    EditText eventDescription;
    TextView location;

    GoogleMap map;

    JSONObject mLocationData;

    Double mLocationLat = 0.0;
    Double mLocationLng = 0.0;
    String mEventName;
    String mEventDesc;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        eventName = (EditText) findViewById(R.id.eventName);
        eventDescription = (EditText) findViewById(R.id.eventDesc);

        dateTime = (TextView) findViewById(R.id.dateTime);
        location = (TextView) findViewById(R.id.location);

        String myFormat = "MM/dd/yy hh:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateTime.setText(sdf.format(myCalendar.getTime()));

        getLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_submit:
                mEventName = eventName.getText().toString();
                mEventDesc = eventDescription.getText().toString();

                if(mEventName.isEmpty() || mEventDesc.isEmpty()){
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(Submit.this);
                    builder.setMessage("Please enter all the fields.")
                            .setTitle("Oops!")
                            .setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    ParseObject message = createMessage();
                    if (message == null) {
                        //error
                        AlertDialog.Builder builder = new AlertDialog.Builder(Submit.this);
                        builder.setMessage("there was an error with the file selected.")
                                .setTitle("We're sorry")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        //send
                        send(message);
                    }
                }
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    protected void send(ParseObject message){
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //success
                    Toast.makeText(Submit.this, "Message sent!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Submit.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(Submit.this);
                    builder.setMessage("there was an error sending your message.Please try again")
                            .setTitle("We're sorry")
                            .setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
    }

    protected ParseObject createMessage(){
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_EVENT_NAME, mEventName);
        message.put(ParseConstants.KEY_EVENT_DESC,mEventDesc);
        message.put(ParseConstants.KEY_LOCATION_LAT,mLocationLat);
        message.put(ParseConstants.KEY_LOCATION_LNG,mLocationLng);
        return message;
    }

    public void getLocation() {

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.setMyLocationEnabled(true);

        LocationManager locMngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locMngr.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locMngr.getLastKnownLocation(provider);

        if(myLocation != null) {
            mLocationLat = myLocation.getLatitude();
            mLocationLng = myLocation.getLongitude();

            location.setText("Lat:" + mLocationLat + " , Lng:" + mLocationLng);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLocationLat, mLocationLng))
                .zoom(12)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
