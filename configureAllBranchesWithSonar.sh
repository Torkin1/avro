#! /bin/sh

cp -r ../.github --remove-destination .

for remote in git branch -r
do 
    git checkout  --track $remote
    cp -r ../.github --remove-destination .github
    git add .github
    git commit -a -m "add sonarcloud config to all repo"
    git push origin $remote
done