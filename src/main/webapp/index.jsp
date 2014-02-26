<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>
<%@page import="org.oscarehr.oscar_apps.util.MiscUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title><%=LocaleUtils.getMessage(request, "AVAILABLE_APPLICATIONS")%></title>
	</head>
	<body>
		<div style="text-align:center">
			<h3><%=LocaleUtils.getMessage(request, "AVAILABLE_APPLICATIONS")%></h3>
		</div>
	
		<ul>
			<li><a href="check_in"><%=LocaleUtils.getMessage(request, "CHECK_IN.PATIENT_CHECK_IN")%></a></li>
		</ul>
		
		<hr style="color:silver" />
		<div style="text-align:right;font-size:9px;color:grey">
			<%=LocaleUtils.getMessage(request, "BUILD_TIME") %> : <%=MiscUtils.getBuildDateTime()%>
		</div>
	</body>
</html>
		