package com.javaEE.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaEE.model.Customer;
import com.javaEE.service.CustomerService;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

	private static final long serialVersionUID = -3457385489599657583L;

	private CustomerService customerService;

	@Override
	public void init() throws ServletException {
		customerService=new CustomerService();
		System.out.println("123");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("doGet");
		List<Customer> customerList=customerService.getCustomerList();
		req.setAttribute("customerList", customerList);
		req.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(req, resp);
		return;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("doPost");
		List<Customer> customerList=customerService.getCustomerList();
		req.setAttribute("customerList", customerList);
		req.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(req, resp);
	}
}
