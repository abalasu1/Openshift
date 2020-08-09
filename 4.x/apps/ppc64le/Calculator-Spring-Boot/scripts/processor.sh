while sleep 0.1; 
  do 
    curl -s -X POST $1/processor/calculate -H 'Content-Type: application/json' -d '{
      "x1": "2 %2B 3 %2B 4",
      "x2": "3 - 2",
      "x3": "3 * 2",
      "x4": "4 / 2",
      "x5": "1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4",
      "x6": "12 / 4 / 3"
    }' | jq '.[]  | "identifier=\(.identifier) expr=\(.output.expr) result=\(.output.value) "'
  done ;
