package com.javali.scorecricket.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.services.FragmentServices;
import com.javali.scorecricket.services.MyDBAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerProcessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PlayerProcessFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private OnFragmentInteractionListener mListener;

    private static final String[] bowlStyles = {"Select Bowling Style",
            "Right-arm Fast",
            "Right-arm Fast Medium",
            "Right-arm Medium Fast",
            "Right-arm Medium",
            "Left-arm Fast",
            "Left-arm Fast Medium",
            "Left-arm Medium Fast",
            "Left-arm Medium",
            "Off Break (Right-arm)",
            "Leg Break (Right-arm)",
            "Slow Left-arm Orthodox",
            "Slow Left-arm Chinaman"
    };

    private MyDBAdapter mMyDBAdapter;

    private TextView mPlayerId;
    private EditText mEditFirstName;
    private EditText mEditLastName;
    private RadioGroup mGender;
    private RadioButton mGenderButton;
    private RadioButton mMale;
    private RadioButton mFemale;
    private TextView mBirthDate;
    private EditText mCity;
    private EditText mState;
    private RadioGroup mBatStyle;
    private RadioButton mBatStyleButton;
    private RadioButton mRHB;
    private RadioButton mLHB;
    private CheckBox mKeeper;
    private Spinner mBowlStyle;
    private Button mSave;
    private Button mCancel;

    private String sFirstName;
    private String sLastName;
    private String sGender;
    private String sBirthDate;
    private String sCity;
    private String sState;
    private String sBatStyle;
    private boolean sKeeper;
    private int sBowlStyle;

    public PlayerProcessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyDBAdapter = new MyDBAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_player_process, container, false);

        mPlayerId = (TextView) mView.findViewById(R.id.playerID);
        mEditFirstName = (EditText) mView.findViewById(R.id.editFirstName);
        mEditLastName = (EditText) mView.findViewById(R.id.editLastName);

        mGender = (RadioGroup) mView.findViewById(R.id.gender);
        mGenderButton = (RadioButton) mView.findViewById(mGender.getCheckedRadioButtonId());
        mMale = (RadioButton) mView.findViewById(R.id.male);
        mFemale = (RadioButton) mView.findViewById(R.id.female);

        mBirthDate = (TextView) mView.findViewById(R.id.dob);
        mCity = (EditText) mView.findViewById(R.id.city);
        mState = (EditText) mView.findViewById(R.id.state);

        mBatStyle = (RadioGroup) mView.findViewById(R.id.batStyle);
        mBatStyleButton = (RadioButton) mView.findViewById(mBatStyle.getCheckedRadioButtonId());
        mRHB = (RadioButton) mView.findViewById(R.id.rhb);
        mLHB = (RadioButton) mView.findViewById(R.id.lhb);

        mKeeper = (CheckBox) mView.findViewById(R.id.keeper);
        mBowlStyle = (Spinner) mView.findViewById(R.id.bowlStyle);

        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Select Bowling Style");
        spinnerArray.add("Right-arm Fast");
        spinnerArray.add("Right-arm Fast Medium");
        spinnerArray.add("Right-arm Medium Fast");
        spinnerArray.add("Right-arm Medium");
        spinnerArray.add("Left-arm Fast");
        spinnerArray.add("Left-arm Fast Medium");
        spinnerArray.add("Left-arm Medium Fast");
        spinnerArray.add("Left-arm Medium");
        spinnerArray.add("Off Break");
        spinnerArray.add("Leg Break");
        spinnerArray.add("Slow Left-arm Orthodox");
        spinnerArray.add("Slow Left-arm Chinaman");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), R.layout.spinner_bowl_style, spinnerArray);
        mBowlStyle.setAdapter(adapter);

        String data = getArguments().getString("page");
        if (data != null) {
            if (data.equalsIgnoreCase("editplayer")) {
                PlayerModel pModel = getArguments().getParcelable("PlayerModel");
                mPlayerId.setText("" + pModel.get_id());
                mEditFirstName.setText(pModel.getFirstName());
                mEditLastName.setText(pModel.getLastName());
                if ((pModel.getGender()).equalsIgnoreCase("male")) {
                    mMale.setChecked(true);
                } else {
                    mFemale.setChecked(true);
                }
                mBirthDate.setText(pModel.getDob());
                mCity.setText(pModel.getCity());
                mState.setText(pModel.getState());
                if ((pModel.getBattingstyle()).contains("Right")) {
                    mRHB.setChecked(true);
                } else {
                    mLHB.setChecked(true);
                }
                mKeeper.setChecked(Boolean.parseBoolean("" + pModel.getKeeper()));
                mBowlStyle.setSelection(pModel.getBowlingstyle());
            } else if (data.equalsIgnoreCase("addplayer")) {
                Log.i("Data::::::::", "Add Player");
            } else {
                Log.i("Data::::::::", "Empty Data");
            }
        } else {
            Log.i("Data::::::::", "No Data is passed");
        }

        mSave = (Button) mView.findViewById(R.id.savePlayer);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sFirstName = mEditFirstName.getText().toString();
                sLastName = mEditLastName.getText().toString();
                sGender = mGenderButton.getText().toString();
                sBirthDate = mBirthDate.getText().toString();
                sCity = mCity.getText().toString();
                sState = mState.getText().toString();
                sBatStyle = mBatStyleButton.getText().toString();
                sKeeper = mKeeper.isChecked();
                sBowlStyle = (int) mBowlStyle.getSelectedItemId();

                boolean flag = true;

                if (sBowlStyle == 0) {
                    flag = false;

                    TextView tvSpinner = (TextView) mBowlStyle.getSelectedView();
                    tvSpinner.setError("Select Bowling Style");

                    Snackbar mSnackbar = Snackbar.make(mView.findViewById(R.id.playerProcessFragment), "Select Bowling Style", Snackbar.LENGTH_LONG);
                    mSnackbar.getView().setBackgroundColor(Color.rgb(86, 196, 249));
                    TextView snackbarTextView = ((TextView) mSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text));
                    snackbarTextView.setTextColor(Color.rgb(93, 64, 64));
                    snackbarTextView.setTextSize(18);
                    mSnackbar.show();
                    mBowlStyle.performClick();
                }
                if (sBirthDate.length() == 0) {
                    flag = false;
                    mBirthDate.setError("Birthdate required");
                    mBirthDate.requestFocus();
                }
                if (sLastName.length() == 0) {
                    flag = false;
                    mEditLastName.setError("Lastname required");
                    mEditLastName.requestFocus();
                }
                if (sFirstName.length() == 0) {
                    flag = false;
                    mEditFirstName.setError("Firstname required");
                    mEditFirstName.requestFocus();
                }

                if (flag) {
                    PlayerModel p = new PlayerModel();
                    if ((mPlayerId.getText().toString()).equalsIgnoreCase("")) {
                        p.set_id(0);
                    } else {
                        p.set_id(Integer.parseInt(mPlayerId.getText().toString()));
                    }
                    p.setFirstName(mEditFirstName.getText().toString());
                    p.setLastName(mEditLastName.getText().toString());
                    p.setGender(mGenderButton.getText().toString());
                    p.setDob(mBirthDate.getText().toString());
                    p.setCity(mCity.getText().toString());
                    p.setState(mState.getText().toString());
                    p.setBattingstyle(mBatStyleButton.getText().toString());
                    p.setKeeper("" + mKeeper.isChecked());
                    p.setBowlingstyle((int) mBowlStyle.getSelectedItemId());
                    long data = mMyDBAdapter.insertPlayer(p);
                    if (data == -1) {
                        Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar snackbar = Snackbar.make(getView(), "Player details saved", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    FragmentServices.closeInputSoftKeyboard(getActivity());
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_container, new PlayerFragment()).commit();

                    //FragmentServices.closeInputSoftKeyboard(getActivity());
                    //FragmentServices.removeFragmentFromStack(getActivity().getSupportFragmentManager());
                }
            }
        });

        mCancel = (Button) mView.findViewById(R.id.cancelPlayer);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentServices.closeInputSoftKeyboard(getActivity());
                //FragmentServices.removeFragmentFromStack(getActivity().getSupportFragmentManager());

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, new PlayerFragment()).commit();
            }
        });

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showDatePickerDialog() {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mBirthDate = (TextView) getView().findViewById(R.id.dob);
        mBirthDate.setText(month + "/" + dayOfMonth + "/" + year);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
