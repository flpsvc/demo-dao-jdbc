package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, birthDate, baseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);				
		
		ps.setString(1, obj.getName());
		ps.setString(2, obj.getEmail());
		ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
		ps.setDouble(4, obj.getBaseSalary());
		ps.setInt(5, obj.getDepartment().getId());
		
		int rowsAffected =  ps.executeUpdate();
		
		if(rowsAffected > 0) {
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				obj.setId(id);
			}
			DB.closeResultSet(rs);
		}
		else {
			throw new DbException("An unknown error has ocurred. No rows affected.");
		}
		
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			DB.closeConnection();
		}
		
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name=?, Email=?, BirthDate=?, BaseSalary=?, DepartmentId=? "
					+ "WHERE Id=?");
					
		
		ps.setString(1, obj.getName());
		ps.setString(2, obj.getEmail());
		ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
		ps.setDouble(4, obj.getBaseSalary());
		ps.setInt(5, obj.getDepartment().getId());
		ps.setInt(6,  obj.getId());
		
		ps.executeUpdate();
		
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeConnection();
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "	
					+ "WHERE seller.Id = ?");
			
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, dep);
				return seller;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(dep);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();			
			Map<Integer, Department> map = new HashMap<>(); 
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId")); //caso n�o exista, retorna nullo para dep
				
				if(dep == null ) { // se for nulo
					dep = instantiateDepartment(rs); // vai instanciar ao inves de criar mais de um objeto department para cada seller
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);			
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartmet(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");

			ps.setInt(1,  department.getId());
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();			
			Map<Integer, Department> map = new HashMap<>(); 
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId")); //caso n�o exista, retorna nullo para dep
				
				if(dep == null ) { // se for nulo
					dep = instantiateDepartment(rs); // vai instanciar ao inves de criar mais de um objeto department para cada seller
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);			
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
}
