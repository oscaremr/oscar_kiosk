<%@page import="org.oscarehr.oscar_apps.check_in.ui.CardReaderAction"%>
<%@page import="net.sf.json.*" %>
<%
	boolean ret = CardReaderAction.savePatientInfo(request);
	
	if (ret) {
%>
<script type="text/javascript">
alert("Update demographic inforamtion successfully!");
location.href="card_reader.jsp";
</script>
<%} else {%>
<script type="text/javascript">
alert("Failed to update demographic information!");
location.href="check_in_fail.jsp";
</script>
<%}%>
