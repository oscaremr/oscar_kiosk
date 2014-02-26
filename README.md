oscar_apps
==========
This project is a standalone Web Appliance for OSCAR. This example will illustrate how to build a kiosk for health card scanner for patient to check in automatically. This facility is designed for Ontario OHIP cards, BC MSP cards and McMaster Student ID, although it is easily extended to read any magnetic stripe card. 

Brief summary of the installation :

    maven build will provide war file
    oscar_apps has a catalina_base and a set_env.sh file with some sane default settings that can be used, note the 2 override configuration parameters in set_env.sh that can be set, one for config.properties and the other for log4j.
    in the config.properties (or your override of it) you need to setup the checkIn.* settings, i.e. service name, upload url and key pair info.

 
Document Version History

    v1.0 – initial public release to oscarmanual.org
    v1.1 - updates for git.code.sf.net and key pair generation - Sept 4,2013 


Installation Instructions

1. install the latest maven (or 2.0.10)
2. check out oscar_apps

$ git clone git://git.code.sf.net/p/oscarmcmaster/oscar_apps oscarmcmaster-oscar_apps

3. change to the oscar_apps directory, build compile and move to the target directory

$ cd oscarmcmaster-oscar_apps
$ mvn clean package
$ cd target

5. copy the war to tomcat webapps or CATALINA_BASE; 

$ sudo cp oscar_apps-0.0-SNAPSHOT.war $CATALINA_BASE/oscar_apps.war

6. Sign into your Oscar server, go to Admin and Click on Keypair Generator

If there is nothing in "Oscar Public Key", you need to generate it.  Click on "Create New Key". Type "oscar" (without quotes) in Your Service Name. This is case-sensitive, so don't put "Oscar" or "OSCAR". The lab type doesn't matter. You should see 'Key pair created successfully' in the top banner. 

To create a new key, click on Create New Key and enter: 

Your service name:	oscarkiosk

Lab type:	OSCAR_TO_OSCAR_HL7_V2

Click Create Key Pair to create keys.

You should see 'Key pair created successfully' in the top banner.
 

7. Edit the configuration file to match the Oscar 10.12+ service URL as well as the keypair and service name that you chose.  (For easy updates you can use an overriding properties file that is external to the webapp instead and invoke it when starting tomcat with  "-Doscar_apps_config=override.properties".)

$ sudo vi $CATALINA_BASE/oscar_apps/WEB-INF/classes/config.properties

checkIn.eDataServiceName=oscarkiosk

checkIn.eDataUrl=http://localhost:8080/oscar_mcmaster/lab/newLabUpload.do

checkIn.eDataServiceKeyBase64=<the base 64 private key you cut and pasted from the Keypair Manger in Oscar>

checkIn.eDataOscarKeyBase64=<the base 64 public key you cut and pasted from the Keypair Manger in Oscar>

 

8. Restart Tomcat

$ sudo /etc/init.d/tomcat6 restart  or  sudo service tomcat6 restart

 

9. Navigate to your test server tomcat directory eg. http://localhost:8080/oscar_apps/check_in/card_reader.jsp to test

If you don't have a health card and mag stripe reader handy, you can manually cut and paste the data of a demo patient into the input field

Assuming that Dr McLuhan has an appointment with you use the following

%b6100542222222222^MCLUHAN/MARSHALL          ^0901799119110121  MCLUM90061201?0

 

The documentation for the Track 1 data format can be found at:

http://www.health.gov.on.ca/english/providers/pub/ohip/tech_specific/pdf/tech_specific.pdf

 
Detail documentation for developers
 (Check here for latest documentation.)

 

This document provides basic information to developers on how to work with his project and code base. It assumes the developers are already fluent with the technologies such as java/tomcat. We will only cover configuration and usages which are specific to this project and not commonly known development standards.

 

Requirements

The following aren't requirements per se but what was used during the development of this version.

    jdk1.6.0_18

    tomcat 6.0.18

    maven2 2.2.1

 

A catalina_base is provided with the source code. This contains a some-what-sane tomcat configuration which can be used as the basis for development or deployment. A maven plugin was added to the package phase to deploy the contents of the war file to the catalina_base, so when you “mvn package”, you will compile, and deploy to the catalina_base.

 

Code Style and Format

If you're using an IDE to work with this code base, you're going to want to "ignore missing serialVersionUID". We do not support compatibility between compiled class files except with in the exact same build instance. Beyond the above change, there should generally be no errors or warnings of any types in the source code, please keep it that way. Maven checkstyle is used to try and enforce some semblance of code-style-format.

 

Separate Apps

The intention is that this server/context will hold multiple small apps. We're trying to save resources and be lazy by putting them all in one webapp instead of having multiple webapps. To break the code up, the following structure should be used :

      webapps/<APP_NAME> : for jsp/web files
      org.oscarehr.oscar_apps.<APP_NAME> : for class files.

 

Files should be put into webapps/ and org.oscarehr.oscar_apps directly only if they are generic common utilities. Any css / js include files etc should be considered specific to your app as some one else might want to style their app differently etc.

 

Translations and I18N

All UI visible strings should be places in oscar_apps_strings_en.properties. There is a LocaleUtils.java class to help retrieve translated strings. All strings should be retrieved through this class.

 

Logging

There are 2 types of logging. Code/Debug logging and User Action / Audit logging.

 For code / debug logging use Log4J. There is a utility method MiscUtils.getLogger() which will retrieve a log4j logger with a category set to the class that calls the getLogger() method. i.e.

private static Logger logger=MiscUtils.getLogger();

 as a class field is generally all you need to do to get a logger with category set to the enclosing class.

 For user action / audit logging there's a class ActionLog.java. It is configured through log4j to log to a rolling set of log files in tomcat/logs called oscar_apps_action.*.log. Note that it's important not to delete these log files unless you really want to delete your audit logs – which is very very rare and in some cases illegal depending on your local laws with regards to retention of medical records. These audit log files should be backed up and treated like any other medical records.

 
Customization for health card scanner application:

This document provides basic information on customization considerations. The issue here is that the card swipe screen provided is fairly generic because of the variety of kiosks or check-in methods potentially available.

 

If you look at the existing screen it's fairly terse. Currently the only way to customize the screen is to edit the existing jsp file. Here are some things to consider :

    Branding. An office may wish to put their office name or logo or doctors name as a title or something on the screen so that patients are more easily aware of the kiosk.

    The font size and alignment. If the kiosk as a small screen, the font size may need to be increased.

    Auto focus. Right now, auto focus is set to the input field upon page load. The assumption is that the card swiper will enter data here like a keyboard would. In the default screen there is an ok/submit button, this means focus can be lost from the input field. If focus is lost on the input field, it means the next card swipe will not function as expected. An installation may consider forcing focus to stay on the input field or occasionally returning focus to the input field.

    Auto-submit. The default screen includes an ok/submit button. If there is no method of input, an installation may consider trying to make it auto-submit every time a card is swiped. For reference, I believe in at least BC and Ontario, track 1 of the cards is exactly 79 characters (no more, no less).

 

Right now there is no facility available for manual type-in entry of a patients data for self-check-in however the additional code to do so would be rather trivial if required.

