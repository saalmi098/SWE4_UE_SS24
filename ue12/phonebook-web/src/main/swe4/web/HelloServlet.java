package swe4.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// direkter Aufruf:
// http://localhost:8080/swe4/hello?name=Franzi

// Aufruf über hello.html (HTTP-GET-Request):
// http://localhost:8080/swe4/hello.html

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");

        String name = req.getParameter("name");
        if (name == null) {
            // wenn kein Name in der URL angegeben wurde, ist er null
            name = "User";
        }

        out.println("<html>");

        out.println("<body>");
        out.printf("<h1>Hello %s from HelloServlet</h1>%n", name);
        out.println("</body>");

        out.println("</html>");
    }
}
