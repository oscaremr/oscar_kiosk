<%@page import="org.oscarehr.oscar_apps.check_in.ui.CardReaderAction"%>
<%@page import="net.sf.json.*" %>
<%
	JSONObject ret = CardReaderAction.processCardData(request.getParameter("cardData"));
	
	int statusCode = 500;
	do {
		try {
			statusCode = ret.getInt("statusCode");
			if (statusCode != 200) {
				break;
			}
			JSONObject body = (JSONObject)ret.get("body");
			int flag = body.getInt("flag");
			
			if (flag == 500) {
				break;
			}
			
			if (flag == 404) { // a new patient is coming
				response.sendRedirect("check_in_ok.jsp?flag=404");
			} else if (flag == 201) { // existing patient but no appointments recently
				response.sendRedirect("check_in_ok.jsp?flag=201");
			} else if (flag == 200) { // existing patient and has appoitments recently
				request.setAttribute("body", body);
				request.getRequestDispatcher("patient_info.jsp").forward(request, response);
			}
			return;
		} catch (Exception e) {	}
	} while(false);
	
	response.sendRedirect("check_in_fail.jsp?reason=" + statusCode);
	
	
//	if (result) response.sendRedirect("check_in_ok.jsp");
//	else  response.sendRedirect("check_in_fail.jsp");


%>