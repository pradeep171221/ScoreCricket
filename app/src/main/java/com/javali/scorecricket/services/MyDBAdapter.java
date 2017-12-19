package com.javali.scorecricket.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.javali.scorecricket.model.BattingModel;
import com.javali.scorecricket.model.BowlingModel;
import com.javali.scorecricket.model.MatchModel;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by shashankshivakumar on 10/16/17.
 */

public class MyDBAdapter extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 20;

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
    private static final String BATTING_INFO = "batting_info";
    private static final String BOWLING_INFO = "bowling_info";

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
            "overs integer, " +
            "toss_win integer, " +
            "elected text, " +
            "progress text, " +
            "result text, " +
            "match_date text not null, " +
            "score_one text, " +
            "score_two text, " +
            "extras text, " +
            "foreign key(team1_id) references team(teamid), " +
            "foreign key(team2_id) references team(teamid), " +
            "foreign key(toss_win) references team(teamid) );";

    //Batting details create statement
    private static final String CREATE_BATTING_INFO = "CREATE TABLE IF NOT EXISTS "
            + BATTING_INFO + " ( bat_matchid integer not null," +
            " bat_teamid integer not null," +
            " bat_playerid integer not null," +
            " out_info text not null," +
            " runs integer not null," +
            " ball integer not null," +
            " four integer not null," +
            " six integer not null," +
            " strike_rate text not null," +
            " striker integer," +
            " primary key(bat_matchid, bat_teamid, bat_playerid)," +
            " foreign key(bat_matchid) references match_details(matchid)," +
            " foreign key(bat_teamid) references team(teamid)," +
            " foreign key(bat_playerid) references player(playerid) );";

    //Bowling details create statement
    private static final String CREATE_BOWLING_INFO = "CREATE TABLE IF NOT EXISTS "
            + BOWLING_INFO + " ( bmatch_id integer not null," +
            " bteam_id integer not null," +
            " bplayer_id integer not null," +
            " overs text not null," +
            " runs integer not null," +
            " wickets integer not null," +
            " wide integer not null," +
            " noball integer not null," +
            " econ text not null," +
            " b_striker integer," +
            " primary key(bmatch_id, bteam_id, bplayer_id)," +
            " foreign key(bmatch_id) references match_details(matchid)," +
            " foreign key(bteam_id) references team(teamid)," +
            " foreign key(bplayer_id) references player(playerid) );";

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

        db.execSQL(CREATE_BATTING_INFO);
        Log.e(LOG, CREATE_BATTING_INFO);

        db.execSQL(CREATE_BOWLING_INFO);
        Log.e(LOG, CREATE_BOWLING_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MyDBAdapter", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + BOWLING_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + BATTING_INFO);
        /*db.execSQL("DROP TABLE IF EXISTS " + MATCH_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TEAM_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + BOWL_STYLE);*/
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

    // Get player details
    public PlayerModel getPlayerData(int playerId) {
        Log.w("MyDBAdapter", "getPlayerDetails");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + PLAYER + " WHERE playerid = " + playerId + ";";
        PlayerModel playermodel = new PlayerModel();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playermodel = new PlayerModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9));
            } while (cursor.moveToNext());
        }
        return playermodel;
    }

    // Create a New Match
    public long createMatch(MatchModel matchModel) {
        Log.w("MyDBAdapter", "createMatch");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;

        ContentValues cv = new ContentValues();
        cv.put("team1_id", matchModel.getTeam_a().get_teamId());
        cv.put("team2_id", matchModel.getTeam_b().get_teamId());
        cv.put("venue", matchModel.getVenue());
        cv.put("umpire1", matchModel.getUmpireA());
        cv.put("umpire2", matchModel.getUmpireB());
        cv.put("overs", matchModel.getOvers());
        cv.put("toss_win", matchModel.getToss_won());
        cv.put("elected", matchModel.getElected());
        cv.put("progress", matchModel.getProgress());
        cv.put("result", matchModel.getResult());
        cv.put("match_date", "" + matchModel.getMatchDate());
        cv.put("score_one", "" + matchModel.getScore_one());
        cv.put("score_two", "" + matchModel.getScore_two());
        cv.put("extras", "0");
        data = db.insert(MATCH_DETAILS, null, cv);
        return data;
    }

    // Update Match
    public long updateMatch(MatchModel matchModel) {
        Log.w("MyDBAdapter", "updateMatch");
        SQLiteDatabase db = this.getWritableDatabase();
        long data = 0;

        ContentValues cv = new ContentValues();
        cv.put("team1_id", matchModel.getTeam_a().get_teamId());
        cv.put("team2_id", matchModel.getTeam_b().get_teamId());
        cv.put("venue", matchModel.getVenue());
        cv.put("umpire1", matchModel.getUmpireA());
        cv.put("umpire2", matchModel.getUmpireB());
        cv.put("overs", matchModel.getOvers());
        cv.put("toss_win", matchModel.getToss_won());
        cv.put("elected", matchModel.getElected());
        cv.put("progress", matchModel.getProgress());
        cv.put("result", matchModel.getResult());
        cv.put("match_date", "" + matchModel.getMatchDate());
        cv.put("score_one", "" + matchModel.getScore_one());
        cv.put("score_two", "" + matchModel.getScore_two());
        cv.put("extras", "" + matchModel.getExtras());

        data = db.update(MATCH_DETAILS, cv, "matchid = ?", new String[]{"" + matchModel.get_matchId()});
        return data;
    }

    // Get specific team details
    public TeamModel getTeamData(int teamId) {
        Log.w("MyDBAdapter", "getTeamData");
        SQLiteDatabase db = this.getWritableDatabase();
        TeamModel teamModel = new TeamModel();

        String query = "SELECT * FROM " + TEAM + " WHERE teamid = " + teamId + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                teamModel.set_teamId(teamId);
                teamModel.setTeamName(cursor.getString(1));
                Log.i("Team", "" + teamModel.get_teamId() + " - " + teamModel.getTeamName());
            } while (cursor.moveToNext());
        }
        return teamModel;
    }

    // Get all matches
    public ArrayList<MatchModel> getAllMatches() {
        Log.w("MyDBAdapter", "getAllMatches");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + MATCH_DETAILS + ";";
        ArrayList<MatchModel> matchModelsArray = new ArrayList<MatchModel>();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                TeamModel team_one = getTeamData(cursor.getInt(1));
                TeamModel team_two = getTeamData(cursor.getInt(2));
                matchModelsArray.add(new MatchModel(cursor.getInt(0), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), team_one, team_two));
            } while (cursor.moveToNext());
        }

        return matchModelsArray;
    }

    //insert or update batting info
    public long updateBatterInfo(BattingModel battingModel) {
        Log.w("MyDBAdapter", "doBatterInfo");
        SQLiteDatabase db = this.getWritableDatabase();

        long data = 0;
        ContentValues cv = new ContentValues();
        cv.put("bat_matchid", battingModel.getMatchId());
        cv.put("bat_teamid", battingModel.getTeamId());
        cv.put("bat_playerid", battingModel.getPlayerId());
        cv.put("out_info", battingModel.getOutDetails());
        cv.put("runs", battingModel.getRuns());
        cv.put("ball", battingModel.getBall());
        cv.put("four", battingModel.get_4s());
        cv.put("six", battingModel.get_6s());
        cv.put("strike_rate", battingModel.getStrikeRate());
        cv.put("striker", battingModel.getStriker());

        String query = "SELECT * FROM " + BATTING_INFO + " WHERE bat_matchid = " + battingModel.getMatchId() + " AND bat_teamid = " + battingModel.getTeamId() + " AND bat_playerid = " + battingModel.getPlayerId() + ";";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            data = db.update(BATTING_INFO, cv, "bat_matchid = ? AND bat_teamid = ? AND bat_playerid = ?", new String[]{"" + battingModel.getMatchId(), "" + battingModel.getTeamId(), ""+battingModel.getPlayerId()});
        } else {
            data = db.insert(BATTING_INFO, null, cv);
        }
        return data;
    }

    //insert or update bowling info
    public long updateBowlerInfo(BowlingModel bowlingModel) {
        Log.w("MyDBAdapter", "doBowlerInfo");
        SQLiteDatabase db = this.getWritableDatabase();

        long data = 0;
        ContentValues cv = new ContentValues();
        cv.put("bmatch_id", bowlingModel.getMatchId());
        cv.put("bteam_id", bowlingModel.getTeamId());
        cv.put("bplayer_id", bowlingModel.getPlayerId());
        cv.put("overs", bowlingModel.getOvers());
        cv.put("runs", bowlingModel.getRuns());
        cv.put("wickets", bowlingModel.getWicket());
        cv.put("wide", bowlingModel.getWide());
        cv.put("noball", bowlingModel.getNo_ball());
        cv.put("econ", bowlingModel.getEcon());
        cv.put("b_striker", bowlingModel.getB_striker());

        String query = "SELECT * FROM " + BOWLING_INFO + " WHERE bmatch_id = " + bowlingModel.getMatchId() + " AND bteam_id = " + bowlingModel.getTeamId() + " AND bplayer_id = " + bowlingModel.getPlayerId() + ";";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            data = db.update(BOWLING_INFO, cv, "bmatch_id = ? AND bteam_id = ? AND bplayer_id = ?", new String[]{"" + bowlingModel.getMatchId(), "" + bowlingModel.getTeamId(), ""+bowlingModel.getPlayerId()});
        } else {
            data = db.insert(BOWLING_INFO, null, cv);
        }
        return data;
    }

    public BowlingModel getInningsBowler(long matchId, long teamId) {
        Log.w("MyDBAdapter", "getInningsBowler");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + BOWLING_INFO + " WHERE bmatch_id = " + matchId + " AND bteam_id = " + teamId + " AND b_striker = 1;";
        BowlingModel bowler = new BowlingModel();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    bowler.setMatchId((int) matchId);
                    bowler.setTeamId((int) teamId);
                    bowler.setPlayerId(cursor.getInt(2));
                    bowler.setOvers(Double.parseDouble(cursor.getString(3)));
                    bowler.setRuns(cursor.getInt(4));
                    bowler.setWicket(cursor.getInt(5));
                    bowler.setWide(cursor.getInt(6));
                    bowler.setNo_ball(cursor.getInt(7));
                    bowler.setEcon(Double.parseDouble(cursor.getString(8)));
                    bowler.setB_striker(cursor.getInt(9));
                } while (cursor.moveToNext());
            }
        } else {
            bowler = null;
        }

        return bowler;
    }

    public BowlingModel getBowlingInfo(long matchId, long teamId, long playerId) {
        Log.w("MyDBAdapter", "getInningsBowler");
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + BOWLING_INFO + " WHERE bmatch_id = " + matchId + " AND bteam_id = " + teamId + " AND bplayer_id = " + playerId + ";";
        BowlingModel bowler = new BowlingModel();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    bowler.setMatchId((int) matchId);
                    bowler.setTeamId((int) teamId);
                    bowler.setPlayerId(cursor.getInt(2));
                    bowler.setOvers(Double.parseDouble(cursor.getString(3)));
                    bowler.setRuns(cursor.getInt(4));
                    bowler.setWicket(cursor.getInt(5));
                    bowler.setWide(cursor.getInt(6));
                    bowler.setNo_ball(cursor.getInt(7));
                    bowler.setEcon(Double.parseDouble(cursor.getString(8)));
                    bowler.setB_striker(cursor.getInt(9));
                } while (cursor.moveToNext());
            }
        } else {
            bowler = null;
        }

        return bowler;
    }

    public ArrayList<BattingModel> getInningsBatters(long matchId, long teamId) {
        Log.w("MyDBAdapter", "getInningsBatters");
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<BattingModel> batArr = new ArrayList<BattingModel>();
        String query = "SELECT * FROM " + BATTING_INFO + " WHERE bat_matchid = " + matchId + " AND bat_teamid = " + teamId + " AND out_info LIKE 'not%';";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    BattingModel battingModel = new BattingModel();
                    battingModel.setMatchId((int) matchId);
                    battingModel.setTeamId((int) teamId);
                    battingModel.setPlayerId(cursor.getInt(2));
                    battingModel.setOutDetails(cursor.getString(3));
                    battingModel.setRuns(cursor.getInt(4));
                    battingModel.setBall(cursor.getInt(5));
                    battingModel.set_4s(cursor.getInt(6));
                    battingModel.set_6s(cursor.getInt(7));
                    battingModel.setStrikeRate(Double.parseDouble(cursor.getString(8)));
                    battingModel.setStriker(cursor.getInt(9));

                    batArr.add(battingModel);
                } while (cursor.moveToNext());
            }
        } else {
            batArr = null;
        }

        return batArr;
    }
}
