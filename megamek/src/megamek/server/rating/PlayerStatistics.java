package megamek.server.rating;

/**
 * @author Haroldas Latonas
 * @version %Id%
 * @since 23/05/2021 20:58
 */
public class PlayerStatistics {
    protected String playerName;
    protected int gamesWon = 0;
    protected int gamesLost = 0;

    public PlayerStatistics (String playerName) {
        this.playerName = playerName;
    }
    
    public float getRating() {
        if ( gamesLost == 0 ) {
            return gamesWon;
        }
        return gamesWon / gamesLost;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public int getGamesLost() {
        return gamesLost;
    }
    
    public void addWin() {
        this.gamesWon += 1;
    }

    public void addLoss() {
        this.gamesLost += 1;
    }
    
    
    public int getTotalGames () {
        return this.gamesLost + this.gamesWon;
    }
    
    public int getGamesWon() {
        return this.gamesWon;
    }
}
