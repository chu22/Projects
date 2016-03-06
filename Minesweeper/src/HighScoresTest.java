import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HighScoresTest {
	
	Score[] s;
	HighScores h;
	
	@Before
	public void setUp() throws Exception {
		h = new HighScores();
		s = new Score[10];
		for(int i = 0; i<10;i++){
			s[i] = new Score(10+i*2);
			s[i].addName("number " + i);
		}
		h.resetScoreFile();
    }
	
	@Test
	public void testScore() {
		for(int i = 0; i<10;i++){
			assertEquals("score " + i, 10+i*2, s[i].getScore());
		}
		
	}
	
	@Test
	public void testLessThan() {
		assertTrue("10<12", s[0].lessThan(s[1]));
		assertFalse("10<10", s[0].lessThan(s[0]));
		assertFalse("12<10", s[1].lessThan(s[0]));
	}
	
	@Test
	public void testName() {
		for(int i = 0; i<10;i++){
			assertEquals("name " + i, "number " + i, s[i].getName());
		}
	}
	
	@Test
	public void testEmptyTopTen(){
		Score t = new Score(999);
		assertTrue("empty 999", h.isTopTen(t));
		t = new Score(50);
		assertTrue("empty 50", h.isTopTen(t));
	}
	
	@Test
	public void testAddScore(){
		Score t = new Score(999);
		for(int i = 0; i<9;i++){
			h.addScore(s[i]);
		}
		assertTrue("almost full", h.isTopTen(t));
		h.addScore(s[9]);
		assertFalse("full", h.isTopTen(t));
		assertFalse("28<28", h.isTopTen(s[9]));
		Score v = new Score(13);
		assertTrue("13<28", h.isTopTen(v));
		h.addScore(v);
		assertFalse("highest now 26", h.isTopTen(s[8]));
		Score w = new Score(25);
		assertTrue("25<26", h.isTopTen(w));
	}
	
	
	
	

}
