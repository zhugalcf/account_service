package faang.school.accountservice.client;

import faang.school.accountservice.dto.Project;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "project-service", url = "${project-service.host}:${project-service.port}")
public interface ProjectServiceClient {
    @GetMapping("/project")
    Project getProjectById(@RequestParam("projectId") long projectId, @RequestParam("userId") long userId);
}
