module spi {
    requires domain;
    requires spring.context;
    requires java.sql;
    requires spring.jdbc;
    requires org.slf4j;
    requires org.postgresql.jdbc;
    requires static lombok;
    requires spring.core;
    requires spring.tx;
}