package io.dmitrypol.resources;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Slf4j
public class RedisMonitoringResource {
    private final StatefulRedisConnection<String, String> redisConnection;

    @GET
    public String redis() {
        return redisConnection.sync().ping();
    }
}
