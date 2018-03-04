#!/bin/bash
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
jekyll serve -H 127.0.0.1 -B
