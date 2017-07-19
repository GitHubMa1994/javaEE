package com.javaEE.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.javaEE.model.Customer;
import com.javaEE.service.CustomerService;

/**
 * CustomerService单元测试
 * @author mwb
 *
 */
public class CustomerServiceTest {
	private final CustomerService customerService;

	public CustomerServiceTest(){
		customerService=new CustomerService();
	}
	
	@Before
	public void init(){
		//TODO初始化数据库
	}
	
	@Test
	public void getCustomerListTest() throws Exception{
		long id=1;
		Customer customer=customerService.getCustomer(id);
		Assert.assertNotNull(customer);
	}
	
	@Test
	public void createCustomerTest() throws Exception{
		Map<String,Object> feilMap=new HashMap<String,Object>();
		feilMap.put("name", "customer100");
		feilMap.put("contact", "John");
		feilMap.put("telephone", "142384893");
		boolean result=customerService.createCustomer(feilMap);
		Assert.assertTrue(result);
	}
	
	@Test
	public void updateCustomerTest() throws Exception{
		long id=1;
		Map<String,Object> feilMap=new HashMap<String,Object>();
		feilMap.put("contact", "Eric");
		boolean result=customerService.updateCustomer(id, feilMap);
		Assert.assertTrue(result);
	}
	
	@Test
	public void deleteCustomerTest() throws Exception{
		long id=1;
		boolean result=customerService.deleteCustomer(id);
		Assert.assertTrue(result);
	}
	
}
