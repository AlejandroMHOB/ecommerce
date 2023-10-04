package com.pruebatecnica.ecommerce.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

  @Value("${openapi.dev.url}")
  private String devUrl;


  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    Contact contact = new Contact();
    contact.setName("Alejandro Murcia Herrero");
    contact.setEmail("alejandromurciaherrero@gmail.com");
    contact.setUrl("https://github.com/AlejandroMHOB");

    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("Aplicación backend para un comercio electrónico")
        .version("0.0.1")
        .contact(contact)
        .description("Esta API expone los endpoints disponibles de la aplicación de comercio electrónico para poder interactuar desde el respectivo frontend").termsOfService("AlejandroMHOB")
        .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}