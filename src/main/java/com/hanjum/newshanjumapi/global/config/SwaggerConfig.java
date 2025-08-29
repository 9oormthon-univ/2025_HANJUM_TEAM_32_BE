package com.hanjum.newshanjumapi.global.config;

import com.hanjum.newshanjumapi.global.annotation.ApiExceptions;
import com.hanjum.newshanjumapi.global.dto.ExampleHolder;
import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;
import com.hanjum.newshanjumapi.global.exception.dto.response.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {

    private static final String JWT = "JWT";
    private static final String BEARER_SCHEME = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .addSecurityItem(getSecurityRequirement())
                .components(getComponents())
                .servers(List.of(getLocalDevServer()));
    }
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiExceptions apiExceptions = handlerMethod.getMethodAnnotation(
                    ApiExceptions.class);

            if (apiExceptions != null) {
                generateErrorCodeResponseExample(operation, apiExceptions.values());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(Operation operation, ErrorStatus[] errorStatuses) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorStatuses)
                .map(
                        errorStatus -> ExampleHolder.builder()
                                .holder(getSwaggerExample(errorStatus))
                                .code(errorStatus.getStatusCode())
                                .name(errorStatus.name())
                                .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::code));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(ErrorStatus errorStatus) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(errorStatus.getStatusCode())
                .message(errorStatus.getMessage())
                .build();
        Example example = new Example();
        example.setValue(errorResponse);

        return example;
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.name(),
                                    exampleHolder.holder()
                            )
                    );
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse);
                }
        );
    }

    private Info getApiInfo() {
        return new Info()
                .title("")
                .description("Zeroroad API 문서")
                .version("1.0.0");
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement()
                .addList(JWT);
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes(JWT, getSecurityScheme());
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme(BEARER_SCHEME)
                .bearerFormat(JWT)
                .description("AccessToken을 입력해주세요. 형식: Bearer {token}");
    }

    private Server getLocalDevServer() {
        return new Server()
                .description("Development Server")
                .url("http://localhost:8080"); // TODO: 변경 예정
    }

}