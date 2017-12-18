package controllers;

import com.threewks.thundr.view.json.JsonView;
import requests.Person;

import java.util.HashMap;
import java.util.Map;

public class GreetingController {

    public JsonView greeting() {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Hello, World!");
        return new JsonView(model);
    }

    public JsonView namedGreeting(Person person) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Hello, " + person.getFirstName() + " " + person.getLastName() + "!") ;
        return new JsonView(model);
    }
}
