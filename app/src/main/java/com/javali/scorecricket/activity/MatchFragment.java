package com.javali.scorecricket.activity;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.services.RecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MatchFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button createNewMatch;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private TextView teamOne;
    private TextView teamTwo;
    private Button next;
    private RadioGroup teamToss;
    private RadioButton teamTossButton;
    private RadioGroup teamChoose;
    private RadioButton teamChooseButton;
    private com.shawnlin.numberpicker.NumberPicker numberPicker;
    private Button create;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_match, container, false);
        return mView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_event:
                createNewMatch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createNewMatch() {

        Log.i("New Match", "creating");
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("Create New Match");
        dialog.setContentView(R.layout.new_match);
        teamOne = (TextView) dialog.findViewById(R.id.teamOne);
        teamTwo = (TextView) dialog.findViewById(R.id.teamTwo);

        teamOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllTeams(dialog);
            }
        });

        teamTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllTeams(dialog);
            }
        });

        next = (Button) dialog.findViewById(R.id.nextPage);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Match", "Toss");
                dialog.setContentView(R.layout.match_toss);
                teamToss = (RadioGroup) dialog.findViewById(R.id.won_details);
                teamTossButton = (RadioButton) dialog.findViewById(teamToss.getCheckedRadioButtonId());
                teamChoose = (RadioGroup) dialog.findViewById(R.id.choose_details);
                teamChooseButton = (RadioButton) dialog.findViewById(teamChoose.getCheckedRadioButtonId());
                numberPicker = (com.shawnlin.numberpicker.NumberPicker) dialog.findViewById(R.id.number_picker);
                create = (Button) dialog.findViewById(R.id.createMatch);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("WonBy:", " " + teamTossButton.getText().toString());
                        Log.i("ChooseTo:", " " + teamChooseButton.getText().toString());
                        Log.i("Overs:", "" + numberPicker.getValue());
                    }
                });
            }
        });
        dialog.show();
    }

    public void loadAllTeams (Dialog dialog) {
        Log.i("Load:", "Teams");
        dialog.setContentView(R.layout.team_list);
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
