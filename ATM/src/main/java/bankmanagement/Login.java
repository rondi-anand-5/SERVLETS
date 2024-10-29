package bankmanagement;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection con;

    public Login() {
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
            throw new ServletException("Failed to initialize the servlet", e);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        try {
            PrintWriter out = response.getWriter();
            String accStr = request.getParameter("AccNo");
            String acpStr = request.getParameter("AccPin");

            if (accStr != null && !accStr.isEmpty() && acpStr != null && !acpStr.isEmpty()) {
                try {
                    long ac = Long.parseLong(accStr);
                    int acp = Integer.parseInt(acpStr);

                    String qry = "SELECT Accno, accpin FROM Acco WHERE Accno = ?";
                    PreparedStatement pstmt = con.prepareStatement(qry);
                    pstmt.setLong(1, ac);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String dbPin = rs.getString("accpin");
                        if (acp == Integer.parseInt(dbPin)) {
                            // Create an HttpSession and set the account number as an attribute
                            HttpSession session = request.getSession();
                            session.setAttribute("accountNumber", accStr);

                            // Get and display session ID
                            String sessionId = session.getId();
                            out.println("<h1>Session ID: " + sessionId + "</h1>");
                            out.println("<h1>Yes, you can enter the bank</h1>");
                            out.println("<h1>User authenticated successfully.</h1>");
                            out.println("<h1><a href='NewAccount.html'>LOGIN NOW</a></h1>");
                        } else {
                            out.println("<h1>Wrong Account Number or Account Pin</h1>");
                        }
                    } else {
                        out.println("<h1>User does not exist</h1>");
                    }
                } catch (NumberFormatException e) {
                    out.println("<h1>Invalid input format for account number or pin</h1>");
                }
            } else {
                out.println("<h1>Account Number and Pin cannot be empty</h1>");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
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
