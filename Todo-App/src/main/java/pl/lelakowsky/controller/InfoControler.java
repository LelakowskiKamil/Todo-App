package pl.lelakowsky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lelakowsky.TaskConfigurationProperties;


@RestController
public class InfoControler {
private DataSourceProperties dataSource;
private TaskConfigurationProperties myProp;

    InfoControler(DataSourceProperties dataSource, TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/info/url")
String url(){
return dataSource.getUrl();
}

    @GetMapping("/info/prop")
    boolean myProp(){
return myProp.getTemplate().isAllowMultipleTasks();
}
}
