package projeto.api.taskmanager.configuration;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;

import projeto.api.taskmanager.user.dtos.UserDTO;

public class CustomKeyGenerator implements KeyGenerator{
    
    public Object generate(Object target, Method method, Object... params) {
        UserDTO userDTO = (UserDTO) params[0];
        Pageable pageable = (Pageable) params[1];
        return "AllTasks_" + userDTO.getId() + "_page="+pageable.getPageNumber() + "_size=" + pageable.getPageSize();
    }
}
