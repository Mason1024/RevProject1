package dev.mason.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import dev.mason.entities.Reimbursement;
import dev.mason.utils.ConnectionUtil;

public class ReimbursementDAOjdbc implements ReimbursementDAO{

	private static Connection conn = ConnectionUtil.getConn();
	private static String rTable = "ReimbursementAppDB.reimbursement";
	private static UserDAO udao = UserDAOjdbc.getDAO();
	
	private static ReimbursementDAO rdao;
	private ReimbursementDAOjdbc() {	};
	public static ReimbursementDAO getDAO() {
		if(rdao==null) {
			rdao = new ReimbursementDAOjdbc();
		}
		return rdao;
	}
	
	@Override
	public Reimbursement createReimbursement(Reimbursement item) {
		try{
			String sql = "INSERT INTO "+rTable+" Values(?,?,?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, 0);
			ps.setInt(2, udao.getUserByUsername(item.getSubmiter()).getU_id());
			ps.setNull(3, java.sql.Types.INTEGER);
			ps.setString(4, item.getDesc());
			ps.setDouble(5, item.getPrice());
			ps.setLong(6, item.getTimestamp());
			ps.setString(7, "");
			ps.setInt(8, 0);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs!=null&&rs.next()) {
				item.setR_id(rs.getInt("r_id")); 
				return item;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Reimbursement getReimbursementById(int r_id) {
		try{
			String sql = "SELECT * FROM "+rTable+" WHERE r_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, r_id);
			ResultSet rs = ps.executeQuery();
			if(rs!=null&&rs.next()) {
				String approver="";
				if(rs.getInt("approver")!=0) {
					approver = udao.getUserById(rs.getInt("approver")).getUsername();
				}
				Reimbursement item = new Reimbursement(
						rs.getInt("r_id"), 
						udao.getUserById(rs.getInt("submitter")).getUsername(), 
						approver, 
						rs.getString("desc"),
						rs.getDouble("price"), 
						rs.getLong("timestamp"), 
						rs.getString("comment"), 
						rs.getInt("state")
					);							
				return item;
			}else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<Reimbursement> getAllReimbursementsFromUser(int u_id) {
		try{
			String sql = "SELECT * FROM "+rTable+" WHERE submitter = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u_id);
			ResultSet rs = ps.executeQuery();
			Set<Reimbursement> items = new HashSet<Reimbursement>();
			while(rs.next()) {
				String approver="";
				if(rs.getInt("approver")!=0) {
					approver = udao.getUserById(rs.getInt("approver")).getUsername();
				}
				Reimbursement item = new Reimbursement(
						rs.getInt("r_id"), 
						udao.getUserById(rs.getInt("submitter")).getUsername(), 
						approver, 
						rs.getString("desc"),
						rs.getDouble("price"), 
						rs.getLong("timestamp"), 
						rs.getString("comment"), 
						rs.getInt("state")
					);							
				items.add(item);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<Reimbursement> getAllReimbursementsFromApprover(int u_id) {
		try{
			String sql = "SELECT * FROM "+rTable+" WHERE approver = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u_id);
			ResultSet rs = ps.executeQuery();
			Set<Reimbursement> items = new HashSet<Reimbursement>();
			while(rs.next()) {
				String approver="";
				if(rs.getInt("approver")!=0) {
					approver = udao.getUserById(rs.getInt("approver")).getUsername();
				}
				Reimbursement item = new Reimbursement(
						rs.getInt("r_id"), 
						udao.getUserById(rs.getInt("submitter")).getUsername(), 
						approver, 
						rs.getString("desc"),
						rs.getDouble("price"), 
						rs.getLong("timestamp"), 
						rs.getString("comment"), 
						rs.getInt("state")
					);							
				items.add(item);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Set<Reimbursement> getAllReimbursements(){
		try{
			String sql = "SELECT * FROM "+rTable;
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			Set<Reimbursement> items = new HashSet<Reimbursement>();
			while(rs.next()) {
				String approver="";
				if(rs.getInt("approver")!=0) {
					approver = udao.getUserById(rs.getInt("approver")).getUsername();
				}
				Reimbursement item = new Reimbursement(
						rs.getInt("r_id"), 
						udao.getUserById(rs.getInt("submitter")).getUsername(), 
						approver,
						rs.getString("desc"),
						rs.getDouble("price"), 
						rs.getLong("timestamp"), 
						rs.getString("comment"), 
						rs.getInt("state")
					);							
				items.add(item);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateReimbursement(Reimbursement item) {
		try{
			String sql = "UPDATE "+rTable+" SET approver=?, comment=?, state=? WHERE r_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, udao.getUserByUsername(item.getApprover()).getU_id());
			ps.setString(2, item.getComment());
			ps.setInt(3, item.getState());
			ps.setInt(4, item.getR_id());
			ps.execute();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteReimbursement(Reimbursement item) {
		// TODO project doesn't delete Reimbursements
		return false;
	}
}
