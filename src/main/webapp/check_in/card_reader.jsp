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
		<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath() %>/images/Oscar.ico" /> 
		<title><%=LocaleUtils.getMessage(request, "CHECK_IN.PATIENT_CHECK_IN")%></title>
	</head>
	<body style="background-color:<%=bkClr%>">
	
	<table width="100%" height="30%ALIGN=center" border="0" bgcolor="white">
		<tbody><tr height="70%">
			<td align="center">
				<img src="<%=request.getContextPath() %>/images/logo.png">
			</td>
		</tr>
		<tr height="20%">
			<td align="center">
				<form action="card_reader_action.jsp" method="post">
               				 <input type="password" value="" name="cardData" id="cardData">
                			<br>
               			 	<input type="submit" value="<%=LocaleUtils.getMessage(request, "OK")%>">
       				 </form>
			</td>
		</tr>
		<tr height="20%">
                        <td align="center">
				<font size="12pix" color="green">
                                <%=LocaleUtils.getMessage(request, "CHECK_IN.SLIDE_CARD_THROUGH_READER")%>
                        </font></td>
                </tr>
	</tbody></table>
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
	