package com.javali.scorecricket.services;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.javali.scorecricket.R;
import com.javali.scorecricket.activity.PlayerProcessFragment;

/**
 * Created by shashankshivakumar on 10/18/17.
 */

public class FragmentServices {

    private static MyDBAdapter mMyDBAdapter;

    public FragmentServices() {}

    // This method will close/hide input soft keyboard
    public static void closeInputSoftKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void addFragmentToStack(String fragmentName, FragmentManager fragmentManager, Fragment fragment) {
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_container, fragment);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        } else {
            Log.e("FragmentServices","Something went wrong");
        }
    }

    public static void removeFragmentFromStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }

    public static String formatDate (String date) {
        date = date.replace(",", "");
        String[] ary = date.split(" ");
        return ary[0] + " " + ary[1];
    }
}
