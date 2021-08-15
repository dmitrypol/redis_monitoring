# How to monitor Redis servers

## Setup local environment with Redis and Sentinels

* To make it easier we will run Redis and Sentinels with Docker
* Install Docker on your computer
* Run `docker-compose up --build -d`

```
# get inside the container
docker exec -it redis1 sh

# setup replication
redis-cli -h redis2 replicaof redis1 6379
redis-cli -h redis3 replicaof redis1 6379
# verify replication - should respond with 2 replicas
redis-cli -h redis1 role

# setup monitoring
ping redis1
# specify IP address in the command below to register Redis master with all 3 Sentinels
redis-cli -h sentinel1 -p 26379 sentinel monitor my-redis IP_ADDRESS_HERE 6379 2
redis-cli -h sentinel2 -p 26379 sentinel monitor my-redis IP_ADDRESS_HERE 6379 2
redis-cli -h sentinel3 -p 26379 sentinel monitor my-redis IP_ADDRESS_HERE 6379 2

# verify monitoring - should respond with info on current master and 2 replicas
redis-cli -h sentinel1 -p 26379 sentinel master my-redis
redis-cli -h sentinel1 -p 26379 sentinel replicas my-redis
```

## Setup Redis Cluster

Redis comes with a handy script to setup Cluster so we will use that instead of Docker.  

```
git clone git@github.com:redis/redis.git
make
cd utils/create-cluster/
./create-cluster start
./create-cluster create

# afterwards run
./create-cluster stop
./create-cluster clean
```

## Run monitoring application

The monitorinig appication is built in Java using https://www.dropwizard.io/en/latest/ but it could be done in another language / framework.  

```
cd RedisMonitoringJava
mvn clean install
java -jar target/RedisMonitoringJava-1.0-SNAPSHOT.jar server config.yml
# verify server is running
http://localhost:8080/
http://localhost:8081/
```


