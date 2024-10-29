package bankmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Deposit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection con;

    public Deposit() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        String Driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String username = "c##tables";
        String psword = "anand12";

        try {
            Class.forName(Driver);
            con = DriverManager.getConnection(url, username, psword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        long accNo = Long.parseLong(request.getParameter("accno"));
        double depositAmount = Double.parseDouble(request.getParameter("amount"));
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String checkAccountQuery = "SELECT AccNo, balance FROM Acco WHERE AccNo = ?";
            pstmt = con.prepareStatement(checkAccountQuery);
            pstmt.setLong(1, accNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                
                if (rs.wasNull()) {
                    currentBalance = 0;
                }

                double newBalance = currentBalance + depositAmount;

                String updateQuery = "UPDATE Acco SET balance = ? WHERE AccNo = ?";
                pstmt = con.prepareStatement(updateQuery);
                pstmt.setDouble(1, newBalance);
                pstmt.setLong(2, accNo);  // Corrected this line

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    printResponse(response, "Deposit Successful",
                                  "Your deposit of $" + depositAmount + " has been processed.<br>" +
                                  "Your new balance is: $" + newBalance, true);
                } else {
                    printResponse(response, "Deposit Failed", "Unable to process the deposit.", false);
                }
            } else {
                printResponse(response, "Deposit Failed", "Account not found.", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void printResponse(HttpServletResponse response, String title, String message, boolean isSuccess) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>" + title + "</title></head>");
        out.println("<body style='background-color: yellow;'>");
        out.println("<center>");
        out.println("<h2 style='background-color: " + (isSuccess ? "blue" : "red") + ";'>" + title + "</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='registrationForm.html'>Back to Home</a>");
        out.println("</center>");
        out.println("</body>");
        out.println("</html>");
    }
}
