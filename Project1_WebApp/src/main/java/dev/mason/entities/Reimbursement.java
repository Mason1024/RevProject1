package dev.mason.entities;

public class Reimbursement {

	private int r_id;
	private String submiter;
	private String approver;
	private String desc;
	private double price;
	private long timestamp;
	private String comment;
	private int state;
		
	public Reimbursement() {
		super();
	}
	public Reimbursement(int r_id, String submiter, String approver, String desc, double price, long timestamp, String comment,
			int state) {
		super();
		this.r_id = r_id;
		this.submiter = submiter;
		this.approver = approver;
		this.desc = desc;
		this.price = price;
		this.timestamp = timestamp;
		this.comment = comment;
		this.state = state;
	}

	public int getR_id() {
		return r_id;
	}
	public void setR_id(int r_id) {
		this.r_id = r_id;
	}
	public String getSubmiter() {
		return submiter;
	}
	public void setSubmiter(String submiter) {
		this.submiter = submiter;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approver == null) ? 0 : approver.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + r_id;
		result = prime * result + state;
		result = prime * result + ((submiter == null) ? 0 : submiter.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (approver == null) {
			if (other.approver != null)
				return false;
		} else if (!approver.equals(other.approver))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (r_id != other.r_id)
			return false;
		if (state != other.state)
			return false;
		if (submiter == null) {
			if (other.submiter != null)
				return false;
		} else if (!submiter.equals(other.submiter))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reimbursement " + r_id  + ", state=" + state + 
				"\n\tsubmitted by user=" + submiter + ", approved by user=" + approver + 
				"\n\tdesc=" + desc +
				"\n\tprice=" + price + ", timestamp=" + timestamp + 
				"\n\tcomment=" + comment;
	}
}
