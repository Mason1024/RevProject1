package dev.mason.controllers;

import java.io.IOException;
import java.util.HashSet;
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
		Set<Reimbursement> rSet = rs.getReimbursementsByUser(((User)request.getSession().getAttribute("User")).getU_id());
		if(!rSet.isEmpty()) {
			Gson gson = new Gson();
			String json = gson.toJson(rSet);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	public void getAllReimbursementsByManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<Reimbursement> rSet = rs.getReimbursementsByApprover(Integer.parseInt(request.getParameter("approver")));
		if(!rSet.isEmpty()) {
			Gson gson = new Gson();
			String json = gson.toJson(rSet);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	public void getAllReimbursements(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<Reimbursement> rSet = rs.getAllReimbursements();
		if(!rSet.isEmpty()) {
			Gson gson = new Gson();
			String json = gson.toJson(rSet);
			response.getWriter().append(json);
		}else {
			//fetch failed
			response.getWriter().append("fetch failed");
		}
	}
	public void getStats(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Set<Stat> stats = new HashSet<Stat>();
		for(User u:rs.getAllUsers()) {
			System.out.println("User - "+u);
			if(u.getIsManager()==1) {
				System.out.println("Manager");
				int approves=0;
				int rejects=0;
				double largest=0.0;
				double sum=0.0;
				double average=0.0;				
				for(Reimbursement r:rs.getReimbursementsByApprover(u.getU_id())) {
					System.out.println("Reim - "+r);
					if(r.getState()==1) approves++;
					if(r.getState()==2) rejects++;
					if(r.getPrice()>largest) largest=r.getPrice();
					sum+=r.getPrice();
				}	
				average=sum/approves;
				Stat s = new Stat(u.getUsername(),approves,rejects,(int)largest,(int)average);
				System.out.println("Stat - "+s);
				stats.add(s);
			}
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(stats);
		response.getWriter().append(json);
		
	}
	private class Stat{
		String username;
		int approves;
		int rejects;
		int largest;
		int average;
		
		public Stat() {	}
		public Stat(String username, int approves, int rejects, int largest, int average) {
			this.username=username;
			this.approves=approves;
			this.rejects=rejects;
			this.largest=largest;
			this.average=average;
		}
		public String getUsername() {return this.username;}
		public void setUsername(String username) {this.username=username;}
		public int getApproves() {return this.approves;}
		public void setApproves(int approves) {this.approves=approves;}
		public int getRejects() {return this.rejects;}
		public void setRejects(int rejects) {this.rejects=rejects;}
		public int getLargest() {return this.largest;}
		public void setLargest(int largest) {this.largest=largest;}
		public int getAverage() {return this.average;}
		public void setAverage(int average) {this.average=average;}
	}
}
