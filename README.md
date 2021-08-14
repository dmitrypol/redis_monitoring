# How to monitor Redis servers

* Setup local environment
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
# specify IP address in the command below
redis-cli -h sentinel1 -p 26379 sentinel monitor my-redis IP_ADDRESS_HERE 6379 2

# verify monitoring - should respond with info on current master and 2 replicas
redis-cli -h sentinel1 -p 26379 sentinel master my-redis
redis-cli -h sentinel1 -p 26379 sentinel replicas my-redis
```
