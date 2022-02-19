package org.test.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.flywaydb.core.Flyway;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jooq.generated.Tables.PERSON;
import static org.jooq.generated.tables.Animal.ANIMAL;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Logger logger = Logger.getAnonymousLogger();

        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());

            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:sqlite:database.db", "", "")
                    .load();
            flyway.migrate();

            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            DSL.using(connection)
                    .insertInto(PERSON)
                    .columns(PERSON.ID, PERSON.NAME)
                    .values(1, "Jane")
                    .execute();

            var people = DSL.using(connection)
                    .select(PERSON.NAME)
                    .from(PERSON)
                    .fetch();

            logger.log(Level.INFO, "%nPeople:%n%s".formatted(people));

            var animals = DSL.using(connection)
                    .select(ANIMAL.NAME)
                    .from(ANIMAL)
                    .fetch();

            logger.log(Level.INFO, "%nAnimals:%n%s".formatted(animals));

        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Could not start SQLite Drivers", e);
        }

        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}