Running the Demo with Vagrant
=============================

Vagrant will set up an Ubuntu virtual machine to install software on. In this example Vagrant will install Docker on the virtual machine and deploy the Docker Containers afterwards.

To run the demo:

- Install Gradle, see http://gradle.org/gradle-download/
- Install Maven, see https://maven.apache.org/download.cgi
- Install Virtual Box from https://www.virtualbox.org/wiki/Downloads
- Install Vagrant, see http://docs.vagrantup.com/v2/installation/index.html
- Go to the directory `max-cloud-demo`. You will see a list of service directories. In the 'max-gateway',
  'resource', and 'ui' directories run `./gradlew clean assemble`. In the 'max-registry' directory run `mvn clean package`.
- Change to the directory `docker-vagrant` and run `vagrant up`. Each time you start the Vagrant VM the Docker containers will be started, too.
- Use `vagrant halt` to shut down the system or `vagrant destroy` to delete the VM. Login to the VM using `vagrant ssh`. Do a new provisioning using `vagrant provision` . Then the containers will be rebuild.

The result should be:

- A new VirtualBox VM is fired up by Vagrant
- Docker has been installed in the VM
- Redis has been installed in the VM
- CAS has been setup as a service in the VM
- You can access the application at http://127.0.0.1:18080/ui. 
- You can access the Eureka dashboard at http://127.0.0.1:18761/
