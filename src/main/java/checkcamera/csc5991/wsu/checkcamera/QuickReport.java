//package

package checkcamera.csc5991.wsu.checkcamera;


//imports

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


/*---This class ia for quick reporting of the incident, by one button click it will
send a report to emergency contacts and concerned authorities --*/

public class QuickReport extends AppCompatActivity {


    //Class Variable

    PendingIntent sentIntent;
    private Location currentLocation;
    private LocationManager locationManager;
    private String address;
    BroadcastReceiver sentReceiver;
    TextView txt;
    ArrayList<String> contactNumbers = new ArrayList<>();
    Geocoder geocoder;

    //On Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_report);
        QuickReport quickReport = new QuickReport();
        quickReport = this;
        ArrayList<PendingIntent> sentIntents =
                new ArrayList<PendingIntent>();
        txt = (TextView) findViewById(R.id.address);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(quickReport,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }


        // Start location manager
        locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                300,
                1,
                locationListener);


        List<Address> addresses = new ArrayList<>();

        //GeoCoder class for getting the address
        geocoder = new Geocoder(this, Locale.getDefault());

        System.out.println(" Inside location manager network");

        currentLocation = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (currentLocation != null){
            System.out.println("currentLocation::latitude " + currentLocation.getLatitude());}

        try {
            if (currentLocation != null)
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (addresses.size()>0) {

                if (addresses.get(0) != null) {
                    String address = addresses.get(0).getAddressLine(0);
                    System.out.println("Address for 0:: " + addresses.get(0).getAddressLine(0));
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    address = address + "  " + city + "  " + state + "  " + country + "  " + postalCode;
                }
            }else{ address="42 W Warren Ave,Detroit Michigan 48201";}
        } catch (IOException e) {
            e.getMessage();
        }


        // Getting the address from the addresses





        System.out.println("Address:: " + address);


        //Help Message with address appended

        String message = "Help me!!" + address;
        //txt.setText(message.toString());

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                1);


        //Callling method for getting emergency Contacts number in list
        contactNumbers = getEmergenyContacts();

        contactNumbers.add("8133857433");
        System.out.println(contactNumbers.size());
        for (String SOSnumber : contactNumbers) {
            System.out.println(SOSnumber);
        }


        //Iterating through the emergency Contacts number
        for (String SOSnumber : contactNumbers) {

            ArrayList<String> messageParts;
            System.out.println("Inside one time in SOS:: " + SOSnumber);
            sentIntent = PendingIntent.getBroadcast(
                    this, 0, new Intent("SMS_SENT"), 0);
            // Define SMS manager and parse message
            SmsManager smsManager = SmsManager.getDefault();
            messageParts = smsManager.divideMessage(message);
            if (messageParts.size() == 1)

                // Send single-part message
                smsManager.sendTextMessage("3138968097", null, message,
                        sentIntent, null);
            else {

                // Loop to set intents for message parts
                for (int i = 0; i < messageParts.size(); i++) {
                    sentIntents.add(sentIntent);

                }

                // Send multi-part message
                smsManager.sendMultipartTextMessage("3138968097", null,
                        messageParts, sentIntents, null);

            }

            txt.setText("Your message is sent");
            txt.setTextSize(20);
            txt.setTextColor(getResources().getColor(R.color.black));


            // Declare SMS status intents

            registerReceiver(sentReceiver, new IntentFilter("SMS_SENT"));


        }

        saveReport("All Contacts", message);


        // Declare SMS status intents


        //catch (SecurityException e) {
        //System.out.println("Error starting location listener.");
        //System.out.println("Exception message in SC:\n" + e.getMessage());
        //    }


    }


    //----------------------------------------------------------------
    // locationListener
    //----------------------------------------------------------------
    public LocationListener locationListener = new LocationListener() {

        //------------------------------------------------------------
        // onLocationChanged
        //------------------------------------------------------------
        @Override
        public void onLocationChanged(Location location) {

            currentLocation = location;
        }

        //------------------------------------------------------------
        // onStatusChanged
        //------------------------------------------------------------
        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {

        }

        //------------------------------------------------------------
        // onProviderEnabled
        //------------------------------------------------------------
        @Override
        public void onProviderEnabled(String provider) {

        }

        //------------------------------------------------------------
        // onProviderDisabled
        //------------------------------------------------------------
        @Override
        public void onProviderDisabled(String provider) {
        }

    };

    //----------------------------------------------------------------
    // onResume
    //----------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();

        // Define SMS send receiver
        sentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == Activity.RESULT_OK)
                    Toast.makeText(getApplicationContext(),
                            "SMS message sent.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),
                            "SMS message NOT sent.", Toast.LENGTH_SHORT).show();
                ;
            }
        };


    }


    // Method for getting the phone numbers
    public ArrayList<String> getEmergenyContacts() {
        try {

            //InputStream for reading data

            FileInputStream fileIn = null;
            Scanner streamIn = null;
            int lineCount = 0;
            String line;

            //getting number from Phonenumber
            fileIn = openFileInput(String.valueOf("Phonenumber.txt"));
            streamIn = new Scanner(fileIn);
            while (streamIn.hasNextLine()) {
                contactNumbers.add(streamIn.nextLine());

            }
        } catch (Exception e) {

        }
        return contactNumbers;
    }

    //Save the reports in Reports filr
    public void saveReport(String number, String messgae) {
        try {
            //Output Stream
            FileOutputStream fileOut = null;
            PrintStream streamOut = null;

            //File name and append
            fileOut = openFileOutput("Your_Reports.txt", Context.MODE_APPEND);
            streamOut = new PrintStream(fileOut);
            streamOut.println(number + "                 " + messgae);

            //closing the streams
            fileOut.close();
            streamOut.close();
        } catch (Exception e) {

        }
    }
}
