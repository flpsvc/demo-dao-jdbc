package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department obj = new Department(1, "Books");
		
		Seller seller = new Seller(1, "Felipe", "felipe.savacini@hotmail.com", new Date(), 2700.00);
		
		System.out.println(seller);

	}

}