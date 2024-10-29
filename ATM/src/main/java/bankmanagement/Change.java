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

@WebServlet("/change")
public class Change extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection conn;

    public Change() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String username = "c##tables";
        String password = "anand12";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPinStr = request.getParameter("cpin");
        String newPinStr = request.getParameter("npin");
        String confirmNewPinStr = request.getParameter("cfnpin");
        PrintWriter pw = response.getWriter();

        // Validation for empty fields
        if (currentPinStr == null || currentPinStr.isEmpty() ||
            newPinStr == null || newPinStr.isEmpty() ||
            confirmNewPinStr == null || confirmNewPinStr.isEmpty()) {
            pw.write("All fields are required.");
            return;
        }

        long currentPin = Long.parseLong(currentPinStr);
        long newPin = Long.parseLong(newPinStr);
        long confirmNewPin = Long.parseLong(confirmNewPinStr);

        if (newPin != confirmNewPin) {
            pw.write("New PIN and Confirm PIN do not match.");
            return;
        }

        String selectQuery = "SELECT Accpin FROM Acco WHERE Accpin = ?";
        String updateQuery = "UPDATE Acco SET Accpin = ? WHERE Accpin = ?";

        try (PreparedStatement ptmt = conn.prepareStatement(selectQuery)) {
            ptmt.setLong(1, currentPin);
            ResultSet rs = ptmt.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setLong(1, newPin);
                    updateStmt.setLong(2, currentPin);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        pw.println("<html>");
                        pw.println("<body style='background-color: lavender;'>");
                        pw.println("<div class='container'>");
                        pw.println("<center>");
                        pw.println("<h3>PIN updated successfully!</h3>");
                        pw.println("<p>Your new PIN is: " + newPin + "</p>");
                        pw.println("<a href='NewAccount.html'>Back to Home</a>");
                        pw.println("</center>");
                        pw.println("</div>");
                        pw.println("</body>");
                        pw.println("</html>");
                    } else {
                        pw.write("Failed to update PIN.");
                    }
                }
            } else {
                pw.write("Current PIN is incorrect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            pw.write("An error occurred while processing the request.");
        }
    }

    @Override
    public void destroy() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
