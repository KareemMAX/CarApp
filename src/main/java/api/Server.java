package api;

import model.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    public static void main(String[] args){
        Database.getInstance();
        SpringApplication.run(Server.class, args);
    }
}
