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
import com.javali.scorecricket.model.MatchModel;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by shashankshivakumar on 10/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<PlayerModel> playerDataSet;
    private Context mContext;
    private FragmentManager mFragmentManager;
    OnItemClickListener mItemClickListener;
    private ArrayList<TeamModel> teamDataSet;
    private ArrayList<MatchModel> matchDataSet;
    private String modelName = "";
    private int playerPageInfo = -1;

    public RecyclerAdapter(ArrayList<?> dataObject, Context context, FragmentManager fragmentManager, String modelName) {
        this.modelName = modelName;
        this.mContext = context;
        this.mFragmentManager = fragmentManager;

        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("teamPlayersList") || modelName.equalsIgnoreCase("addPlayersToTeam")) {
            playerDataSet = (ArrayList<PlayerModel>) dataObject;
        } else if (modelName.equalsIgnoreCase("team")) {
            teamDataSet = (ArrayList<TeamModel>) dataObject;
        } else if (modelName.equalsIgnoreCase("match")) {
            matchDataSet = (ArrayList<MatchModel>) dataObject;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = null;
        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, playerDataSet, modelName);
        } else if (modelName.equalsIgnoreCase("teamPlayersList")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_players_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, playerDataSet, modelName);
        } else if (modelName.equalsIgnoreCase("team")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, teamDataSet, "team");
        } else if (modelName.equalsIgnoreCase("match")) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list, parent, false);
            viewHolder = new MyViewHolder(mView, mContext, matchDataSet, "match");
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
        } else if (modelName.equalsIgnoreCase("match")) {
            MatchModel matchModel = matchDataSet.get(position);
            holder.tvTeamOne.setText(matchModel.getTeam_a().getTeamName().toString());
            holder.tvTeamTwo.setText(matchModel.getTeam_b().getTeamName().toString());
            String date = FragmentServices.formatDate(matchModel.getMatchDate().toString());
            holder.tvMatchDate.setText(date);
            holder.tvMatchProgress.setText(matchModel.getProgress().toString());
            holder.tvTeamOneScore.setText(matchModel.getScore_one());
            holder.tvTeamTwoScore.setText(matchModel.getScore_two());
            holder.tvResult.setText(matchModel.getResult());
        }
    }

    @Override
    public int getItemCount() {
        if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam") || modelName.equalsIgnoreCase("teamPlayersList")) {
            return playerDataSet.size();
        } else if (modelName.equalsIgnoreCase("team")) {
            return teamDataSet.size();
        } else if (modelName.equalsIgnoreCase("match")) {
            return matchDataSet.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvFullName;
        private TextView tvTeamFullName;
        private ArrayList<PlayerModel> mPlayerModels = new ArrayList<PlayerModel>();
        private ArrayList<TeamModel> mTeamModels = new ArrayList<TeamModel>();
        private ArrayList<MatchModel> mMatchModels = new ArrayList<MatchModel>();
        private Context mCtx;
        private ImageButton removePlayer;

        private TextView tvTeamOne;
        private TextView tvTeamTwo;
        private TextView tvMatchDate;
        private TextView tvMatchProgress;
        private TextView tvTeamOneScore;
        private TextView tvTeamTwoScore;
        private TextView tvResult;

        //public MyViewHolder(View itemView, Context context, ArrayList<PlayerModel> playerModels) {
        public MyViewHolder(View itemView, Context context, ArrayList<?> dataObject, String name) {
            super(itemView);
            this.mCtx = context;
            Log.i("modelName", "" + modelName);
            if (modelName.equalsIgnoreCase("player") || modelName.equalsIgnoreCase("addPlayersToTeam") || modelName.equalsIgnoreCase("teamPlayersList")) {
                this.mPlayerModels = (ArrayList<PlayerModel>) dataObject;

                if (!modelName.equalsIgnoreCase("teamPlayersList")) {
                    tvFullName = (TextView) itemView.findViewById(R.id.playerFullName);
                    tvFullName.setSelected(true);
                    tvFullName.setSingleLine();
                }

                if (modelName.equalsIgnoreCase("teamPlayersList")) {
                    tvTeamFullName = (TextView) itemView.findViewById(R.id.playerName);
                    tvTeamFullName.setSelected(true);
                    tvTeamFullName.setSingleLine();
                    removePlayer = (ImageButton) itemView.findViewById(R.id.imageButton);
                    removePlayer.setOnClickListener(this);
                }

            } else if (modelName.equalsIgnoreCase("team")) {
                this.mTeamModels = (ArrayList<TeamModel>) dataObject;
                tvFullName = (TextView) itemView.findViewById(R.id.teamFullName);
                tvTeamFullName = (TextView) itemView.findViewById(R.id.playerName);
            }else if (modelName.equalsIgnoreCase("match")) {
                this.mMatchModels = (ArrayList<MatchModel>) dataObject;
                tvTeamOne = (TextView) itemView.findViewById(R.id.match_team1);
                tvTeamTwo = (TextView) itemView.findViewById(R.id.match_team2);
                tvMatchDate = (TextView) itemView.findViewById(R.id.match_date);
                tvMatchProgress = (TextView) itemView.findViewById(R.id.match_progress);
                tvMatchProgress.setSelected(true);
                tvMatchProgress.setSingleLine();
                tvTeamOneScore = (TextView) itemView.findViewById(R.id.team1_score);
                tvTeamTwoScore = (TextView) itemView.findViewById(R.id.team2_score);
                tvResult = (TextView) itemView.findViewById(R.id.result);
                tvResult.setSelected(true);
                tvResult.setSingleLine();
            }
            itemView.setOnClickListener(this);
        }

        /*public MyViewHolder(View itemView, Context context, ArrayList<TeamModel> teamModels, String team) {
            super(itemView);
            this.mTeamModels = teamModels;
            this.mCtx = context;
            itemView.setOnClickListener(this);
            tvFullName = (TextView) itemView.findViewById(R.id.teamFullName);
            tvTeamFullName = (TextView) itemView.findViewById(R.id.playerName);
        }*/

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.i("inside", " onClick = " + modelName);

            ArrayList<PlayerModel> playerElement = new ArrayList<>();
            ArrayList<TeamModel> teamElement = new ArrayList<>();
            ArrayList<MatchModel> matchElement = new ArrayList<>();

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
                } else if (modelName.equalsIgnoreCase("match")) {
                    matchElement.add(matchDataSet.get(position));
                    Log.i("Returning", "" + matchDataSet.get(position).get_matchId());
                    mItemClickListener.onItemClick(matchElement, position, modelName);
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