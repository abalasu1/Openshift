while sleep 0.1; 
  do 
    curl "http://$1/basicop/divide?n1=200&n2=2"
    echo ""; 
  done ;
