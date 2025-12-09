module domain {
    requires static lombok;
    exports com.acme.jga.domain.input.functions.tenants to app;
    exports com.acme.jga.domain.output.functions.tenants to spi;
    exports com.acme.jga.domain.exceptions to app, spi;
    exports com.acme.jga.domain.annotations to app, spi;
    exports com.acme.jga.domain.model.tenant to app, spi;
    exports com.acme.jga.domain.model.sorting to app, spi;
    exports com.acme.jga.domain.model.generic to app, spi;
    exports com.acme.jga.domain.shared to app, spi;
}