package com.rawad.gpstracker;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/**
 * Created by Rawad on 23-Oct-17.
 */

public class SendLocationTile extends TileService {

    LocationAlarmSender alarm = new LocationAlarmSender();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();

    }

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        if (tile.getState() == Tile.STATE_INACTIVE) {
            alarm.setAlarm(this);
            //startService(new Intent(this, SendLocationSms.class));
            getQsTile().setState(Tile.STATE_ACTIVE);
        } else {
            alarm.cancelAlarm(this);
            //stopService(new Intent(this, SendLocationSms.class));
            getQsTile().setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }
}
