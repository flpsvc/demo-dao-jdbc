package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		//Integer a = in.nextInt();
		
		SellerDao sellerDao = DaoFactory.createSellerDao();		
		
		System.out.println("=== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		Department dep = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartmet(dep);
		
		
		System.out.println("=== TEST 2: seller findByDepartment ====");
		for(Seller s : list) {
			System.out.println(s);
		}
		
		System.out.println("=== TEST 3: seller findByAll ====");
		list = sellerDao.findAll();
		for(Seller s : list) {
			System.out.println(s);
		}
		
		System.out.println("=== TEST 4: seller insertSeller ====");
		Seller seller2 = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, dep);
		sellerDao.insert(seller2);
		System.out.println("Inserted ID: "+seller2.getId());
	}

}
