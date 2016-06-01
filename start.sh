#!/bin/bash
cd /root/rustic-z.github.io/
git pull origin master
echo "pull code on github..."
sleep 5
tid=$(ps -ef |grep jekyll |grep -v 'grep'|awk '{print $2}')
for i in ${tid[@]};do
    echo $i
    kill -9 $i
    echo "KILLING JEKYLL"
    sleep 5
done
rm -rf _site/*
jekyll serve -H 114.215.83.238 -B
