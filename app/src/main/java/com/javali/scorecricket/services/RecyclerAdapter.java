package com.javali.scorecricket.services;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by shashankshivakumar on 10/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<PlayerModel> playerDataSet;
    private Context mContext;
    private FragmentManager mFragmentManager;
    OnItemClickListener mItemClickListener;
    private ArrayList<TeamModel> teamDataSet;
    //private ArrayList<MatchModel> matchDataSet;
    private String modelName = "";
    private int playerPageInfo = -1;

    public RecyclerAdapter(ArrayList<?> dataObject, Context context, FragmentManager fragmentManager, String modelName) {
        this.modelName = modelName;
        this.mContext = context;
        this.mFragmentManager = fragmentManager;

        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("teamPlayersList") || modelName.equalsIgnoreCase("addPlayersToTeam")) {
            playerDataSet = (ArrayList<PlayerModel>) dataObject;
        } else {
            teamDataSet = (ArrayList<TeamModel>) dataObject;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = null;
        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, playerDataSet);
        } else if (modelName.equalsIgnoreCase("teamPlayersList")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_players_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, playerDataSet);
        } else if (modelName.equalsIgnoreCase("team")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, teamDataSet, "team");
        }

        if (modelName.equalsIgnoreCase("addPlayersToTeam")) {
            viewHolder.tvFullName.setTextSize(14);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam")) {
            PlayerModel playerModel = playerDataSet.get(position);
            holder.tvFullName.setText(playerModel.getFirstName() + " " + playerModel.getLastName());
        } else if (modelName.equalsIgnoreCase("teamPlayersList")) {
            PlayerModel playerModel = playerDataSet.get(position);
            holder.tvTeamFullName.setText(playerModel.getFirstName() + " " + playerModel.getLastName());
        } else if (modelName.equalsIgnoreCase("team")) {
            TeamModel teamModel = teamDataSet.get(position);
            holder.tvFullName.setText(teamModel.getTeamName().toString());
        }
    }

    @Override
    public int getItemCount() {
        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam") || modelName.equalsIgnoreCase("teamPlayersList")) {
            return playerDataSet.size();
        } else if (modelName.equalsIgnoreCase("team")) {
            return teamDataSet.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvFullName;
        private TextView tvTeamFullName;
        private ArrayList<PlayerModel> mPlayerModels = new ArrayList<PlayerModel>();
        private ArrayList<TeamModel> mTeamModels = new ArrayList<TeamModel>();
        private Context mCtx;
        private ImageButton removePlayer;

        public MyViewHolder(View itemView, Context context, ArrayList<PlayerModel> playerModels) {
            super(itemView);
            this.mPlayerModels = playerModels;
            this.mCtx = context;
            itemView.setOnClickListener(this);
            tvFullName = (TextView) itemView.findViewById(R.id.playerFullName);
            tvTeamFullName = (TextView) itemView.findViewById(R.id.playerName);
            if (modelName.equalsIgnoreCase("teamPlayersList")) {
                removePlayer = (ImageButton) itemView.findViewById(R.id.imageButton);
                removePlayer.setOnClickListener(this);
            }
        }

        public MyViewHolder(View itemView, Context context, ArrayList<TeamModel> teamModels, String team) {
            super(itemView);
            this.mTeamModels = teamModels;
            this.mCtx = context;
            itemView.setOnClickListener(this);
            tvFullName = (TextView) itemView.findViewById(R.id.teamFullName);
            tvTeamFullName = (TextView) itemView.findViewById(R.id.playerName);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.i("inside", " onClick = " + modelName);

            ArrayList<PlayerModel> playerElement = new ArrayList<>();
            ArrayList<TeamModel> teamElement = new ArrayList<>();

            if (mItemClickListener != null) {
                if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam") || modelName.equalsIgnoreCase("teamPlayersList")) {
                    playerElement.add(playerDataSet.get(position));
                    Log.i("Returning", "" + playerElement.get(0).getLastName());
                    if (v.getId() == R.id.imageButton) {
                        Log.i("Image button", "was clicked");
                        mItemClickListener.onRemovePlayer(position);
                    } else {
                        mItemClickListener.onItemClick(playerElement, position, modelName);
                    }
                } else if (modelName.equalsIgnoreCase("team")) {
                    teamElement.add(teamDataSet.get(position));
                    Log.i("Returning", "" + teamDataSet.get(0).getTeamName());
                    mItemClickListener.onItemClick(teamElement, position, modelName);
                }
            }
        }

    }

    public void setFilter(ArrayList<?> newFilterdList, String listName) {
        if (listName.equalsIgnoreCase("player")) {
            playerDataSet = new ArrayList<>();
            playerDataSet.addAll((Collection<? extends PlayerModel>) newFilterdList);
        } else {
            teamDataSet = new ArrayList<>();
            teamDataSet.addAll((Collection<? extends TeamModel>) newFilterdList);
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClick(ArrayList<?> p, int position, String arrayName);

        public void onRemovePlayer(int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}