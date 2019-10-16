package dev.mason.daos;

import java.util.Set;

import dev.mason.entities.Reimbursement;

public interface ReimbursementDAO {

	Reimbursement createReimbursement(Reimbursement item);
	
	Reimbursement getReimbursementById(int r_id);
	Set<Reimbursement> getAllReimbursementsFromUser(int u_id);
	Set<Reimbursement> getAllReimbursementsFromApprover(int u_id);
	Set<Reimbursement> getAllReimbursements();
	
	boolean updateReimbursement(Reimbursement item);
	
	boolean deleteReimbursement(Reimbursement item);
}
