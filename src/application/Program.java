package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		Integer a = in.nextInt();
		
		SellerDao sellerDao = DaoFactory.createSellerDao();		
		
		System.out.println("=== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(a);
		System.out.println(seller);
		
		Department dep = new Department(a, null);
		List<Seller> list = sellerDao.findByDepartmet(dep);
		
		
		System.out.println("=== TEST 2: seller findByDepartment ====");
		for(Seller s : list) {
			System.out.println(s);
		}

	}

}
