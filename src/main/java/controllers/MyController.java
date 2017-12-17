package controllers;

import com.threewks.thundr.view.json.JsonView;

import java.util.HashMap;
import java.util.Map;

public class MyController {

    public JsonView home() {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Hello World!");
        return new JsonView(model);
    }
}
