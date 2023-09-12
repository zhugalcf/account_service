package faang.school.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", url = "${project-service.url}")
public interface ProjectServiceClient {

    @GetMapping("/projects/exists/{id}")
    Boolean checkProjectExist(@PathVariable long id);
}
