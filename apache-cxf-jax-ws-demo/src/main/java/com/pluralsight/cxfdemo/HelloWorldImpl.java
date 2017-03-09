
package com.pluralsight.cxfdemo;

import javax.jws.WebService;

@WebService(endpointInterface = "com.pluralsight.cxfdemo.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}

