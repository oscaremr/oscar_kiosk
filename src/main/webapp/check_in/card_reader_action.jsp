<%@page import="org.oscarehr.oscar_apps.check_in.ui.CardReaderAction"%>
<%
	boolean result=CardReaderAction.processCardData(request.getParameter("cardData"));

	if (result) response.sendRedirect("check_in_ok.jsp");
	else  response.sendRedirect("check_in_fail.jsp");
%>