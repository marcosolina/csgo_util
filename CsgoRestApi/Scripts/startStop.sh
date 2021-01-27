#!/bin/bash


APP_FOLDER=/opt/csgoutil/
APP_JAR=CsgoRestApi.jar
APP_NOHUP=${APP_FOLDER}CsgoRestApi.log
APP_PID_FILE=${APP_FOLDER}CsgoRestApi.pid

case "$1" in
start)
   cd $APP_FOLDER
   nohup java -jar $APP_JAR >> $APP_NOHUP 2>&1&
   echo $!>$APP_PID_FILE
   chmod 775 $APP_PID_FILE
   ;;
stop)
   kill `cat $APP_PID_FILE`
   rm $APP_PID_FILE
   ;;
restart)
   $0 stop
   $0 start
   ;;
status)
   if [ -e $APP_PID_FILE ]; then
      echo CsgoRestApi is running, pid=cat $APP_PID_FILE
   else
      echo CsgoRestApi is NOT running
      exit 1
   fi
   ;;
*)
   echo "Usage: $0 {start|stop|status|restart}"
esac

exit 0
