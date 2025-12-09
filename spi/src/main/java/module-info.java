module spi {
    requires domain;
    requires java.sql;
    exports com.acme.jga.spi.dao.tenant.api;
    exports com.acme.jga.spi.dao.tenant.impl;
    exports com.acme.jga.spi.jdbc.model;
    requires spring.context;
    requires spring.tx;
    requires static lombok;
    requires spring.jdbc;
    requires org.postgresql.jdbc;
    requires org.slf4j;
}