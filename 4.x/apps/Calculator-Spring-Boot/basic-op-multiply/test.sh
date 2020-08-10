while sleep 0.1; 
  do 
    curl "http://$1/basicop/multiply?n1=4&n2=9"
    echo ""; 
  done ;
