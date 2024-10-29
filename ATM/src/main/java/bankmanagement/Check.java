package bankmanagement;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
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

public class Check extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection con;

    public Check() {
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
        PrintWriter out = response.getWriter();

        long accNo = Long.parseLong(request.getParameter("accno"));
        int accPin = Integer.parseInt(request.getParameter("accpin"));
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT balance FROM Acco WHERE AccNo = ? AND AccPin = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setLong(1, accNo);
            pstmt.setInt(2, accPin);
            rs = pstmt.executeQuery();

            out.println("<html><body>");

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                out.println("<h2>Balance Information</h2>");
                out.println("<p>Account Number: " + accNo + "</p>");
                out.println("<p>Balance: $" + balance + "</p>");
            } else {
                out.println("<h2>Invalid Account or PIN</h2>");
            }

            out.println("<p><a href='check_balance.html'>Go Back</a></p>");
            out.println("</body></html>");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e.getMessage());
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

    @Override
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
