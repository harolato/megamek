/*
 * MegaMek - Copyright (C) 2000-2002 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */

package megamek.server.commands;

import megamek.common.options.OptionsConstants;
import megamek.server.Server;

import java.util.Arrays;

/**
 *
 * @author  fastsammy
 * @version
 */
public class NukeCommand extends ServerCommand {
    private static final String description = "Drops a nuke onto the board, to be exploded at" +
                "the end of the next weapons attack phase.";
    private static final String help = "Allowed formats:"+
                "/nuke <x> <y> <type> and" +
                "/nuke <x> <y> <damage> <degredation> <secondary radius> <craterdepth>" +
                "where type is 0-4 (0: Davy-Crockett-I, 1: Davy-Crockett-M, 2: Alamo, 3: Santa Ana, 4: Peacemaker)" +
                "and hex x,y is x=column number and y=row number (hex 0923 would be x=9 and y=23)";

    /** Creates new NukeCommand */
    public NukeCommand(Server server) {
        super(server, "nuke", description + help);

    }

    /**
     * Run this command with the arguments supplied
     */
    @Override
    public void run(int connId, String[] args) {
        
        if ( this.precondition(connId, args) ) {
            server.sendServerChat(connId, "Nuke command failed." + help);
            return;
        }

        try {
            // nuke type is based on number of arguments
            int[] nuke = new int[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                nuke[i-1] = Integer.parseInt(args[i]);
            }
            // is the hex on the board?
            if (!server.getGame().getBoard().contains(nuke[0]-1, nuke[1]-1)) {
                server.sendServerChat(connId, "Specified hex is not on the board.");
                return;
            }
            server.addScheduledNuke(nuke);
            server.sendServerChat(connId, "A nuke is incoming!  Take cover!");
        } catch (Exception e) {
            server.sendServerChat(connId, "Nuke command failed (arg len:" + args.length + "). " + help);
        }
    }
    
    protected boolean precondition(int connId, String[] args) {
        // Check to make sure nuking is allowed by game options!
        if (!(server.getGame().getOptions().booleanOption(OptionsConstants.ALLOWED_REALLY_ALLOW_NUKES) && server.getGame().getOptions().booleanOption(OptionsConstants.ALLOWED_ALLOW_NUKES))) {
            server.sendServerChat(connId, "Command-line nukes are not enabled in this game.");
            return false;
        }
        // Allowed command length
        if (!Arrays.asList(new int[] {4, 7}).contains(args.length)) {
            return false;
        }
        return true;
    }
    
}
