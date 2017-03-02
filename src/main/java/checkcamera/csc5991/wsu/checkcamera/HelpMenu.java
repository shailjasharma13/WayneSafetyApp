package checkcamera.csc5991.wsu.checkcamera;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//This is the class for "Help menu".
//Corresponding XML file is help_menu.
//This file doesn't require changes except the text to be filled in the XML.

public class HelpMenu extends Fragment{
    View myview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.help_menu,container,false);
        return myview;
    }
}


