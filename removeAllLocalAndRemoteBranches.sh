#! /bin/sh

git branch | grep -v "main" | xargs git branch -D
git branch -r | grep origin/ | grep -v 'master$' | grep -v HEAD| cut -d/ -f2 | while read line; do git push origin :$line; done;