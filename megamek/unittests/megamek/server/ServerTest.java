package megamek.server;

import megamek.client.Client;
import megamek.common.Game;
import megamek.common.IGame;
import megamek.common.net.ConnectionFactory;
import megamek.common.net.IConnection;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Deric Page (deric.page@nisc.coop) (ext 2335)
 * @version %Id%
 * @since 22/05/2021 17:38
 */
@RunWith(JUnit4.class)
public class ServerTest {
    
    Server s;
    Client c;
    
    @Before
    public void setUp() throws IOException {
        s = new Server("test", 123, false, "");
        s.run();
    }
    
    @After
    public void tearDown() {
        s.die();
    }
    
    @Test
    public void serverConnectionTest() {
        try(Socket ableToConnect = new Socket("127.0.0.1", 123)) {
            assertTrue("Accepts connection when server in listening",
                       ableToConnect.isConnected());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try(Socket ableToConnect = new Socket("127.0.0.1", 1234)) {
            assertFalse("Does not accept connection when server in down",
                       ableToConnect.isConnected());
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void serverClientTest() {
        c = new Client("test", "127.0.0.1", 123);
        assertTrue(c.connect());

        c = new Client("test", "127.0.0.1", 1234);
        assertFalse(c.connect());
    }
}
