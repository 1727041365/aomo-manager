package com.yupi.springbootinit.aop;// IpWhitelistAspect.java
import com.yupi.springbootinit.annotation.IpWhitelist;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class IpWhitelistAspect {

    @Before("@annotation(ipWhitelist)")
    public void checkIpWhitelist(IpWhitelist ipWhitelist) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String clientIp = request.getRemoteAddr();
        
        Set<String> allowedIps = new HashSet<>(Arrays.asList(ipWhitelist.value()));
        
        if (!allowedIps.isEmpty() && !allowedIps.contains(clientIp)) {
            throw new RuntimeException("Access denied: your IP is not in the whitelist");
        }
    }
}
