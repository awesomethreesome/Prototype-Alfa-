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
		NodeHash generator = new NodeHash();
		test.hashGenerator = generator;
		CharDesc newInput = new CharDesc();
		newInput.name = "General Grievous";
		newInput.gender = "Male";
		newInput.birthDate = "2001-9-21";
		newInput.profession = "close combat";
		newInput.institution = "close combat";
		test.logInCheck("test", "123");
		//stage 0 1 2 3
		//test.search("Dooku", "");
		//test.get("30000");
		/*
		test.add(newInput);
		newInput.name = "Dooku";
		test.add(newInput);
		newInput.name = "General Grievous";
		test.add(newInput);
		test.syncDB();
		
		test.delete("00000");//General Grievous
		newInput.name = "Darth Moore";
		test.add(newInput); 
		test.delete("10000");//Darth Moore 
        test.syncDB();
        */
        //stage 4
		/*
        newInput.hash = "09000";
        test.edit(newInput);
        
        newInput.name = "General Grievous";
        test.add(newInput);
        test.delete("20000");//General Grievous
        newInput.hash = "20000";
        test.edit(newInput);
        newInput.name = "Luke";
        test.add(newInput);
        newInput.hash = "30000";//Luke
        test.edit(newInput);
        newInput.hash = "00001";
        test.edit(newInput);
        
        test.delete("00001");
        test.syncDB();
        */
        //stage 5 6 
        /*
		test.link( "20001", "40001", "inf");
		test.sever( "20001", "40001" );
		test.sever("30001", "10001");
        test.syncDB();
        */
        //stage 7
        test.findPath( "30001", "10001");
        
        
        assertEquals(2, test.neighborList1.size());
        assertEquals(2, test.neighborList2.size());
        assertEquals(0, test.neighborList3.size());
        assertEquals(0, test.directedWeb.size());
        
        
        
        
        
	}
}
