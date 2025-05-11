package Tests;

import model.InvoicesModel;
import util.Database;
import util.ModelManager;
import org.junit.*;
import static org.junit.Assert.*;

public class GenerateInvoicesTest {

    private InvoicesModel invoicesModel;
    
    private static final String dateAgreement = "2024-01-15";
    private static Database db = new Database();


    @Before
    public void setUp() {
        invoicesModel = ModelManager.getInstance().getInvoicesModel();
        
        db.createDatabase(true);
        loadCleanDatabase(db);
    }
    
    public static void loadCleanDatabase(Database db) {
    	db.executeBatch(new String[] {
    			"delete from SponsorshipAgreements",
    			"INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES (1, 1, 1, 5000.00, '2024-01-15', 'signed')",
    	});
    }

    @Test
    /*
     * Non-registered invoice
     * Date issued after the agreement date
     * Previously registered agreement
     * Positive amount
     * Tax rate between 0-100
     * "signed" status
     */
    public void testTC2_1() {
    	invoicesModel.insertNewInvoice("POK9", "1", "2024-01-16", "72", "21", dateAgreement);
    	assertEquals("POK9", invoicesModel.getInvoiceById("POK9").getId());
    }

    @Test
    /*
     * Non-registered invoice
     * Same date issued and agreement date
     * Previously registered agreement
     * No amount
     * Tax rate between 0-100
     */
    public void testTC2_2() {
        invoicesModel.insertNewInvoice("POK10", "1", "2024-01-15", "72", "21", dateAgreement);
    	assertEquals("POK10", invoicesModel.getInvoiceById("POK10").getId());
    }

    @Test
    /*
     * Duplicated invoice ID
     * Same date issued and agreement date
     * Previously registered agreement
     * "signed" status
     * Positive amount
     * Tax rate between 0-100
     */
    public void testTC2_3() {
    	String ex = null;
    	try {
            invoicesModel.insertNewInvoice("POK9", "1", "2024-01-15", "72", "21", dateAgreement);
    	}
    	catch (Exception e) {
			ex = "Not valid ID";
		}
        assertEquals("Not valid ID", ex);
    }

    @Test
    /*
     * Non-registered invoice
     * Date issued before the agreement date
     * Previously registered agreement
     * "signed" status
     * Positive amount
     * Tax rate between 0-100
     */
    public void testTC2_4() {
    	Exception ex = null;
    	try {
    		invoicesModel.insertNewInvoice("POK11", "1", "2024-01-14", "72", "21", dateAgreement);
    	}
	    catch (Exception e) {
	    	ex = e;
	    }
	    assertEquals("Not valid date", ex.getMessage());
    }

    @Test
    /*
     * Non-registered invoice
     * Date issued after today
     * Previously registered agreement
     * "signed" status
     * Positive amount
     * Tax rate between 0-100
     */
    public void testTC2_5() {
    	Exception ex = null;
    	try {
    		invoicesModel.insertNewInvoice("POK12", "1", "2025-05-15", "72", "21", dateAgreement);
    	}
	    catch (Exception e) {
	    	ex = e;
	    }
	    assertEquals("Not valid date", ex.getMessage());
    }

    @Test
    /*
     * Non-registered agreement
     */
    public void testTC2_6() {
    	Exception ex = null;
    	try {
            invoicesModel.insertNewInvoice("POK13", "99", "2024-01-15", "72", "21", dateAgreement);
    	}
    	catch (Exception e) {
			ex = e;
		}
        assertEquals("Not valid ID", ex.getMessage());
    }

    @Test
    /*
     * Previously registered agreement
     * "modified" status
     */
    public void testTC2_7() {
    	Exception ex = null;
    	try {
    		invoicesModel.insertNewInvoice("POK14", "2", "2024-01-15", "72", "21", dateAgreement);
    	}
    	catch (Exception e) {
			ex = e;
		}
        assertEquals("Not valid ID", ex.getMessage());
    }

    @Test
    /*
     * Non-registered invoice
     * Same date issued and agreement date
     * Previously registered agreement
     * "signed" status
     * Negative amount
     * Tax rate between 0-100
     */
    public void testTC2_8() {
    	Exception ex = null;
    	try{
    		invoicesModel.insertNewInvoice("POK15", "1", "2024-01-15", "-56", "21", dateAgreement);
    	}
    	catch (Exception e) {
			ex = e;
		}
        assertEquals("Not valid number", ex.getMessage());
    }

    @Test
    /*
     * Non-registered invoice
     * Same date issued and agreement date
     * Previously registered agreement
     * "signed" status
     * Positive amount
     * Tax rate bigger than 100
     */
    public void testTC2_9() {
    	Exception ex = null;
    	try {
    		invoicesModel.insertNewInvoice("POK16", "1", "2024-01-15", "72", "113", dateAgreement);
    	}
    	catch (Exception e) {
			ex = e;
		}
        assertEquals("Not valid number (0-100)", ex.getMessage());
    }
}
