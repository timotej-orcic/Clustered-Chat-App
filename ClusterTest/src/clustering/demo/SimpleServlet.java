package clustering.demo;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("")
public class SimpleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SimpleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write("Server: " + System.getProperty("server.name"));
		if(request.getParameter("stateful") != null) {
			HttpSession session = request.getSession(true);
			Integer count = (Integer) session.getAttribute("count");
			
			if(count == null) {
				count = 1;
			}
			
			response.getWriter().write("Request Count" + count);
			session.setAttribute("count", ++count);
		}
	}

}
