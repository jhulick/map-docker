#!/bin/sh

# update the system
apt-get update
apt-get upgrade

export JAVA_VERSION='8'
export JAVA_HOME='/usr/lib/jvm/java-${JAVA_VERSION}-openjdk-amd64'

export MAVEN_VERSION='3.3.9'
export MAVEN_HOME='/usr/share/maven'
export PATH=$PATH:$MAVEN_HOME/bin

export LANGUAGE='en_US.UTF-8'
export LANG='en_US.UTF-8'
export LC_ALL='en_US.UTF-8'
locale-gen en_US.UTF-8
dpkg-reconfigure locales

# install development utilities
apt-get -y install vim git zip bzip2 fontconfig curl software-properties-common python-software-properties

# install Java 8
add-apt-repository ppa:openjdk-r/ppa
apt-get update
apt-get install -y openjdk-8-jdk

# install maven
curl -fsSL http://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /usr/share && mv /usr/share/apache-maven-${MAVEN_VERSION} /usr/share/maven && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# install node.js
curl -sL https://deb.nodesource.com/setup_4.x | bash -
apt-get install -y nodejs unzip python g++ build-essential

# update npm
npm install -g npm

# install yeoman grunt bower grunt gulp
npm install -g yo bower grunt-cli gulp

# TODO install MAX dev utilities (generator, domain tools, etc)

################################################################################
# Install the graphical environment - uncomment below to install GUI
################################################################################

## force UTF-8 encoding
#echo 'LANG=en_US.UTF-8' >> /etc/environment
#echo 'LANGUAGE=en_US.UTF-8' >> /etc/environment
#echo 'LC_ALL=en_US.UTF-8' >> /etc/environment
#echo 'LC_CTYPE=en_US.UTF-8' >> /etc/environment
#
## run GUI as non-privileged user
#echo 'allowed_users=anybody' > /etc/X11/Xwrapper.config
#
## install Ubuntu desktop and VirtualBox guest tools
#apt-get install -y ubuntu-desktop virtualbox-guest-dkms virtualbox-guest-utils virtualbox-guest-x11
#apt-get install -y gnome-session-flashback

################################################################################
# Install the development tools
################################################################################

# install Chromium Browser
#apt-get install -y chromium-browser

# install PgAdmin
#apt-get install -y pgadmin3

#install Guake
#apt-get install -y guake
#cp /usr/share/applications/guake.desktop /etc/xdg/autostart/

# install Atom

#wget https://github.com/atom/atom/releases/download/v1.6.0/atom-amd64.deb
#dpkg -i atom-amd64.deb
#rm -f atom-amd64.deb
#dpkg --configure -a

# install Docker
curl -sL https://get.docker.io/ | sh

# configure docker group (docker commands can be launched without sudo)
usermod -aG docker vagrant

# install docker compose
curl -L https://github.com/docker/compose/releases/download/1.7.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# provide m2
mkdir -p /home/vagrant/.m2

# clean the box
apt-get clean
dd if=/dev/zero of=/EMPTY bs=1M > /dev/null 2>&1
rm -f /EMPTY