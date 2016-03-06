import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CellTest {
	Cell cell1;
	Cell cell2;
	Cell cell3;
	Cell cell4;
	
	@Before
	public void setUp() throws Exception {
		cell1 = new Cell(0,0);
		cell1.setVal(4);
		cell2 = new Cell(9,9);
		cell2.setVal(0);
		cell3 = new Cell(0,0);
		cell3.setVal(-1);
		cell4 = new Cell(0,0);
		cell4.setVal(-1);
    }
	
	@Test
    public void testisAdjacent() {
		assertTrue("isAdjacent(0,1): ", cell1.isAdjacent(0,1) );
		assertTrue("isAdjacent(1,0): ", cell1.isAdjacent(1,0) );
		assertTrue("isAdjacent(1,1): ", cell1.isAdjacent(1,1) );
		assertFalse("isAdjacent(-1,0): ", cell1.isAdjacent(-1,0) );
		assertFalse("isAdjacent(0,-1): ", cell1.isAdjacent(0,-1) );
		assertFalse("isAdjacent(0,0): ", cell1.isAdjacent(0,0) );
		assertFalse("isAdjacent(2,0): ", cell1.isAdjacent(2,0) );
		assertFalse("isAdjacent(0,2): ", cell1.isAdjacent(0,2) );
		assertFalse("isAdjacent(10,9): ", cell2.isAdjacent(10,9) );
		assertFalse("isAdjacent(9,10): ", cell2.isAdjacent(9,10) );
		
    }
    
    
    @Test
    public void testisBomb() {
        assertTrue("cell3.isBomb()", cell3.isBomb());
        assertFalse("cell2.isBomb()", cell2.isBomb());
        assertFalse("cell1.isBomb()", cell1.isBomb());
    }
    
    
    @Test
    public void testisZero() {
    	assertTrue("cell2.isZero()", cell2.isZero());
        assertFalse("cell3.isZero()", cell3.isZero());
        assertFalse("cell1.isZero()", cell1.isZero());
        
    }
    
    
    @Test
    public void testMouseState0() {
    	cell1.LPressed();
    	assertEquals("state 0 LPress:", 1, cell1.getState());
    	cell2.LReleasedSame();
    	assertEquals("state 0 same LRelease:", 0, cell2.getState());
    	cell3.LReleasedOther();
    	assertEquals("state 0 other LRelease:", 0, cell3.getState());
    	cell4.RPressed();
    	assertEquals("state 0 RPress:", 2, cell4.getState());
    }
    
    @Test
    public void testMouseState1() {
    	cell1.LPressed();
    	cell2.LPressed();
    	cell3.LPressed();
    	cell4.LPressed();
    	
    	cell1.LPressed();
    	assertEquals("state 1 LPress:", 1, cell1.getState());
    	cell2.LReleasedSame();
    	assertEquals("state 1 same LRelease:", 6, cell2.getState());
    	cell3.LReleasedOther();
    	assertEquals("state 1 other LRelease:", 0, cell3.getState());
    	cell4.RPressed();
    	assertEquals("state 1 RPress:", 1, cell4.getState());
    }
    
    @Test
    public void testMouseState2() {
    	cell1.RPressed();
    	cell2.RPressed();
    	cell3.RPressed();
    	cell4.RPressed();
    	
    	cell1.LPressed();
    	assertEquals("state 2 LPress:", 2, cell1.getState());
    	cell2.LReleasedSame();
    	assertEquals("state 2 same LRelease:", 2, cell2.getState());
    	cell3.LReleasedOther();
    	assertEquals("state 2 other LRelease:", 2, cell3.getState());
    	cell4.RPressed();
    	assertEquals("state 2 RPress:", 3, cell4.getState());
    }
    
    @Test
    public void testMouseState3() {
    	cell1.RPressed();
    	cell2.RPressed();
    	cell3.RPressed();
    	cell4.RPressed();
    	
    	cell1.RPressed();
    	cell2.RPressed();
    	cell3.RPressed();
    	cell4.RPressed();
    	
    	cell1.LPressed();
    	assertEquals("state 3 LPress:", 4, cell1.getState());
    	cell2.LReleasedSame();
    	assertEquals("state 3 same LRelease:", 3, cell2.getState());
    	cell3.LReleasedOther();
    	assertEquals("state 3 other LRelease:", 3, cell3.getState());
    	cell4.RPressed();
    	assertEquals("state 3 RPress:", 0, cell4.getState());
    }
    
    @Test
    public void testMouseState4() {
    	cell1.RPressed();
    	cell2.RPressed();
    	cell3.RPressed();
    	cell4.RPressed();
    	
    	cell1.RPressed();
    	cell2.RPressed();
    	cell3.RPressed();
    	cell4.RPressed();
    	
    	cell1.LPressed();
    	cell2.LPressed();
    	cell3.LPressed();
    	cell4.LPressed();
    	
    	cell1.LPressed();
    	assertEquals("state 4 LPress:", 4, cell1.getState());
    	cell2.LReleasedSame();
    	assertEquals("state 4 same LRelease:", 3, cell2.getState());
    	cell3.LReleasedOther();
    	assertEquals("state 4 other LRelease:", 3, cell3.getState());
    	cell4.RPressed();
    	assertEquals("state 4 RPress:", 4, cell4.getState());
    }
    
    @Test
    public void testMouseState5() {
    	cell2.RPressed();
    	cell4.RPressed();
    	
    	cell1.showBomb();
    	assertEquals("number NORM showBomb():", 0, cell1.getState());
    	cell2.showBomb();
    	assertEquals("number FLAG showBomb():", 5, cell2.getState());
    	cell3.showBomb();
    	assertEquals("bomb NORM showBomb():", 5, cell3.getState());
    	cell4.showBomb();
    	assertEquals("bomb FLAG showBomb():", 2, cell4.getState());
    }
    
    @Test
    public void testMouseState6() {
    	cell1.LPressed();
    	cell2.LPressed();
    	cell3.LPressed();
    	cell4.LPressed();
    	
    	cell1.LReleasedSame();
    	cell2.LReleasedSame();
    	cell3.LReleasedSame();
    	cell4.LReleasedSame();
    	
    	cell1.LPressed();
    	assertEquals("state 6 LPress:", 6, cell1.getState());
    	cell2.LReleasedSame();
    	assertEquals("state 6 same LRelease:", 6, cell2.getState());
    	cell3.LReleasedOther();
    	assertEquals("state 6 other LRelease:", 6, cell3.getState());
    	cell4.RPressed();
    	assertEquals("state 6 RPress:", 6, cell4.getState());
    }


}
