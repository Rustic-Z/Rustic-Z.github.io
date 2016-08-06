---
layout: page
title: Rustic_Z 's Blog
tagline: 南柯一梦
---
{% include JB/setup %}

<!-- ## Google Doodle -- first-day-of-summer-2016   -->
<!-- ![google doodles](http://rustic.img-cn-qingdao.aliyuncs.com/gif/summer-solstice-strawberry-moon-5156509353771008-hp2x.gif@600w)   -->
<!-- <img src="http://rustic.img-cn-qingdao.aliyuncs.com/beautifulPhoto/mmexport1467998694280.jpg@600w" alt="纪念"/> -->

<head><style>img{max-width:320px !important;}</style></head><!-- 屏幕自适应设置 -->
<img src="http://rustic.img-cn-qingdao.aliyuncs.com/beautifulPhoto/mmexport1467998694280.jpg@600w" alt="纪念"/>
<!-- ![纪念](http://rustic.img-cn-qingdao.aliyuncs.com/beautifulPhoto/mmexport1467998694280.jpg@600w)   -->

## Learning...   

<ul class="posts">
  {% for post in site.posts %}
    <li><span>{{ post.date | date_to_string }}</span> &raquo; {{ post.draft_flag }} <a href="{{ BASE_PATH }}{{ post.url }}">{{ post.title }}</a></li>
  {% endfor %}
</ul>
