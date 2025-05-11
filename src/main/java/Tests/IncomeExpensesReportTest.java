package Tests;

import model.ActivitiesModel;
import model.MovementsModel;
import util.Database;
import util.ModelManager;
import org.junit.*;
import static org.junit.Assert.*;

public class IncomeExpensesReportTest {

    private ActivitiesModel activitiesModel;
    private MovementsModel movementsModel;
    
    private static Database db = new Database();

    @Before
    public void setUp() {
        activitiesModel = ModelManager.getInstance().getActivitiesModel();
        movementsModel = ModelManager.getInstance().getMovementsModel();
        
        db.createDatabase(true);
        loadCleanDatabase(db);
    }
    
    public static void loadCleanDatabase(Database db) {
    	db.executeBatch(new String[] {
    			"delete from Activities",
    			"delete from IncomesExpenses",
    			"delete from Movements",
    			"INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES ('Informatics Olimpics 2020', 13, 'planned', '2020-01-01', '2020-01-01', 'Convention Center')",
    			"INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES ('Informatics Olimpics 2020', 13, 'planned', '2020-01-01', '2020-01-01', 'Convention Center')",
    			"INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES ('Informatics Olimpics 2020', 13, 'planned', '2020-01-01', '2020-01-01', 'Convention Center')",
    			"INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES ('Informatics Olimpics 2020', 13, 'planned', '2020-01-01', '2020-01-01', 'Convention Center')",
    			"INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES ('Informatics Olimpics 2020', 13, 'planned', '2020-01-01', '2020-01-01', 'Convention Center')",
    			"INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES ('Informatics Olimpics 2020', 13, 'planned', '2020-01-01', '2020-01-01', 'Convention Center')",
    			"INSERT INTO IncomesExpenses(idActivity, type, status, amountEstimated, dateEstimated, concept) VALUES (1, 'income', 'estimated', 100, '2020-01-01', 'Sponsorship from Company A')",
    			"INSERT INTO IncomesExpenses(idActivity, type, status, amountEstimated, dateEstimated, concept) VALUES (1, 'expense', 'estimated', 100, '2020-01-01', 'Sponsorship from Company A')",
    			"INSERT INTO Movements (idType, concept, amount, date) VALUES (1, 'First payment from Company A', 100, '2020-01-01')",
    			"INSERT INTO Movements (idType, concept, amount, date) VALUES (2, 'First payment from Company A', 100, '2020-01-01')",
    			"INSERT INTO IncomesExpenses(idActivity, type, status, amountEstimated, dateEstimated, concept) VALUES (7, 'income', 'estimated', -100, '2020-01-01', 'Sponsorship from Company A')",
    			"INSERT INTO IncomesExpenses(idActivity, type, status, amountEstimated, dateEstimated, concept) VALUES (4, 'expense', 'estimated', -100, '2020-01-01', 'Sponsorship from Company A')",
    			"INSERT INTO Movements (idType, concept, amount, date) VALUES (5, 'First payment from Company A', -100, '2020-01-01')",
    			"INSERT INTO Movements (idType, concept, amount, date) VALUES (6, 'First payment from Company A', -100, '2020-01-01')"
    	});
    }

    @Test
    /*
     * Start date before the end date
     * End date after the end date
     * Valid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_1() {
    	String sol = String.valueOf(activitiesModel.getActivitiesFromCurrentYear("2020-01-01", "2025-01-01").get(0).getId());
    	String ei = String.valueOf(movementsModel.getEstimatedIncome("1"));
    	String pi = String.valueOf(movementsModel.getActualIncome("1"));
    	String ee = String.valueOf(movementsModel.getEstimatedExpenses("1"));
    	String pe = String.valueOf(movementsModel.getActualExpenses("1"));

        assertEquals("1", sol);
        assertEquals("100.0", ei);
        assertEquals("100.0", pi);
        assertEquals("100.0", ee);
        assertEquals("100.0", pe);
    }

    @Test
    /*
     * Same start date and end date
     * Valid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_2() {
    	String sol = String.valueOf(activitiesModel.getActivitiesFromCurrentYear("2020-01-01", "2020-01-01").get(0).getId());
    	String ei = String.valueOf(movementsModel.getEstimatedIncome("2"));
    	String pi = String.valueOf(movementsModel.getActualIncome("2"));
    	String ee = String.valueOf(movementsModel.getEstimatedExpenses("2"));
    	String pe = String.valueOf(movementsModel.getActualExpenses("2"));
    	
        assertEquals("7", sol);
        assertEquals("0.0", ei);
        assertEquals("0.0", pi);
        assertEquals("0.0", ee);
        assertEquals("0.0", pe);
    }

    @Test
    /*
     * Start date after the end date
     * End date before the end date
     * Valid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_3() {
        Exception ex = null;
        try {
            activitiesModel.getActivitiesFromCurrentYear("2025-01-01", "2020-01-01");
        } catch (Exception e) {
            ex = e;
        }
        assertEquals("Not valid date", ex.getMessage());
    }

    @Test
    /*
     * Start date after the end date
     * End date before the end date
     * Valid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_4() {
        Exception ex = null;
        try {
            activitiesModel.getActivitiesFromCurrentYear("2025-01-01", "2020-01-01");
        } catch (Exception e) {
            ex = e;
        }
        assertEquals("Not valid date", ex.getMessage());
    }

    @Test
    /*
     * Start date before the end date
     * End date after the end date
     * Invalid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_5() {
    	String sol = String.valueOf(movementsModel.getEstimatedIncome("3"));
        assertEquals("0.0", sol);
    }

    @Test
    /*
     * Start date before the end date
     * End date after the end date
     * Valid income estimated
     * Invalid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_6() {
        String sol = String.valueOf(movementsModel.getActualIncome("4"));
        assertEquals("0.0", sol);
    }

    @Test
    /*
     * Start date before the end date
     * End date after the end date
     * Valid income estimated
     * Valid income paid
     * Invalid expenses estimated
     * Valid expenses paid
     * Previously registered activity
     */
    public void testTC1_7() {
        String sol = String.valueOf(movementsModel.getEstimatedExpenses("5"));
        assertEquals("0.0", sol);
    }

    @Test
    /*
     * Start date before the end date
     * End date after the end date
     * Valid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Invalid expenses paid
     * Previously registered activity
     */
    public void testTC1_8() {
        String sol = String.valueOf(movementsModel.getActualExpenses("6"));
        assertEquals("0.0", sol);
    }

    @Test
    /*
     * Start date before the end date
     * End date after the end date
     * Valid income estimated
     * Valid income paid
     * Valid expenses estimated
     * Valid expenses paid
     * Non-registered activity
     */
    public void testTC1_9() {
    	String sol = "valid";
        if (movementsModel.getEstimatedIncome("99") == 0.0) {
        	sol = "ERROR. Provided idActivity for getEstimatedIncome does not exist.";
        }
        assertEquals("ERROR. Provided idActivity for getEstimatedIncome does not exist.", sol);
    }
}
