package checkcamera.csc5991.wsu.checkcamera;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

// This class is for the implementation of three time power button press functionality.
//On the power button being pressed 3 times, the application call the QuickResponse Activity.
//Add the actual quick response class name to the intent in the last line.
public class MyReceiver extends BroadcastReceiver {

    static int countPowerOff = 0;
    private Activity activity = null;

    public MyReceiver(Activity activity){
        this.activity = activity;
    }

    public void onReceive(Context context, Intent intent){

        Log.v("onReceive","Power button is pressed");
        Toast.makeText(context,"Power button pressed",Toast.LENGTH_SHORT).show();

        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            countPowerOff++;
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

            if (countPowerOff == 3){
                Intent i = new Intent(activity,QuickReport.class);
                activity.startActivity(i);
            }

        }
    }

}