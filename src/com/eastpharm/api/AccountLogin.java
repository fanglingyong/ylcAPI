package com.eastpharm.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastpharm.ylc.DBConnect;
import com.eastpharm.ylc.ResultDataModel;

/**
 * Servlet implementation class AccountLogin
 */
@WebServlet("/AccountLogin")
public class AccountLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    response.setHeader("Cache-Control", "no-cache");
	    
	    String un = request.getParameter("username");
	    String pswd = request.getParameter("password");
	    if (un == null || pswd == null || un.isEmpty() || pswd.isEmpty()) {
	    	ResultDataModel rdm = new ResultDataModel();
			response.getWriter().print(rdm.getParameterErrorMsg());
		}else {
			DBConnect dbs = new DBConnect();
			response.getWriter().print(dbs.getLoginMenthod(un, pswd));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}

}
