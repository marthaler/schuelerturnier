#!/bin/bash
killall -9 java

rm -R /usr/local/server/

mkdir /usr/local/server/
cd /usr/local/server

# wget http://mirror.atlanticmetro.net/apache/tomcat/tomcat-7/v7.0.47/bin/apache-tomcat-7.0.47.tar.gz
cp /root/apache-tomcat-7.0.47.tar.gz /usr/local/server/apache-tomcat-7.0.47.tar.gz

tar xvzf apache-tomcat-7.0.47.tar.gz
rm apache-tomcat-7.0.47.tar.gz
mv apache-tomcat-7.0.47 tomcat

#  wget --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com" "http://download.oracle.com/otn-pub/java/jdk/7/jdk-7-linux-x64.tar.gz"

 cp /root/jdk-7-linux-x64.tar.gz /usr/local/server/jdk-7-linux-x64.tar.gz

  tar xvzf jdk-7-linux-x64.tar.gz
  rm jdk-7-linux-x64.tar.gz
  mv jdk-7-linux-x64 jdk7

export JAVA_HOME=/usr/local/server/jdk1.7.0
export CATALINA_HOME=/usr/local/server/tomcat

cd /usr/local/server/tomcat/webapps
rm -R *

wget https://www.dropbox.com/s/yspycr51pl44ni2/ROOT.war

cd /usr/local/server/

cd /usr/local/server/tomcat/conf
rm server.xml

wget https://www.dropbox.com/s/088hmqranh2kq6f/keystore
wget https://www.dropbox.com/s/6lqh68d3jkc37it/server.xml

/usr/local/server/tomcat/bin/startup.sh

ls -la
