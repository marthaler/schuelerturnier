#!/bin/bash
ps -ef | grep "[M]ary" | awk '{print $2}' | xargs kill -9
ps -ef | grep "[C]lassLoaderLogManager" | awk '{print $2}' | xargs kill -9

rm -R /usr/local/server/

mkdir /usr/local/server/
cd /usr/local/server

#### TOMCAT
if test "$1" = "remote"
then
cd /root/
rm apache-tomcat-7.0.53.tar.gz
# wget http://mirror.atlanticmetro.net/apache/tomcat/tomcat-7/v7.0.52/bin/apache-tomcat-7.0.52.tar.gz
wget http://schuelerturnier-scworb.ch/schuetu-software/apache-tomcat-7.0.53.tar.gz
fi

cd /usr/local/server
cp /root/apache-tomcat-7.0.53.tar.gz /usr/local/server/apache-tomcat-7.0.53.tar.gz

tar xvzf apache-tomcat-7.0.53.tar.gz
rm apache-tomcat-7.0.53.tar.gz
mv apache-tomcat-7.0.53 tomcat

### JAVA
if test "$1" = "remote"
then
cd /root/
rm jdk-7-linux-x64.tar.*
wget http://schuelerturnier-scworb.ch/schuetu-software/jdk-7u51-linux-x64.gz
fi

cd /usr/local/server
cp /root/jdk-7u51-linux-x64.gz /usr/local/server/jdk-7u51-linux-x64.gz

tar xvzf jdk-7u51-linux-x64.gz
rm jdk-7u51-linux-x64.gz
mv jdk1.7.0_51 jdk7

### MARY
if test "$1" = "remote"
then
cd /root/
rm marytts-5.0.tar.gz
wget http://schuelerturnier-scworb.ch/schuetu-software/marytts-5.0.tar.gz
fi

cp /root/marytts-5.0.tar.gz /usr/local/server/marytts-5.0.tar.gz

cd /usr/local/server
tar xvzf marytts-5.0.tar.gz
rm marytts-5.0.tar.gz 
mv /usr/local/server/Users/dama/Desktop/marytts-5.0  mary
rm -R Users

# WEITER

export JAVA_HOME=/usr/local/server/jdk7
export CATALINA_HOME=/usr/local/server/tomcat
export PATH=$PATH:${JAVA_HOME}/bin

cd /usr/local/server/tomcat/webapps
rm -R *

# WEBAPP
wget https://www.dropbox.com/s/yspycr51pl44ni2/ROOT.war

cd /usr/local/server/

cd /usr/local/server/tomcat/conf
rm server.xml

wget https://www.dropbox.com/s/088hmqranh2kq6f/keystore
wget https://www.dropbox.com/s/6lqh68d3jkc37it/server.xml

/usr/local/server/mary/bin/marytts-server.sh >/usr/local/server/mary.log 2>&1 &
/usr/local/server/tomcat/bin/startup.sh

if test "$1" = "remote"
ps -ef | grep "dropbox" | awk '{print $2}' | xargs kill -9
then cd /root/
rm dropbox.jar
wget https://www.dropbox.com/s/o82wlb7j0rpsjew/dropbox.jar
nohup /usr/local/server/jdk7/bin/java -jar /root/dropbox.jar &
fi

ls -la

