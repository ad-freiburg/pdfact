#!/bin/sh
# A script that is taken from [1] and prepended to the executable Java jar-files of this project so
# that the jar-files can be used like Unix executable. The script is used as follows:
#   cat stub.sh foo.jar > foo && chmod +x foo
#
# [1] https://coderwall.com/p/ssuaxa/how-to-make-a-jar-file-linux-executable

MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
java=java
if test -n "$JAVA_HOME"; then
    java="$JAVA_HOME/bin/java"
fi
exec "$java" $java_args -jar $MYSELF "$@"
exit 1 