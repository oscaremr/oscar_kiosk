This is the oscar apps project. 

--------
Overview
--------
This project is meant to hold all apps that we might create which are on
the periphery of oscar.The first app is going to be the client check in
kiosks using health card swipers. These are applications which connect to
oscar but are not part of the oscar system themselves. In general these
are small applications which are independent of each other but since they're
small and may have a lot of common infrastructure we can put them all here. 

This system requires an OSCAR server already running and configured properly.


------------
Requirements
------------
The following aren't requirements per se but what was used during the
development of this version.

	jdk1.6.0_18
	tomcat 6.0.18
	maven2 2.2.1


---------------------
System Administration
---------------------
When you start tomcat you may pass it a java system property (a.k.a. -D
property) oscar_apps_properties=<property_file> which overrides any values
found in the config.properties file. You can also pass in 
log4j.override.configuration=<path to log4j.xml> which will configure
those settings on top of the ones in the distributed log4j.xml file.
This is to allow you to customise your configuration with out touching things
with in the war archive / directory (which results is easier upgrades, or
refreshes of the code base). 

Once the system is running, you should be able to go to 
	http://127.0.0.1:8085/oscar_apps 
	https://127.0.0.1:8086/oscar_apps
to see the index page which should link to apps available on the system.

Audit logs are produced in tomcat/logs/oscar_apps_action.*.log. Those files
should be backed up and saved and treated like any other medical records.
They are audit trails for actions taken on the server.

VmStat logs are produced in tomcat/logs/oscar_apps_vmstat.*.log. These
logs can be used to monitor and performance tune the jvm instance.