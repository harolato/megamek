/**
 * MegaMek - Copyright (C) 2000,2001,2002,2005 Ben Mazur (bmazur@sev.org)
 * UnitStatusFormatter.java - Copyright (C) 2002,2004 Joshua Yockey
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

package megamek.server;

import megamek.common.CommonConstants;
import megamek.common.CriticalSlot;
import megamek.common.Entity;
import megamek.common.Mech;
import megamek.common.MechFileParser;
import megamek.common.MechSummary;
import megamek.common.MechSummaryCache;
import megamek.common.Mounted;
import megamek.common.Protomech;
import megamek.common.util.StringUtil;

public abstract class UnitStatusFormatter {    
    /**
     * Much of the layout for the status string is heavily inspired by the
     * Battletech MUSE/MUX code
     */
    public static String format(Entity e) {
        StringBuffer sb = new StringBuffer(2048);
        sb.append(
                "=============================================================")
                .append(CommonConstants.NL);
        sb.append(formatHeader(e));
        sb.append("--- Armor: ").append(e.getTotalArmor()).append("/")
                .append(e.getTotalOArmor())
                .append("-------------------------------------------")
                .append(CommonConstants.NL);
        sb.append("--- Internal: ").append(e.getTotalInternal()).append("/")
                .append(e.getTotalOInternal())
                .append("----------------------------------------")
                .append(CommonConstants.NL);
        sb.append(formatArmor(e));
        if ((e instanceof Mech) || (e instanceof Protomech)) {
            sb.append(
                    "-------------------------------------------------------------")
                    .append(CommonConstants.NL);
            sb.append(formatCrits(e));
        }
        sb.append(
                "-------------------------------------------------------------")
                .append(CommonConstants.NL);
        sb.append(formatAmmo(e));
        sb.append(
                "=============================================================")
                .append(CommonConstants.NL);
        return sb.toString();
    }

    private static String formatHeader(Entity e) {
        StringBuffer sb = new StringBuffer(1024);
        sb.append("Model: ").append(e.getChassis()).append(" - ")
                .append(e.getModel()).append(CommonConstants.NL);
        for (int i = 0; i < e.getCrew().getSlotCount(); i++) {
            if (e.getCrew().isMissing(i)) {
                sb.append("No ").append(e.getCrew().getCrewType().getRoleName(i));
            } else {
                sb.append(e.getCrew().getCrewType().getRoleName(i)).append(": ")
                    .append(e.getCrew().getName(i));
                sb.append(" (").append(e.getCrew().getGunnery(i)).append("/")
                    .append(e.getCrew().getPiloting(i)).append(")");
            }
            sb.append(CommonConstants.NL);
        }
        if (e.isCaptured()) {
            sb.append("  *** CAPTURED BY THE ENEMY ***");
            sb.append(CommonConstants.NL);
        }
        return sb.toString();
    }

    private static String formatAmmo(Entity e) {
        StringBuffer sb = new StringBuffer(1024);
        for (Mounted ammo : e.getAmmo()) {
            sb.append(ammo.getName());
            sb.append(": ").append(ammo.getBaseShotsLeft())
                    .append(CommonConstants.NL);
        }
        return sb.toString();
    }

    private static String formatCrits(Entity e) {
        StringBuffer sb = new StringBuffer();
        for (int x = 0; x < e.locations(); x++) {
            sb.append(StringUtil.makeLength(e.getLocationName(x), 12)).append(
                    ": ");
            int nCount = 0;
            for (int y = 0; y < e.getNumberOfCriticals(x); y++) {
                CriticalSlot cs = e.getCritical(x, y);
                if (cs == null) {
                    continue;
                }
                nCount++;
                if (nCount == 7) {
                    sb.append(CommonConstants.NL);
                    sb.append("              ");
                } else if (nCount > 1) {
                    sb.append(",");
                }
                if (cs.getType() == CriticalSlot.TYPE_SYSTEM) {
                    if (cs.isHit() || cs.isDestroyed() || cs.isMissing()) {
                        sb.append("*");
                    }
                    sb.append(e.getSystemName(cs.getIndex()));
                } else if (cs.getType() == CriticalSlot.TYPE_EQUIPMENT) {
                    Mounted m = cs.getMount();
                    sb.append(cs.isHit() ? "*" : "").append(cs.isDestroyed() ? "*" : "").append(cs.isBreached() ? "x" : "").append(m.getDesc()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
            sb.append(CommonConstants.NL);
        }
        return sb.toString();
    }

    private static String formatArmor(Entity e) {
        return e.formatArmorOutput();
    }

    public static String renderArmor(int nArmor) {
        return renderArmor(nArmor, 2);
    }

    public static String renderArmor(int nArmor, int spaces) {
        if (nArmor <= 0) {
            if (1 == spaces) {
                return "x";
            }
            return "xx";
        }
        return StringUtil.makeLength(String.valueOf(nArmor), spaces, true);
    }

    public static void main(String[] ARGS) throws Exception {
        MechSummary ms = MechSummaryCache.getInstance().getMech(ARGS[0]);
        Entity e = new MechFileParser(ms.getSourceFile(), ms.getEntryName())
                .getEntity();
        System.out.println(format(e));
    }
}
