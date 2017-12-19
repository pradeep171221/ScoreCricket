package com.javali.scorecricket.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.MatchModel;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;
import com.javali.scorecricket.services.FragmentServices;
import com.javali.scorecricket.services.MyDBAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchTossFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MatchTossFragment extends Fragment {

    private MyDBAdapter mMyDBAdapter;
    private RadioGroup teamToss;
    private RadioButton teamTossButton;
    private RadioGroup teamChoose;
    private RadioButton teamChooseButton;
    private RadioButton team_one;
    private RadioButton team_two;
    private RadioButton choose_bat;
    private RadioButton choose_bowl;
    private com.shawnlin.numberpicker.NumberPicker numberPicker;
    private EditText venue;
    private EditText umpireOne;
    private EditText umpireTwo;
    private Button startMatch;
    private MatchModel matchModel;

    private OnFragmentInteractionListener mListener;

    public MatchTossFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyDBAdapter = new MyDBAdapter(getActivity());
        matchModel = getArguments().getParcelable("match_model");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_match_toss, container, false);

        Log.i("Match", "Toss");

        team_one = (RadioButton) mView.findViewById(R.id.team_one);
        team_two = (RadioButton) mView.findViewById(R.id.team_two);
        team_one.setText(matchModel.getTeam_a().getTeamName());
        team_two.setText(matchModel.getTeam_b().getTeamName());
        teamToss = (RadioGroup) mView.findViewById(R.id.won_details);

        teamChoose = (RadioGroup) mView.findViewById(R.id.choose_details);
        choose_bat = (RadioButton) mView.findViewById(R.id.choose_bat);
        choose_bowl = (RadioButton) mView.findViewById(R.id.choose_bowl);

        venue = (EditText) mView.findViewById(R.id.venue);
        umpireOne = (EditText) mView.findViewById(R.id.umpireOne);
        umpireTwo = (EditText) mView.findViewById(R.id.umpireTwo);
        startMatch = (Button) mView.findViewById(R.id.startMatch);

        startMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamTossButton = (RadioButton) mView.findViewById(teamToss.getCheckedRadioButtonId());
                teamChooseButton = (RadioButton) mView.findViewById(teamChoose.getCheckedRadioButtonId());
                numberPicker = (com.shawnlin.numberpicker.NumberPicker) mView.findViewById(R.id.number_picker);
                Log.i("Won_By:", " " + teamTossButton.getText().toString());
                Log.i("Choose_To:", " " + teamChooseButton.getText().toString());
                Log.i("Overs:", "" + numberPicker.getValue());

                matchModel.setVenue(venue.getText().toString());
                matchModel.setUmpireA(umpireOne.getText().toString());
                matchModel.setUmpireB(umpireTwo.getText().toString());
                matchModel.setOvers(numberPicker.getValue());
                matchModel.setElected(teamChooseButton.getText().toString());
                TeamModel team = new TeamModel();

                if ((teamTossButton.getText().toString()).equalsIgnoreCase(matchModel.getTeam_a().getTeamName())) {
                    matchModel.setToss_won(matchModel.getTeam_a().get_teamId());
                    if (!(teamChooseButton.getText().toString()).equalsIgnoreCase("bat")) {
                        team = matchModel.getTeam_b();
                        matchModel.setTeam_b(matchModel.getTeam_a());
                        matchModel.setTeam_a(team);
                    }
                } else {
                    matchModel.setToss_won(matchModel.getTeam_b().get_teamId());
                    if ((teamChooseButton.getText().toString()).equalsIgnoreCase("bat")) {
                        team = matchModel.getTeam_a();
                        matchModel.setTeam_a(matchModel.getTeam_b());
                        matchModel.setTeam_b(team);
                    }
                }
                matchModel.setProgress("1st Inning in progress");
                matchModel.setResult("In Progress");

                Bundle args = new Bundle();
                args.putString("page", "ScoreManagement");
                args.putParcelable("match_model", (Parcelable) matchModel);

                long data = mMyDBAdapter.updateMatch(matchModel);
                if (data == -1) {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                } else {
                    ScoringSheetFragment ssf = new ScoringSheetFragment();
                    ssf.setArguments(args);
                    FragmentServices.closeInputSoftKeyboard(getActivity());
                    FragmentServices.addFragmentToStack("score_management", getFragmentManager(), ssf);
                }
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
