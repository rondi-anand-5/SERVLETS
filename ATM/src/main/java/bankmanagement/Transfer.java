package bankmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Transfer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Connection connection;

    public void init() {
        String Driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String username = "c##tables";
        String psword = "anand12";

        try {
            Class.forName(Driver);
            connection = DriverManager.getConnection(url, username, psword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String fromAccNo = request.getParameter("FromAccNo");
        String toAccNo = request.getParameter("ToAccNo");
        String amountStr = request.getParameter("Amount");
        String description = request.getParameter("Description");
        double amount = Double.parseDouble(amountStr);

        try {
            // Check current balance of fromAccNo
            String balanceQuery = "SELECT BALANCE FROM Acco WHERE ACCNO = ?";
            PreparedStatement balanceStmt = connection.prepareStatement(balanceQuery);
            balanceStmt.setString(1, fromAccNo);
            ResultSet rs = balanceStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("BALANCE");

                // Ensure sufficient funds
                if (currentBalance >= amount) {
                    double newBalance = currentBalance - amount;

                    // Update fromAccNo balance
                    String updateBalanceQuery = "UPDATE Acco SET BALANCE = ? WHERE ACCNO = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateBalanceQuery);
                    updateStmt.setDouble(1, newBalance);
                    updateStmt.setString(2, fromAccNo);
                    updateStmt.executeUpdate();

                    // Log the transfer
                    String insertQuery = "INSERT INTO Acco (from_account, to_account, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, fromAccNo);
                    preparedStatement.setString(2, toAccNo);
                    preparedStatement.setDouble(3, amount);
                    preparedStatement.setString(4, description);
                    preparedStatement.executeUpdate();

                    // Display response
                    out.println("<html>");
                    out.println("<head><title>Transfer Status</title>");
                    out.println("<style>");
                    out.println("body { font-family: Arial, sans-serif; background-color: lavender; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
                    out.println(".container { background-color: #f8f9fa; padding: 30px; border-radius: 10px; box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.2); max-width: 400px; text-align: center; }");
                    out.println("h1 { color: #333; margin-bottom: 20px; }");
                    out.println("p { color: #555; margin: 10px 0; }");
                    out.println("</style>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div class='container'>");
                    out.println("<h1>Bank Transfer Successful</h1>");
                    out.println("<p><strong>From Account No:</strong> " + fromAccNo + "</p>");
                    out.println("<p><strong>To Account No:</strong> " + toAccNo + "</p>");
                    out.println("<p><strong>Amount:</strong> $" + amount + "</p>");
                    out.println("<p><strong>Description:</strong> " + description + "</p>");
                    out.println("<p><strong>Updated Balance:</strong> $" + newBalance + "</p>");
                    out.println("</div>");
                    out.println("</body>");
                    out.println("</html>");
                } else {
                    out.println("Insufficient funds for this transfer.");
                }
            } else {
                out.println("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
