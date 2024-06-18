package swe4.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import swe4.dal.Person;
import swe4.dal.PersonDao;
import swe4.dal.PersonDaoJdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class PhoneBookServlet extends HttpServlet {

    private PersonDao personDao;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            Context initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:comp/env");

            // lookup name ist als Resource definiert in context.xml (type DataSource, daher cast moeglich)
            DataSource ds = (DataSource)envContext.lookup("jdbc/PhoneBookDb");
            personDao = new PersonDaoJdbc(ds);
            System.out.println("PhoneServlet: DataSource created");
        } catch (Exception e) {
            System.out.println("PhoneServlet: Problems creating servlet instance");
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            out.println("<html>");
            out.println("<head>");

            if (req.getParameter("list") != null) {
                out.println("<h3>List of all entries in phone book</h3>");
                out.println("<table border='1'>");
                out.println("  <tr><td>ID</td><td>First Name</td><td>Last Name</td><td>Address</td><td>Phone Number</td></tr>");
                for (Person person : personDao.getAll()) {
                    out.println("  <tr>");
                    out.println("    <td>" + person.getId() + "</td>");
                    out.println("    <td>" + person.getFirstName() + "</td>");
                    out.println("    <td>" + person.getLastName() + "</td>");
                    out.println("    <td>" + person.getAddress() + "</td>");
                    out.println("    <td>" + person.getPhoneNumber() + "</td>");
                    out.println("  </tr>");
                }
            } else if (req.getParameter("find") != null) {
                String lastName = req.getParameter("findLastName");
                Collection<Person> persons = personDao.get(lastName);

                out.println("<h3>Persons with last name " + lastName + "</h3>");
                out.println("<table border='1'>");
                out.println("  <tr><td>ID</td><td>First Name</td><td>Last Name</td><td>Address</td><td>Phone Number</td></tr>");
                for (Person person : persons) {
                    out.println("  <tr>");
                    out.println("    <td>" + person.getId() + "</td>");
                    out.println("    <td>" + person.getFirstName() + "</td>");
                    out.println("    <td>" + person.getLastName() + "</td>");
                    out.println("    <td>" + person.getAddress() + "</td>");
                    out.println("    <td>" + person.getPhoneNumber() + "</td>");
                    out.println("  </tr>");
                }
                out.println("</table>");
            } else if (req.getParameter("insert") != null) {
                String firstName = req.getParameter("insertFirstName");
                String lastName = req.getParameter("insertLastName");
                String address = req.getParameter("insertAddress");
                String phoneNumber = req.getParameter("insertPhoneNumber");
                Person person = new Person(firstName, lastName, address, phoneNumber);
                personDao.store(person);

                out.printf("<h3>Inserted new person in phone book: %s</h3>", person);
            } else if (req.getParameter("delete") != null) {
                int id = Integer.parseInt(req.getParameter("id"));
                personDao.delete(id);
                out.printf("<h3>Deleted person</h3>");
            }
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        } finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }

    @Override
    public void destroy() {
        try {
            personDao.close();
            System.out.println("PhoneServlet: PersonDao closed successfully");
        } catch (Exception e) {
            System.out.println("PhoneServlet: Problems closing PersonDao");
        }

        super.destroy();
    }
}
