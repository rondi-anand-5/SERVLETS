package bankmanagement;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;

public class Welcome extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Welcome Page</title>");
        out.println("<style>");
        out.println("body {");
        out.println("    background-color: lavender;");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    height: 100vh;");
        out.println("    margin: 0;");
        out.println("}");
        out.println(".container {");
        out.println("    text-align: center;");
        out.println("    background-color: #f0f0f0;");
        out.println("    padding: 30px;");
        out.println("    border-radius: 10px;");
        out.println("    box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.2);");
        out.println("}");
        out.println("h1 {");
        out.println("    color: #333;");
        out.println("}");
        out.println("a {");
        out.println("    display: inline-block;");
        out.println("    margin: 10px;");
        out.println("    padding: 10px 20px;");
        out.println("    color: white;");
        out.println("    background-color: #4CAF50;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 5px;");
        out.println("    transition: background-color 0.3s;");
        out.println("}");
        out.println("a:hover {");
        out.println("    background-color: #45a049;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>WELCOME TO THE ANAND ATM </h1>");
        out.println("<a href='Logform.html'>Go to Login Page</a>");
        out.println("<a href='registrationForm.html'>Go to Register Page</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
