package bankmanagement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con;

    public Register() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        try {
            String driver = "oracle.jdbc.driver.OracleDriver";
            String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
            String username = "c##tables";
            String password = "anand12";

            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle GET requests
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h2>Access through the registration form</h2>");
        response.getWriter().println("<a href='registrationForm.html'>Go to Registration Page</a>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	long ac = (long) Double.parseDouble(request.getParameter("accno"));
            int ap = Integer.parseInt(request.getParameter("accpin"));
            
            
            String entryOfDateStr = request.getParameter("entry_of_date");
            if (entryOfDateStr == null || entryOfDateStr.isEmpty()) {
                throw new IllegalArgumentException("Entry date is required.");
            }
            Date da = Date.valueOf(entryOfDateStr); // Ensure this is in YYYY-MM-DD format
            
            String s1 = request.getParameter("name");
            String s2 = request.getParameter("pass");

            
            String dobStr = request.getParameter("dob");
            if (dobStr == null || dobStr.isEmpty()) {
                throw new IllegalArgumentException("Date of birth is required.");
            }
            Date dob = Date.valueOf(dobStr); // Ensure this is in YYYY-MM-DD format
            
            
            long aadh = Long.parseLong(request.getParameter("aadharno"));
            
            String gender = request.getParameter("gender");
            String education = request.getParameter("education");
            
           
            long contact = Long.parseLong(request.getParameter("contact"));
            
            String email = request.getParameter("email");
            String address = request.getParameter("address");

            String inse = "INSERT INTO Acco (ACCNO, ACCPIN, ENTRY_OF_DATE, NAME, PASS, DOB, AADHARNO, GENDER, EDUCATION, CONTACT, EMAIL, ADDRESS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = null;
            
            try {
                pstmt = con.prepareStatement(inse);
                pstmt.setLong(1, ac);
                pstmt.setInt(2, ap);
                pstmt.setDate(3, da);
                pstmt.setString(4, s1);
                pstmt.setString(5, s2);
                pstmt.setDate(6, dob); 
                pstmt.setLong(7, aadh);
                pstmt.setString(8, gender);
                pstmt.setString(9, education);
                pstmt.setLong(10, contact);
                pstmt.setString(11, email);
                pstmt.setString(12, address);

                pstmt.executeUpdate();
                response.setContentType("text/html");
                response.getWriter().println("<h2>Registration successful!</h2>");
                response.getWriter().println("<a href='Logform.html'> Click on Login</a>");
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("An error occurred: " + e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.getWriter().println("Input error: " + e.getMessage());
        }
    }
}
