package megamek.server;

import megamek.common.BattleArmor;
import megamek.common.GunEmplacement;
import megamek.common.Infantry;
import megamek.common.Protomech;
import megamek.common.QuadMech;
import megamek.common.VTOL;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Haroldas Latonas
 * @version %Id%
 * @since 23/05/2021 11:17
 */
public class UnitStatusFormatterTest {

    @Test
    public void formatTank() {
        VTOL tank = new VTOL();
        tank.setModel("test");
        tank.setChassis("testChasis");
        tank.setArmor(1, 1);
        tank.setArmor(2, 2);
        tank.setArmor(3, 3);
        tank.setArmor(4, 4);
        tank.setArmor(5, 5);
        tank.setArmor(6, 6);
        tank.setInternal(1, 1);
        tank.setInternal(2, 2);
        tank.setInternal(3, 3);
        tank.setInternal(4, 4);
        tank.setInternal(5, 5);
        tank.setInternal(6, 6);
        String out = UnitStatusFormatter.format(tank);
        String[] outLines = out.split("\n");
        assertEquals(17, outLines.length);
        assertEquals("Model: testChasis - test\r", outLines[1]);
        assertEquals("--- Armor: 21/0-------------------------------------------\r", outLines[3]);
        assertEquals("--- Internal: 21/0----------------------------------------\r", outLines[4]);
        assertEquals("    | 3/ 6\\ 2|           | 3/  \\ 2|\r", outLines[10]);
    }

    @Test
    public void formatMech() {
        QuadMech unit = new QuadMech();
        unit.setModel("test");
        unit.setChassis("testChasis");
        unit.setArmor(1, 1);
        unit.setArmor(2, 2);
        unit.setArmor(3, 3);
        unit.setArmor(4, 4);
        unit.setArmor(5, 5);
        unit.setArmor(6, 6);
        unit.setInternal(1, 1);
        unit.setInternal(2, 2);
        unit.setInternal(3, 3);
        unit.setInternal(4, 4);
        unit.setInternal(5, 5);
        unit.setInternal(6, 6);
        String out = UnitStatusFormatter.format(unit);
        String[] outLines = out.split("\n");
        assertEquals(23, outLines.length);
        assertEquals("Model: testChasis - test\r", outLines[1]);
        assertEquals("--- Armor: 21/0-------------------------------------------\r", outLines[3]);
        assertEquals("--- Internal: 21/0----------------------------------------\r", outLines[4]);
        assertEquals("         FRONT                REAR                INTERNAL\r", outLines[5]);
        assertEquals("      / 3| 1| 2\\           /xx|xx|xx\\            / 3| 1| 2\\\r", outLines[7]);
    }

    @Test
    public void formatGunEmplacement() {
        GunEmplacement unit = new GunEmplacement();
        unit.setModel("test");
        unit.setChassis("testChasis");
        unit.setArmor(1,0);
        unit.setInternal(1,0);
        String out = UnitStatusFormatter.format(unit);
        String[] outLines = out.split("\n");
        
        assertEquals(12, outLines.length);
        assertEquals("Model: testChasis - test\r", outLines[1]);
        assertEquals("--- Armor: 1/0-------------------------------------------\r", outLines[3]);
        assertEquals("--- Internal: 1/0----------------------------------------\r", outLines[4]);
        assertEquals("  CF       |     1    |\r", outLines[7]);
    }

    @Test
    public void formatBattleArmor() {
        BattleArmor unit = new BattleArmor();
        unit.setModel("test");
        unit.setChassis("testChasis");
        unit.setArmor(1,1);
        unit.setInternal(1,1);
        unit.setTroopers(1);
        String out = UnitStatusFormatter.format(unit);
        String[] outLines = out.split("\n");

        assertEquals(8, outLines.length);
        assertEquals("Model: testChasis - test\r", outLines[1]);
        assertEquals("--- Armor: 1/0-------------------------------------------\r", outLines[3]);
        assertEquals("--- Internal: 1/0----------------------------------------\r", outLines[4]);
        assertEquals("Trooper 1:  1 /  1\r", outLines[5]);
    }

    @Test
    public void formatInfantryArmor() {
        Infantry unit = new Infantry();
        unit.setModel("test");
        unit.setChassis("testChasis");
        unit.setInternal(2,0);
        String out = UnitStatusFormatter.format(unit);
        String[] outLines = out.split("\n");

        assertEquals(8, outLines.length);
        assertEquals("Model: testChasis - test\r", outLines[1]);
        assertEquals("--- Armor: 0/0-------------------------------------------\r", outLines[3]);
        assertEquals("--- Internal: 2/0----------------------------------------\r", outLines[4]);
        assertEquals("Surviving troopers:  2\r", outLines[5]);
    }

    @Test
    public void formatProtomechArmor() {
        Protomech unit = new Protomech();
        unit.setModel("test");
        unit.setChassis("testChasis");
        unit.setArmor(1,1);
        unit.setArmor(2,2);
        unit.setArmor(3,3);
        unit.setArmor(4,4);
        unit.setArmor(5,5);
        unit.setArmor(6,6);
        unit.setInternal(1,1);
        unit.setInternal(2,2);
        unit.setInternal(3,3);
        unit.setInternal(4,4);
        unit.setInternal(5,5);
        unit.setInternal(6,6);
        String out = UnitStatusFormatter.format(unit);
        String[] outLines = out.split("\n");

        assertEquals(21, outLines.length);
        assertEquals("Model: testChasis - test\r", outLines[1]);
        assertEquals("--- Armor: 15/0-------------------------------------------\r", outLines[3]);
        assertEquals("--- Internal: 15/0----------------------------------------\r", outLines[4]);
        assertEquals("         FRONT                INTERNAL\r", outLines[5]);
        assertEquals("      (4 / 2 \\ 3)            (4 / 2 \\ 3)\r", outLines[8]);
    }

    @Test
    public void renderArmor() {
        assertEquals(" 1", UnitStatusFormatter.renderArmor(1));
        assertEquals(" 2", UnitStatusFormatter.renderArmor(2));
        assertEquals(" 3", UnitStatusFormatter.renderArmor(3));

        assertEquals("1", UnitStatusFormatter.renderArmor(1,1));
        assertEquals("x", UnitStatusFormatter.renderArmor(0, 1));
        assertEquals("xx", UnitStatusFormatter.renderArmor(0, 2));
    }

}