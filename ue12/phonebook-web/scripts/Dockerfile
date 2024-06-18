FROM tomcat:10

RUN rm -rf /usr/local/tomcat/webapps \
 && mv /usr/local/tomcat/webapps.dist /usr/local/tomcat/webapps

COPY tomcat-config/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
COPY tomcat-config/context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY tomcat-config/mysql-connector-j-*.jar /usr/local/tomcat/lib

CMD ["catalina.sh", "run"]