<%@ page language="java"%>
<%@ page import="net.sf.json.*" %>
<%@page import="org.oscarehr.oscar_apps.util.LocaleUtils"%>
<!DOCTYPE html>
<%
JSONObject ret = (JSONObject)request.getAttribute("body");
JSONObject ptInfo = (JSONObject)ret.get("patientInfo");

String ctx = request.getContextPath();
%>

<html>
<head>
<title>Demographic Information</title>
<link rel="stylesheet" href="<%=ctx %>/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=ctx %>/css/ace.min.css">
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" rel="stylesheet">
<link type="image/x-icon" href="<%=ctx %>/images/Oscar.ico" rel="shortcut icon">

<style type="text/css">
dt,dd{padding:1px 0px; }
.dtPadding{padding: 5px 4px;}
body{font-size:28px;}
dl.dl-horizontal dt{width:232px;}
dl.dl-horizontal dd{margin-left:252px;}
</style>
</head>
<body onload="selPorvice('<%if(ptInfo.getString("province")!=null){out.print(ptInfo.getString("province"));}%>')">
	<form action="update_patient_action.jsp" onsubmit="return check();" name="updatePt" id="updatePt" method="post">
		<div class="widget-box" style="padding-top:1%; width:95%; margin-left: auto; margin-right: auto;">
			<div class="widget-header widget-header-flat" style="padding-top: 5px; padding-bottom: 10px;">
				<h4 class="smaller" style="font-size:40px; line-height:45px; color:green;"><%=LocaleUtils.getMessage(request, "CHECK_IN.VERIFY_INFO")%></h4>
			</div>
		
			<div class="widget-body">
				<div class="widget-main" >
					<input type="hidden" name="demoNo" value="<%if(ptInfo.getString("demoNo")!=null){out.print(ptInfo.getString("demoNo"));}%>">
					<input type="hidden" name="first_name" value="<%if(ptInfo.getString("first_name")!=null){out.print(ptInfo.getString("first_name"));}%>">
					<input type="hidden" name="last_name" value="<%if(ptInfo.getString("last_name")!=null){out.print(ptInfo.getString("last_name"));}%>">
					<input type="hidden" name="hin" value="<%if(ptInfo.getString("hin")!=null){out.print(ptInfo.getString("hin"));}%>">
					<input type="hidden" name="ver" value="<%if(ptInfo.getString("ver")!=null){out.print(ptInfo.getString("ver"));}%>">
					<input type="hidden" name="hcType" value="<%if(ptInfo.getString("hcType")!=null){out.print(ptInfo.getString("ver"));}%>">
					<input type="hidden" name="dob" value="<%if(ptInfo.getString("dob")!=null){out.print(ptInfo.getString("dob"));}%>">
					<input type="hidden" name="sex" value="<%if(ptInfo.getString("sex")!=null){out.print(ptInfo.getString("sex"));}%>">
					
					<input type="hidden" name="effYear" value="<%if(ptInfo.getString("effYear")!=null){out.print(ptInfo.getString("effYear"));}%>">
					<input type="hidden" name="effMonth" value="<%if(ptInfo.getString("effMonth")!=null){out.print(ptInfo.getString("effMonth"));}%>">
					<input type="hidden" name="effDay" value="<%if(ptInfo.getString("effDay")!=null){out.print(ptInfo.getString("effDay"));}%>">
					<input type="hidden" name="expiryYear" value="<%if(ptInfo.getString("expiryYear")!=null){out.print(ptInfo.getString("expiryYear"));}%>">
					<input type="hidden" name="expiryMonth" value="<%if(ptInfo.getString("expiryMonth")!=null){out.print(ptInfo.getString("expiryMonth"));}%>">

					<dl id="ptInfo" class="dl-horizontal">
						<dt class="topBotPadding">Appointment:</dt>
						<%if (ptInfo.getString("aptTime") != null) {%>
						<dd class="topBotPadding"><%=ptInfo.getString("aptTime")%></dd>
						<%} else { %>
						<dd class="topBotPadding"></dd>
						<%} %>
						
						<dt class="topBotPadding">Name:</dt>
						<dd class="topBotPadding"><%if (ptInfo.getString("first_name") != null) {out.print(ptInfo.getString("first_name"));}%>&nbsp;<% if (ptInfo.getString("last_name") != null) {out.print(ptInfo.getString("last_name"));} %></dd>
						
						<dt class="topBotPadding">Gender:</dt>
						<dd class="topBotPadding"><%if(ptInfo.getString("sex")!=null){out.print(ptInfo.getString("sex"));}%></dd>
						
						<dt class="topBotPadding">Date of Birth:</dt>
						<dd class="topBotPadding"><%if(ptInfo.getString("dob")!=null){out.print(ptInfo.getString("dob"));}%></dd>
						
						<dt class="topBotPadding">Health Card #:</dt>
						<dd class="topBotPadding"><%if(ptInfo.getString("hin")!=null){out.print(ptInfo.getString("hin"));}%></dd>
						
						<dt class="topBotPadding">HC VER</dt>
						<dd class="topBotPadding"><%if (ptInfo.getString("ver")!=null){out.print(ptInfo.getString("ver"));} %></dd>
						
						<dt class="topBotPadding">HC Type</dt>
						<dd class="topBotPadding"><%if(ptInfo.getString("hcType")!=null){out.print(ptInfo.getString("hcType"));} %></dd>
						
						<dt class="dtPadding">Street:</dt>
						<dd><input style="font-size:28px;" type="text" name="street" value="<%if(ptInfo.getString("street")!=null){out.print(ptInfo.getString("street"));}%>" size="60"/></dd>
						
						<dt class="dtPadding">City:</dt>
						<dd><input style="font-size:28px;" type="text" name="city" value="<%if(ptInfo.getString("city")!=null){out.print(ptInfo.getString("city"));}%>" /></dd>
						
						<dt class="dtPadding">Province:</dt>
						<dd>
						<select id="province" name="province" style="height: 50px;">
							<option value="OT">Other</option>
							<option value="AB">AB-Alberta</option>
							<option value="BC">BC-British Columbia</option>
							<option value="MB">MB-Manitoba</option>
							<option value="NB">NB-New Brunswick</option>
							<option value="NL">NL-Newfoundland & Labrador</option>
							<option value="NT">NT-Northwest Territory</option>
							<option value="NS">NS-Nova Scotia</option>
							<option value="NU">NU-Nunavut</option>
							<option value="ON">ON-Ontario</option>
							<option value="PE">PE-Prince Edward Island</option>
							<option value="QC">QC-Quebec</option>
							<option value="SK">SK-Saskatchewan</option>
							<option value="YT">YT-Yukon</option>
							<option value="US">US resident</option>
							<option value="US-AK">US-AK-Alaska</option>
							<option value="US-AL">US-AL-Alabama</option>
							<option value="US-AR">US-AR-Arkansas</option>
							<option value="US-AZ">US-AZ-Arizona</option>
							<option value="US-CA">US-CA-California</option>
							<option value="US-CO">US-CO-Colorado</option>
							<option value="US-CT">US-CT-Connecticut</option>
							<option value="US-CZ">US-CZ-Canal Zone</option>
							<option value="US-DC">US-DC-District Of Columbia</option>
							<option value="US-DE">US-DE-Delaware</option>
							<option value="US-FL">US-FL-Florida</option>
							<option value="US-GA">US-GA-Georgia</option>
							<option value="US-GU">US-GU-Guam</option>
							<option value="US-HI">US-HI-Hawaii</option>
							<option value="US-IA">US-IA-Iowa</option>
							<option value="US-ID">US-ID-Idaho</option>
							<option value="US-IL">US-IL-Illinois</option>
							<option value="US-IN">US-IN-Indiana</option>
							<option value="US-KS">US-KS-Kansas</option>
							<option value="US-KY">US-KY-Kentucky</option>
							<option value="US-LA">US-LA-Louisiana</option>
							<option value="US-MA">US-MA-Massachusetts</option>
							<option value="US-MD">US-MD-Maryland</option>
							<option value="US-ME">US-ME-Maine</option>
							<option value="US-MI">US-MI-Michigan</option>
							<option value="US-MN">US-MN-Minnesota</option>
							<option value="US-MO">US-MO-Missouri</option>
							<option value="US-MS">US-MS-Mississippi</option>
							<option value="US-MT">US-MT-Montana</option>
							<option value="US-NC">US-NC-North Carolina</option>
							<option value="US-ND">US-ND-North Dakota</option>
							<option value="US-NE">US-NE-Nebraska</option>
							<option value="US-NH">US-NH-New Hampshire</option>
							<option value="US-NJ">US-NJ-New Jersey</option>
							<option value="US-NM">US-NM-New Mexico</option>
							<option value="US-NU">US-NU-Nunavut</option>
							<option value="US-NV">US-NV-Nevada</option>
							<option value="US-NY">US-NY-New York</option>
							<option value="US-OH">US-OH-Ohio</option>
							<option value="US-OK">US-OK-Oklahoma</option>
							<option value="US-OR">US-OR-Oregon</option>
							<option value="US-PA">US-PA-Pennsylvania</option>
							<option value="US-PR">US-PR-Puerto Rico</option>
							<option value="US-RI">US-RI-Rhode Island</option>
							<option value="US-SC">US-SC-South Carolina</option>
							<option value="US-SD">US-SD-South Dakota</option>
							<option value="US-TN">US-TN-Tennessee</option>
							<option value="US-TX">US-TX-Texas</option>
							<option value="US-UT">US-UT-Utah</option>
							<option value="US-VA">US-VA-Virginia</option>
							<option value="US-VI">US-VI-Virgin Islands</option>
							<option value="US-VT">US-VT-Vermont</option>
							<option value="US-WA">US-WA-Washington</option>
							<option value="US-WI">US-WI-Wisconsin</option>
							<option value="US-WV">US-WV-West Virginia</option>
							<option value="US-WY">US-WY-Wyoming</option>
						</select>
						</dd>
						
						<dt class="dtPadding">Postal Code:</dt>
						<dd><input style="font-size:28px;" type="text" name="postal" value="<%if(ptInfo.getString("postal")!=null){out.print(ptInfo.getString("postal"));}%>"></dd>
						
						<dt class="dtPadding">Home Phone:</dt>
						<dd><input style="font-size:28px;" type="text" name="phone" value="<%if(ptInfo.getString("phone")!=null){out.print(ptInfo.getString("phone"));}%>" onblur="formatPhoneNum()"></dd>
						
						<dt class="dtPadding">Cell Phone:</dt>
						<dd><input style="font-size:28px;" type="text" name="cell" value="<%if(ptInfo.getString("cell")!=null){out.print(ptInfo.getString("cell"));}%>" onblur="formatPhoneNum()"></dd>
						<br/><br/>
						<dt><dt>
						<dd>
						<input type="submit" class="btn btn-sm btn-primary" name="save" value="save" style="margin-right:20px;"/> 
						<input type="button" class="btn btn-sm btn-primary" value="back" onclick="backCardReader();" />
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</form>
</body>

<script type="text/javascript">

function selPorvice(province) {
	if (!province || province.length == 0) {
		document.getElementById("province").value = "ON";
	} else {
		document.getElementById("province").value = province;
	}
}

function check() {
	if (document.getElementsByName("phone")[0].value.trim().length == 0 
			&& document.getElementsByName("cell")[0].value.trim().length == 0) {
		alert("Please enter phone number.");
		return false;
	}
	return true;
}

function backCardReader() {
	location.href="<%=ctx%>/check_in/card_reader.jsp";
}

function formatPhoneNum() {
	var phone = document.getElementsByName("phone")[0];
	if (phone.value.length == 10) {
		phone.value = phone.value.substring(0,3) + "-" + phone.value.substring(3,6) + "-" + phone.value.substring(6);
	}
	if (phone.value.length == 11 && phone.value.charAt(3) == '-') {
		phone.value = phone.value.substring(0,3) + "-" + phone.value.substring(4,7) + "-" + phone.value.substring(7);
	}
	
	var phone2 = document.getElementsByName("cell")[0];
	if (phone2.value.length == 10) {
		phone2.value = phone2.value.substring(0,3) + "-" + phone2.value.substring(3,6) + "-" + phone2.value.substring(6);
	}
	if (phone2.value.length == 11 && phone2.value.charAt(3) == '-') {
		phone2.value = phone2.value.substring(0,3) + "-" + phone2.value.substring(4,7) + "-" + phone2.value.substring(7);
	}
} 

</script>
</html>