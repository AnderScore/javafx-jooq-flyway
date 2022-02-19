module org.test.javafx {
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires javafx.controls;
    requires org.jooq;
    requires org.flywaydb.core;

    opens db.migration;

    exports org.test.javafx;
}