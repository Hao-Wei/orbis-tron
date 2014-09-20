./runserver.sh 2>/dev/null &
sleep 2
./runclient.sh 2>/dev/null $@
exit 0
