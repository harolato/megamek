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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Haroldas Latonas
 * @version %Id%
 * @since 23/05/2021 20:44
 */
public class Rating implements GameListener {
        
    Server server;
    protected HashMap<String, PlayerStatistics> playerStatistics = new HashMap<>();
    
    public Rating (Server server) {
        this.server = server;
    }
    
    public List<PlayerStatistics> scoreboard() {
        ArrayList<PlayerStatistics> scoreboard = new ArrayList<>();
        for (Map.Entry<String, PlayerStatistics> entry:playerStatistics.entrySet()) {
            scoreboard.add(entry.getValue());
        }
        scoreboard.sort((PlayerStatistics o1, PlayerStatistics o2) -> (o1.getRating() > o2.getRating() ? 1 : 0));
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
        //
    }

    @Override
    public void gamePlayerChange(final GamePlayerChangeEvent e) {
        //
    }

    @Override
    public void gamePlayerChat(final GamePlayerChatEvent e) {
        //
    }

    @Override
    public void gameTurnChange(final GameTurnChangeEvent e) {
        //
    }

    @Override
    public void gamePhaseChange(final GamePhaseChangeEvent e) {
        //
    }

    @Override
    public void gameReport(final GameReportEvent e) {
        //
    }

    @Override
    public void gameEnd(final GameEndEvent e) {
        //
    }

    @Override
    public void gameBoardNew(final GameBoardNewEvent e) {
        //
    }

    @Override
    public void gameBoardChanged(final GameBoardChangeEvent e) {
        //
    }

    @Override
    public void gameSettingsChange(final GameSettingsChangeEvent e) {
        //
    }

    @Override
    public void gameMapQuery(final GameMapQueryEvent e) {
        //
    }

    @Override
    public void gameEntityNew(final GameEntityNewEvent e) {
        //
    }

    @Override
    public void gameEntityNewOffboard(final GameEntityNewOffboardEvent e) {
        //
    }

    @Override
    public void gameEntityRemove(final GameEntityRemoveEvent e) {
        //
    }

    @Override
    public void gameEntityChange(final GameEntityChangeEvent e) {
        //
    }

    @Override
    public void gameNewAction(final GameNewActionEvent e) {
        //
    }

    @Override
    public void gameClientFeedbackRequest(final GameCFREvent e) {
        //
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
