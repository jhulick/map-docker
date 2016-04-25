#
# Redis Dockerfile
#
# https://github.com/dockerfile/redis
#

# Pull base image
FROM ubuntu:trusty

# Install Redis
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv C7917B12 && \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y redis-server pwgen && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Define mountable directories.
VOLUME ["/data"]

# Define working directory.
WORKDIR /data

# Define default command.
ENTRYPOINT ["redis-server"]

# Expose ports locally on VM
EXPOSE 6379