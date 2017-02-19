---
title: Let's Encrypt By Nginx 网站上锁实操笔记
tagline: ""
last_updated: ""
category : ssl
layout: post
tags : [SSL, Let's Encrypt, Nginx]
published: true
description: ""
---
{% include JB/setup %}

参考:  
<https://ksmx.me/letsencrypt-ssl-https/>  

<https://imququ.com/post/letsencrypt-certificate.html>  

## 前言  
随着HTTPS的不断普及以及各个厂家的浏览器对HTTP网站的各种安全警告,HTTPS似乎已经成了网站标配.而对于个人网站来说,付费的HTTPS证书似乎并不是很适合,最近网上看到了不少关于`Let's Encrypt`的HTTPS认证,于是就自己也实操了一番之后,根据上述参考的网站把内容重新整理了一下,以做为自己的一个学习笔记.  

## 生成 Let's Encrypt 证书  

**1、安装certbot**  

```  
[root@iZwz92us1dza0axtrwza0bZ ~]# yum install certbot
```  

**2、创建对应域名的配置文件**  

```  
[root@iZwz92us1dza0axtrwza0bZ ~]# mkdir /etc/letsencrypt/configs
```  

**3、编辑配置文件**  

```  
[root@iZwz92us1dza0axtrwza0bZ ~]# vim /etc/letsencrypt/configs/yourdomain.com.conf
```  

配置文件如下:  

```  
#写你的域名和邮箱
domains=yourdomain.com,www.yourdomain.com
rsa-key-size=2048
email=111@gmail.com
text=True

#把下面的路径修改为example.com的目录位置
authenticator=webroot
webroot-path=/usr/share/nginx/ssl
```  

上面的`domains`需要额外注意,当我们只指定域名yourdomain.com时,那么我们在访问www.yourdomain.com会报证书认证的域名与该域名不符,小绿锁不在并会被警报.所以我们可以在这里同时指定yourdomain.com和www.yourdomain.com.但是不同但域名还是分开配置文件较好.  

此处的`webroot-path`稍后将会用到,主要是配合nginx做主机所有权验证.启动证书申请程序的时候,需要根据这个指定的根目录访问认证文件来确认帧数申请人是否真的拥有该域名及主机.  

**4、配置nginx以支持Let's Encrypt验签**  

上面那步已经在`yourdomain.com.conf`中设置了`webroot-path`为`/usr/share/nginx/ssl`,这是因为,我在nginx应用中只是把nginx作为一个请求分发的服务器,根据不同域名做本地的端口跳转到各个tomcat中.所以,需要让Let's Encrypt直接访问域名根目录下的校验文件有两种办法:  
1、在tomcat部署的每个应用中放置校验文件,即根据不同域名对应的webapp设置不同的根目录到对应的`webroot-path`中,这样的话,配置不统一,也不好维护.  
2、第二种也就是现在用的这种,在nginx的server配置中将Let's Encrypt校验的域名访问路径在跳转tomcat应用之前重定向到一个固定的路径下,也就是上文中设置的`/usr/share/nginx/ssl`了,这样的话,当我们部署不同的应用时,只要保证nginx的ssl认证配置有效就ok了.配置文件如下:  

```  
server {
    listen       80;
    server_name  www.yourdomain.com yourdomain.com;

    location ^~ /.well-known/acme-challenge {
        alias /usr/share/nginx/ssl/.well-known/acme-challenge;
        try_files $uri =404;
    }

    location / {
        proxy_pass  http://127.0.0.1:8080/;
    }
}
```  

将`http://www.yourdomain.com/.well-known/acme-challenge`的请求重定向到本地的`/usr/share/nginx/ssl/.well-known/acme-challenge`去.  

当然如果你的nginx没有做其他跳转的话,是直接可以省略上面步骤的,直接将`yourdomain.com.conf`的`webroot-path`设置成`/usr/share/nginx/html`即可.  

**5、执行证书自动化生成命令**  

好了,现在我们可以执行证书申请程序来申请我们自己的ssl证书了.   

```  
[root@iZwz92us1dza0axtrwza0bZ ~]# letsencrypt -c /etc/letsencrypt/configs/yourdomain.com.conf certonly
```  

当看到:  

```  
Saving debug log to /var/log/letsencrypt/letsencrypt.log
Starting new HTTPS connection (1): acme-v01.api.letsencrypt.org
Obtaining a new certificate
Performing the following challenges:
http-01 challenge for lingxuexi.com
http-01 challenge for www.yourdomain.com
Using the webroot path /usr/share/nginx/ssl for all unmatched domains.
Waiting for verification...
Cleaning up challenges
Generating key (2048 bits): /etc/letsencrypt/keys/0000_key-certbot.pem
Creating CSR: /etc/letsencrypt/csr/0000_csr-certbot.pem

IMPORTANT NOTES:
 - Congratulations! Your certificate and chain have been saved at
   /etc/letsencrypt/live/yourdomain.com/fullchain.pem. Your cert will
   expire on 2017-05-20. To obtain a new or tweaked version of this
   certificate in the future, simply run certbot again. To
   non-interactively renew *all* of your certificates, run "certbot
   renew"
 - If you like Certbot, please consider supporting our work by:

   Donating to ISRG / Let's Encrypt:   https://letsencrypt.org/donate
   Donating to EFF:                    https://eff.org/donate-le
```  

说明证书已经申请成功了.证书以及私钥都已经放在`/etc/letsencrypt/live/yourdomain.com`目录下.  

## 配置nginx加入SSL证书  

访问:[Mozilla SSL Configuration Generator](https://mozilla.github.io/server-side-tls/ssl-config-generator/)生成参考配置文件.  

![ssl-1](http://rustic.img-cn-qingdao.aliyuncs.com/ssl/ssl-1.png@888w)  

将配置文件复制到你的nginx配置文件中,并修改其中的配置的证书路径:  

```  
server {
    listen 80 default_server;
    listen [::]:80 default_server;

    # Redirect all HTTP requests to HTTPS with a 301 Moved Permanently response.
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;

    # certs sent to the client in SERVER HELLO are concatenated in ssl_certificate
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    ssl_session_timeout 1d;
    ssl_session_cache shared:SSL:50m;
    ssl_session_tickets off;

    # Diffie-Hellman parameter for DHE ciphersuites, recommended 2048 bits
    ssl_dhparam /etc/nginx/ssl/dhparam.pem;

    # intermediate configuration. tweak to your needs.
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers 'ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-ECDSA-AES128-SHA:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA:ECDHE-ECDSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA:ECDHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA256:DHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA256:DHE-RSA-AES256-SHA:ECDHE-ECDSA-DES-CBC3-SHA:ECDHE-RSA-DES-CBC3-SHA:EDH-RSA-DES-CBC3-SHA:AES128-GCM-SHA256:AES256-GCM-SHA384:AES128-SHA256:AES256-SHA256:AES128-SHA:AES256-SHA:DES-CBC3-SHA:!DSS';
    ssl_prefer_server_ciphers on;

    # HSTS (ngx_http_headers_module is required) (15768000 seconds = 6 months)
    add_header Strict-Transport-Security max-age=15768000;

    # OCSP Stapling ---
    # fetch OCSP records from URL in ssl_certificate and cache them
    ssl_stapling on;
    ssl_stapling_verify on;

    ## verify chain of trust of OCSP response using Root CA and Intermediate certs
    #ssl_trusted_certificate /path/to/root_CA_cert_plus_intermediates;

    resolver dns9.yourdomain.com dns10.yourdomain.com;

    server_name yourdomain.com www.yourdomain.com;

    location ^~ /.well-known/acme-challenge {
        alias /usr/share/nginx/ssl/.well-known/acme-challenge;
        try_files $uri =404;
    }

    location / {
        proxy_pass  http://127.0.0.1:8080;
    }
}
```  

如以上,我将`ssl_certificate`和`ssl_certificate_key`的路径改成了上面生成的文件路径,其中`ssl_dhparam`需要另外生成,这个文件不针对某一个域名,所以可以针对某一个nginx生成一个就好,不需要对不同的域名配置.执行命令:  

```  
[root@iZwz92us1dza0axtrwza0bZ conf.d]# mkdir /etc/nginx/ssl
[root@iZwz92us1dza0axtrwza0bZ conf.d]# openssl dhparam -out /etc/nginx/ssl/dhparam.pem 2048
```  

然后将`ssl_dhparam`的路径设置成`/etc/nginx/ssl/dhparam.pem`就好.  
 `ssl_trusted_certificate`我这里没有设置,可以直接去掉就好.  
接着就是设置`resolver`了,这里指定的是你的域名DNS解析服务器,我的域名解析服务器是阿里云的,直接在域名管理控制台找到设置便可.  


最后就是将自己之前设置的自定义路径转发放在后面.到这里,我们的ssl认证就基本完成了.  
重新加载nginx配置文件:  

```  
[root@iZwz92us1dza0axtrwza0bZ conf.d]# service nginx reload
```  

打开浏览器访问域名,看看是不是已经加上小绿锁了.  

![ssl-2](http://rustic.img-cn-qingdao.aliyuncs.com/ssl/ssl-2.png)  

## 测试服务器SSL安全性  

访问:[Qualys SSL Labs](https://www.ssllabs.com/ssltest/index.html)查看证书安全等级.  

![ssl-3](http://rustic.img-cn-qingdao.aliyuncs.com/ssl/ssl-3.png)  

## 自动化定期更新证书  

由于`Let's Encrypt`的免费证书有效期自由90天,所以我们需要定期去重新生成证书,`Let's Encrypt`有很方便的定期重新生成证书的脚本,我们只要调用系统定时器按时更新即可.  

在centos下,我们可以增加`crontab`定时器来更新证书,执行命令:  

```  
[root@iZwz92us1dza0axtrwza0bZ conf.d]# crontab -u root -e
```  

编辑该用户下的定时任务:  

```  
0 0 1 * * /usr/bin/letsencrypt renew
```  

每个月1号执行命令`/usr/bin/letsencrypt renew`更新证书.  
crontab定时器参考:<http://www.ha97.com/910.html>  

好了,到这里我们就已经完成了网站HTTPS的升级了.好好享受小绿锁吧～

## 感谢
