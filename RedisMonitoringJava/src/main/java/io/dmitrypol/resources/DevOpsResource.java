package io.dmitrypol.resources;

import io.dmitrypol.client.RedisSentinelClient;
import io.dmitrypol.views.DevOpsView;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@AllArgsConstructor
@Slf4j
public class DevOpsResource {
    private final RedisSentinelClient redisSentinelClient;
    private final StatefulRedisConnection<String, String> redisConnection;
    private final StatefulRedisClusterConnection<String, String> redisClusterConnection;

    @GET
    public DevOpsView devops() {
        return new DevOpsView(redisSentinelClient.masters());
    }

    @GET
    @Path("redis")
    public String[] redis() {
        var tmp = redisConnection.sync().info();
        return tmp.split("\\n");
    }

    @GET
    @Path("get/{key}")
    public String get(@PathParam("key") @NonNull String key) {
        return Optional.ofNullable(redisConnection.sync().get(key)).orElse("null");
    }

    @GET
    @Path("masters")
    public List<List<Map<String, String>>> masters(){
        return redisSentinelClient.masters();
    }

    @GET
    @Path("master/{name}")
    public List<Map<String, String>> master(@PathParam("name") @NonNull String name){
        return redisSentinelClient.master(name);
    }

    @GET
    @Path("replicas/{name}")
    public List<List<Map<String, String>>> replicas(@PathParam("name") @NonNull String name){
        return redisSentinelClient.replicas(name);
    }

    // cluster specific endpoints

    @GET
    @Path("cluster")
    public String[] cluster() {
        var tmp = redisClusterConnection.sync().info();
        return tmp.split("\\n");
    }

    @GET
    @Path("clusterGet/{key}")
    public String clusterGet(@PathParam("key") @NonNull String key) {
        return Optional.ofNullable(redisClusterConnection.sync().get(key)).orElse("null");
    }

    @GET
    @Path("clusterInfo")
    public String[] clusterInfo() {
        var tmp = redisClusterConnection.sync().clusterInfo();
        return tmp.split("\\n");
    }

    @GET
    @Path("clusterNodes")
    public String[] clusterNodes() {
        var tmp = redisClusterConnection.sync().clusterNodes();
        return tmp.split("\\n");
    }

    @GET
    @Path("clusterSlots")
    public List<Object> clusterSlots() {
        return redisClusterConnection.sync().clusterSlots();
    }

}
