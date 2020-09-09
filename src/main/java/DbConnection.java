import java.sql.*;

public class DbConnection {
	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/";

	//  Database credentials
	private static final String USER = "postgres";
	private static final String PASS = "admin";

	public static void load_driver() throws Exception{
		System.out.println("Loading the driver");
		Class.forName(JDBC_DRIVER);
	}

	public static Connection open_connection(String dbName) throws Exception {
		System.out.println("Connecting to database...");
		Connection conn = DriverManager.getConnection(DB_URL + dbName, USER, PASS);
		System.out.println(String.format("Connected to %s with user name: %s and password: %s", DB_URL+dbName, USER, PASS));
		return conn;
	}


	public static Statement create_statement(Connection conn) throws Exception {
		System.out.println("Creating database...");
		return conn.createStatement();
	}

	public static void create_db(Statement stm, String sql) throws Exception {
		System.out.println("Going to run query to create DB: " + sql);
		stm.executeUpdate(sql);
		System.out.println("Database created successfully...");
	}

	public static void close_statement(Statement stm) throws Exception {
		stm.close();
	}

	public static void close_connection( Connection conn) throws Exception {
		System.out.println("Going to close connection...");
		conn.close();
		System.out.println("Connection safely closed!");

	}

	public static void create_table(Statement stm, String sql) throws Exception {
		System.out.println("Going to create table with sql: " + sql);
		stm.executeUpdate(sql);
		System.out.println("Table created!");
	}

	public static void insert_data(Statement stm, String sql) throws SQLException {
		System.out.println("Insert data into table...");
		stm.executeUpdate(sql);
		System.out.println("Data insert successfully!");
	}

	public static void show_data(Statement stm, String sql) throws Exception {
		System.out.println("Going to print data...");
		ResultSet rs = stm.executeQuery(sql);
		//STEP 5: Extract data from result set
		while(rs.next()){
			//Retrieve by column name
			int id  = rs.getInt("id");
			int age = rs.getInt("age");
			String first = rs.getString("first");
			String last = rs.getString("last");

			//Display values
			System.out.print("ID: " + id);
			System.out.print(", Age: " + age);
			System.out.print(", First: " + first);
			System.out.println(", Last: " + last);
		}
		System.out.println("Data was printed");
	}

	public static void delete_record(Statement stm, String sql) throws Exception {
		System.out.println("Going to delete one record from table");
		stm.executeUpdate(sql);
		System.out.println("Record deleted!");
	}

	public static void main(String[] args) {
		Connection conn = null;
		Statement stm = null;


		try{
			// STEP 1: Register JDBC driver
			load_driver();

			// STEP 2: Open a connection
			conn = open_connection("");

			// STEP 3: Execute a query
			stm = create_statement(conn);

			// STEP 4: Creating DB.
			String sql = "CREATE DATABASE STUDENTS";
			create_db(stm, sql);

			// STEP 5: Closing statement
			close_statement(stm);

			// STEP 6: Closing connection
			close_connection(conn);

			// STEP 6: Connect to the DB you have just created
			conn = open_connection("students");

			// STEP 7: Re-create statement.
			stm = create_statement(conn);

			// STEP 8: Create table....
			sql = "CREATE TABLE REGISTRATION " +
					"(id INTEGER not NULL, " +
					" first VARCHAR(255), " +
					" last VARCHAR(255), " +
					" age INTEGER, " +
					" PRIMARY KEY ( id ))";

			create_table(stm, sql);

			// STEP 9: Insert data into the table....
			sql = "INSERT INTO registration (id, first, last, age) " +
					"VALUES (1, 'Eli', 'Barak', 18), "+
					"(2, 'Mor', 'Cohen', 27), "+
					"(3, 'Bil', 'Kole', 32), "+
					"(4, 'Alex', 'WhoKnows', 20);";
			insert_data(stm, sql);

			// STEP 10: Read data from table...
			sql = "SELECT id, first, last, age FROM registration";
			show_data(stm, sql);

			// STEP 11: Delete one line from registration
			sql = "DELETE FROM Registration " +
					"WHERE id = 2";
			delete_record(stm, sql);

			// STEP 12: Print data again
			sql = "SELECT id, first, last, age FROM registration";
			show_data(stm, sql);

			// STEP 13: Closing statement
			close_statement(stm);

			// STEP 14: Closing connection
			close_connection(conn);

		}

		catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		}

		catch(Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			//finally block used to close resources
			try {
				if(stm!=null)
					stm.close();
			}

			catch(SQLException se2){}// nothing we can do

			try {
				if(conn!=null)
					conn.close();
			}

			catch(SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		System.out.println("Goodbye!");

	}
}
