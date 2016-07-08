package wsu.csc5991.wsusafetyapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//This is the class for "Help menu".
//Corresponding XML file is help_menu.
//This file doesn't require changes except the text to be filled in the XML.

public class HelpMenu extends Fragment{
    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.help_menu,container,false);
        return rootview;
    }
}
