package io.dmitrypol.client;

import io.dmitrypol.RedisMonitoringJavaConfiguration;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class RedisSentinelClient {
    private List<StatefulRedisSentinelConnection<String, String>> sentClients = new ArrayList<>();

    public RedisSentinelClient(RedisMonitoringJavaConfiguration config){
        for (Map<String, String> sentinel : config.getSentinels()) {
            String sentHost = sentinel.get("host");
            int sentPort = Integer.parseInt(sentinel.get("port"));
            RedisURI redisUri = RedisURI.Builder.redis(sentHost, sentPort).build();
            sentClients.add(RedisClient.create(redisUri).connectSentinel());
        }
    }

    public List<List<Map<String, String>>> masters(){
        List<List<Map<String, String>>> output = new ArrayList<>();
        for (StatefulRedisSentinelConnection<String, String> sentinel : sentClients) {
            if (sentinel.sync().ping().equals("PONG")) {
                output.add(sentinel.sync().masters());
            }
        }
        return output;
    }

    public List<Map<String, String>> master(String name) {
        List<Map<String, String>> output = new ArrayList<>();
        for (StatefulRedisSentinelConnection<String, String> sentinel : sentClients) {
            if (sentinel.sync().ping().equals("PONG")) {
                output.add(sentinel.sync().master(name));
            }
        }
        return output;
    }

    public List<List<Map<String, String>>> replicas(String clusterName) {
        List<List<Map<String, String>>> output = new ArrayList<>();
        for (StatefulRedisSentinelConnection<String, String> sentinel : sentClients) {
            if (sentinel.sync().ping().equals("PONG")) {
                output.add(sentinel.sync().slaves(clusterName));
            }
        }
        return output;
    }
}
