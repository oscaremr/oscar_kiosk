<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>

<%@include file="/check_in/templates/html_top.jspf"%>

	<%
	int reason = 500;
	try {
		reason = Integer.parseInt(request.getParameter("reason"));
	} catch (Exception e) {}
	
	%>
	<div style="color:green;text-align:center;font-weight:bold">
		<font size="12pix">
		<%
		if (reason == 500) { 
			out.print(LocaleUtils.getMessage(request, "CHECK_IN.CHECK_IN_FAIL"));
		} else {
			out.print(LocaleUtils.getMessage(request, "CHECK_IN.INVALID_CARD"));
		}
		%>
		</font>
	</div>
	
	
	<script type="text/JavaScript">
		setTimeout("location.href = 'card_reader.jsp';", 10000);
	</script>
	

<%@include file="/check_in/templates/html_bottom.jspf"%>
	