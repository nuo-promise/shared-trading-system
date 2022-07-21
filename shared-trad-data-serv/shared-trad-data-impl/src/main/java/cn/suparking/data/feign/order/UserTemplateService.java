package cn.suparking.data.feign.order;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shared-trad-user-serv", path = "/user-center/user")
public interface UserTemplateService {

    /**
     * 根据用户手机号获取用户信息.
     *
     * @param iphone 手机号
     * @return JSONObject
     */
    @PostMapping("/getUserByIphone")
    JSONObject getUserByIphone(@RequestParam String iphone);
}
