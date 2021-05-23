package megamek.server.rating;

import megamek.common.IPlayer;
import megamek.common.Team;
import megamek.common.event.GameBoardChangeEvent;
import megamek.common.event.GameBoardNewEvent;
import megamek.common.event.GameCFREvent;
import megamek.common.event.GameEndEvent;
import megamek.common.event.GameEntityChangeEvent;
import megamek.common.event.GameEntityNewEvent;
import megamek.common.event.GameEntityNewOffboardEvent;
import megamek.common.event.GameEntityRemoveEvent;
import megamek.common.event.GameListener;
import megamek.common.event.GameMapQueryEvent;
import megamek.common.event.GameNewActionEvent;
import megamek.common.event.GamePhaseChangeEvent;
import megamek.common.event.GamePlayerChangeEvent;
import megamek.common.event.GamePlayerChatEvent;
import megamek.common.event.GamePlayerConnectedEvent;
import megamek.common.event.GamePlayerDisconnectedEvent;
import megamek.common.event.GameReportEvent;
import megamek.common.event.GameSettingsChangeEvent;
import megamek.common.event.GameTurnChangeEvent;
import megamek.common.event.GameVictoryEvent;
import megamek.server.Server;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author Deric Page (deric.page@nisc.coop) (ext 2335)
 * @version %Id%
 * @since 23/05/2021 20:44
 */
public class Rating implements GameListener {
        
    Server server;
    protected HashMap<String, PlayerStatistics> playerStatistics = new HashMap<>();
    
    public Rating (Server server) {
        this.server = server;
    }
    
    public ArrayList<PlayerStatistics> scoreboard() {
        ArrayList<PlayerStatistics> scoreboard = new ArrayList<>();
        for (String key:playerStatistics.keySet()) {
            scoreboard.add(playerStatistics.get(key));
        }
        scoreboard.sort(new Comparator<PlayerStatistics>() {
            @Override
            public int compare(final PlayerStatistics o1,
                               final PlayerStatistics o2) {
                return o1.getRating() > o2.getRating() ? 1 : 0;
            }
        });
        return scoreboard;
    }
    
    private boolean playerExists(String playerName) {
        return playerStatistics.containsKey(playerName);
    }
    
    private void addPlayer(String playerName) {
        if ( !this.playerExists(playerName) ) {
            playerStatistics.put(playerName, new PlayerStatistics(playerName));
        }
    }

    private PlayerStatistics getPlayer(final String playerName) {
        return this.playerStatistics.get(playerName);
    }

    @Override
    public void gamePlayerConnected(final GamePlayerConnectedEvent e) {
        this.addPlayer(e.getPlayer().getName());
    }

    @Override
    public void gamePlayerDisconnected(final GamePlayerDisconnectedEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gamePlayerChange(final GamePlayerChangeEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gamePlayerChat(final GamePlayerChatEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameTurnChange(final GameTurnChangeEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gamePhaseChange(final GamePhaseChangeEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameReport(final GameReportEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameEnd(final GameEndEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameBoardNew(final GameBoardNewEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameBoardChanged(final GameBoardChangeEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameSettingsChange(final GameSettingsChangeEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameMapQuery(final GameMapQueryEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameEntityNew(final GameEntityNewEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameEntityNewOffboard(final GameEntityNewOffboardEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameEntityRemove(final GameEntityRemoveEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameEntityChange(final GameEntityChangeEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameNewAction(final GameNewActionEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameClientFeedbackRequest(final GameCFREvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void gameVictory(final GameVictoryEvent e) {
        int teamWonId = this.server.getGame().getVictoryTeam();
        
        List<Team> allTeams = this.server.getGame().getTeamsVector();
        
        for (Team team: allTeams) {
            for (IPlayer player : team.getPlayersVector()) {
                this.addPlayer(player.getName());
                PlayerStatistics p = this.getPlayer(player.getName());
                if ( player.getTeam() == teamWonId ) {
                    p.addWin();
                } else {
                    p.addLoss();
                }
            }
        }
    }

    
}
