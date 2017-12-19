package com.javali.scorecricket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shashankshivakumar on 10/13/17.
 */

public class TeamModel implements Parcelable {
    private long _teamId;
    private String teamName;
    private ArrayList<TeamModel> teamModels;

    public TeamModel() {}

    public TeamModel(int teamId) {
        this._teamId = teamId;
    }

    public TeamModel(int teamId, String teamName) {
        this._teamId = teamId;
        this.teamName = teamName;
    }

    protected TeamModel(Parcel in) {
        _teamId = in.readInt();
        teamName = in.readString();
        teamModels = in.createTypedArrayList(TeamModel.CREATOR);
    }

    public static final Creator<TeamModel> CREATOR = new Creator<TeamModel>() {
        @Override
        public TeamModel createFromParcel(Parcel in) {
            return new TeamModel(in);
        }

        @Override
        public TeamModel[] newArray(int size) {
            return new TeamModel[size];
        }
    };

    public long get_teamId() {
        return _teamId;
    }

    public void set_teamId(long _teamId) {
        this._teamId = _teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<TeamModel> getTeamModels() {
        return teamModels;
    }

    public void setTeamModels(ArrayList<TeamModel> teamModels) {
        this.teamModels = teamModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_teamId);
        dest.writeString(teamName);
    }
}
