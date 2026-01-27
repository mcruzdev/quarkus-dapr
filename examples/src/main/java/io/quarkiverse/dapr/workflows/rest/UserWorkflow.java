package io.quarkiverse.dapr.workflows.rest;

import jakarta.enterprise.context.ApplicationScoped;

import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowStub;

@ApplicationScoped
public class UserWorkflow implements Workflow {

    @Override
    public WorkflowStub create() {
        return ctx -> {

            String instanceId = ctx.getInstanceId();

            ctx.getLogger().info("Workflow instance: {}", instanceId);

            User result = ctx
                    .callActivity(GetUserWorkflowActivity.class.getName(), ctx.getInput(String.class), User.class).await();

            ctx.getLogger().info("Workflow result (User): {}", result);

            ctx.complete(result);
        };
    }
}
