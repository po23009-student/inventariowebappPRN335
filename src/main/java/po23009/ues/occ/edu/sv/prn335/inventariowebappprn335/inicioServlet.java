package po23009.ues.occ.edu.sv.prn335.inventariowebappprn335;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "inicioServlet", value = "/")
public class inicioServlet extends HttpServlet {
    private String message;

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/plantillas/template-comun.jsf");
            dispatcher.forward(request, response);

        } catch(Exception ex) {
            throw new ServletException(ex);
        }
    }

    public void destroy() {
    }
}