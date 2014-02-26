<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>

<%@include file="/check_in/templates/html_top.jspf"%>

	<div style="color:#990000;text-align:center;font-weight:bold">
		<%=LocaleUtils.getMessage(request, "CHECK_IN.CHECK_IN_FAIL")%>
	</div>
	
	<script type="text/JavaScript">
		setTimeout("location.href = 'card_reader.jsp';", 10000);
	</script>
	

<%@include file="/check_in/templates/html_bottom.jspf"%>
	