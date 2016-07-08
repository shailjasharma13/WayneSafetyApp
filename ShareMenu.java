package wsu.csc5991.wsusafetyapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// This is the file for "Share menu".
//Correseponding XML is share_menu.
public class ShareMenu extends Fragment {
    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.share_menu,container,false);
        return rootview;
    }
}
