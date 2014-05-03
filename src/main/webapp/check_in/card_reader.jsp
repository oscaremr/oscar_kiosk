<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>
<%@page import="org.oscarehr.oscar_apps.util.ConfigUtils" %>


<%
	String bkClr = ConfigUtils.getProperty("main_page_bk_color");
	if (bkClr == null || bkClr.isEmpty()) {
		bkClr = "#CCFFCC";
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title><%=LocaleUtils.getMessage(request, "CHECK_IN.PATIENT_CHECK_IN")%></title>
	</head>
	<body style="background-color:<%=bkClr%>" onload="document.cardFrm.cardData.focus();">
	<div style="width:700px; height:80px; margin:auto; text-align:center; padding-top:1%;">
		<%=LocaleUtils.getMessage(request, "CHECK_IN.SLIDE_CARD_THROUGH_READER")%>
		<br /><br />
		<form name="cardFrm" method="post" onsubmit="return check();" action="card_reader_action.jsp">
		<%
		// 
		%>
			<input id="cardData" name="cardData" type="password" value="" />
			<input type="submit" value="<%=LocaleUtils.getMessage(request, "OK")%>" />
		</form>
	</div>
	<script type="text/javascript">
		var cardDataField = document.getElementById("cardData");
		cardDataField.focus();
		
		function check() {
			var cardData = document.getElementById("cardData").value;
			if (cardData == null || cardData.length == 0) {
				alert("Please enter phone number.");
				return false;
			}
			return true;
		}
		
	</script>

<%@include file="/check_in/templates/html_bottom.jspf"%>
	