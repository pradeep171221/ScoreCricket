package com.javali.scorecricket.services;

import com.javali.scorecricket.model.TeamModel;

import java.util.Comparator;

/**
 * Created by shashankshivakumar on 11/26/17.
 */

public class TeamModelComparator implements Comparator<TeamModel> {
    @Override
    public int compare(TeamModel a, TeamModel b) {
        return (a.get_teamId() < b.get_teamId()) ? -1 : ((a.get_teamId() == b.get_teamId()) ? 0 : 1);
    }
}