package com.javali.scorecricket.activity;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.MatchModel;
import com.javali.scorecricket.model.TeamModel;
import com.javali.scorecricket.services.FragmentServices;
import com.javali.scorecricket.services.MyDBAdapter;
import com.javali.scorecricket.services.RecyclerAdapter;
import com.javali.scorecricket.services.TeamModelComparator;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
    private RecyclerAdapter mMatchAdapter;
    private MyDBAdapter mMyDBAdapter;
    private RecyclerAdapter mTeamAdapter;

    private TextView teamOne;
    private TextView teamTwo;
    private Button next;
    private Button cancelMatch;
    private RadioGroup teamToss;
    private RadioButton teamTossButton;
    private RadioGroup teamChoose;
    private RadioButton teamChooseButton;
    private RadioButton team_one;
    private RadioButton team_two;
    private com.shawnlin.numberpicker.NumberPicker numberPicker;
    private EditText venue;
    private EditText umpireOne;
    private EditText umpireTwo;
    private Button create;
    private String onTeam;

    private ArrayList<MatchModel> matchModelsArray;
    private MatchModel matchModel;

    private TeamModel teamAModel;
    private TeamModel teamBModel;
    private ArrayList<TeamModel> teamModels;
    private ArrayList<TeamModel> filteredTeamList;

    private SearchView mSearchTeam;
    private RecyclerView mRecyclerTeamList;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMyDBAdapter = new MyDBAdapter(getActivity());
        matchModelsArray = mMyDBAdapter.getAllMatches();
        Log.i("match array", "" + matchModelsArray.size());
        teamModels = new ArrayList<TeamModel>();
        teamModels = mMyDBAdapter.getAllTeams();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_match, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.matchListView);
        mRecyclerView.setHasFixedSize(true);

        mMatchAdapter = new RecyclerAdapter(matchModelsArray, getContext(), getFragmentManager(), "match");
        mRecyclerView.setAdapter(mMatchAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMatchAdapter.SetOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<?> p, int position, String arrayName) {
                ArrayList<MatchModel> returnedMatchArray = (ArrayList<MatchModel>) p;
                matchModel = returnedMatchArray.get(0);
                if (matchModel.getOvers() > 0) {
                    tossFragment("score");
                } else {
                    tossFragment("toss");
                }
            }

            @Override
            public void onRemovePlayer(int position) {

            }
        });
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

    public void tossFragment(String data) {
        Bundle args = new Bundle();
        args.putString("page", "matchManagement");
        args.putParcelable("match_model", (Parcelable) matchModel);

        if (data.equalsIgnoreCase("toss")) {
            MatchTossFragment mtf = new MatchTossFragment();
            mtf.setArguments(args);
            FragmentServices.closeInputSoftKeyboard(getActivity());
            FragmentServices.addFragmentToStack("match_toss_management", getFragmentManager(), mtf);
        } else {
            ScoringSheetFragment ssf = new ScoringSheetFragment();
            ssf.setArguments(args);
            FragmentServices.closeInputSoftKeyboard(getActivity());
            FragmentServices.addFragmentToStack("score_management", getFragmentManager(), ssf);
        }
    }

    public void createNewMatch() {
        Log.i("New Match", "creating");
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("Create New Match");
        dialog.setContentView(R.layout.new_match);
        teamOne = (TextView) dialog.findViewById(R.id.teamOne);
        teamTwo = (TextView) dialog.findViewById(R.id.teamTwo);

        if (teamAModel != null) {
            teamOne.setText(teamAModel.getTeamName());
        }

        if (teamBModel != null) {
            teamTwo.setText(teamBModel.getTeamName());
        }

        teamOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onTeam = "one";
                loadAllTeams();
            }
        });

        teamTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onTeam = "two";
                loadAllTeams();
            }
        });

        cancelMatch = (Button) dialog.findViewById(R.id.cancelMatch);
        cancelMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamAModel != null) {
                    teamModels.add(teamAModel);
                    teamAModel = null;
                }
                if (teamBModel != null) {
                    teamModels.add(teamBModel);
                    teamBModel = null;
                }
                dialog.dismiss();
            }
        });

        next = (Button) dialog.findViewById(R.id.nextPage);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchModel = new MatchModel();
                matchModel.setProgress(" ");
                matchModel.setResult("Yet to start.");
                matchModel.setMatchDate(DateFormat.getDateTimeInstance().format(new Date()));
                matchModel.setTeam_a(teamAModel);
                matchModel.setTeam_b(teamBModel);
                matchModel.setScore_one(" ");
                matchModel.setScore_two(" ");
                matchModel.setExtras("0");

                long data = mMyDBAdapter.createMatch(matchModel);
                dialog.dismiss();
                if (data == -1) {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                } else {
                    matchModel.set_matchId(data);
                    matchModelsArray.add(matchModel);
                    mMatchAdapter.notifyDataSetChanged();
                }
                cancelMatch.callOnClick();
                dialog.dismiss();
                tossFragment("toss");
            }
        });

        /*next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Match", "Toss");
                dialog.setContentView(R.layout.match_toss);
                team_one = (RadioButton) dialog.findViewById(R.id.team_one);
                team_two = (RadioButton) dialog.findViewById(R.id.team_two);
                team_one.setText(teamAModel.getTeamName());
                team_two.setText(teamBModel.getTeamName());
                teamToss = (RadioGroup) dialog.findViewById(R.id.won_details);
                teamTossButton = (RadioButton) dialog.findViewById(teamToss.getCheckedRadioButtonId());
                teamChoose = (RadioGroup) dialog.findViewById(R.id.choose_details);
                teamChooseButton = (RadioButton) dialog.findViewById(teamChoose.getCheckedRadioButtonId());
                numberPicker = (com.shawnlin.numberpicker.NumberPicker) dialog.findViewById(R.id.number_picker);
                venue = (EditText) dialog.findViewById(R.id.venue);
                umpireOne = (EditText) dialog.findViewById(R.id.umpireOne);
                umpireTwo = (EditText) dialog.findViewById(R.id.umpireTwo);
                create = (Button) dialog.findViewById(R.id.createMatch);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("WonBy:", " " + teamTossButton.getText().toString());
                        Log.i("ChooseTo:", " " + teamChooseButton.getText().toString());
                        Log.i("Overs:", "" + numberPicker.getValue());

                        matchModel = new MatchModel();
                        matchModel.setVenue(venue.getText().toString());
                        matchModel.setUmpireA(umpireOne.getText().toString());
                        matchModel.setUmpireB(umpireTwo.getText().toString());
                        matchModel.setOvers(numberPicker.getValue());
                        matchModel.setElected(teamChooseButton.getText().toString());
                        matchModel.setResult("Yet to start.");

                        if (teamTossButton.getText().toString().equalsIgnoreCase(teamAModel.getTeamName())) {
                            matchModel.setToss_won(teamAModel.get_teamId());
                            matchModel.setProgress(teamAModel.getTeamName() + ", elected to" + teamChooseButton.getText().toString() + " first.");
                        } else {
                            matchModel.setToss_won(teamBModel.get_teamId());
                            matchModel.setProgress(teamBModel.getTeamName() + ", elected to" + teamChooseButton.getText().toString() + " first.");
                        }

                        matchModel.setMatchDate(DateFormat.getDateTimeInstance().format(new Date()));
                        matchModel.setTeam_a(teamAModel);
                        matchModel.setTeam_b(teamBModel);
                        matchModel.setScore_one(" ");
                        matchModel.setScore_two(" ");

                        Bundle args = new Bundle();
                        args.putString("page", "matchManagement");

                        long data = mMyDBAdapter.createMatch(matchModel);
                        Log.i("Match Count", "" + matchModelsArray.size());
                        dialog.dismiss();
                        if (data == -1) {
                            Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                        } else {
                            matchModel.set_matchId(data);
                            args.putParcelable("match_model", (Parcelable) matchModel);
                            ScoringSheetFragment ssf = new ScoringSheetFragment();
                            ssf.setArguments(args);
                            FragmentServices.closeInputSoftKeyboard(getActivity());
                            FragmentServices.addFragmentToStack("match_management", getFragmentManager(), ssf);
                        }
                    }
                });
            }
        });*/
        dialog.show();
    }

    public void loadAllTeams() {
        Log.i("teamModels", "" + teamModels.size());
        if (filteredTeamList != null)
            Log.i("filteredTeamList", "" + filteredTeamList.size());
        Log.i("Load:", "Teams");
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_team);
        dialog.setTitle("Select Team");

        mRecyclerTeamList = (RecyclerView) dialog.findViewById(R.id.teamListView);
        mRecyclerTeamList.setHasFixedSize(true);

        FloatingActionButton addNewTeam = (FloatingActionButton) dialog.findViewById(R.id.addNewTeam);
        addNewTeam.hide();

        if (filteredTeamList != null) {
            filteredTeamList = null;
        }

        mSearchTeam = (SearchView) dialog.findViewById(R.id.teamSearch);
        mSearchTeam.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toString().toLowerCase();
                filteredTeamList = new ArrayList<TeamModel>();
                for (TeamModel teamModel : teamModels) {
                    final String name = teamModel.getTeamName().toString().toLowerCase();
                    if (name.contains(newText)) {
                        Log.i("Team_ID::", "" + teamModel.get_teamId() + ", " + teamModel.getTeamName());
                        filteredTeamList.add(teamModel);
                    }
                }
                mTeamAdapter.setFilter(filteredTeamList, "team");
                return false;
            }
        });

        mTeamAdapter = new RecyclerAdapter(teamModels, getContext(), getFragmentManager(), "team");
        mRecyclerTeamList.setAdapter(mTeamAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerTeamList.setLayoutManager(llm);
        mTeamAdapter.SetOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<?> p, int position, String arrayName) {
                if (filteredTeamList != null && filteredTeamList.size() != 0) {
                    Log.i("Clicked filter", "" + filteredTeamList.get(position).getTeamName());
                    if (onTeam.equalsIgnoreCase("one")) {
                        Log.i("Clicked_1", "" + filteredTeamList.get(position).getTeamName());
                        if (teamAModel != null) {
                            teamModels.add(teamAModel);
                        }
                        teamAModel = filteredTeamList.get(position);
                    } else {
                        Log.i("Clicked_2", "" + filteredTeamList.get(position).getTeamName());
                        if (teamBModel != null) {
                            teamModels.add(teamBModel);
                        }
                        teamBModel = filteredTeamList.get(position);
                    }

                    //Remove team  from all  team list
                    Comparator<TeamModel> teamModelComparator = new TeamModelComparator();
                    Collections.sort(teamModels, teamModelComparator);

                    for (TeamModel tArray : filteredTeamList) {
                        // Passing a object to be searched
                        int index = Collections.binarySearch(teamModels, new TeamModel((int) tArray.get_teamId()), teamModelComparator);
                        teamModels.remove(index);
                    }

                    filteredTeamList.remove(position);
                } else {
                    Log.i("Clicked", "" + teamModels.get(position).getTeamName());
                    if (onTeam.equalsIgnoreCase("one")) {
                        if (teamAModel != null) {
                            teamModels.add(teamAModel);
                        }
                        teamAModel = teamModels.get(position);
                    } else {
                        if (teamBModel != null) {
                            teamModels.add(teamBModel);
                        }
                        teamBModel = teamModels.get(position);
                    }
                    teamModels.remove(position);
                }
                mTeamAdapter.notifyItemRemoved(position);
                mTeamAdapter.notifyDataSetChanged();
                dialog.dismiss();
                createNewMatch();
            }

            @Override
            public void onRemovePlayer(int position) {

            }
        });
        dialog.show();
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
