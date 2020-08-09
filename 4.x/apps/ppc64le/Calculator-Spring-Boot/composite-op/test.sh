COUNTER=0
while [  $COUNTER -lt 100 ];
  do 
    curl -s "http://$1/compositeop/mean?n1=100&n2=20&n3=20&n4=20&n5=20";
    echo
    echo "=MEAN (100, 20, 20, 20, 20) = 36";
    
    curl -s "http://$1/compositeop/square?n1=10";  
    echo
    echo "=SQUARE (10) = 100";
    
    curl -s "http://$1/compositeop/cube?n1=10";  
    echo
    echo "=CUBE (10) = 1000";

    curl -s "http://$1/compositeop/eval?expr=(10*3)%2B5" | jq -r '.result'; 
    echo "=EVALUATE ((10*3)+5) = 35";
    
    curl -s "http://$1/compositeop/eval?expr=(5*6)%2B(3*2)-6" | jq -r '.result';
    echo "=EVALUATE ((5*6)+(3*2)-6) = 30";

    curl -s "http://$1/compositeop/evalrpnexpr?expr=5%206%20*" | jq -r '.result';
    echo "=EVALUATERPN (5 6 *) = 30";

    curl -s "http://$1/compositeop/eval?expr=(10*10)" | jq -r '.result';
    echo "=EVALUATE (10*10) = 100"; 

    curl -s "http://$1/compositeop/eval?expr=10*10*10" | jq -r '.result';
    echo "=EVALUATE (10*10*10) = 1000"; 

    curl -s "http://$1/compositeop/eval?expr=1%20%2B%202%20*%203" | jq -r '.result';
    echo "=EVALUATE (1 + 2 * 3) = 7"; 

    curl -s "http://$1/compositeop/eval?expr=(1%20%2B%202)%20*%203" | jq -r '.result';
    echo "=EVALUATE ((1 + 2) * 3) = 9";            

    curl -s "http://$1/compositeop/eval?expr=(12%20%2F%203%20%2F%202)" | jq -r '.result';
    echo "=EVALUATE (12 / 3 / 2) = 2";

    curl -s "http://$1/compositeop/eval?expr=(12%20%2F%203%20%2F%200)" | jq -r '.result';
    echo "=EVALUATE (12 / 3 / 0) = NaN";    

    let COUNTER=COUNTER+1 
  done
