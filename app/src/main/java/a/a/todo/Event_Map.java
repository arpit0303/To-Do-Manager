package a.a.todo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class Event_Map extends ActionBarActivity {

    GoogleMap map;
    Double mLocationLat;
    Double mLocationLng;
    protected List<ParseObject> mMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                if(e == null){
                    //we found messages!
                    mMessages = messages;

                    Double[] eventLocationLat = new Double[mMessages.size()];
                    Double[] eventLocationLng = new Double[mMessages.size()];
                    String[] eventName = new String[mMessages.size()];
                    String[] eventDesc = new String[mMessages.size()];

                    int i = 0;
                    for (ParseObject message : mMessages) {
                        eventLocationLat[i] = message.getDouble(ParseConstants.KEY_LOCATION_LAT);
                        eventLocationLng[i] = message.getDouble(ParseConstants.KEY_LOCATION_LNG);
                        eventName[i] = message.getString(ParseConstants.KEY_EVENT_NAME);
                        eventDesc[i] = message.getString(ParseConstants.KEY_EVENT_DESC);

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(eventLocationLat[i], eventLocationLng[i]))
                                .title(eventName[i])
                                .snippet(eventDesc[i]));
                        i++;
                    }
                }
            }
        });


    }

}
