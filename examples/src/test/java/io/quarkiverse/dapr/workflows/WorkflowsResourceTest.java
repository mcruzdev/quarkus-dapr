package io.quarkiverse.dapr.workflows;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
class WorkflowsResourceTest {

    @Test
    void shouldExecuteUserWorkflow() {
        String workflowInstanceID = RestAssured.given()
                .header("Content-Type", "application/json")
                .post("/workflows/user/1")
                .then()
                .statusCode(202)
                .extract()
                .header("Workflow-Instance-Id");

        RestAssured.given()
                .get("/workflows/{workflowInstanceId}/result", Map.of("workflowInstanceId", workflowInstanceID))
                .then()
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.equalTo("John Doe"))
                .statusCode(200);

    }

    @Test
    void shouldExecuteDemoChainWorkflow() {
        String workflowInstanceID = RestAssured.given()
                .header("Content-Type", "text/plain")
                .post("/workflows/uppercase")
                .then()
                .statusCode(202)
                .extract()
                .header("Workflow-Instance-Id");

        RestAssured.given()
                .get("/workflows/{workflowInstanceId}/result", Map.of("workflowInstanceId", workflowInstanceID))
                .then()
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.equalTo("John Doe"))
                .statusCode(200);
    }
}
