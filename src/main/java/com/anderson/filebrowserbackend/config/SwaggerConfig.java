package com.anderson.filebrowserbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API FileBrowser",
                description = "Sistema de archivo virtual usando sesiones http",
                version = "1.0.0",
                contact = @Contact(
                        name = "Anderson Burga",
                        email = "andersonbdavila00@gmail.com"
                ),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = {
                @Server(
                    description = "dev server",
                    url = "http://localhost:9091"
                )
        }
)
public class SwaggerConfig {}
