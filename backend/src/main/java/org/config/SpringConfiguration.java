package org.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.repository.IMessageRepository;
import org.repository.IUserRepository;
import org.repository.InMemoryUserRepository;
import org.repository.Neo4jMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SpringConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Lazy
    @Bean(name = "neo4j")
    public DataSource connection(@Value("${JDBC_DATABASE_URL:jdbc:neo4j:bolt://localhost:7687/?user=neo4j,password=password,scheme=basic}") String jdbcUrl) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        return new HikariDataSource(config);
    }

    @Bean
    public IMessageRepository messageRepository(DataSource neo4j) {
        return new Neo4jMessageRepository(neo4j);
    }

    @Bean
    public IUserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Message board API")
                        .version("1.2")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName, new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("bearer")
                        )
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }

}
