package megamek.server.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Deric Page (deric.page@nisc.coop) (ext 2335)
 * @version %Id%
 * @since 22/05/2021 18:54
 */

@RunWith(JUnit4.class)
public class ServerCommandTest {

    @Test
    public void testcommand() {
        // stub
    }
    
    protected String[] parseCommand(String command) {
        return command.split(" ");
    }
    
}
