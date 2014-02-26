<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>

<%@include file="/check_in/templates/html_top.jspf"%>

	<%=LocaleUtils.getMessage(request, "CHECK_IN.SLIDE_CARD_THROUGH_READER")%>
	<br />
	<form method="post" action="card_reader_action.jsp">
		<input id="cardData" name="cardData" type="password" value="" />
		<br />
		<input type="submit" value="<%=LocaleUtils.getMessage(request, "OK")%>" />
	</form>
	
	<script type="text/javascript">
		var cardDataField = document.getElementById("cardData");
		cardDataField.focus();
	</script>

<%@include file="/check_in/templates/html_bottom.jspf"%>
	