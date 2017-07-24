package com.javaEE.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaEE.helper.DatabaseHelper;
import com.javaEE.model.Customer;

public class CustomerService {
	private static final Logger LOGGER=LoggerFactory.getLogger(CustomerService.class);
	
	/**
	 * 获取客户列表
	 */
	public List<Customer> getCustomerList(){
		String sql="SELECT * FROM customer";
		return DatabaseHelper.queryEntityList(Customer.class, sql);
	}
	
	/**
	 * 获取客户
	 */
	public Customer getCustomer(long id){
		String sql="SELECT * FROM customer WHERE id=?";
		return DatabaseHelper.queryEntity(Customer.class, sql, id);
	}
	
	/**
	 * 创建客户
	 */
	public boolean createCustomer(Map<String,Object> feilMap){
		return DatabaseHelper.insertEntity(Customer.class,feilMap);
	}
	
	/**
	 * 更新客户
	 */
	public boolean updateCustomer(long id,Map<String,Object> feilMap){
		return DatabaseHelper.updateEntity(Customer.class, id, feilMap);
	}
	
	/**
	 * 删除客户
	 */
	public boolean deleteCustomer(long id){
		return DatabaseHelper.deleteEntity(Customer.class, id);
	}
}
