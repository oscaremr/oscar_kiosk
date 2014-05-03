<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>

<%@include file="/check_in/templates/html_top.jspf"%>
	
	<%
	int flag = 404;
	try {
		flag = Integer.parseInt(request.getParameter("flag"));
	} catch (Exception e) {	}
	
	%>

	<%if (flag == 404) { %>
	<div style="color:blue;text-align:center;font-weight:bold">
	<%=LocaleUtils.getMessage(request, "CHECK_IN.NEW_PATIENT_CHECK_IN")%>
	</div>
	<%} else if (flag == 201){ %>
	<div style="color:#6495ED;text-align:center;font-weight:bold">
	<%=LocaleUtils.getMessage(request, "CHECK_IN.CHECK_IN_NO_APPOINTMENT")%>
	</div>
	<%} else {%>
	<div style="color:#009900;text-align:center;font-weight:bold">
	<%=LocaleUtils.getMessage(request, "CHECK_IN.CHECK_IN_OK")%>
	</div>
	<%} %>

	
	<script type="text/JavaScript">
		setTimeout("location.href = 'card_reader.jsp';", 10000);
	</script>
	

<%@include file="/check_in/templates/html_bottom.jspf"%>
	