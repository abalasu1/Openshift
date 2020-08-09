while sleep 0.1; 
  do 
    curl "$1/basicop/add?n1=100&n2=200"
    echo ""; 
  done ;
