package com.javali.scorecricket.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.javali.scorecricket.R;
import com.javali.scorecricket.model.BattingModel;
import com.javali.scorecricket.model.BowlingModel;
import com.javali.scorecricket.model.MatchModel;
import com.javali.scorecricket.model.PlayerModel;
import com.javali.scorecricket.model.TeamModel;
import com.javali.scorecricket.services.MyDBAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoringSheetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ScoringSheetFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    NumberFormat formatter = new DecimalFormat("#0.0");
    private int innings = 1;
    private TextView innings1_team;
    private TextView innings1_score;
    private TextView innings2_team;
    private TextView innings2_score;
    private List<String> batsmenOrderStriker = new ArrayList<>();
    private List<String> batsmenOrderNonStriker = new ArrayList<>();
    private List<String> bowlerOrder = new ArrayList<>();

    private TextView runs;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button zero;
    private Button six;
    private Button noBall;
    private Button wide;
    private Button bye;
    private Button legBye;
    private Button clear;
    //private Button undo;
    private Button ok_score;
    private Button out;

    private MyDBAdapter mMyDBAdapter;
    private MatchModel matchModel;

    private TeamModel team_one;
    private TeamModel team_two;
    private TeamModel currentBattingTeam;
    private TeamModel currentBowlingTeam;

    private ArrayList<PlayerModel> teamOnePlayersArray;
    private ArrayList<PlayerModel> teamTwoPlayersArray;
    private ArrayList<PlayerModel> battingTeamPlayersArray;
    private ArrayList<PlayerModel> bowlingTeamPlayersArray;
    ArrayList<BattingModel> currentBatters;

    //Player One
    private TextView playerOne_TextView;
    private TextView p1_run;
    private TextView p1_ball;
    private TextView p1_four;
    private TextView p1_six;
    private TextView p1_sr;
    private int p1_run_data = 0;
    private int p1_ball_data = 0;
    private int p1_four_data = 0;
    private int p1_six_data = 0;
    private Double p1_sr_data = 0.0;

    //Player Two
    private TextView playerTwo_TextView;
    private TextView p2_run;
    private TextView p2_ball;
    private TextView p2_four;
    private TextView p2_six;
    private TextView p2_sr;
    private int p2_run_data = 0;
    private int p2_ball_data = 0;
    private int p2_four_data = 0;
    private int p2_six_data = 0;
    private Double p2_sr_data = 0.0;

    //Bowler
    private TextView current_bowler;
    private TextView bowler_runs;

    //Score updater for each ball
    private Boolean playerOne_Two = true;//checking which player is on strike playerOne(true) / playerTwo(false)
    private int currentBallScored = 0;
    private Boolean check_wide = false;
    private Boolean check_no_ball = false;
    private Boolean check_bye = false;
    private Boolean check_legbye = false;
    private int ballCount = 0;
    private Boolean check_four = false;
    private Boolean check_six = false;

    private int innScore = 0;
    private int innWicket = 0;
    private Double innOver = 0.0;
    private Boolean isOut = false;

    //Current Striker, Non Striker and Main Bowler
    private PlayerModel playerOnePlayModel;
    private PlayerModel playerTwoPlayModel;
    private PlayerModel currentBowlerPlayModel;

    private BattingModel pOneBatterModel;
    private BattingModel pTwoBatterModel;
    private BowlingModel pCurrentBowlerModel;

    public ScoringSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyDBAdapter = new MyDBAdapter(getActivity());
        matchModel = getArguments().getParcelable("match_model");
        team_one = matchModel.getTeam_a();
        team_two = matchModel.getTeam_b();
        teamOnePlayersArray = mMyDBAdapter.getAllPlayersInTeam(team_one.get_teamId());
        teamTwoPlayersArray = mMyDBAdapter.getAllPlayersInTeam(team_two.get_teamId());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_scoring_sheet, container, false);


        innings1_team = (TextView) mView.findViewById(R.id.innings1_team);
        innings1_score = (TextView) mView.findViewById(R.id.innings1_score);
        innings2_team = (TextView) mView.findViewById(R.id.innings2_team);
        innings2_score = (TextView) mView.findViewById(R.id.innings2_score);

        runs = (TextView) mView.findViewById(R.id.runs);
        runs.setSelected(true);
        runs.setSingleLine();

        clear = (Button) mView.findViewById(R.id.clear);
        //undo = (Button) mView.findViewById(R.id.undo);
        ok_score = (Button) mView.findViewById(R.id.ok_score);
        out = (Button) mView.findViewById(R.id.out);
        one = (Button) mView.findViewById(R.id.one);
        two = (Button) mView.findViewById(R.id.two);
        three = (Button) mView.findViewById(R.id.three);
        four = (Button) mView.findViewById(R.id.four);
        six = (Button) mView.findViewById(R.id.six);
        zero = (Button) mView.findViewById(R.id.zero);
        noBall = (Button) mView.findViewById(R.id.no_ball);
        wide = (Button) mView.findViewById(R.id.wide);
        bye = (Button) mView.findViewById(R.id.bye);
        legBye = (Button) mView.findViewById(R.id.leg_bye);

        playerOne_TextView = (TextView) mView.findViewById(R.id.playerOne_TextView);
        playerOne_TextView.setSelected(true);
        playerOne_TextView.setSingleLine();
        p1_run = (TextView) mView.findViewById(R.id.p1_run);
        p1_ball = (TextView) mView.findViewById(R.id.p1_ball);
        p1_four = (TextView) mView.findViewById(R.id.p1_four);
        p1_six = (TextView) mView.findViewById(R.id.p1_six);
        p1_sr = (TextView) mView.findViewById(R.id.p1_sr);

        playerTwo_TextView = (TextView) mView.findViewById(R.id.playerTwo_TextView);
        playerTwo_TextView.setSelected(true);
        playerTwo_TextView.setSingleLine();
        p2_run = (TextView) mView.findViewById(R.id.p2_run);
        p2_ball = (TextView) mView.findViewById(R.id.p2_ball);
        p2_four = (TextView) mView.findViewById(R.id.p2_four);
        p2_six = (TextView) mView.findViewById(R.id.p2_six);
        p2_sr = (TextView) mView.findViewById(R.id.p2_sr);

        current_bowler = (TextView) mView.findViewById(R.id.current_bowler);
        current_bowler.setSelected(true);
        current_bowler.setSingleLine();
        bowler_runs = (TextView) mView.findViewById(R.id.bowler_runs);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeInnings();
    }

    public void initializeInnings() {

        currentBatters = new ArrayList<BattingModel>();
        pCurrentBowlerModel = null;
        if ((matchModel.getProgress().toString()).contains("1")) {
            innings = 1;
            currentBatters = mMyDBAdapter.getInningsBatters(matchModel.get_matchId(), matchModel.getTeam_a().get_teamId());
            pCurrentBowlerModel = mMyDBAdapter.getInningsBowler(matchModel.get_matchId(), matchModel.getTeam_b().get_teamId());
        } else {
            innings = 2;
            currentBatters = mMyDBAdapter.getInningsBatters(matchModel.get_matchId(), matchModel.getTeam_b().get_teamId());
            pCurrentBowlerModel = mMyDBAdapter.getInningsBowler(matchModel.get_matchId(), matchModel.getTeam_a().get_teamId());
        }

        innings1_team.setText(team_one.getTeamName());
        innings2_team.setText(team_two.getTeamName());

        if (innings == 1) {
            currentBattingTeam = team_one;
            currentBowlingTeam = team_two;
            battingTeamPlayersArray = teamOnePlayersArray;
            bowlingTeamPlayersArray = teamTwoPlayersArray;
            if (matchModel.getScore_one().equalsIgnoreCase(" ")) {
                matchModel.setScore_one("0/0 0.0");
            }
            innings1_score.setText(matchModel.getScore_one());
            innings2_score.setText("Not yet started");
        } else {
            if (matchModel.getScore_two().equalsIgnoreCase("")) {
                matchModel.setScore_two("0/0 0.0");
            }
            innings2_score.setText(matchModel.getScore_two());
            innings1_score.setText(matchModel.getScore_one());
            currentBattingTeam = team_two;
            currentBowlingTeam = team_one;
            battingTeamPlayersArray = teamTwoPlayersArray;
            bowlingTeamPlayersArray = teamOnePlayersArray;
        }


        if (currentBatters == null) {
            if (innings == 1) {
                inningsStartSetup(currentBattingTeam, currentBowlingTeam, battingTeamPlayersArray, bowlingTeamPlayersArray);
            } else {
                inningsStartSetup(currentBowlingTeam, currentBattingTeam, bowlingTeamPlayersArray, battingTeamPlayersArray);
            }
        } else {
            if (pCurrentBowlerModel == null) {
                selectNextBowler();
            } else {
                currentBowlerPlayModel = mMyDBAdapter.getPlayerData(pCurrentBowlerModel.getPlayerId());
                String overs = "" + pCurrentBowlerModel.getOvers();
                ballCount = Integer.parseInt("" + overs.charAt(overs.length() - 1));
                current_bowler.setText(currentBowlerPlayModel.getFirstName() + " " + currentBowlerPlayModel.getLastName());
            }

            if (currentBatters.size() == 1) {
                if (currentBatters.get(0).getStriker() == 1) {
                    pOneBatterModel = currentBatters.get(0);
                    playerOnePlayModel = mMyDBAdapter.getPlayerData(pOneBatterModel.getPlayerId());

                    //playerTwoPlayModel = selectNextBatsmen();
                    selectNextBatsmen();
                    pTwoBatterModel = setBatter((int) currentBattingTeam.get_teamId(), playerTwoPlayModel.get_id(), 0);
                    long data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);
                } else {
                    pTwoBatterModel = currentBatters.get(0);
                    playerTwoPlayModel = mMyDBAdapter.getPlayerData(pTwoBatterModel.getPlayerId());

                    //playerOnePlayModel = selectNextBatsmen();
                    selectNextBatsmen();
                    pOneBatterModel = setBatter((int) matchModel.getTeam_a().get_teamId(), playerOnePlayModel.get_id(), 1);
                    long data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);
                }
            } else {
                if (currentBatters.get(0).getStriker() == 1) {
                    pOneBatterModel = currentBatters.get(0);
                    playerOnePlayModel = mMyDBAdapter.getPlayerData(pOneBatterModel.getPlayerId());
                    pTwoBatterModel = currentBatters.get(1);
                    playerTwoPlayModel = mMyDBAdapter.getPlayerData(pTwoBatterModel.getPlayerId());
                } else {
                    pOneBatterModel = currentBatters.get(1);
                    playerOnePlayModel = mMyDBAdapter.getPlayerData(pOneBatterModel.getPlayerId());
                    pTwoBatterModel = currentBatters.get(0);
                    playerTwoPlayModel = mMyDBAdapter.getPlayerData(pTwoBatterModel.getPlayerId());
                }
            }

            //Batsmen Data Striker
            playerOne_TextView.setText(playerOnePlayModel.getFirstName() + " " + playerOnePlayModel.getLastName());
            p1_run_data = pOneBatterModel.getRuns();
            p1_run.setText("" + p1_run_data);
            p1_ball_data = pOneBatterModel.getBall();
            p1_ball.setText("" + p1_ball_data);
            p1_four_data = pOneBatterModel.get_4s();
            p1_four.setText("" + p1_four_data);
            p1_six_data = pOneBatterModel.get_6s();
            p1_six.setText("" + p1_six_data);
            p1_sr_data = pOneBatterModel.getStrikeRate();
            p1_sr.setText("" + p1_sr_data);

            //Batsmen Data Striker
            playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + " " + playerTwoPlayModel.getLastName());
            p2_run_data = pTwoBatterModel.getRuns();
            p2_run.setText("" + p2_run_data);
            p2_ball_data = pTwoBatterModel.getBall();
            p2_ball.setText("" + p2_ball_data);
            p2_four_data = pTwoBatterModel.get_4s();
            p2_four.setText("" + p2_four_data);
            p2_six_data = pTwoBatterModel.get_6s();
            p2_six.setText("" + p2_six_data);
            p2_sr_data = pTwoBatterModel.getStrikeRate();
            p2_sr.setText("" + p2_sr_data);

        }

        scoreCricket();
    }

    public void selectNextBatsmen() {
        final Dialog ddialog = new Dialog(getContext());
        final PlayerModel[] batsmenModel = {new PlayerModel()};

        final long[] batsmenMain = {0};
        ddialog.setContentView(R.layout.scoring_initial_setup);
        ddialog.setCanceledOnTouchOutside(false);

        TextView init_setup_info = (TextView) ddialog.findViewById(R.id.init_setup_info);
        init_setup_info.setText("Select Next Batsmen");

        LinearLayout linearLayoutBatNonStrikerPlayers = (LinearLayout) ddialog.findViewById(R.id.batting_team_nonstriker);
        linearLayoutBatNonStrikerPlayers.setVisibility(View.GONE);

        LinearLayout linearLayoutBowlTeamName = (LinearLayout) ddialog.findViewById(R.id.bowling_teamname);
        linearLayoutBowlTeamName.setVisibility(View.GONE);

        LinearLayout linearLayoutBowlTeamPlayers = (LinearLayout) ddialog.findViewById(R.id.bowling_team_player);
        linearLayoutBowlTeamPlayers.setVisibility(View.GONE);

        TextView batTeamName = (TextView) ddialog.findViewById(R.id.init_batTeam);
        batTeamName.setText(currentBattingTeam.getTeamName());
        final Spinner striker = (Spinner) ddialog.findViewById(R.id.init_striker);
        final Button ok = (Button) ddialog.findViewById(R.id.init_ok);

        batsmenOrderStriker = new ArrayList<>();
        batsmenOrderStriker.add("- none -");
        for (PlayerModel playerModel : battingTeamPlayersArray) {
            batsmenOrderStriker.add(playerModel.getFirstName() + " " + playerModel.getLastName());
        }

        ArrayAdapter<String> battingStrikerAdapter = new ArrayAdapter<String>(ddialog.getContext(), R.layout.spinner_bowl_style, batsmenOrderStriker);
        striker.setAdapter(battingStrikerAdapter);

        striker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("striker", "" + striker.getSelectedItem());
                batsmenMain[0] = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("-----------------------", "-----------------------");
                Log.i("batsmen", "" + battingTeamPlayersArray.get((int) batsmenMain[0] - 1).getFirstName());
                Log.i("-----------------------", "-----------------------");
                batsmenModel[0] = battingTeamPlayersArray.get((int) batsmenMain[0] - 1);

                if (pOneBatterModel.getStriker() == 1) {
                    Toast.makeText(getContext(), ""+playerOnePlayModel.getFirstName(), Toast.LENGTH_SHORT).show();
                    pOneBatterModel.setStriker(0);
                    pOneBatterModel.setBall(pOneBatterModel.getBall() + 1);
                    pOneBatterModel.setOutDetails("Bowled "+currentBowlerPlayModel.getFirstName()+" "+currentBowlerPlayModel.getLastName());
                    pOneBatterModel.setStrikeRate(Double.valueOf((pOneBatterModel.getRuns() / pOneBatterModel.getBall()) * 100));
                    long data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);
                    playerOnePlayModel = batsmenModel[0];
                    pOneBatterModel = setBatter((int) currentBattingTeam.get_teamId(), playerOnePlayModel.get_id(), 1);
                    data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);

                    if((playerOne_TextView.getText().toString()).contains(playerOnePlayModel.getLastName())){
                        //Batsmen Data Striker
                        playerOne_TextView.setText(playerOnePlayModel.getFirstName() + " " + playerOnePlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                        p1_run_data = pOneBatterModel.getRuns();
                        p1_run.setText("" + p1_run_data);
                        p1_ball_data = pOneBatterModel.getBall();
                        p1_ball.setText("" + p1_ball_data);
                        p1_four_data = pOneBatterModel.get_4s();
                        p1_four.setText("" + p1_four_data);
                        p1_six_data = pOneBatterModel.get_6s();
                        p1_six.setText("" + p1_six_data);
                        p1_sr_data = pOneBatterModel.getStrikeRate();
                        p1_sr.setText("" + p1_sr_data);
                    } else {
                        //Batsmen Data Striker
                        playerTwo_TextView.setText(playerOnePlayModel.getFirstName() + " " + playerOnePlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                        p2_run_data = pOneBatterModel.getRuns();
                        p2_run.setText("" + p2_run_data);
                        p2_ball_data = pOneBatterModel.getBall();
                        p2_ball.setText("" + p2_ball_data);
                        p2_four_data = pOneBatterModel.get_4s();
                        p2_four.setText("" + p2_four_data);
                        p2_six_data = pOneBatterModel.get_6s();
                        p2_six.setText("" + p2_six_data);
                        p2_sr_data = pOneBatterModel.getStrikeRate();
                        p2_sr.setText("" + p2_sr_data);
                    }

                } else {
                    Toast.makeText(getContext(), ""+playerTwoPlayModel.getFirstName(), Toast.LENGTH_SHORT).show();
                    pTwoBatterModel.setStriker(0);
                    pTwoBatterModel.setBall(pTwoBatterModel.getBall() + 1);
                    pTwoBatterModel.setOutDetails("Bowled "+currentBowlerPlayModel.getFirstName()+" "+currentBowlerPlayModel.getLastName());
                    pTwoBatterModel.setStrikeRate(Double.valueOf((pOneBatterModel.getRuns() / pOneBatterModel.getBall()) * 100));
                    long data = mMyDBAdapter.updateBatterInfo(pTwoBatterModel);
                    playerTwoPlayModel = batsmenModel[0];
                    pTwoBatterModel = setBatter((int) currentBattingTeam.get_teamId(), playerTwoPlayModel.get_id(), 1);
                    data = mMyDBAdapter.updateBatterInfo(pTwoBatterModel);

                    if((playerOne_TextView.getText().toString()).contains(playerTwoPlayModel.getLastName())){
                        //Batsmen Data Striker
                        playerOne_TextView.setText(playerTwoPlayModel.getFirstName() + " " + playerTwoPlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                        p1_run_data = pTwoBatterModel.getRuns();
                        p1_run.setText("" + p1_run_data);
                        p1_ball_data = pTwoBatterModel.getBall();
                        p1_ball.setText("" + p1_ball_data);
                        p1_four_data = pTwoBatterModel.get_4s();
                        p1_four.setText("" + p1_four_data);
                        p1_six_data = pTwoBatterModel.get_6s();
                        p1_six.setText("" + p1_six_data);
                        p1_sr_data = pTwoBatterModel.getStrikeRate();
                        p1_sr.setText("" + p1_sr_data);
                    } else {
                        //Batsmen Data Striker
                        playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + " " + playerTwoPlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                        p2_run_data = pTwoBatterModel.getRuns();
                        p2_run.setText("" + p2_run_data);
                        p2_ball_data = pTwoBatterModel.getBall();
                        p2_ball.setText("" + p2_ball_data);
                        p2_four_data = pTwoBatterModel.get_4s();
                        p2_four.setText("" + p2_four_data);
                        p2_six_data = pTwoBatterModel.get_6s();
                        p2_six.setText("" + p2_six_data);
                        p2_sr_data = pTwoBatterModel.getStrikeRate();
                        p2_sr.setText("" + p2_sr_data);
                    }
                }
                isOut = true;
                clear.callOnClick();
                ok_score.callOnClick();

                ddialog.dismiss();
            }
        });
        ddialog.show();
    }

    public void selectNextBowler() {
        ballCount = 0;
        final Dialog dialog = new Dialog(getContext());
        final long[] bowlerMain = {0};
        dialog.setContentView(R.layout.scoring_initial_setup);
        dialog.setCanceledOnTouchOutside(false);

        TextView init_setup_info = (TextView) dialog.findViewById(R.id.init_setup_info);
        init_setup_info.setText("Select Next Bowler");

        LinearLayout linearLayoutBatTeamName = (LinearLayout) dialog.findViewById(R.id.batting_team_name);
        linearLayoutBatTeamName.setVisibility(View.GONE);

        LinearLayout linearLayoutBatStrikerPlayers = (LinearLayout) dialog.findViewById(R.id.batting_team_striker);
        linearLayoutBatStrikerPlayers.setVisibility(View.GONE);

        LinearLayout linearLayoutBatNonStrikerPlayers = (LinearLayout) dialog.findViewById(R.id.batting_team_nonstriker);
        linearLayoutBatNonStrikerPlayers.setVisibility(View.GONE);

        TextView bowlTeamName = (TextView) dialog.findViewById(R.id.init_bowlTeam);
        bowlTeamName.setText(currentBowlingTeam.getTeamName());
        final Spinner bowler = (Spinner) dialog.findViewById(R.id.init_bowler);
        Button ok = (Button) dialog.findViewById(R.id.init_ok);

        bowlerOrder = new ArrayList<>();
        bowlerOrder.add("- none -");
        for (PlayerModel playerModel : bowlingTeamPlayersArray) {
            bowlerOrder.add(playerModel.getFirstName() + " " + playerModel.getLastName());
        }

        ArrayAdapter<String> bowlingAdapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.spinner_bowl_style, bowlerOrder);
        bowler.setAdapter(bowlingAdapter);

        bowler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("bowler", "" + bowler.getSelectedItem());
                bowlerMain[0] = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("-----------------------", "-----------------------");
                Log.i("bowler", "" + bowlingTeamPlayersArray.get((int) bowlerMain[0] - 1).getFirstName());
                Log.i("-----------------------", "-----------------------");

                currentBowlerPlayModel = bowlingTeamPlayersArray.get((int) bowlerMain[0] - 1);
                pCurrentBowlerModel = mMyDBAdapter.getBowlingInfo(matchModel.get_matchId(), currentBowlingTeam.get_teamId(), currentBowlerPlayModel.get_id());
                if (pCurrentBowlerModel == null) {
                    pCurrentBowlerModel = setBowler((int) currentBowlingTeam.get_teamId(), currentBowlerPlayModel.get_id());
                    long data = mMyDBAdapter.updateBowlerInfo(pCurrentBowlerModel);
                }
                String overs = "" + pCurrentBowlerModel.getOvers();
                ballCount = Integer.parseInt("" + overs.charAt(overs.length() - 1));
                current_bowler.setText(currentBowlerPlayModel.getFirstName() + " " + currentBowlerPlayModel.getLastName());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void inningsStartSetup(TeamModel batTeam, TeamModel bowlTeam, final ArrayList<PlayerModel> batTeamPlayers, final ArrayList<PlayerModel> bowlTeamPlayers) {
        final boolean[] flag = {true};
        final Dialog dialog = new Dialog(getContext());

        final long[] batOne = {0};
        final long[] batTwo = {0};
        final long[] bowlerMain = {0};
        dialog.setContentView(R.layout.scoring_initial_setup);
        dialog.setCanceledOnTouchOutside(false);

        TextView batTeamName = (TextView) dialog.findViewById(R.id.init_batTeam);
        batTeamName.setText(matchModel.getTeam_a().getTeamName());
        final Spinner striker = (Spinner) dialog.findViewById(R.id.init_striker);
        final Spinner nonStriker = (Spinner) dialog.findViewById(R.id.init_nonstriker);
        TextView bowlTeamName = (TextView) dialog.findViewById(R.id.init_bowlTeam);
        bowlTeamName.setText(matchModel.getTeam_b().getTeamName());
        final Spinner bowler = (Spinner) dialog.findViewById(R.id.init_bowler);
        Button ok = (Button) dialog.findViewById(R.id.init_ok);

        batsmenOrderStriker = new ArrayList<>();
        batsmenOrderNonStriker = new ArrayList<>();
        batsmenOrderStriker.add("- none -");
        batsmenOrderNonStriker.add("- none -");
        for (PlayerModel playerModel : batTeamPlayers) {
            batsmenOrderStriker.add(playerModel.getFirstName() + " " + playerModel.getLastName());
            batsmenOrderNonStriker.add(playerModel.getFirstName() + " " + playerModel.getLastName());
        }
        bowlerOrder = new ArrayList<>();
        bowlerOrder.add("- none -");
        for (PlayerModel playerModel : bowlTeamPlayers) {
            bowlerOrder.add(playerModel.getFirstName() + " " + playerModel.getLastName());
        }

        ArrayAdapter<String> battingStrikerAdapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.spinner_bowl_style, batsmenOrderStriker);
        striker.setAdapter(battingStrikerAdapter);

        ArrayAdapter battingNonStrikerAdapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.spinner_bowl_style, batsmenOrderNonStriker);
        nonStriker.setAdapter(battingNonStrikerAdapter);

        ArrayAdapter<String> bowlingAdapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.spinner_bowl_style, bowlerOrder);
        bowler.setAdapter(bowlingAdapter);

        striker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("striker", "" + striker.getSelectedItem());
                batOne[0] = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nonStriker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("non striker", "" + nonStriker.getSelectedItem());
                batTwo[0] = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bowler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("bowler", "" + bowler.getSelectedItem());
                bowlerMain[0] = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("-----------------------", "-----------------------");
                Log.i("striker", "" + batTeamPlayers.get((int) batOne[0] - 1).getFirstName());
                Log.i("non striker", "" + batTeamPlayers.get((int) batTwo[0] - 1).getFirstName());
                Log.i("bowler", "" + bowlTeamPlayers.get((int) bowlerMain[0] - 1).getFirstName());
                Log.i("-----------------------", "-----------------------");

                playerOnePlayModel = batTeamPlayers.get((int) batOne[0] - 1);
                playerTwoPlayModel = batTeamPlayers.get((int) batTwo[0] - 1);
                currentBowlerPlayModel = bowlTeamPlayers.get((int) bowlerMain[0] - 1);

                playerOne_TextView.setText(playerOnePlayModel.getFirstName() + " " + playerOnePlayModel.getLastName());
                playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + " " + playerTwoPlayModel.getLastName());
                current_bowler.setText(currentBowlerPlayModel.getFirstName() + " " + currentBowlerPlayModel.getLastName());

                playerOne_TextView.setText(playerOne_TextView.getText().toString() + Html.fromHtml("<sup>*</sup>"));
                currentBallScored = 0;
                playerOne_Two = true;

                pOneBatterModel = setBatter((int) currentBattingTeam.get_teamId(), playerOnePlayModel.get_id(), 1);
                long data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);

                pTwoBatterModel = setBatter((int) currentBattingTeam.get_teamId(), playerTwoPlayModel.get_id(), 0);
                data = mMyDBAdapter.updateBatterInfo(pTwoBatterModel);

                pCurrentBowlerModel = setBowler((int) currentBowlingTeam.get_teamId(), currentBowlerPlayModel.get_id());
                data = mMyDBAdapter.updateBowlerInfo(pCurrentBowlerModel);

                flag[0] = false;
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (flag[0]) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        dialog.show();
    }

    public void scoreCricket() {
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runs.setText("");
                currentBallScored = 0;
                check_wide = false;
                check_no_ball = false;
                check_bye = false;
                check_legbye = false;
                check_four = false;
                check_six = false;
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 1) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.equalsIgnoreCase(""))
                            runs.setText("1");
                        else
                            runs.setText(runs.getText() + "+1");
                        currentBallScored += 1;
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 2) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.equalsIgnoreCase(""))
                            runs.setText("2");
                        else
                            runs.setText(runs.getText() + "+2");
                        currentBallScored += 2;
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 3) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.equalsIgnoreCase(""))
                            runs.setText("3");
                        else
                            runs.setText(runs.getText() + "+3");
                        currentBallScored += 3;
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    String s_runs = runs.getText().toString();
                    if (s_runs.contains("0")) {

                    } else if (s_runs.equalsIgnoreCase(""))
                        runs.setText("0");
                    else
                        runs.setText(runs.getText() + "+0");
                }
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 4) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.contains("4")) {

                        } else if (s_runs.contains("6")) {
                            alertMessage("Can't score 2 boundries on a ball.");
                        } else if (s_runs.equalsIgnoreCase("")) {
                            runs.setText("4");
                            currentBallScored += 4;
                            check_four = true;
                        } else {
                            runs.setText(runs.getText() + "+4");
                            currentBallScored += 4;
                            check_four = true;
                        }
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 6) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.contains("6")) {

                        } else if (s_runs.contains("4")) {
                            alertMessage("Can't score 2 boundries on a ball.");
                        } else if (s_runs.contains("b")) {
                            alertMessage("Bye and Legbye cannot have Six.");
                        } else if (s_runs.equalsIgnoreCase("")) {
                            runs.setText("6");
                            currentBallScored += 6;
                            check_six = true;
                        } else {
                            runs.setText(runs.getText() + "+6");
                            currentBallScored += 6;
                            check_six = true;
                        }
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        noBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 1) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.contains("n")) {

                        } else if (s_runs.equalsIgnoreCase("")) {
                            runs.setText("n");
                            check_no_ball = true;
                            check_wide = false;
                        } else {
                            runs.setText(runs.getText() + "+n");
                            check_no_ball = true;
                            check_wide = false;
                        }
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        wide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    if ((currentBallScored + 1) <= 20) {
                        String s_runs = runs.getText().toString();
                        if (s_runs.contains("w")) {

                        } else if (s_runs.contains("b")) {
                            alertMessage("Wide ball can't be Bye or LegBye and ViceVersa.");
                        } else if (s_runs.equalsIgnoreCase("")) {
                            runs.setText("w");
                            check_wide = true;
                        } else {
                            runs.setText(runs.getText() + "+w");
                            if (!s_runs.contains("n"))
                                check_wide = true;
                        }
                    } else {
                        alertMessage("Can't score more than 20 runs in a single ball.");
                    }
                }
            }
        });

        bye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    String s_runs = runs.getText().toString();
                    if (s_runs.contains("w") || s_runs.contains("lb")) {
                        alertMessage("Wide ball can't be Bye or LegBye and ViceVersa.");
                    } else if (s_runs.contains("b")) {

                    } else if (s_runs.equalsIgnoreCase("")) {
                        runs.setText("b");
                        check_bye = true;
                    } else {
                        runs.setText(runs.getText() + "+b");
                        check_bye = true;
                    }
                }
            }
        });

        legBye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount == 6) {
                    selectNextBowler();
                } else {
                    String s_runs = runs.getText().toString();
                    if (s_runs.contains("lb")) {

                    } else if (s_runs.contains("b") || s_runs.contains("w")) {
                        alertMessage("Wide ball can't be Bye or LegBye and ViceVersa.");
                    } else if (s_runs.equalsIgnoreCase("")) {
                        runs.setText("lb");
                        check_legbye = true;
                    } else {
                        runs.setText(runs.getText() + "+lb");
                        check_legbye = true;
                    }
                }
            }
        });

        ok_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(runs.getText().toString().equalsIgnoreCase(""))
                    currentBallScored = 0;
                currentBallScored += 0;
                int extras = 0;
                int _is4 = 0;
                int _is6 = 0;
                Boolean scoredWithBat = true;
                Boolean validBallCount = true;
                PlayerModel currentPlayer = checkPlayer();

                Log.i("check_no_ball    ", "" + check_no_ball);
                Log.i("check_wide       ", "" + check_wide);
                Log.i("check_bye        ", "" + check_bye);
                Log.i("check_legbye     ", "" + check_legbye);
                Log.i("check_four       ", "" + check_four);
                Log.i("check_six        ", "" + check_six);
                Log.i("currentBallScore ", "" + currentBallScored);
                Log.i("extras           ", "" + extras);

                if (check_no_ball == true && check_wide == true || check_no_ball == true && check_bye == true || check_no_ball == true && check_legbye == true) {
                    extras = currentBallScored + 1;
                    scoredWithBat = false;
                    validBallCount = false;
                    currentBallScored = 0;
                } else if (check_no_ball) {
                    extras += 1;
                    validBallCount = false;
                } else if (check_wide) {
                    extras += 1;
                    scoredWithBat = false;
                    validBallCount = false;
                } else if (check_bye || check_legbye) {
                    extras = currentBallScored;
                    scoredWithBat = false;
                    validBallCount = true;
                    currentBallScored = 0;
                }

                if (validBallCount) {
                    ballCount++;
                    pCurrentBowlerModel.setOvers(pCurrentBowlerModel.getOvers() + 0.1);
                    pCurrentBowlerModel.setRuns(pCurrentBowlerModel.getRuns() + currentBallScored);
                } else {
                    pCurrentBowlerModel.setRuns(pCurrentBowlerModel.getRuns() + currentBallScored + extras);
                    if (check_no_ball) {
                        pCurrentBowlerModel.setNo_ball(pCurrentBowlerModel.getNo_ball() + 1);
                    } else if (check_wide) {
                        pCurrentBowlerModel.setWide(pCurrentBowlerModel.getWide() + 1);
                    }
                }
                String overs = "" + pCurrentBowlerModel.getOvers();
                Double balls = (Double.parseDouble("" + overs.charAt(0)) * 6) + (Double.parseDouble("" + overs.charAt(overs.length() - 1)));
                pCurrentBowlerModel.setEcon(Double.valueOf(pCurrentBowlerModel.getRuns() / ((10 / 6) * balls)));
                if (ballCount < 6) {
                    pCurrentBowlerModel.setB_striker(1);
                } else {
                    pCurrentBowlerModel.setB_striker(0);
                    pCurrentBowlerModel.setOvers(pCurrentBowlerModel.getOvers() + 0.4);
                }
                long data = mMyDBAdapter.updateBowlerInfo(pCurrentBowlerModel);

                if(!isOut) {
                    if (pOneBatterModel.getStriker() == 1) {
                        if (scoredWithBat) {
                            pOneBatterModel.setRuns(pOneBatterModel.getRuns() + currentBallScored);
                            pOneBatterModel.setBall(pOneBatterModel.getBall() + 1);
                            if (check_four) {
                                pOneBatterModel.set_4s(pOneBatterModel.get_4s() + 1);
                            }
                            if (check_six) {
                                pOneBatterModel.set_6s(pOneBatterModel.get_6s() + 1);
                            }
                            Log.i("S/R:", "" + Double.valueOf((pOneBatterModel.getRuns() / pOneBatterModel.getBall()) * 100));
                            pOneBatterModel.setStrikeRate(Double.valueOf((pOneBatterModel.getRuns() / pOneBatterModel.getBall()) * 100));

                            if ((currentBallScored % 2) == 0 && ballCount < 6) {
                                pOneBatterModel.setStriker(1);
                                pTwoBatterModel.setStriker(0);
                                playerOne_Two = true;
                                playerOne_TextView.setText(playerOnePlayModel.getFirstName() + "" + playerOnePlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                                playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + "" + playerTwoPlayModel.getLastName());
                            } else {
                                if (ballCount < 6) {
                                    pTwoBatterModel.setStriker(1);
                                    pOneBatterModel.setStriker(0);
                                    playerOne_Two = false;
                                    playerOne_TextView.setText(playerOnePlayModel.getFirstName() + "" + playerOnePlayModel.getLastName());
                                    playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + "" + playerTwoPlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                                }
                            }
                            data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);
                            data = mMyDBAdapter.updateBatterInfo(pTwoBatterModel);
                        } else {
                            matchModel.setExtras(matchModel.getExtras() + extras);
                            data = mMyDBAdapter.updateMatch(matchModel);
                        }
                    } else {
                        if (scoredWithBat) {
                            pTwoBatterModel.setRuns(pTwoBatterModel.getRuns() + currentBallScored);
                            pTwoBatterModel.setBall(pTwoBatterModel.getBall() + 1);
                            if (check_four) {
                                pTwoBatterModel.set_4s(pTwoBatterModel.get_4s() + 1);
                            }
                            if (check_six) {
                                pTwoBatterModel.set_6s(pTwoBatterModel.get_6s() + 1);
                            }
                            Log.i("S/R:", "" + Double.valueOf((pTwoBatterModel.getRuns() / pTwoBatterModel.getBall()) * 100));
                            pTwoBatterModel.setStrikeRate(Double.valueOf((pTwoBatterModel.getRuns() / pTwoBatterModel.getBall()) * 100));

                            if ((currentBallScored % 2) == 0 && ballCount < 6) {
                                pTwoBatterModel.setStriker(1);
                                pOneBatterModel.setStriker(0);
                                playerOne_Two = false;
                                playerOne_TextView.setText(playerOnePlayModel.getFirstName() + "" + playerOnePlayModel.getLastName());
                                playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + "" + playerTwoPlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                            } else {
                                if (ballCount < 6) {
                                    pTwoBatterModel.setStriker(0);
                                    pOneBatterModel.setStriker(1);
                                    playerOne_Two = true;
                                    playerOne_TextView.setText(playerOnePlayModel.getFirstName() + "" + playerOnePlayModel.getLastName() + Html.fromHtml("<sup>*</sup>"));
                                    playerTwo_TextView.setText(playerTwoPlayModel.getFirstName() + "" + playerTwoPlayModel.getLastName());
                                }
                            }
                            data = mMyDBAdapter.updateBatterInfo(pOneBatterModel);
                            data = mMyDBAdapter.updateBatterInfo(pTwoBatterModel);
                        } else {
                            matchModel.setExtras(matchModel.getExtras() + extras);
                            data = mMyDBAdapter.updateMatch(matchModel);
                        }
                    }
                } else {
                    isOut = false;
                }

                innScore = innScore + currentBallScored + extras;
                if(validBallCount)
                    innOver = innOver + 0.1;

                if (ballCount == 6) {
                    innOver = innOver + 0.4;
                }

                if (innings == 1) {
                    matchModel.setScore_one(innScore + "/" + innWicket + " " + formatter.format(innOver));
                    innings1_score.setText(matchModel.getScore_one());
                } else {
                    matchModel.setScore_two(innScore + "/" + innWicket + " " + formatter.format(innOver));
                    innings2_score.setText(matchModel.getScore_two());
                }

                //call to DB

                updateBatsmen();

                if (ballCount == 6) {
                    String s = ""+innOver;
                    int ov = Integer.parseInt(""+s.charAt(0));
                    if(ov != matchModel.getOvers())
                        selectNextBowler();
                    else {
                        innings = 2;
                        matchModel.setProgress("2nd Inning in progress");
                        matchModel.setResult(currentBattingTeam.getTeamName() + " scored "+matchModel.getScore_one());
                        data = mMyDBAdapter.updateMatch(matchModel);
                        innOver = 0.0;
                        innWicket = 0;
                        innScore = 0;
                        initializeInnings();
                    }
                }
                clear.callOnClick();
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innWicket++;
                selectNextBatsmen();
            }
        });
    }

    public void setup_bowler(BowlingModel bowler) {
        //skncskanas;
    }

    public void updateBatsmen() {
        p1_run.setText("" + pOneBatterModel.getRuns());
        p1_ball.setText("" + pOneBatterModel.getBall());
        p1_four.setText("" + pOneBatterModel.get_4s());
        p1_six.setText("" + pOneBatterModel.get_6s());
        p1_sr.setText("" + pOneBatterModel.getStrikeRate());

        p2_run.setText("" + pTwoBatterModel.getRuns());
        p2_ball.setText("" + pTwoBatterModel.getBall());
        p2_four.setText("" + pTwoBatterModel.get_4s());
        p2_six.setText("" + pTwoBatterModel.get_6s());
        p2_sr.setText("" + pTwoBatterModel.getStrikeRate());
    }

    public BattingModel setBatter(int teamId, int playerId, int striker) {
        BattingModel battingModel = new BattingModel();
        battingModel.setMatchId((int) matchModel.get_matchId());
        battingModel.setTeamId(teamId);
        battingModel.setPlayerId(playerId);
        battingModel.setOutDetails("not out");
        battingModel.setRuns(0);
        battingModel.setBall(0);
        battingModel.set_4s(0);
        battingModel.set_6s(0);
        battingModel.setStrikeRate(0.0);
        battingModel.setStriker(striker);

        return battingModel;
    }

    public BowlingModel setBowler(int teamId, int playerId) {
        BowlingModel bowlingModel = new BowlingModel();
        bowlingModel.setMatchId((int) matchModel.get_matchId());
        bowlingModel.setTeamId(teamId);
        bowlingModel.setPlayerId(playerId);
        bowlingModel.setOvers(0.0);
        bowlingModel.setRuns(0);
        bowlingModel.setWicket(0);
        bowlingModel.setEcon(0.0);
        bowlingModel.setNo_ball(0);
        bowlingModel.setWide(0);
        bowlingModel.setB_striker(1);

        return bowlingModel;
    }

    public void playerOut(PlayerModel playerModel, BattingModel battingModel) {

    }

    public void alertMessage(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public PlayerModel checkPlayer() {
        if (playerOne_Two) {
            return playerOnePlayModel;
        } else {
            return playerTwoPlayModel;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
