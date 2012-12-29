#!/bin/sh
# Push javadoc files to a website hosted by github
# <http://pages.github.com/>.
# Before executing this script, generate the javadoc files into doc
#
# David Fraser - http://dephraser.net/blog/2012/04/16/ant-javadox-and-github/

# find out the current branch so we know where to switch back
OLD_BRANCH=`git branch --no-color | sed -e '/^[^*]/d' -e 's/* \(.*\)/\1/'`

git checkout gh-pages || exit $?

# Clear out the old files: (files which will be served)
rm -rf javadoc/* 

# Replace them with new files and commit them:
cp -pr doc/* javadoc \
&& git add javadoc \
&& git commit -a -m "generated javadoc"

#Remove the generated doc
rm -rf doc/

git push origin gh-pages || exit $?

# Switch back to the old branch
git checkout $OLD_BRANCH || exit $?
