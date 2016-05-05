---
title: java发送httpPost请求实例
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,http]
published: true
description: ""
---
{% include JB/setup %}

在java中要发送http请求并获得参数的最常用的就是get和post的方法了，get方法大家相对比较容易，直接在url后添加上参数发送调用即可，方法也很多。而至于post请求则相对麻烦。  

下面记一则利用apach的HttpClient Api实现post的实例：  

```java
/**
     * 微博接入 登录回调后 第一步 用code获取access token
     * @param code
     * @return
     */
    public Map<String,String> getWBAccessToken(String code) {
        Map<String,String> map = new HashMap<String,String>();
        String result = null;
        HttpClient httpClient = new HttpClient();
        //建立post请求对象
        PostMethod postMethod =
                new PostMethod(Constants.BASE_WEIBO_AUTH_TOKEN_URL);
        //设置post参数
        NameValuePair[] params =
            {new NameValuePair("client_id", Constants.WEIBO_APP_ID),
                new NameValuePair("client_secret", Constants.WEIBO_APP_SECRET),
                new NameValuePair("grant_type", Constants.WEIBO_GRANT_TYPE),
                new NameValuePair("redirect_uri", Constants.WEIBO_REDIRECT_URL),
                new NameValuePair("code", code)};
        logger.info(params);
        //请求中塞入参数
        postMethod.setRequestBody(params);
        int statusCode = 0;
        try {
            //获得请求返回状态
            statusCode = httpClient.executeMethod(postMethod);
            logger.info(statusCode);
            //返回状态码200表示请求发送成功
            if (statusCode == HttpStatus.SC_OK) {
                //获得请求返回结果值
                result = postMethod.getResponseBodyAsString();
                logger.info(result);
            } else {
                return null;
            }
        } catch (HttpException e1) {
            // http请求异常
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        JSONObject jo = null;
        try {
            //由于返回的json数组被保存为String，这里将其格式成JSON对象取值
            jo = new JSONObject(result);
            logger.info(jo);
        } catch (JSONException e) {
            // String转换成JSon数组对象异常，只抛出
            e.printStackTrace();
        }
        String accesstoken = null;
        String uId = null;
        if (jo != null) {
            try {
                accesstoken = jo.getString("access_token");
                uId = jo.getString("uid");
            } catch (JSONException e) {
                // 将json转换成String异常，只抛出
                e.printStackTrace();
            }
        }
        map.put("accesstoken", accesstoken);
        map.put("uId", uId);
        return map;
    }
```
