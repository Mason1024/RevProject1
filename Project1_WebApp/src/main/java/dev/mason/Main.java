package dev.mason;

import dev.mason.services.RService;
import dev.mason.services.ReimbursementService;

public class Main {
	public static void main(String[] args) {
		ReimbursementService rs = RService.getService();
		System.out.println(rs.login("joe", "pizza"));
	}
}
