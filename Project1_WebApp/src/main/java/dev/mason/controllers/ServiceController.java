package dev.mason.controllers;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dev.mason.entities.Reimbursement;
import dev.mason.entities.User;
import dev.mason.services.RService;
import dev.mason.services.ReimbursementService;

public class ServiceController {
	
	private static ReimbursementService rs = RService.getService();
	
	private static ServiceController sc;	
	private ServiceController() {	};
	public static ServiceController getController() {
		if(sc==null) {
			sc = new ServiceController();
		}
		return sc;
	}
	
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String uname = request.getParameter("username");
		String pass = request.getParameter("password");
		User u = rs.login(uname, pass);
		if(u!=null) {
			request.getSession().setAttribute("User", u);
			Gson gson = new Gson();
			String json = gson.toJson(u);
			response.getWriter().append(json);
		}else {
			//login failed
			response.getWriter().append("-1");
		}
	}
	
	public void openReimbursement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Gson gson = new Gson();
	
		User user = (User) request.getSession().getAttribute("User");
		String desc = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("amount"));
		
		Reimbursement r = rs.openReimbursement(user, desc, price);
		
		if(r!=null) {
			//pass
			response.getWriter().append(gson.toJson(r));
		}else {
			//fail
			response.getWriter().append("fail");
		}
	}
	
	public void approve(HttpServletRequest request, HttpServletResponse response) {
		Gson gson = new Gson();
		Reimbursement item = gson.fromJson(request.getParameter("reimbursement"), Reimbursement.class);
		User manager = (User) request.getSession().getAttribute("User");
		String comment = item.getComment();
		System.out.println("SC - Approve");
		rs.approveReimbursement(item, manager, comment);
	}
	public void reject(HttpServletRequest request, HttpServletResponse response) {
		Gson gson = new Gson();
		Reimbursement item = gson.fromJson(request.getParameter("reimbursement"), Reimbursement.class);
		User manager = (User) request.getSession().getAttribute("User");
		String comment = item.getComment();
		
		rs.rejectReimbursement(item, manager, comment);
	}
	
	public void getReimbursementById(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Reimbursement r = rs.getReimbursementById(Integer.parseInt(request.getParameter("r_id")));
		if(r!=null) {
			Gson gson = new Gson();
			String json = gson.toJson(r);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	public void getAllReimbursementsByUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<Reimbursement> rSet = rs.getReimbursementByUser(((User)request.getSession().getAttribute("User")).getU_id());
		if(rSet.size()!=0) {
			Gson gson = new Gson();
			String json = gson.toJson(rSet);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	public void getAllReimbursementsByManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<Reimbursement> rSet = rs.getReimbursementByApprover(Integer.parseInt(request.getParameter("approver")));
		if(rSet.size()!=0) {
			Gson gson = new Gson();
			String json = gson.toJson(rSet);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	public void getAllReimbursements(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<Reimbursement> rSet = rs.getAllReimbursement();
		if(rSet.size()!=0) {
			Gson gson = new Gson();
			String json = gson.toJson(rSet);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	
}
