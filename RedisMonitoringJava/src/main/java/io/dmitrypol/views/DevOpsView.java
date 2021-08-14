package io.dmitrypol.views;

import io.dropwizard.views.View;
import lombok.Getter;
import java.util.List;
import java.util.Map;

public class DevOpsView extends View {
    @Getter private final List<List<Map<String, String>>> redisMasters;

    public DevOpsView(List<List<Map<String, String>>> redisMasters) {
        super("devops.mustache");
        this.redisMasters = redisMasters;
    }
}
