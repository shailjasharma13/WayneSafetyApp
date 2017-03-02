//package anme
package checkcamera.csc5991.wsu.checkcamera;

//imports

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;



/*---This class is to report incident to the responsible authorities --*/

public class ReportIncident extends AppCompatActivity {


    //Class Variable
    ImageView cameraPicture;
    Button takePicture;
    Button openGallery;
    Button makeVideo;
    Button sendMessage;
    Uri selectedImageUriCamera;
    EditText messageText;
    Bitmap bp;
    File temp;
    Uri selectedImageUri;
    Bitmap bitmapImage;
    ReportIncident reportIncident;
    String imageFolder;
    File imageFile;
    String imageFileName;

    // Oncreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_incident);

        //Initializing the local variables
        cameraPicture = (ImageView) findViewById(R.id.cameraPicture);
        takePicture = (Button) findViewById(R.id.takePicture);
        openGallery = (Button) findViewById(R.id.openGallery);
        sendMessage = (Button) findViewById(R.id.sendMessgae);
        messageText = (EditText) findViewById(R.id.messgaeText);
        reportIncident = new ReportIncident();
        reportIncident = this;


        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println(" can be mounted");
        }


        // listener for taking pictures
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent for  starting camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });


        /// listener for opening gallery
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent for  taking picture  from gallery
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });


        //sending the reported incident as method

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Getting the text message from textview
                String messagecontent = messageText.getText().toString();
                //Starting the message intent

                Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.setType("vnd.android-dir/mms-sms");
                intent.setType("image/*");
                System.out.println(" sleected Image" + selectedImageUri);

                if (selectedImageUri != null) {
                    intent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
                }
                // intent.putExtra(Intent.EXTRA_STREAM, messagecontent);
                //intent.setPackage("com.android.mms");
                //intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("sms_body", messagecontent);
                intent.putExtra("address", new String("3138968097"));
                intent.setType("image/*");

                if (selectedImageUriCamera != null) {
                    intent.putExtra(Intent.EXTRA_STREAM, selectedImageUriCamera);
                }


                startActivity(intent);
                saveReport("3138968097", messagecontent);


            }
        });

    }

    //OnACtivity method for intents
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                bp = (Bitmap) data.getExtras().get("data");
                cameraPicture.setImageBitmap(bp);
                saveBitmap();
                selectedImageUriCamera = Uri.fromFile(imageFile);

            } else if (requestCode == 2) {
                selectedImageUri = data.getData();

                try {
                    bitmapImage = decodeBitmap(selectedImageUri);

                } catch (Exception e) {
                    e.getMessage();
                }
                cameraPicture.setImageBitmap(bitmapImage);


            }

        }
    }


    //Save the image in a file
    private void saveBitmap() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
        }



        String root = Environment.getExternalStorageDirectory().toString();
        imageFile = new File(root ,"takenimage.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


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

    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
