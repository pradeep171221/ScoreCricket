package com.javali.scorecricket.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.services.FragmentCallBack;
import com.javali.scorecricket.services.FragmentServices;
import com.javali.scorecricket.services.MyDBAdapter;
import com.javali.scorecricket.services.PlayerModelComparator;
import com.javali.scorecricket.services.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PlayerFragment extends Fragment implements SearchView.OnQueryTextListener, FragmentCallBack {
    private SearchView playerSearch;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private FloatingActionButton addNewPlayer;
    private MyDBAdapter mMyDBAdapter;
    private ArrayList<PlayerModel> playerArray;
    private ArrayList<PlayerModel> filteredList;

    private OnFragmentInteractionListener mListener;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMyDBAdapter = new MyDBAdapter(getActivity());
        playerArray = new ArrayList<PlayerModel>();
        playerArray = mMyDBAdapter.getAllPlayers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        addNewPlayer = (FloatingActionButton) view.findViewById(R.id.addNewPlayer);
        addNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("page", "addplayer");
                PlayerProcessFragment ppf = new PlayerProcessFragment();
                ppf.setArguments(args);
                ppf.setFragmentCallback(new FragmentCallBack() {
                    @Override
                    public void onDataSent(PlayerModel playerModel, boolean flag) {
                        getData(playerModel, flag);
                    }
                });
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_container, ppf);
//                fragmentTransaction.commit();

//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_container, ppf).commit();
                FragmentServices.addFragmentToStack("Add Player", getFragmentManager(), ppf);
            }
        });

        playerSearch = (SearchView) view.findViewById(R.id.searchPlayer);
        playerSearch.setOnQueryTextListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.playerListView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new RecyclerAdapter(playerArray, getContext(), getFragmentManager(), "player");
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<?> p, int position, String name) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("page", "editplayer");
                if (filteredList != null && filteredList.size() != 0) {
                    args.putParcelable("PlayerModel", (Parcelable) filteredList.get(position));
                } else {
                    args.putParcelable("PlayerModel", (Parcelable) playerArray.get(position));
                }
                PlayerProcessFragment ppf = new PlayerProcessFragment();
                ppf.setArguments(args);
                ppf.setFragmentCallback(new FragmentCallBack() {
                    @Override
                    public void onDataSent(PlayerModel playerModel, boolean flag) {
                        getData(playerModel, flag);
                    }
                });

                FragmentServices.closeInputSoftKeyboard(getActivity());
                //fragmentTransaction.replace(R.id.main_container, ppf).commit();
                FragmentServices.addFragmentToStack("Add Player", getFragmentManager(), ppf);
            }

            @Override
            public void onRemovePlayer(int position) {

            }
        });
        return view;
    }

    public void getData(PlayerModel playerModel, boolean flag) {
        if (playerModel != null) {

            if(flag) {
                playerArray.add(playerModel);
            } else {
                //Add player to array. If already existReplace player from all players list
                Comparator<PlayerModel> playerModelComparator = new PlayerModelComparator();
                ArrayList<PlayerModel> arr = new ArrayList<PlayerModel>();
                arr.add(playerModel);
                int index = Collections.binarySearch(playerArray, new PlayerModel(playerModel.get_id()), playerModelComparator);
                playerArray.remove(index);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_event:
                addPlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPlayer() {
        Bundle args = new Bundle();
        args.putString("page", "addplayer");
        PlayerProcessFragment ppf = new PlayerProcessFragment();
        ppf.setArguments(args);
        ppf.setFragmentCallback(new FragmentCallBack() {
            @Override
            public void onDataSent(PlayerModel playerModel, boolean flag) {
                getData(playerModel, flag);
            }
        });
        FragmentServices.addFragmentToStack("Add Player", getFragmentManager(), ppf);
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
        newText = newText.toLowerCase();
        filteredList = new ArrayList<PlayerModel>();
        for (PlayerModel playerModel : playerArray) {
            String name = playerModel.getFirstName().toLowerCase() + " " + playerModel.getLastName().toLowerCase();
            if (name.contains(newText)) {
                filteredList.add(playerModel);
            }
        }
        mAdapter.setFilter(filteredList, "player");
        return false;
    }

    @Override
    public void onDataSent(PlayerModel playerModel, boolean flag) {
        //Toast.makeText(getContext(), "" + yourData, Toast.LENGTH_SHORT).show();
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
