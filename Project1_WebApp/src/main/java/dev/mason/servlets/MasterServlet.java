package dev.mason.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.mason.controllers.ServiceController;

public class MasterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static ServiceController sc = ServiceController.getController();
	
    public MasterServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		
		switch(uri){
			case "/Project1_WebApp/login.do":
				sc.login(request, response);
				break;
			case "/Project1_WebApp/openReimbursement.do":
				sc.openReimbursement(request, response);
				break;
			case "/Project1_WebApp/approve.do":
				sc.approve(request, response);
				break;
			case "/Project1_WebApp/reject.do":
				sc.reject(request, response);
				break;
			case "/Project1_WebApp/getReimbursementById.do":
				sc.getReimbursementById(request, response);
				break;
			case "/Project1_WebApp/getAllReimbursementsByUser.do":
				sc.getAllReimbursementsByUser(request, response);
				break;
			case "/Project1_WebApp/getAllReimbursementsByManager.do":
				sc.getAllReimbursementsByManager(request, response);
				break;
			case "/Project1_WebApp/getAllReimbursements.do":
				sc.getAllReimbursements(request, response);
				break;
			case "/Project1_WebApp/coffee.do":
				response.sendError(418, 
						"	I'm a teapot\r\n" + 
						"   Any attempt to brew coffee with a teapot should result in the error\r\n" + 
						"   code \"418 I'm a teapot\". The resulting entity body MAY be short and\r\n" + 
						"   stout.");
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
