package net.devh.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@Api
public class UserController {

    @Value("${name:unknown}")
    private String name; // TODO Убрать!

    @Autowired
    private Registration registration; // TODO Убрать!

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printServiceA() {
        return registration.getServiceId() + " (" + registration.getHost() + ":" + registration.getPort() + ")" + "===>name:" + name;
    }
}
