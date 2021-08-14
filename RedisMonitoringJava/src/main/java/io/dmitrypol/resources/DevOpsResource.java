package io.dmitrypol.resources;

import io.dmitrypol.client.RedisSentinelClient;
import io.dmitrypol.views.DevOpsView;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@AllArgsConstructor
@Slf4j
public class DevOpsResource {
    private final StatefulRedisConnection<String, String> redisConnection;
    private final RedisSentinelClient redisSentinelClient;

    @GET
    public DevOpsView devops() {
        return new DevOpsView(redisSentinelClient.masters());
    }

    @GET
    @Path("redis")
    public String redis() {
        return redisConnection.sync().ping();
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

}
