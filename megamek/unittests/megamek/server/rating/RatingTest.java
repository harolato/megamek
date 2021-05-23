package megamek.server.rating;

import megamek.common.Game;
import megamek.common.IGame;
import megamek.common.IPlayer;
import megamek.common.Team;
import megamek.common.event.GameEvent;
import megamek.common.event.GameListener;
import megamek.common.event.GamePlayerConnectedEvent;
import megamek.common.event.GameVictoryEvent;
import megamek.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author Deric Page (deric.page@nisc.coop) (ext 2335)
 * @version %Id%
 * @since 23/05/2021 23:53
 */
@RunWith(JUnit4.class)
public class RatingTest {

    Rating rating;
    
    IPlayer mockPlayer1;
    IPlayer mockPlayer2;
    IPlayer mockPlayer3;
    IPlayer mockPlayer4;

    IGame mockGame;
    Server mockServer;
    
    @Before
    public void setup() {

        mockPlayer1 = Mockito.mock(IPlayer.class);
        Mockito.when(mockPlayer1.getName()).thenReturn("Han Solo");
        Mockito.when(mockPlayer1.getId()).thenReturn(0);
        Mockito.when(mockPlayer1.getTeam()).thenReturn(0);

        mockPlayer2 = Mockito.mock(IPlayer.class);
        Mockito.when(mockPlayer2.getName()).thenReturn("Chewbaka");
        Mockito.when(mockPlayer2.getId()).thenReturn(1);
        Mockito.when(mockPlayer2.getTeam()).thenReturn(0);

        Vector<IPlayer> playersList1 = new Vector<>();
        playersList1.add(mockPlayer1);
        playersList1.add(mockPlayer2);
        
        mockPlayer3 = Mockito.mock(IPlayer.class);
        Mockito.when(mockPlayer3.getName()).thenReturn("Jaba");
        Mockito.when(mockPlayer3.getId()).thenReturn(2);
        Mockito.when(mockPlayer3.getTeam()).thenReturn(1);

        mockPlayer4 = Mockito.mock(IPlayer.class);
        Mockito.when(mockPlayer4.getName()).thenReturn("Death Star");
        Mockito.when(mockPlayer4.getId()).thenReturn(3);
        Mockito.when(mockPlayer4.getTeam()).thenReturn(1);

        Vector<IPlayer> playersList2 = new Vector<>();
        playersList2.add(mockPlayer3);
        playersList2.add(mockPlayer4);

        Team mockTeam1 = new Team(0);
        mockTeam1.addPlayer(mockPlayer1);
        mockTeam1.addPlayer(mockPlayer2);

        Team mockTeam2 = new Team(1);
        mockTeam2.addPlayer(mockPlayer3);
        mockTeam2.addPlayer(mockPlayer4);
        
        List<Team> mockTeams = new ArrayList<>();
        mockTeams.add(mockTeam1);
        mockTeams.add(mockTeam2);
        
        mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.getTeamsVector()).thenReturn(mockTeams);
        Mockito.when(mockGame.getVictoryTeam()).thenReturn(0);
        
        mockServer = Mockito.mock(Server.class);
        Mockito.when(mockServer.getGame()).thenReturn(mockGame);
        rating = new Rating(mockServer);        
    }
    
    @After
    public void tearDown() {
        mockServer.die();
    }
    
    @Test
    public void testAddNew() {
        GamePlayerConnectedEvent mockConnectedEvent = Mockito.mock(GamePlayerConnectedEvent.class);
        Mockito.when(mockConnectedEvent.getPlayer()).thenReturn(mockPlayer1);

        assertEquals(0, rating.playerStatistics.size());
        
        rating.gamePlayerConnected(mockConnectedEvent);
        
        assertEquals(1, rating.playerStatistics.size());
        assertEquals("Han Solo", mockPlayer1.getName());
        assertEquals("Han Solo", rating.playerStatistics.get("Han Solo").getPlayerName());
        assertEquals(0, rating.playerStatistics.get("Han Solo").getGamesWon());
        assertEquals(0, rating.playerStatistics.get("Han Solo").getGamesLost());
        assertEquals(0, rating.playerStatistics.get("Han Solo").getTotalGames());
    }

    @Test
    public void testVictory() {
        assertEquals(0, rating.playerStatistics.size());
        
        GameVictoryEvent mockVictory = Mockito.mock(GameVictoryEvent.class);
        rating.gameVictory(mockVictory);

        ArrayList<PlayerStatistics> scoreboard = null;
        
        assertEquals(4, rating.playerStatistics.size());
        assertEquals("Han Solo", mockPlayer1.getName());
        assertEquals("Han Solo", rating.playerStatistics.get("Han Solo").getPlayerName());
        assertEquals(1, rating.playerStatistics.get("Han Solo").getGamesWon());
        assertEquals(0, rating.playerStatistics.get("Han Solo").getGamesLost());
        assertEquals(1, rating.playerStatistics.get("Han Solo").getTotalGames());

        assertEquals("Chewbaka", mockPlayer2.getName());
        assertEquals("Chewbaka", rating.playerStatistics.get("Chewbaka").getPlayerName());
        assertEquals(1, rating.playerStatistics.get("Chewbaka").getGamesWon());
        assertEquals(0, rating.playerStatistics.get("Chewbaka").getGamesLost());
        assertEquals(1, rating.playerStatistics.get("Chewbaka").getTotalGames());

        assertEquals("Jaba", mockPlayer3.getName());
        assertEquals("Jaba", rating.playerStatistics.get("Jaba").getPlayerName());
        assertEquals(0, rating.playerStatistics.get("Jaba").getGamesWon());
        assertEquals(1, rating.playerStatistics.get("Jaba").getGamesLost());
        assertEquals(1, rating.playerStatistics.get("Jaba").getTotalGames());

        assertEquals("Death Star", mockPlayer4.getName());
        assertEquals("Death Star", rating.playerStatistics.get("Death Star").getPlayerName());
        assertEquals(0, rating.playerStatistics.get("Death Star").getGamesWon());
        assertEquals(1, rating.playerStatistics.get("Death Star").getGamesLost());
        assertEquals(1, rating.playerStatistics.get("Death Star").getTotalGames());

        scoreboard = rating.scoreboard();
        Mockito.when(mockGame.getVictoryTeam()).thenReturn(1);
        rating.gameVictory(mockVictory);

        assertEquals(4, rating.playerStatistics.size());
        assertEquals("Han Solo", mockPlayer1.getName());
        assertEquals("Han Solo", rating.playerStatistics.get("Han Solo").getPlayerName());
        assertEquals(1, rating.playerStatistics.get("Han Solo").getGamesWon());
        assertEquals(1, rating.playerStatistics.get("Han Solo").getGamesLost());
        assertEquals(2, rating.playerStatistics.get("Han Solo").getTotalGames());

        assertEquals("Chewbaka", mockPlayer2.getName());
        assertEquals("Chewbaka", rating.playerStatistics.get("Chewbaka").getPlayerName());
        assertEquals(1, rating.playerStatistics.get("Chewbaka").getGamesWon());
        assertEquals(1, rating.playerStatistics.get("Chewbaka").getGamesLost());
        assertEquals(2, rating.playerStatistics.get("Chewbaka").getTotalGames());

        assertEquals("Jaba", mockPlayer3.getName());
        assertEquals("Jaba", rating.playerStatistics.get("Jaba").getPlayerName());
        assertEquals(1, rating.playerStatistics.get("Jaba").getGamesWon());
        assertEquals(1, rating.playerStatistics.get("Jaba").getGamesLost());
        assertEquals(2, rating.playerStatistics.get("Jaba").getTotalGames());

        assertEquals("Death Star", mockPlayer4.getName());
        assertEquals("Death Star", rating.playerStatistics.get("Death Star").getPlayerName());
        assertEquals(1, rating.playerStatistics.get("Death Star").getGamesWon());
        assertEquals(1, rating.playerStatistics.get("Death Star").getGamesLost());
        assertEquals(2, rating.playerStatistics.get("Death Star").getTotalGames());
        
        scoreboard = rating.scoreboard();
        System.out.println(123);
    }
    
}
