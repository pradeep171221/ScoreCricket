package com.javali.scorecricket.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.TeamModel;
import com.javali.scorecricket.services.FragmentServices;
import com.javali.scorecricket.services.MyDBAdapter;
import com.javali.scorecricket.services.RecyclerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerTeamList;
    private SearchView mSearchTeam;
    private EditText mTeamName;
    private String sTeamName;
    private FloatingActionButton mAddTeam;
    private RecyclerAdapter mAdapter;
    private MyDBAdapter mMyDBAdapter;
    private ArrayList<TeamModel> teamArray;
    private ArrayList<TeamModel> filteredTeamList;

    public TeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mMyDBAdapter = new MyDBAdapter(getActivity());
        teamArray = new ArrayList<TeamModel>();
        teamArray = mMyDBAdapter.getAllTeams();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_team, container, false);

        mRecyclerTeamList = (RecyclerView) mView.findViewById(R.id.teamListView);
        mRecyclerTeamList.setHasFixedSize(true);

        mSearchTeam = (SearchView) mView.findViewById(R.id.teamSearch);
        mSearchTeam.setOnQueryTextListener(this);

        mTeamName = (EditText) mView.findViewById(R.id.teamName);

        mAddTeam = (FloatingActionButton) mView.findViewById(R.id.addNewTeam);
        mAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("New Team", "adding");
                final Dialog dialog = new Dialog(getContext());
                dialog.setTitle("Add Team");
                dialog.setContentView(R.layout.add_team);
                mTeamName = (EditText) dialog.findViewById(R.id.teamName);
                Button cancel = (Button) dialog.findViewById(R.id.cancelTeam);
                Button save = (Button) dialog.findViewById(R.id.saveTeam);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("Save Button", "Clicked");
                        sTeamName = mTeamName.getText().toString();

                        if (sTeamName.length() == 0) {
                            mTeamName.setError("Teamname required");
                            mTeamName.requestFocus();
                        } else {
                            TeamModel teamModel = new TeamModel();
                            teamModel.setTeamName(sTeamName);
                            long data = mMyDBAdapter.insertTeam(teamModel);
                            Log.i("DATA::::::::::::::", "" + data);
                            if (data == -1) {
                                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar snackbar = Snackbar.make(getView(), "Team saved", Snackbar.LENGTH_LONG);
                                snackbar.show();

                                teamModel.set_teamId(data);
                                teamArray.add(teamModel);
                                mAdapter = new RecyclerAdapter(teamArray, getContext(), getFragmentManager(), "Team");
                                mRecyclerTeamList.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();  // data set changed
                            }
                            dialog.dismiss();
                        }
                        FragmentServices.closeInputSoftKeyboard(getActivity()); // Close soft input keyboard
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });

        mAdapter = new RecyclerAdapter(teamArray, getContext(), getFragmentManager(), "team");
        mRecyclerTeamList.setAdapter(mAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerTeamList.setLayoutManager(llm);

        mAdapter.SetOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<?> p, int position, String arrayName) {
                Bundle args = new Bundle();
                args.putString("page", "teammanagement");
                if (filteredTeamList != null && filteredTeamList.size() != 0) {
                    Log.i("Clicked filter", "" + filteredTeamList.get(position).getTeamName());
                    args.putParcelable("TeamModel", (Parcelable) filteredTeamList.get(position));
                } else {
                    Log.i("Clicked", "" + teamArray.get(position).getTeamName());
                    args.putParcelable("TeamModel", (Parcelable) teamArray.get(position));
                }
                TeamManagementFragment tmf = new TeamManagementFragment();
                tmf.setArguments(args);
                FragmentServices.closeInputSoftKeyboard(getActivity());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main_container, tmf);
                fragmentTransaction.addToBackStack("team_management").commit();
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
                Toast.makeText(getContext(), "Add New Team", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toString().toLowerCase();
        filteredTeamList = new ArrayList<TeamModel>();
        for (TeamModel teamModel : teamArray) {
            final String name = teamModel.getTeamName().toString().toLowerCase();
            if (name.contains(newText)) {
                Log.i("Team_ID::", "" + teamModel.get_teamId() + ", " + teamModel.getTeamName());
                filteredTeamList.add(teamModel);
            }
        }
        mAdapter.setFilter(filteredTeamList, "team");
        return false;
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
