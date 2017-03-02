package checkcamera.csc5991.wsu.checkcamera;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// This is the class for "About menu".
//Corresponding XML file is menu_layout.
//This class probably doesn't require any changes.Only text must be entered in the XML file.

public class Menu extends Fragment {

    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.menu_layout,container,false);
        return rootview;
    }
}

