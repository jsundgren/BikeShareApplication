package com.example.drakdraparen.bikeshareproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  ###STARTUIFRAGMENT###
 *  This class handles the setup of the UI element holding the
 *  user information retrieved from the database.
 */

public class StartUIFragment extends Fragment {
    private static final String START_UI_TAG = "START_UI_TAG";
    private static UserDB sUserDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sUserDB = UserDB.get(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_start_ui, container, false);
        setUpUI(v);
        return v;
    }

    private void setUpUI(View v){
        try {
            User user = sUserDB.findUser();
            ImageView userProfilePic = v.findViewById(R.id.user_profile_pic);
            userProfilePic.setImageBitmap(ConvertByteArrayToBitmap(user.getMUserProfilePic()));
            TextView userName = v.findViewById(R.id.user_name);
            TextView userBalance = v.findViewById(R.id.user_balance);
            userName.setText(user.getMName());
            userBalance.setText(String.valueOf(user.getMBalance()) + "kr");
        }catch(NullPointerException e){
            Toast.makeText(getActivity(), "ERROR DATABASE CONNECTION NOT ESTABLISHED - Please restart application", Toast.LENGTH_SHORT).show();
        }
    }
    /* Getting the picture (in byte) from the database and converts it to a bitmap */
    private static Bitmap ConvertByteArrayToBitmap(byte[] blop) {
        return BitmapFactory.decodeByteArray(blop, 0, blop.length);
    }
}
