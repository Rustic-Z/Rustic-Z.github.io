---
layout: page
title: Rustic_Z 's Blog
tagline: 南柯一梦
---
{% include JB/setup %}

## Latest Posts

<ul class="posts">
  {% for post in site.posts %}
    <li><span>{{ post.date | date_to_string }}</span> &raquo; {{ post.draft_flag }} <a href="{{ BASE_PATH }}{{ post.url }}">{{ post.title }}</a></li>
  {% endfor %}
</ul>

<ul class="imgs">
  <!-- <img src="http://rustic.img-cn-qingdao.aliyuncs.com/myCollege/IMG_20160610_143824.jpg@888w"  alt="上犹陡水湖 绿水青山" /> -->
  <h2>我爱绿水</h2>
  ![上犹陡水湖 绿水青山](http://rustic.img-cn-qingdao.aliyuncs.com/myCollege/IMG_20160610_143824.jpg@888w)
</ul>
