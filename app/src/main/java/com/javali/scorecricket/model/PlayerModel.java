package com.javali.scorecricket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shashankshivakumar on 10/13/17.
 */

public class PlayerModel implements Parcelable{

    private int _playerId;
    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String city;
    private String state;
    private String battingstyle;
    private String keeper;
    private int bowlingstyle;
    private ArrayList<PlayerModel> playerModels;

    public PlayerModel(){}

    public PlayerModel(int _playerId) {
        this._playerId = _playerId;
    }

    public PlayerModel(int _playerId, String firstName, String lastName, String gender, String dob, String city, String state, String battingstyle, String keeper, int bowlingstyle){
        this._playerId = _playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.city = city;
        this.state = state;
        this.battingstyle = battingstyle;
        this.keeper = keeper;
        this.bowlingstyle = bowlingstyle;
    }

    public PlayerModel(ArrayList<PlayerModel> playerModels) {
        this.playerModels = playerModels;
    }

    protected PlayerModel(Parcel in) {
        _playerId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        gender = in.readString();
        dob = in.readString();
        city = in.readString();
        state = in.readString();
        battingstyle = in.readString();
        keeper = in.readString();
        bowlingstyle = in.readInt();
        playerModels = in.createTypedArrayList(PlayerModel.CREATOR);
    }

    public static final Creator<PlayerModel> CREATOR = new Creator<PlayerModel>() {
        @Override
        public PlayerModel createFromParcel(Parcel in) {
            return new PlayerModel(in);
        }

        @Override
        public PlayerModel[] newArray(int size) {
            return new PlayerModel[size];
        }
    };

    public int get_id() {
        return _playerId;
    }

    public void set_id(int _playerId) {
        this._playerId = _playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBattingstyle() {
        return battingstyle;
    }

    public void setBattingstyle(String battingstyle) {
        this.battingstyle = battingstyle;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }

    public int getBowlingstyle() {
        return bowlingstyle;
    }

    public void setBowlingstyle(int bowlingstyle) {
        this.bowlingstyle = bowlingstyle;
    }

    public ArrayList<PlayerModel> getPlayerModels() {
        return playerModels;
    }

    public void setPlayerModels(ArrayList<PlayerModel> playerModels) {
        this.playerModels = playerModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_playerId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(battingstyle);
        dest.writeInt(bowlingstyle);

    }

    public String toString() {
        return( firstName + " " + lastName);
    }
}
