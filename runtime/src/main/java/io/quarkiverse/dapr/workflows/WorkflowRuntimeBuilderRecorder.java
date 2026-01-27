package io.quarkiverse.dapr.workflows;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowRuntime;
import io.dapr.workflows.runtime.WorkflowRuntimeBuilder;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.ShutdownContext;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class WorkflowRuntimeBuilderRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowRuntimeBuilderRecorder.class);

    public RuntimeValue<WorkflowRuntimeBuilder> build(Set<Class> workflows,
            Set<Class> workflowActivityClasses, ShutdownContext shutdownContext) {

        WorkflowRuntimeBuilder builder = new WorkflowRuntimeBuilder();

        for (Class<Workflow> workflow : workflows) {
            builder.registerWorkflow(workflow);
        }

        for (Class<WorkflowActivity> activityClass : workflowActivityClasses) {
            builder.registerActivity(activityClass);
        }

        WorkflowRuntime runtime = builder.build();

        runtime.start(false);

        shutdownContext.addShutdownTask(() -> {
            LOG.info("Shutdown context task: Closing WorkflowRuntime");
            runtime.close();
        });

        return new RuntimeValue<>(builder);
    }
}
