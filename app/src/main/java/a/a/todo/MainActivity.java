package a.a.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MainActivity extends ActionBarActivity {

//    CustomAdapter adapter;
//    Boolean columnView = false;
    protected List<ParseObject> mMessages;
    ListView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventList = (ListView) findViewById(R.id.eventList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveEvents();
    }

    private void retrieveEvents() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.orderByDescending(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                if(e == null){
                    //we found messages!
                    mMessages = messages;

                    String[] eventName = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        eventName[i] = message.getString(ParseConstants.KEY_EVENT_NAME);
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_expandable_list_item_1,
                            eventName);
                    eventList.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent();
        switch (id) {
            case R.id.action_new:
                intent.setClass(getApplicationContext(), Submit.class);
                startActivity(intent);
                break;
            case R.id.action_map:
                intent.setClass(getApplicationContext(), Event_Map.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
