package actions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionProxy;

public class TestDB {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testSearch() throws Exception {
		ActionProxy proxy = null;
		MainProcedure test = new MainProcedure();
		
		//stage 1
		
		//test.search("Dooku", "");
		test.get("30000");
        //request.setParameter("queryType", "Title");
        //request.setParameter("queryInput", "AutoMata Theory");
        //proxy = getActionProxy("SEARCH_QUERY"); 
        //test = (MainProcedure)proxy.getAction();
        //proxy.execute();
        //String result = test.searchQuery();
		//assertEquals(1, test.searchList.size());
        assertEquals(2, test.neighborList1.size());
        assertEquals(2, test.neighborList2.size());
        assertEquals(0, test.neighborList3.size());
        assertEquals(0, test.directedWeb.size());
        //assertEquals( 1, test.getBookSR().size());
        
        //stage 2
        
        
	}
}
