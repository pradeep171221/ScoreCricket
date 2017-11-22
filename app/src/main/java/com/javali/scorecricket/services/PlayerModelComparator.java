package com.javali.scorecricket.services;

import com.javali.scorecricket.model.PlayerModel;

import java.util.Comparator;

/**
 * Created by shashankshivakumar on 11/6/17.
 */

public class PlayerModelComparator implements Comparator<PlayerModel> {
    @Override
    public int compare(PlayerModel a, PlayerModel b) {
        return (a.get_id() < b.get_id()) ? -1 : ((a.get_id() == b.get_id()) ? 0 : 1);
    }
}
