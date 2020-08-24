while sleep 0.1; 
  do 
    curl "$1/basicop/divide?n1=200&n2=100"; 
    echo ""; 
  done;
