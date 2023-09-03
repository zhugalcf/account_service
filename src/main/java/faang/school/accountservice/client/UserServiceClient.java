package faang.school.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}/api/v1")
public interface UserServiceClient {

    @GetMapping("/users/exists/{id}")
    Boolean checkUserExist(@PathVariable long id);
}
