package megamek.server.commands;

import megamek.MegaMek;
import megamek.client.Client;
import megamek.client.bot.TestBot;
import megamek.client.bot.princess.BehaviorSettings;
import megamek.client.bot.princess.Princess;
import megamek.common.Coords;
import megamek.common.IGame;
import megamek.common.IPlayer;
import megamek.common.event.GameListener;
import megamek.common.logging.LogLevel;
import megamek.common.net.ConnectionFactory;
import megamek.common.net.ConnectionListenerAdapter;
import megamek.common.net.DisconnectedEvent;
import megamek.common.net.IConnection;
import megamek.common.net.Packet;
import megamek.common.net.PacketReceivedEvent;
import megamek.common.util.AddBotUtil;
import megamek.server.ConnectionHandler;
import megamek.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Deric Page (deric.page@nisc.coop) (ext 2335)
 * @version %Id%
 * @since 22/05/2021 17:37
 */
@RunWith(JUnit4.class)
public class NukeCommandTest extends ServerCommandTest {
    @Test
    public void testNuke() {

        Server mockServer = Mockito.mock(Server.class);
        
        NukeCommand spyNuke = Mockito.spy(new NukeCommand(mockServer));
        Mockito.when(spyNuke.getName()).thenReturn("nuke");
        
        boolean result;
        String[] command;
        
        command = parseCommand("/nuke 1 1 4");
        result = spyNuke.precondition(command);
        Mockito.verify(spyNuke).precondition(command);
        assertEquals(true, result);

        command = parseCommand("/nuke 1 1 444 433 333 2");
        result = spyNuke.precondition(command);
        Mockito.verify(spyNuke).precondition(command);
        assertEquals(true, result);
        
        command = parseCommand("/nuke 1 1 444 433 333 2 5");
        result = spyNuke.precondition(command);
        Mockito.verify(spyNuke).precondition(command);
        assertEquals(false, result);
        
        command = parseCommand("/nuke 1 1 444 433 333");
        result = spyNuke.precondition( command);
        Mockito.verify(spyNuke).precondition(command);
        assertEquals(false, result);
        
        command = parseCommand("/nuke 1 1");
        result = spyNuke.precondition(command);
        Mockito.verify(spyNuke).precondition(command);
        assertEquals(false, result);
        
        mockServer.die();
    }
    
}
