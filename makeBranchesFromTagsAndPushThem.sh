#! /bin/sh

RELEASES=$(git tag)

for RELEASE in $RELEASES
do
    git branch "branch-$RELEASE" $RELEASE
done
git push --all -u
