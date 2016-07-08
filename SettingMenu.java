package wsu.csc5991.wsusafetyapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//This class will contain the "Setting Menu" functionality.
//Corresponding XML file is setting_menu.

public class SettingMenu extends Fragment {
    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.settings_menu,container,false);
        return rootview;
    }
}
