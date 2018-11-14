/*
 * Copyright (c) 2019-Present Pivotal Software, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.uaa.bundled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.loader.WebappLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

/**
 * Startup a bundled UAA Server.
 *
 * @author Gunnar Hillert
 *
 */
@SpringBootApplication
public class UaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaaApplication.class, args);
	}

	@Bean
	public ServletWebServerFactory servletContainer() throws IOException {

		final File tempDirectory = Files.createTempDirectory("uaa").toFile();
		final File tempUaaYmlFile = new File(tempDirectory, "uaa.yml");
		final File tempUaaWarFile = new File(tempDirectory, "uaa.war");

		FileCopyUtils.copy(
				new ClassPathResource("uaa.yml").getInputStream(),
				new FileOutputStream(tempUaaYmlFile));

		FileCopyUtils.copy(
				new ClassPathResource("uaa.war").getInputStream(),
				new FileOutputStream(tempUaaWarFile));

		System.out.println("uaa.yml: " + tempUaaYmlFile.getAbsolutePath());
		System.out.println("uaa.war: " + tempUaaWarFile.getAbsolutePath());

		System.setProperty("UAA_CONFIG_FILE", tempUaaYmlFile.getAbsolutePath());

		return new TomcatServletWebServerFactory() {
			protected TomcatWebServer getTomcatWebServer(org.apache.catalina.startup.Tomcat tomcat) {
				final Server tomcatServer = tomcat.getServer();
				final File catalinaBase = new File(tempDirectory, "catalina");
				catalinaBase.mkdirs();

				tomcatServer.setCatalinaBase(catalinaBase);
				new File(tomcatServer.getCatalinaBase(), "webapps").mkdirs();
				try {
					Context context = tomcat.addWebapp("/uaa", tempUaaWarFile.toString());
					final ClassLoader properClassLoader = UaaApplication.class.getClassLoader();

					WebappLoader loader =
							new WebappLoader(properClassLoader);
						context.setLoader(loader);

				} catch (Exception ex) {
					throw new IllegalStateException("Failed to add webapp", ex);
				}
				return super.getTomcatWebServer(tomcat);
			}
		};
	}
}
