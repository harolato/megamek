package megamek.server;

import megamek.client.Client;
import megamek.common.IPlayer;
import megamek.common.options.IBasicOption;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Haroldas Latonas
 * @version %Id%
 * @since 24/05/2021 14:20
 */
@RunWith(JUnit4.class)
public class DedicatedServerTest {

    Client c;
    
    @Test
    public void connectClientTest() {
        String[] args = {"-port", "123"};
        DedicatedServer.start(args);
        
        c = new Client("test", "127.0.0.1", 123);
        assertTrue(c.connect());

        //Wait for one second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Server s = Server.getServerInstance();
        assertEquals(1, s.getGame().getPlayersVector().size());
        IPlayer p = s.getPlayer(0);
        assertEquals("test", p.getName());
        
        c.die();

        c = new Client("test", "127.0.0.1", 1234);
        assertFalse(c.connect());
        c.die();

        Server.getServerInstance().die();
    }

    @Test
    public void loadSavedGameTest() {
        Path currentRelativePath = Paths.get("");
        String abs_path = currentRelativePath.toAbsolutePath().toString();
        
        String[] args = {abs_path + "/unittests/megamek/server/test.sav.gz", "", "-port", "123"};
        DedicatedServer.start(args);

        c = new Client("test", "127.0.0.1", 123);
        assertTrue(c.connect());
        
        
        Server s = Server.getServerInstance();
        //Wait for one second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(s.getGame().getEntitiesVector().size() > 0);
        assertTrue(s.getGame().getPlayersVector().size() > 0);
        assertEquals("test", s.getGame().getPlayer(0).getName());
        assertEquals("Princess", s.getGame().getPlayer(1).getName());
        
        c.die();
        Server.getServerInstance().die();
    }

    @Test
    public void startDSPasswordTest() {
        String[] args = {"-port", "123","-password", "test"};
        DedicatedServer.start(args);

        c = new Client("test", "127.0.0.1", 123);
        assertTrue(c.connect());
        c.sendGameOptions("test", new Vector <IBasicOption>());
        

        Server s = Server.getServerInstance();
        //Wait for one second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(s.isPassworded());
        assertTrue(s.getGame().getPlayersVector().size() > 0);
        assertEquals("test", s.getGame().getPlayer(0).getName());
        
        c.die();

        //Wait for one second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(s.getGame().getPlayersVector().size() == 0);
        
        c = new Client("test", "127.0.0.1", 123);
        assertTrue(c.connect());
        c.sendGameOptions("test1", new Vector <IBasicOption>());

        assertTrue(s.getGame().getPlayersVector().size() == 0);
        
        Server.getServerInstance().die();
    }

    @Test
    public void startDSDefaultPortTest() {
        String[] args = {};
        DedicatedServer.start(args);
        
        //random port, can't connect
        c = new Client("test", "127.0.0.1", 123);
        assertFalse(c.connect());
        c.die();

        Server.getServerInstance().die();
    }

    @Test  @Ignore
    public void startDSInvalidPortTest() {
        // Off point
        String[] args = {"-port", "65536"};
        DedicatedServer.start(args);

        //random port, can't connect
        c = new Client("test", "127.0.0.1", 65536);
        assertFalse(c.connect());
        c.die();
        
        assertNull(Server.getServerInstance());
        
        // On point
        String[] args1 = {"-port", "65535"};
        DedicatedServer.start(args1);

        c = new Client("test", "127.0.0.1", 65535);
        assertTrue(c.connect());
        c.die();
        Server.getServerInstance().die();        
    }

    /**
     * Integration Test to improve coverage
     */
    @Test
    public void startDSAnnounceTest() {
        String[] args = {"-announce", "http://[::1]:1234"};
        DedicatedServer.start(args);
        Server.getServerInstance().die();
    }
}
