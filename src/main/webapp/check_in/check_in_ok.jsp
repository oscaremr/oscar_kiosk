<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>

<%@include file="/check_in/templates/html_top.jspf"%>
	
	<%
	int flag = 404;
	try {
		flag = Integer.parseInt(request.getParameter("flag"));
	} catch (Exception e) {	}
	
	%>
	<div class="centerMiddle" style="color:green;text-align:center;font-weight:bold">
	<%if (flag == 404) { %>
	
	<font size="12pix">
	<%=LocaleUtils.getMessage(request, "CHECK_IN.NEW_PATIENT_CHECK_IN")%>
	</font>
	<%} else if (flag == 201){ %>
	<font size="12pix">
	<%=LocaleUtils.getMessage(request, "CHECK_IN.CHECK_IN_NO_APPOINTMENT")%>
	</font>
	<%} else {%>
	<font size="12pix">
	<%=LocaleUtils.getMessage(request, "CHECK_IN.CHECK_IN_OK")%>
	</font>
	<%} %>
	</div>
	
	<script type="text/JavaScript">
		setTimeout("location.href = 'card_reader.jsp';", 10000);
	</script>
	

<%@include file="/check_in/templates/html_bottom.jspf"%>
	