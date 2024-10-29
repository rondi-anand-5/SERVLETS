package bankmanagement;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Withdraw")
public class Withdraw extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection con;

    public Withdraw() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String username = "c##tables";
        String password = "anand12";

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        long ac = Long.parseLong(request.getParameter("AccNo"));
        double withdrawalAmount = Double.parseDouble(request.getParameter("Amount"));

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Check if the account exists
            String checkAccountQuery = "SELECT AccNo, balance FROM Acco WHERE AccNo = ?";
            pstmt = con.prepareStatement(checkAccountQuery);
            pstmt.setLong(1, ac);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");

                if (currentBalance >= withdrawalAmount) {
                    String updateQuery = "UPDATE Acco SET balance=? WHERE AccNo=?";
                    pstmt = con.prepareStatement(updateQuery);
                    pstmt.setDouble(1, currentBalance - withdrawalAmount);
                    pstmt.setLong(2, ac);

                    int rowsUpdated = pstmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        out.println("<html>");
                        out.println("<head><title>Withdrawal Successful</title></head>");
                        out.println("<body style='background-color: yellow;'>");
                        out.println("<center>");
                        out.println("<h2 style='background-color: blue;'>Withdrawal Successful</h2>");
                        out.println("<p>Your withdrawal of $" + withdrawalAmount + " has been processed.</p>");
                        out.println("<p>Avaliable balance is:"+currentBalance +"</p>");
                        out.println("<a href='index.html'>Back to Home</a>");
                        out.println("</center>");
                        out.println("</body>");
                        out.println("</html>");
                    } else {
                        out.println("<html>");
                        out.println("<head><title>Withdrawal Failed</title></head>");
                        out.println("<body style='background-color: yellow;'>");
                        out.println("<center>");
                        out.println("<h2 style='background-color: red;'>Withdrawal Failed</h2>");
                        out.println("<p>Unable to process the withdrawal.</p>");
                        out.println("<a href='index.html'>Back to Home</a>");
                        out.println("</center>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                } else {
                    out.println("<html>");
                    out.println("<head><title>Withdrawal Failed</title></head>");
                    out.println("<body style='background-color: yellow;'>");
                    out.println("<center>");
                    out.println("<h2 style='background-color: red;'>Withdrawal Failed</h2>");
                    out.println("<p>Insufficient balance for withdrawal.</p>");
                    out.println("<a href='index.html'>Back to Home</a>");
                    out.println("</center>");
                    out.println("</body>");
                    out.println("</html>");
                }
            } else {
                out.println("<html>");
                out.println("<head><title>Withdrawal Failed</title></head>");
                out.println("<body style='background-color: yellow;'>");
                out.println("<center>");
                out.println("<h2 style='background-color: red;'>Withdrawal Failed</h2>");
                out.println("<p>Account not found.</p>");
                out.println("<a href='index.html'>Back to Home</a>");
                out.println("</center>");
                out.println("</body>");
                out.println("</html>");
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

    public void destroy() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
