package com.rawad.gpstracker;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/**
 * Created by Rawad on 23-Oct-17.
 */

public class TileServ extends TileService {
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
            startService(new Intent(this, MyService.class));
            getQsTile().setState(Tile.STATE_ACTIVE);
        } else {
            stopService(new Intent(this, MyService.class));
            getQsTile().setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }
}
