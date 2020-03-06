package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.OpenIdJson;
import com.example.utils.GetPhone;
import com.example.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    private String appID = "wx02449700f01e6d5f";
    private String appSecret = "a79c487a23c866886d10c7b47cef25ff";

    @RequestMapping("/")
    public String hi() {
        return " Hello Spring Boot";
    }

    @PostMapping("/user/login")
    public String userLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String result = "";
        //微信服务器获取用户openid的url
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + this.appID + "&secret="
                + this.appSecret + "&js_code="
                + code
                + "&grant_type=authorization_code";
        //请求微信服务器，用code换取openid。HttpUtil是工具类，后面会给出实现，Configure类是小程序配置信息，后面会给出代码//
        try {
            result = HttpUtil.doGet(url, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        OpenIdJson openIdJson = mapper.readValue(result, OpenIdJson.class);
        System.out.println(result.toString());
        System.out.println(openIdJson.getOpenid());
        return result;
    }

    @PostMapping(value = "/user/mini_getPhone")
    public String mini_getPhone(@RequestParam("encryptedData") String encryptedData, @RequestParam("iv") String iv, @RequestParam("session_key") String session_key) {
        System.out.println("session_key: " + session_key + "encryptedData: " + encryptedData + "iv: " + iv);
        //调用解密
        JSONObject obj=GetPhone.getPhoneNumber(session_key,encryptedData,iv);//解密电话号码
        String sphone=obj.get("phoneNumber").toString();
        return sphone;
    }
}
