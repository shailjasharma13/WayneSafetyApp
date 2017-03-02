//package

package checkcamera.csc5991.wsu.checkcamera;


//imports

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.Scanner;


//This class show the incident reported by the user
public class YourReports extends AppCompatActivity {

    //class Variable
    TableLayout displayReports;

    //On Create

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_reports);

        //Initializing the display Reports Table Layout
        displayReports = (TableLayout) findViewById(R.id.displayReports);
        try {

            //Reading from the Reports file
            FileInputStream fileIn = null;

            Scanner streamIn = null;

            String line;

            // Declare variables
            String name;
            int number;


            fileIn = openFileInput(String.valueOf("Your_Reports.txt"));
            streamIn = new Scanner(fileIn);

            // Loop to read lines from input file

            while (streamIn.hasNextLine()) {
                line = streamIn.nextLine();

                //Text View for displaying  Report
                TextView reportTextView = new TextView(this);
                reportTextView.setText(line.toString());
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                reportTextView.setTextColor(getResources().getColor(R.color.black));
                reportTextView.setTextSize(15);

                //adding textView in table Row
                tableRow.addView(reportTextView);

                //adding tableView in table Layout
                displayReports.addView(tableRow);


                //added on table row for space
                TableRow t1 = new TableRow(this);
                TextView blank = new TextView(this);
                t1.addView(blank);
                displayReports.addView(t1);

            }


            // Close input file
            fileIn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}