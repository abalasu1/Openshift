while sleep 0.1; 
  do 
    curl "http://$1/basicop/subtract?n1=200&n2=50"
    echo ""; 
  done ;
