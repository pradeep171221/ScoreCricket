package com.javali.scorecricket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shashankshivakumar on 11/28/17.
 */

public class BowlingModel implements Parcelable {
    private int matchId;
    private int teamId;
    private int playerId;
    private Double overs;
    private int runs;
    private int wicket;
    private int wide;
    private int no_ball;
    private Double econ;
    private int b_striker;
    private ArrayList<BowlingModel> bowlingModelsArray;

    public BowlingModel() {}

    protected BowlingModel(Parcel in) {
        matchId = in.readInt();
        teamId = in.readInt();
        playerId = in.readInt();
        if (in.readByte() == 0) {
            overs = null;
        } else {
            overs = in.readDouble();
        }
        runs = in.readInt();
        wicket = in.readInt();
        if (in.readByte() == 0) {
            econ = null;
        } else {
            econ = in.readDouble();
        }
        wide = in.readInt();
        no_ball = in.readInt();
        b_striker = in.readInt();
        bowlingModelsArray = in.createTypedArrayList(BowlingModel.CREATOR);
    }

    public static final Creator<BowlingModel> CREATOR = new Creator<BowlingModel>() {
        @Override
        public BowlingModel createFromParcel(Parcel in) {
            return new BowlingModel(in);
        }

        @Override
        public BowlingModel[] newArray(int size) {
            return new BowlingModel[size];
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

    public Double getOvers() {
        return overs;
    }

    public void setOvers(Double overs) {
        this.overs = overs;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public Double getEcon() {
        return econ;
    }

    public void setEcon(Double econ) {
        this.econ = econ;
    }

    public int getWide() {
        return wide;
    }

    public void setWide(int wide) {
        this.wide = wide;
    }

    public int getNo_ball() {
        return no_ball;
    }

    public void setNo_ball(int no_ball) {
        this.no_ball = no_ball;
    }

    public int getB_striker() {
        return b_striker;
    }

    public void setB_striker(int b_striker) {
        this.b_striker = b_striker;
    }

    public ArrayList<BowlingModel> getBowlingModelsArray() {
        return bowlingModelsArray;
    }

    public void setBowlingModelsArray(ArrayList<BowlingModel> bowlingModelsArray) {
        this.bowlingModelsArray = bowlingModelsArray;
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
        if (overs == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(overs);
        }
        dest.writeInt(runs);
        dest.writeInt(wicket);
        if (econ == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(econ);
        }
        dest.writeInt(wide);
        dest.writeInt(no_ball);
        dest.writeInt(b_striker);
        dest.writeTypedList(bowlingModelsArray);
    }
}
