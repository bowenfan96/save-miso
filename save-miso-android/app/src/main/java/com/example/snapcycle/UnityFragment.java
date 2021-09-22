package com.example.snapcycle;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.unity3d.player.UnityPlayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UnityFragment extends Fragment {

    SharedPreferences prefs;

    protected UnityPlayer mUnityPlayer;
    FrameLayout frameLayoutForUnity;

    public UnityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUnityPlayer = new UnityPlayer(getActivity());
        View view = inflater.inflate(R.layout.fragment_unity, container, false);
        this.frameLayoutForUnity = view.findViewById(R.id.frameLayoutForUnity);
        this.frameLayoutForUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer.requestFocus();
        mUnityPlayer.windowFocusChanged(true);

        prefs = requireContext().getSharedPreferences("saveMiso_settings", 0);
        String username = prefs.getString("username", "null");

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long daysPassed = -1;
        String lastRecordedDay = prefs.getString("lastDate", "null");
        System.out.println("date from shared preferences = " + lastRecordedDay);
        if (lastRecordedDay == "null") {
            String currentDate = dateFormatter.format(new Date());
            SharedPreferences.Editor editor = prefs.edit();
            System.out.println("Storing date " + currentDate + " in shared preferences");
            editor.putString("lastDate", currentDate);
            editor.apply();
            daysPassed = 0;
        } else {
            try {
                Date lastDate = dateFormatter.parse(lastRecordedDay);
                System.out.println("Date lastDate = " + lastDate);
                String currentDate = dateFormatter.format(new Date());
                Date current = dateFormatter.parse(currentDate);
                long diffInMillies = Math.abs(current.getTime() - lastDate.getTime());
                daysPassed = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                System.out.println("daysPassed = " + daysPassed);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (daysPassed >= 1) {
            int days = (int) daysPassed;
            String input = Integer.toString(days);
            UnityPlayer.UnitySendMessage("rabbit", "decrementHealth", input);
        }
        UnityPlayer.UnitySendMessage("rabbit", "GetPointsFromServer", username);
        return view;
    }

    @Override
    public void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }
}