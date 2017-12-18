package controllers;

import com.threewks.thundr.view.json.JsonView;
import requests.Greeting;

import java.util.HashMap;
import java.util.Map;

public class GreetingController {

    public JsonView greeting() {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Hello, World!");
        return new JsonView(model);
    }

    public JsonView namedGreeting(Greeting greeting) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Hello, " + greeting.getFirstName() + " " + greeting.getLastName() + "!") ;
        return new JsonView(model);
    }
}
