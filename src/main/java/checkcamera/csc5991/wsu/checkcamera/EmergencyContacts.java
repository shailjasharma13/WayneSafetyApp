//package name
package checkcamera.csc5991.wsu.checkcamera;


//imports
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


/*-- Class Emergency Contacts provide list of all the contacts and add new contact--*/

public class EmergencyContacts extends AppCompatActivity {

    // Variable

    TableLayout displayContacts;
    TableRow tableRow;
    Button insertRow;
    TextView contactName;
    TextView contactNumber;
    EmergencyContacts emergencyContacts;
    FileOutputStream fileOut = null;
    PrintStream streamOut = null;

  //On Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);

        // Initializing the  variable
        displayContacts = (TableLayout) findViewById(R.id.displayContacts);
        insertRow = (Button) findViewById(R.id.insert);
        selectRow(null);
        emergencyContacts = new EmergencyContacts();
        emergencyContacts = this;

        //reading the contacts to add in the contact emergency list
        insertRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(emergencyContacts,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                1);

                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, 1001);

                    }
                });



    }

    //On Activity Result Method for contact Read Contacts Activity

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String contactID = null;
        String displayName = null;
        String number = null;
        Cursor cursorPhone = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                         //Using Content Resolver to get Contact id and name
                        try {
                            Cursor cursorID = getContentResolver().query(uri,
                                    new String[]{ContactsContract.Contacts._ID},
                                    null, null, null);


                            if (cursorID.moveToFirst()) {

                                contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
                            }

                            cursorID.close();


                            // Using the contact ID now we will get contact phone number
                            cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                                    new String[]{contactID},
                                    null);
                            if (cursorPhone != null && cursorPhone.moveToFirst()) {

                                number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }

                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                            if (cursor.moveToFirst())

                                // DISPLAY_NAME = The display name for the contact.

                                displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                            cursor.close();

                            // Using table to display data
                            tableRow = new TableRow(this);
                            //setting layout params for row
                            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                           //Name of the contact
                            contactName = new TextView(this);
                            //Number of the contact
                            contactNumber = new TextView(this);
                            contactName.setText(displayName);
                            contactName.setTextColor(getResources().getColor(R.color.black));
                            contactNumber.setText(number);
                            contactNumber.setTextColor(getResources().getColor(R.color.black));

                            // adding the contact name in tabel row
                            tableRow.addView(contactName);

                            TextView blankSpace = new TextView(this);
                            blankSpace.setText("  ");

                            tableRow.addView(blankSpace);

                            //adding contact number in view
                            tableRow.addView(contactNumber);
                            displayContacts.addView(tableRow);

                            // writing the contact information in internal file storage Emergency_Contacts.txt
                            String contactforFile = contactName.getText().toString() + blankSpace.getText().toString() + contactNumber.getText().toString();
                            System.out.println(contactforFile + "   contactforFile");

                            fileOut = openFileOutput("Emergency_Contacts.txt", Context.MODE_APPEND);
                            streamOut = new PrintStream(fileOut);
                            streamOut.println(contactforFile);
                            fileOut.close();
                            streamOut.close();

                            //writing number in Phonenumbers,txt
                            fileOut = openFileOutput("PhoneNumbers.txt", Context.MODE_APPEND);
                            streamOut = new PrintStream(fileOut);
                            streamOut.println(contactNumber);
                            fileOut.close();
                            streamOut.close();



                        } catch (Exception e){

                            System.out.println(e.getMessage());

                        }

                        finally {
                            if (cursorPhone != null) {
                                cursorPhone.close();
                            }
                        }
                    }
                }

            }
        }
    }


//This method will show thw added contacts when Emergency Contacts button is pressed

    public void selectRow(View v) {

        FileInputStream fileIn = null;

        Scanner streamIn = null;
        int lineCount = 0;
        String line;
        // Declare variables
        String name;
        int number;

        // Attempt to add data
        try {


            fileIn = openFileInput(String.valueOf("Emergency_Contacts.txt"));
            streamIn = new Scanner(fileIn);

            // Loop to read lines from input file
            lineCount = 0;
            while (streamIn.hasNextLine()) {
                line = streamIn.nextLine();
                lineCount = lineCount + 1;
                TextView contacts = new TextView(this);
                contacts.setText(line.toString());
                tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                contacts.setTextColor(getResources().getColor(R.color.black));
                tableRow.addView(contacts);
                displayContacts.addView(tableRow);
                TableRow t1=new TableRow(this);
                TextView blank=new TextView(this);
                t1.addView(blank);
                displayContacts.addView(t1);

            }


            // Close input file
            fileIn.close();



        } catch (Exception e) {
            System.out.println("Error adding table data.");
            System.out.println("Exception message:\n" + e.getMessage());
        }

    }



}


