package com.javali.scorecricket.activity;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;
import com.javali.scorecricket.services.MyDBAdapter;
import com.javali.scorecricket.services.PlayerModelComparator;
import com.javali.scorecricket.services.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamManagementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TeamManagementFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {
    private OnFragmentInteractionListener mListener;

    private static final int MAXIMUM_PLAYERS = 16;
    private TextView teamName;
    private TeamModel mTeamModel;
    private RecyclerView playersTeamList;
    private RecyclerView playerList;
    private RecyclerAdapter mTeamAdapter;
    private RecyclerAdapter mPlayerAdapter;
    private MyDBAdapter mMyDBAdapter;
    private FloatingActionButton addPlayersToTeam;
    private ArrayList<PlayerModel> teamPlayersArray;
    private ArrayList<PlayerModel> playersArray;
    private ArrayList<PlayerModel> filteredPlayerList;
    private ArrayList<PlayerModel> innerPlayerArray;
    private int teamPlayersCount = 0;

    public TeamManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyDBAdapter = new MyDBAdapter(getContext());
        playersArray = mMyDBAdapter.getAllPlayers();
        mTeamModel = getArguments().getParcelable("TeamModel");
        teamPlayersArray = new ArrayList<PlayerModel>();
        teamPlayersArray = mMyDBAdapter.getAllPlayersInTeam(mTeamModel.get_teamId());
        teamPlayersCount = MAXIMUM_PLAYERS - teamPlayersArray.size();

        //Remove team players from all players list
        Comparator<PlayerModel> playerModelComparator = new PlayerModelComparator();
        Collections.sort(playersArray, playerModelComparator);

        for (PlayerModel pArray : teamPlayersArray) {
            // Passing a object to be searched
            int index = Collections.binarySearch(playersArray, new PlayerModel(pArray.get_id()), playerModelComparator);
            playersArray.remove(index);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_team_management, container, false);

        teamName = (TextView) mView.findViewById(R.id.teamDisplay);
        teamName.setText(mTeamModel.getTeamName().toString());

        Log.i("teamPlayersArray ::: ", "" + teamPlayersArray.size());
        Log.i("playersArray ::: ", "" + playersArray.size());

        //Team Player List
        playersTeamList = (RecyclerView) mView.findViewById(R.id.teamPlayerList);
        playersTeamList.setHasFixedSize(true);

        mTeamAdapter = new RecyclerAdapter(teamPlayersArray, getContext(), getFragmentManager(), "teamPlayersList");

        playersTeamList.setAdapter(mTeamAdapter);
        playersTeamList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTeamAdapter.SetOnItemClickListener(this);

        addPlayersToTeam = (FloatingActionButton) mView.findViewById(R.id.addPlayersToTeam);
        addPlayersToTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setTitle("Add Team");
                dialog.setContentView(R.layout.fragment_player);

                FloatingActionButton addNewPlayer = (FloatingActionButton) dialog.findViewById(R.id.addNewPlayer);
                addNewPlayer.hide();

                //Load All Players List
                playerList = (RecyclerView) dialog.findViewById(R.id.playerListView);
                playerList.setHasFixedSize(true);
                mPlayerAdapter = new RecyclerAdapter(playersArray, getContext(), getFragmentManager(), "addPlayersToTeam");
                playerList.setAdapter(mPlayerAdapter);
                playerList.setLayoutManager(new LinearLayoutManager(getActivity()));
                filteredPlayerList = null;
                mPlayerAdapter.SetOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ArrayList<?> p, int position, String arrayName) {
                        if (teamPlayersCount != 0) {
                            if (filteredPlayerList != null && filteredPlayerList.size() != 0) {
                                teamPlayersArray.add(filteredPlayerList.get(position));
                                mTeamAdapter.notifyItemChanged(position);
                                mTeamAdapter.notifyDataSetChanged();

                                //Delete the click object in playerModelArrayList
                                ArrayList<PlayerModel> delObj = new ArrayList<PlayerModel>();
                                delObj = (ArrayList<PlayerModel>) p;

                                Comparator<PlayerModel> playerModelComparator = new PlayerModelComparator();
                                Collections.sort(playersArray, playerModelComparator);

                                // Passing aa object to be searched
                                int index = Collections.binarySearch(playersArray, new PlayerModel(delObj.get(0).get_id()), playerModelComparator);
                                PlayerModel removedItem = null;
                                // binarySearch will return -1 if it does not find the element.
                                Log.i("index", "index +++++++++++++++++++++++++= " + index);
                                long data = 0;
                                data = mMyDBAdapter.addPlayerToTeam(mTeamModel.get_teamId(), filteredPlayerList.get(position).get_id());
                                Log.i("After player", "add data = " + data);
                                if (index > -1) {
                                    // This will remove the element
                                    removedItem = playersArray.remove(index);
                                }
                                filteredPlayerList.remove(position);
                                mPlayerAdapter.setFilter(filteredPlayerList, "player");
                            } else {
                                long data = 0;
                                data = mMyDBAdapter.addPlayerToTeam(mTeamModel.get_teamId(), playersArray.get(position).get_id());
                                Log.i("After player", "add data = " + data);
                                teamPlayersArray.add(playersArray.get(position));
                                mTeamAdapter.notifyDataSetChanged();
                                playersArray.remove(position);
                                mPlayerAdapter.setFilter(playersArray, "player");
                            }
                            teamPlayersCount--;
                        } else {
                            Toast.makeText(getContext(), "16 Players is the limit.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onRemovePlayer(int position) {

                    }
                });
                SearchView playerSearch = (SearchView) dialog.findViewById(R.id.searchPlayer);
                playerSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        newText = newText.toString().toLowerCase();
                        filteredPlayerList = new ArrayList<PlayerModel>();
                        for (PlayerModel playerModel : playersArray) {
                            final String name = playerModel.getFirstName().toLowerCase() + " " + playerModel.getLastName().toLowerCase();
                            if (name.contains(newText)) {
                                Log.i("Player_ID::", "" + playerModel.get_id() + ", " + playerModel.getLastName());
                                filteredPlayerList.add(playerModel);
                            }
                        }
                        mPlayerAdapter.setFilter(filteredPlayerList, "player");
                        return false;
                    }
                });

                dialog.show();
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int screenHeight = (int) (metrics.widthPixels * 0.70);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, screenHeight);
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

    @Override
    public void onItemClick(ArrayList<?> p, int position, String arrayName) {

    }

    @Override
    public void onRemovePlayer(int position) {
        Log.i("Image button", "clicked");
        Log.i("clicked", "" + teamPlayersArray.get(position).getLastName());
        long data = 0;

        data = mMyDBAdapter.removePlayerFromTeam(mTeamModel.get_teamId(), teamPlayersArray.get(position).get_id());
        Log.i("After player", "add data = " + data);
        teamPlayersCount++;
        playersArray.add(teamPlayersArray.get(position));
        teamPlayersArray.remove(position);
        mTeamAdapter.notifyItemRemoved(position);
        mTeamAdapter.notifyDataSetChanged();
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