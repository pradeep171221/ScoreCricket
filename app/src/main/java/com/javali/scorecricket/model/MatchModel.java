package com.javali.scorecricket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shashankshivakumar on 10/13/17.
 */

public class MatchModel implements Parcelable{

    private long _matchId;
    private String venue;
    private String umpireA;
    private String umpireB;
    private int overs;
    private String elected;
    private String progress;
    private String result;
    private String matchDate;
    private long toss_won;
    private TeamModel team_a;
    private TeamModel team_b;
    private String score_one;
    private String score_two;
    private String extras;

    private ArrayList<MatchModel> matchModels;

    public MatchModel() {}

    public MatchModel(ArrayList<MatchModel> matchModels) {
        this.matchModels = matchModels;
    }

    public MatchModel(long _matchId, String venue, String umpireA, String umpireB, int overs, long toss_won, String elected, String progress, String result, String matchDate, String score_one, String score_two, String extras, TeamModel team_a, TeamModel team_b) {
        this._matchId = _matchId;
        this.venue = venue;
        this.umpireA = umpireA;
        this.umpireB = umpireB;
        this.overs = overs;
        this.toss_won = toss_won;
        this.elected = elected;
        this.progress = progress;
        this.result = result;
        this.matchDate = matchDate;
        this.score_one = score_one;
        this.score_two = score_two;
        this.extras = extras;
        this.team_a = team_a;
        this.team_b = team_b;
    }

    protected MatchModel(Parcel in) {
        _matchId = in.readLong();
        venue = in.readString();
        umpireA = in.readString();
        umpireB = in.readString();
        overs = in.readInt();
        elected = in.readString();
        progress = in.readString();
        result = in.readString();
        matchDate = in.readString();
        toss_won = in.readLong();
        team_a = in.readParcelable(TeamModel.class.getClassLoader());
        team_b = in.readParcelable(TeamModel.class.getClassLoader());
        score_one = in.readString();
        score_two = in.readString();
        extras = in.readString();
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

    public long getToss_won() {
        return toss_won;
    }

    public void setToss_won(long toss_won) {
        this.toss_won = toss_won;
    }

    public TeamModel getTeam_a() {
        return team_a;
    }

    public void setTeam_a(TeamModel team_a) {
        this.team_a = team_a;
    }

    public TeamModel getTeam_b() {
        return team_b;
    }

    public void setTeam_b(TeamModel team_b) {
        this.team_b = team_b;
    }

    public String getScore_one() {
        return score_one;
    }

    public void setScore_one(String score_one) {
        this.score_one = score_one;
    }

    public String getScore_two() {
        return score_two;
    }

    public void setScore_two(String score_two) {
        this.score_two = score_two;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
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
        dest.writeValue(team_a);
        dest.writeValue(team_b);
        dest.writeString(venue);
        dest.writeString(umpireA);
        dest.writeString(umpireB);
        dest.writeInt(overs);
        dest.writeLong(toss_won);
        dest.writeString(elected);
        dest.writeString(progress);
        dest.writeString(result);
        dest.writeString(matchDate);
        dest.writeString(score_one);
        dest.writeString(score_two);
        dest.writeString(extras);
        dest.writeTypedList(matchModels);
    }
}
