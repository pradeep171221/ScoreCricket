package com.javali.scorecricket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shashankshivakumar on 11/28/17.
 */

public class BattingModel implements Parcelable{
    private int matchId;
    private int teamId;
    private int playerId;
    private String outDetails;
    private int runs;
    private int ball;
    private int _4s;
    private int _6s;
    private Double strikeRate;
    private int striker;
    private ArrayList<BattingModel> battingModelArray;

    public BattingModel() {}

    protected BattingModel(Parcel in) {
        matchId = in.readInt();
        teamId = in.readInt();
        playerId = in.readInt();
        outDetails = in.readString();
        runs = in.readInt();
        ball = in.readInt();
        _4s = in.readInt();
        _6s = in.readInt();
        if (in.readByte() == 0) {
            strikeRate = null;
        } else {
            strikeRate = in.readDouble();
        }
        striker = in.readInt();
        battingModelArray = in.createTypedArrayList(BattingModel.CREATOR);
    }

    public static final Creator<BattingModel> CREATOR = new Creator<BattingModel>() {
        @Override
        public BattingModel createFromParcel(Parcel in) {
            return new BattingModel(in);
        }

        @Override
        public BattingModel[] newArray(int size) {
            return new BattingModel[size];
        }
    };

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getOutDetails() {
        return outDetails;
    }

    public void setOutDetails(String outDetails) {
        this.outDetails = outDetails;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBall() {
        return ball;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    public int get_4s() {
        return _4s;
    }

    public void set_4s(int _4s) {
        this._4s = _4s;
    }

    public int get_6s() {
        return _6s;
    }

    public void set_6s(int _6s) {
        this._6s = _6s;
    }

    public Double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(Double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public ArrayList<BattingModel> getBattingModelArray() {
        return battingModelArray;
    }

    public void setBattingModelArray(ArrayList<BattingModel> battingModelArray) {
        this.battingModelArray = battingModelArray;
    }

    public int getStriker() {
        return striker;
    }

    public void setStriker(int striker) {
        this.striker = striker;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(matchId);
        dest.writeInt(teamId);
        dest.writeInt(playerId);
        dest.writeString(outDetails);
        dest.writeInt(runs);
        dest.writeInt(ball);
        dest.writeInt(_4s);
        dest.writeLong(_6s);
        dest.writeDouble(strikeRate);
        dest.writeInt(striker);
        dest.writeTypedList(battingModelArray);
    }
}
