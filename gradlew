#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# If no SNAPS is set, default to local
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# The default JVM option set for gradle
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that fixed value
MAX_FD="maximum"

warn ( ) {
    echo "$*"
}

die ( ) {
    echo
    echo "$*"
    echo
    exit 1
}

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses fixed locations for the java executable
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Increase the maximum file descriptors if we can
if [ "$cygwin" = "false" -a "$darwin" = "false" -a "$nonstop" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$max" = "maximum" -o "$max" -gt "$MAX_FD_LIMIT" ] ; then
            MAX_FD_LIMIT="$MAX_FD_LIMIT"
        fi
        ulimit -n "$MAX_FD_LIMIT" >/dev/null 2>&1
    fi
fi

# Split up the JVM_OPTS and ADDITIONAL_ARGS
if [ -n "$CYGWIN" ] ; then
    cygwin=true
else
    cygwin=false
fi

# Escape an argument to safely enclose in double quotes
escape_arg() {
    echo "$1" | sed 's/\([\"]\)/\\\1/g'
}

# Execute Gradle
exec "$JAVACMD" $DEFAULT_JVM_OPTS$JAVA_OPTS $GRADLE_OPTS "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$PRGDIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"