#! /bin/sh

for remote in $(git branch -r)
do 
    git checkout  --track $remote
    cp -r ../.github --remove-destination .
    git add .github
    git commit -a -m "add sonarcloud config to all repo"
    git push origin $remote
done