---
title: nginx配置小记
tagline: ""
last_updated: ""
category : nginx
layout: post
tags : [nginx]
published: trus
description: ""
---
{% include JB/setup %}

我是菜鸡，一个nginx重定向的配置就搞了那么久.... 菜鸡 菜鸡  

## server配置  

## location配置  

## rewrite配置  

## default.conf配置  

```shell

```  

## wordpress.conf配置  

```shell
server {
    listen 8888;
    server_name www.alltuu.com;
    root  /home/wordpress/wordpress;
    index index.html index.php;
    #try_files $uri $uri/ /index.php?$args;
    #autoindex off;
    # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000

    #location / {
    #    try_files $uri $uri/ /index.php;
    #}
    location ~ \.php$ {
        #root           /usr/local/wordpress;
        #root          /home/wordpress;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_pass   127.0.0.1:9000;
        #fastcgi_pass unix:///home/wordpress/phpfpm.sock;
        fastcgi_index  index.php;
        #fastcgi_param  SCRIPT_FILENAME  /home/wordpress$fastcgi_script_name;
        fastcgi_param  SCRIPT_FILENAME  $document_root$fastcgi_script_name;
        include        fastcgi_params;
    }
}
```  
