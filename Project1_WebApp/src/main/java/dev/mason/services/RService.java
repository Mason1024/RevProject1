package dev.mason.services;

import java.util.Set;

import dev.mason.daos.*;
import dev.mason.entities.Reimbursement;
import dev.mason.entities.User;

public class RService implements ReimbursementService{

	private static ReimbursementDAO rdao = ReimbursementDAOjdbc.getDAO();
	private static UserDAO udao = UserDAOjdbc.getDAO();
	
	private static ReimbursementService rs;
	private RService() {};
	public static ReimbursementService getService() {
		if(rs==null) {
			rs=new RService();
		}
		return rs;
	}
	
	@Override
	public User login(String username, String password) {
		User user = udao.getUserByUsername(username);
		if(user!=null) {
			if(user.getPassword().equals(password)) {
				return user;
			}else {
				return null;
			}
		}
		return null;
	}
	
	@Override
	public User getUserById(int u_id) {
		return udao.getUserById(u_id);
	}
	
	@Override
	public User getUserByUsername(String username) {
		return udao.getUserByUsername(username);
	}
	
	@Override
	public Set<User> getAllUsers(){
		return udao.getAllUsers();
	}
	
	@Override
	public Reimbursement openReimbursement(User employee, String desc, double price) {
		if(employee.getIsManager()==0) {
			Reimbursement r = new Reimbursement(0, employee.getUsername(), "", desc, price, System.currentTimeMillis(), "", 0);
			return rdao.createReimbursement(r);
		}else {
			return null;
		}
	}

	@Override
	public int getReimbursementStatus(int r_id) {
		return rdao.getReimbursementById(r_id).getState();
	}

	@Override
	public boolean approveReimbursement(Reimbursement item, User manager, String comment) {
		if(manager.getIsManager()==1) {
			item.setApprover(manager.getUsername());
			item.setComment(comment);
			item.setState(1);
			return rdao.updateReimbursement(item);
		}
		return false;
	}

	@Override
	public boolean rejectReimbursement(Reimbursement item, User manager, String comment) {
		if(manager.getIsManager()==1) {
			item.setApprover(manager.getUsername());
			item.setComment(comment);
			item.setState(2);
			return rdao.updateReimbursement(item);
		}
		return false;
	}

	@Override
	public Reimbursement getReimbursementById(int r_id) {
		return rdao.getReimbursementById(r_id);
	}

	@Override
	public Set<Reimbursement> getReimbursementsByUser(int submitter) {
		return rdao.getAllReimbursementsFromUser(submitter);
	}
	
	@Override
	public Set<Reimbursement> getReimbursementsByApprover(int approver) {
		return rdao.getAllReimbursementsFromApprover(approver);
	}

	@Override
	public Set<Reimbursement> getAllReimbursements() {
		return rdao.getAllReimbursements();
	}

}
