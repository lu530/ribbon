package com.springcloud.ribbon.controller;


import com.sun.deploy.net.URLEncoder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RestTemplate 使用大全
 */
@RestController
public class RestTemplateContoller {

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;



    String host = null;
    String port = null;

    /**
     * get是用来查询资源的
     * 通过占位符封装get请求url 参数
     * getForEntity 可以获取得到状态码 和请求头
     * @param name
     * @return
     */
    public String RESTFul_get_1_Test(String name){
        /**
         * 获取注册在eureka 上的provider
         */
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        ServiceInstance instance = list.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        String url = "http://" + host + ":" + port + "/hello?name={1}";//通过占位符封装请求 url 可以用到 {2},{3},{4}
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, name);
        StringBuffer sb = new StringBuffer();
        HttpStatus statusCode = responseEntity.getStatusCode();
        String body = responseEntity.getBody();
        sb.append("statusCode：")
                .append(statusCode)
                .append("</br>")
                .append("body：")
                .append(body)
                .append("</br>");
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> keySet = headers.keySet();
        for (String s : keySet) {
            sb.append(s)
                    .append(":")
                    .append(headers.get(s))
                    .append("</br>");
        }
        return sb.toString();
    };

    /**
     * get是用来查询资源
     * 通过map符封装 get请求 参数
     * getForObject 只会获取provider 得返回值 常用
     * @param name
     * @return
     */
    public String RESTFul_get_2_Test(String name){
        Map<String, Object> map = new HashMap<>();
        String url = "http://" + host + ":" + port + "/hello?name={name}";  //url中包含参数中的key
        map.put("name", name);
        String result = restTemplate.getForObject(url, String.class, map);
        return result;
    }

    /**
     * post 是用来新建资源的
     * post 通过MultiValueMap封装参数
     * MultiValueMap 支持 一个key 有多个value 适合那些参数需要传递集合
     * @param name
     * @return
     */
    public String RESTFul_post_map_Test(String name){
        String url = "http://" + host + ":" + port + "/hello2";
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("name", name);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, map, String.class);
        return responseEntity.getBody();
    }

    /**
     * post 是用来新建资源的
     * 以json 方式发送，那prover 就不能通过context.getParameters() 获取了，
     * 不过用 springBoot 自动封装成对象更加方便
     * @return
     */
    public User RESTFul_post_json_test(){
        String url = "http://" + host + ":" + port + "/user";
        User u1 = new User();
        u1.setUsername("牧码小子");
        u1.setAddress("深圳");
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(url, u1, User.class);
        return responseEntity.getBody();
    }


    @Data
    public class User {
        private String username;
        private String address;
    }


    @RequestMapping("/register")
    public String register(User user) throws UnsupportedEncodingException {
        return "redirect:/loginPage?username=" + URLEncoder.encode(user.getUsername(),"UTF-8") + "&address=" + URLEncoder.encode(user.getAddress(),"UTF-8");
    }
    @GetMapping("/loginPage")
    @ResponseBody
    public String loginPage(User user) {
        return "loginPage:" + user.getUsername() + ":" + user.getAddress();
    }

    /**
     * 通过 postForLocation 获取 provider返回的 URL对象
     * @return
     */
    public String RESTFul_location_test(){
        String url = "http://" + host + ":" + port + "/register";
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("username", "牧码小子");
        map.add("address", "深圳");
        URI uri = restTemplate.postForLocation(url, map);
        String s = restTemplate.getForObject(uri, String.class);
        return s;
    }


    /**
     * put 用来更新资源 put 一般都是没有返回值的
     * put同样可以用 MultiValueMap 和 JSON 作为参数请求provider接口
     * @return
     */
    public void RESTFul_put_test(){
        String url1 = "http://" + host + ":" + port + "/user/name";
        String url2 = "http://" + host + ":" + port + "/user/address";
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("username", "牧码小子");
        map.add("address", "深圳");
        restTemplate.put(url1, map);
        User u1 = new User();
        u1.setAddress("广州");
        u1.setUsername("江南一点雨");
        restTemplate.put(url2, u1);
    }

    /**
     * delete 你一看就知道是用来删除数据用的啦
     * delete 方法和get方法一样都是只能在url上传递参数
     * 可以通过占位符和MultiValueMap 方法的方式实现url的参数封装
     *
     */
    public void RESTFul_delete_test(){
        String url1 = "http://" + host + ":" + port + "/user/{1}";
        String url2 = "http://" + host + ":" + port + "/user/?username={username}";
        Map<String,String> map = new HashMap<>();
        map.put("username", "牧码小子");
        restTemplate.delete(url1, 99);
        restTemplate.delete(url2, map);
    }


    /**
     * 通用的方法 exchange。为什么说它通用呢？因为这个方法需要你在调用的时候去指定请求类型，即它既能做 GET 请求，也能做 POST
     * 请求，也能做其它各种类型的请求。如果开发者需要对请求进行封装，使用它再合适不过了，举个简单例子：
     */
    public void RESTFul_exchange_exchange(){
        String url = "http://" + host + ":" + port + "/customheader";
        HttpHeaders headers = new HttpHeaders();
        headers.add("cookie","justdojava");
        HttpEntity<MultiValueMap<String,String>> request =  new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println(responseEntity.getBody());
    }


    /**
     *
     * @param args
     */
    public static void main(String[] args){
        MultiValueMap<String, String> stringMultiValueMap = new LinkedMultiValueMap<>();
        stringMultiValueMap.add("早班 9:00-11:00", "周一");
        stringMultiValueMap.add("早班 9:00-11:00", "周二");
        stringMultiValueMap.add("中班 13:00-16:00", "周三");
        stringMultiValueMap.add("早班 9:00-11:00", "周四");
        stringMultiValueMap.add("测试1天2次 09:00 - 12:00", "周五");
        stringMultiValueMap.add("测试1天2次 09:00 - 12:00", "周六");
        stringMultiValueMap.add("中班 13:00-16:00", "周日");
        //打印所有值
        Set<String> keySet = stringMultiValueMap.keySet();
        for (String key : keySet) {
            List<String> values = stringMultiValueMap.get(key);
            System.out.println(StringUtils.join(values.toArray()," ")+":"+key);
        }
    }
}
