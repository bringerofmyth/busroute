#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Keep the pwd in mind!
# Example: RUN="java -jar $DIR/target/magic.jar"
DATA_FILE=$2
NAME="busroute-0.0.1-SNAPSHOT.jar"
RUN="java -DrouteMap=$DATA_FILE -jar build/libs/"$NAME

PIDFILE=/tmp/$NAME.pid
LOGFILE=logs/app.log

start() {
    if [ -f $PIDFILE ]; then
        if kill -0 $(cat $PIDFILE); then
            echo 'Service already running' >&2
            return 1
        else
            rm -f $PIDFILE
        fi
    fi
    echo 'Service starting, please wait'
    local CMD="$RUN $DATA_FILE &> \"$LOGFILE\" & echo \$!"
    sh -c "$CMD" > $PIDFILE

    tail -f $LOGFILE | while read LOGLINE
    do
       [[ "${LOGLINE}" == *"Started BusRouteApplication"* ]] && pkill -P $$ tail && tail -10 $LOGFILE && echo 'Service started'
    done

}

stop() {
    if [ ! -f $PIDFILE ] || ! kill -0 $(cat $PIDFILE); then
        echo 'Service not running' >&2
        return 1
    fi
    kill -15 $(cat $PIDFILE) && rm -f $PIDFILE
    tail -20 $LOGFILE
    echo 'Service stopped, bye!'
}

case $1 in
    start)
        start
        ;;
    stop)
        stop
        ;;
    block)
        start
        sleep infinity
        ;;
    *)
        echo "Usage: $0 {start|stop|block} DATA_FILE"
esac