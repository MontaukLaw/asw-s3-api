package com.wulala.awss3api.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyTomcatConnectorCustomizer implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer factory) {
        if (factory instanceof TomcatEmbeddedServletContainerFactory) {
            tomcatEmbeddedServletContainerFactory();
        }
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);
                connector.setParseBodyMethods("POST,PUT,DELETE");
            }
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
    }

//    @Bean
//    public Connector httpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8910);
//        connector.setSecure(false);
//        connector.setRedirectPort(8443);//端口
//        return connector;
//    }
}