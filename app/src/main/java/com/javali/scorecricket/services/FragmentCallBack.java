package com.javali.scorecricket.services;

import com.javali.scorecricket.model.PlayerModel;

/**
 * Created by shashankshivakumar on 11/30/17.
 */

public interface FragmentCallBack {
    void onDataSent(PlayerModel playerModel, boolean flag);
}
