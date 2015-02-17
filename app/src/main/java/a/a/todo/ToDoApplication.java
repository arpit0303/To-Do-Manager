package a.a.todo;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Arpit on 16/02/15.
 */
public class ToDoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "vAB2rVy3rVmVst8dCRtI39i1Lg1HIziSAGsHAbeE", "r3PKNpQCK71EK2Cn9OFPUi6Y68fJZBDCNJAD9Wcp");

    }
}
