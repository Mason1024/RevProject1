package dev.mason.services;

import java.util.Set;

import dev.mason.entities.Reimbursement;
import dev.mason.entities.User;

public interface ReimbursementService {
	
	public User login(String username, String password);
	public User getUserById(int u_id);
	public User getUserByUsername(String username);
	public Set<User> getAllUsers();
	
	public Reimbursement openReimbursement(User employee, String desc, double price);
	public int getReimbursementStatus(int r_id);
	
	public boolean approveReimbursement(Reimbursement item, User manager, String comment);
	public boolean rejectReimbursement(Reimbursement item, User manager, String comment);
		
	public Reimbursement getReimbursementById(int r_id);
	public Set<Reimbursement> getReimbursementsByUser(int submitter);
	public Set<Reimbursement> getReimbursementsByApprover(int approver);
	public Set<Reimbursement> getAllReimbursements();	
}
