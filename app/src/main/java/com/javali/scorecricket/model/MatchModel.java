package com.javali.scorecricket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shashankshivakumar on 10/13/17.
 */

public class MatchModel implements Parcelable{

    private long _matchId;
    private long team_aId;
    private long team_bId;
    private String venue;
    private String umpireA;
    private String umpireB;
    private int overs;
    private long toss_won;
    private String elected;
    private String progress;
    private String result;
    private String matchDate;
    private ArrayList<MatchModel> matchModels;

    public MatchModel() {}

    public MatchModel(ArrayList<MatchModel> matchModels) {
        this.matchModels = matchModels;
    }

    public MatchModel(long _matchId, long team_aId, long team_bId, String venue, String umpireA, String umpireB, int overs, long toss_won, String elected, String progress, String result, String matchDate) {
        this._matchId = _matchId;
        this.team_aId = team_aId;
        this.team_bId = team_bId;
        this.venue = venue;
        this.umpireA = umpireA;
        this.umpireB = umpireB;
        this.overs = overs;
        this.toss_won = toss_won;
        this.elected = elected;
        this.progress = progress;
        this.result = result;
        this.matchDate = matchDate;
    }

    protected MatchModel(Parcel in) {
        _matchId = in.readLong();
        team_aId = in.readLong();
        team_bId = in.readLong();
        venue = in.readString();
        umpireA = in.readString();
        umpireB = in.readString();
        overs = in.readInt();
        toss_won = in.readLong();
        elected = in.readString();
        progress = in.readString();
        result = in.readString();
        matchDate = in.readString();
        matchModels = in.createTypedArrayList(MatchModel.CREATOR);
    }

    public static final Creator<MatchModel> CREATOR = new Creator<MatchModel>() {
        @Override
        public MatchModel createFromParcel(Parcel in) {
            return new MatchModel(in);
        }

        @Override
        public MatchModel[] newArray(int size) {
            return new MatchModel[size];
        }
    };

    public long get_matchId() {
        return _matchId;
    }

    public void set_matchId(long _matchId) {
        this._matchId = _matchId;
    }

    public long getTeam_aId() {
        return team_aId;
    }

    public void setTeam_aId(long team_aId) {
        this.team_aId = team_aId;
    }

    public long getTeam_bId() {
        return team_bId;
    }

    public void setTeam_bId(long team_bId) {
        this.team_bId = team_bId;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getUmpireA() {
        return umpireA;
    }

    public void setUmpireA(String umpireA) {
        this.umpireA = umpireA;
    }

    public String getUmpireB() {
        return umpireB;
    }

    public void setUmpireB(String umpireB) {
        this.umpireB = umpireB;
    }

    public int getOvers() {
        return overs;
    }

    public void setOvers(int overs) {
        this.overs = overs;
    }

    public long getToss_won() {
        return toss_won;
    }

    public void setToss_won(long toss_won) {
        this.toss_won = toss_won;
    }

    public String getElected() {
        return elected;
    }

    public void setElected(String elected) {
        this.elected = elected;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public ArrayList<MatchModel> getMatchModels() {
        return matchModels;
    }

    public void setMatchModels(ArrayList<MatchModel> matchModels) {
        this.matchModels = matchModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_matchId);
        dest.writeLong(team_aId);
        dest.writeLong(team_bId);
        dest.writeString(venue);
        dest.writeString(umpireA);
        dest.writeString(umpireB);
        dest.writeInt(overs);
        dest.writeLong(toss_won);
        dest.writeString(elected);
        dest.writeString(progress);
        dest.writeString(result);
        dest.writeString(matchDate);
        dest.writeTypedList(matchModels);
    }
}
