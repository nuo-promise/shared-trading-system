package cn.suparking.order.feign.order;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shared-trad-user-serv", path = "/user-center/user")
public interface UserTemplateService {

    /**
     * 根据用户手机号获取用户信息.
     *
     * @param iphone 手机号
     * @return JSONObject
     */
    @GetMapping("/getUserByIphone")
    JSONObject getUserByIphone(@RequestParam String iphone);

    /**
     * 根据用户id获取用户信息.
     *
     * @param id 用户id
     * @return JSONObject
     */
    @GetMapping("/{id}")
    JSONObject detailUser(@PathVariable("id") Long id);
}
