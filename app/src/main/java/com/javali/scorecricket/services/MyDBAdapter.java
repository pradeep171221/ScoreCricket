package com.javali.scorecricket.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.javali.scorecricket.model.MatchModel;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by shashankshivakumar on 10/16/17.
 */

public class MyDBAdapter extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 9;

    //Database Name
    private static final String DATABASE_NAME = "scorecric";

    // Logcat tag
    private static final String LOG = MyDBAdapter.class.getName();

    private static final String[] bowlStyleArray = {"Right-arm Fast",
            "Right-arm Fast Medium",
            "Right-arm Medium Fast",
            "Right-arm Medium",
            "Left-arm Fast",
            "Left-arm Fast Medium",
            "Left-arm Medium Fast",
            "Left-arm Medium",
            "Off Break",
            "Leg Break",
            "Slow Left-arm Orthodox",
            "Slow Left-arm Chinaman"};

    // Table Names
    private static final String PLAYER = "player";
    private static final String BOWL_STYLE = "bowl_style";
    private static final String TEAM = "team";
    private static final String TEAM_DETAILS = "team_details";
    private static final String MATCH_DETAILS = "match_details";

    // Table Create Statements
    //Bowl_style table create statement
    private static final String CREATE_TABLE_BOWL_STYLE = "CREATE TABLE IF NOT EXISTS "
            + BOWL_STYLE +
            " ( bowlstyle_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "style TEXT NOT NULL );";

    // Player table create statement
    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE IF NOT EXISTS "
            + PLAYER + " ( playerid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "firstname TEXT NOT NULL, " +
            "lastname TEXT NOT NULL, " +
            "gender TEXT NOT NULL, " +
            "dob DATE, " +
            "city TEXT, " +
            "state TEXT, " +
            "battingstyle TEXT, " +
            "keeper TEXT, " +
            "bowlingstyle INTEGER," +
            "foreign key(bowlingstyle) references bowl_style(bowlstyle_id) );";

    // Team table create statement
    private static final String CREATE_TEAM = "CREATE TABLE IF NOT EXISTS "
            + TEAM + " ( teamid INTEGER primary key autoincrement, " +
            "teamname TEXT not null );";

    // Team_Details create statement
    private static final String CREATE_TEAM_DETAILS = "CREATE TABLE IF NOT EXISTS "
            + TEAM_DETAILS + " ( team_id INTEGER not null, " +
            "player_id INTEGER not null, " +
            "foreign key(team_id) references team(teamid), " +
            "foreign key(player_id) references player(teamid) );";

    // Match_Details create statement
    private static final String CREATE_MATCH_DETAILS = "CREATE TABLE IF NOT EXISTS "
            + MATCH_DETAILS + " ( matchid integer primary key autoincrement,\n" +
            "team1_id integer not null, " +
            "team2_id integer not null, " +
            "venue text, " +
            "umpire1 text, " +
            "umpire2 text, " +
            "overs integer not null, " +
            "toss_win integer not null, " +
            "elected text not null, " +
            "progress text not null, " +
            "result text not null, " +
            "match_date text not null, " +
            "foreign key(team1_id) references team(teamid), " +
            "foreign key(team2_id) references team(teamid), " +
            "foreign key(toss_win) references team(teamid) );";

    public MyDBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("MyDBAdapter", "onCreate");
        db.execSQL(CREATE_TABLE_BOWL_STYLE);
        Log.e(LOG, CREATE_TABLE_BOWL_STYLE);

        db.execSQL(CREATE_TABLE_PLAYER);
        Log.e(LOG, CREATE_TABLE_PLAYER);

        for (int i = 0; i < bowlStyleArray.length; i++) {
            Log.i("BowlStyleArray::::::", "" + bowlStyleArray[i]);
            ContentValues cv = new ContentValues();
            cv.put("style", bowlStyleArray[i].toString());
            db.insert(BOWL_STYLE, null, cv);
        }

        db.execSQL(CREATE_TEAM);
        Log.e(LOG, CREATE_TEAM);

        db.execSQL(CREATE_TEAM_DETAILS);
        Log.e(LOG, CREATE_TEAM_DETAILS);

        db.execSQL(CREATE_MATCH_DETAILS);
        Log.e(LOG, CREATE_MATCH_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MyDBAdapter", "onUpgrade");
        //db.execSQL("DROP TABLE IF EXISTS " + MATCH_DETAILS);
        //db.execSQL("DROP TABLE IF EXISTS " + TEAM_DETAILS);
        //db.execSQL("DROP TABLE IF EXISTS " + TEAM);
        //db.execSQL("DROP TABLE IF EXISTS " + PLAYER);
        //db.execSQL("DROP TABLE IF EXISTS " + BOWL_STYLE);
        onCreate(db);
    }

    public void inserToBowlStyle() {
        Log.w("MyDBAdapter", "inserToBowlStyle");
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < bowlStyleArray.length; i++) {
            Log.i("BowlStyleArray::::::", "" + bowlStyleArray[i]);
            ContentValues cv = new ContentValues();
            cv.put("style", bowlStyleArray[i].toString());
            db.insert(BOWL_STYLE, null, cv);
        }
    }

    public long insertPlayer(PlayerModel playerModel) {
        Log.w("MyDBAdapter", "insertPlayer");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;
        ContentValues cv = new ContentValues();
        cv.put("firstname", playerModel.getFirstName());
        cv.put("lastname", playerModel.getLastName());
        cv.put("gender", playerModel.getGender());
        cv.put("dob", playerModel.getDob());
        cv.put("city", playerModel.getCity());
        cv.put("state", playerModel.getState());
        cv.put("battingstyle", playerModel.getBattingstyle());
        cv.put("keeper", playerModel.getKeeper());
        cv.put("bowlingstyle", playerModel.getBowlingstyle());

        if (playerModel.get_id() > 0) {
            data = db.update(PLAYER, cv, "playerid='" + playerModel.get_id() + "'", null);
        } else {
            data = db.insert(PLAYER, null, cv);
        }
        return data;
    }

    public ArrayList<PlayerModel> getAllPlayers() {
        Log.w("MyDBAdapter", "getAllPlayers");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + PLAYER;
        ArrayList<PlayerModel> allPlayers = new ArrayList<PlayerModel>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                allPlayers.add(new PlayerModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9)));
            } while (cursor.moveToNext());
        }
        return allPlayers;
    }

    public ArrayList<String> getBowlStyles() {
        Log.w("MyDBAdapter", "getBowlStyles");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + BOWL_STYLE;
        ArrayList<String> bs = new ArrayList<String>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                bs.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.i("style", "" + bs.toString());
        return bs;
    }

    public long insertTeam(TeamModel teamModel) {
        Log.w("MyDBAdapter", "insertTeam");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;
        ContentValues cv = new ContentValues();
        cv.put("teamname", teamModel.getTeamName());
        data = db.insert(TEAM, null, cv);
        return data;
    }

    public ArrayList<TeamModel> getAllTeams() {
        Log.w("MyDBAdapter", "getAllTeams");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TEAM;
        ArrayList<TeamModel> allTeams = new ArrayList<TeamModel>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                allTeams.add(new TeamModel(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        return allTeams;
    }

    public long addPlayerToTeam(long teamId, long playerId) {
        Log.w("MyDBAdapter", "addPlayerToTeam");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;
        ContentValues cv = new ContentValues();
        cv.put("team_id", teamId);
        cv.put("player_id", playerId);
        data = db.insert(TEAM_DETAILS, null, cv);
        return data;
    }

    public long removePlayerFromTeam(long teamId, long playerId) {
        Log.w("MyDBAdapter", "addPlayerToTeam");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;
        data = db.delete(TEAM_DETAILS, "team_id = ? AND player_id = ?", new String[]{"" + teamId, "" + playerId});
        return data;
    }

    // Get all players in team
    public ArrayList<PlayerModel> getAllPlayersInTeam(long teamId) {
        Log.w("MyDBAdapter", "getAllPlayersInTeam");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + PLAYER + " WHERE playerid IN (SELECT player_id FROM " + TEAM_DETAILS + " WHERE team_id = " + teamId + ");";
        ArrayList<PlayerModel> playersInTeam = new ArrayList<PlayerModel>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playersInTeam.add(new PlayerModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9)));
            } while (cursor.moveToNext());
        }
        return playersInTeam;
    }

    // Create a New Match
    public long createMatch(MatchModel matchModel) {
        Log.w("MyDBAdapter", "createMatch");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;
        ContentValues cv = new ContentValues();
        cv.put("matchid", matchModel.get_matchId());
        cv.put("team1_id", matchModel.getTeam_aId());
        cv.put("team2_id", matchModel.getTeam_bId());
        cv.put("venue", matchModel.getVenue());
        cv.put("umpire1", matchModel.getUmpireA());
        cv.put("umpire2", matchModel.getUmpireB());
        cv.put("overs", matchModel.getOvers());
        cv.put("toss_win", matchModel.getToss_won());
        cv.put("elected", matchModel.getElected());
        cv.put("progress", matchModel.getProgress());
        cv.put("result", matchModel.getResult());
        cv.put("mdate", matchModel.getMatchDate());
        data = db.insert(MATCH_DETAILS, null, cv);
        return data;
    }
}
