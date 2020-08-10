while sleep 0.1; 
  do 
    curl "$1/basicop/multiply?n1=200&n2=10"; 
    echo ""; 
  done;
