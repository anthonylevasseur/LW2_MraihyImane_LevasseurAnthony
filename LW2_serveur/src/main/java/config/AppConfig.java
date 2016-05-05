package config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"controller"})
public class AppConfig {

	public static Connection getConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		String ConnectionString = "jdbc:postgresql://ec2-23-21-42-29.compute-1.amazonaws.com:5432/dctmk7ae8ib8n6?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		String username = "cpjdzdovglsrvz";
		String password = "iXLcGVxL5GosH0ymO3jLfqoq2L";
		Connection connection = DriverManager.getConnection(ConnectionString, username, password);
		return connection;
	}
}
