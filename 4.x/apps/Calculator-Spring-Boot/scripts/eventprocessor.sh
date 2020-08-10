while sleep 0.1; 
  do 
    curl -s -X POST $1/eventprocessor/submit -H 'Content-Type: application/json' -d '[
	{ 
		"identifier": "x1", 
		"expression": "2 %2B 3 %2B 4" 
	},	
	{ 
		"identifier": "x2", 
		"expression": "3 - 2" 
	}, 
	{ 
		"identifier": "x3", 
		"expression": "3 %2A 2"
	},
	{ 
		"identifier": "x4", 
		"expression": "4 / 2"
	},	
	{ 
		"identifier": "x5", 
		"expression": "1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4"
	},
	{ 
		"identifier": "x6", 
		"expression": "12 / 4 / 3"
	},
	{ 
		"identifier": "x7", 
		"expression": "3 %2A 7 %2A 6 %2A 2"
	},
	{ 
		"identifier": "x8", 
		"expression": "(12 - 1) %2A 6 / 3"
	},
        {
                "identifier": "x9",
                "expression": "(1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4)"
        },
        {
                "identifier": "x10",
                "expression": "(1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4) + (1 %2B 2 %2B 3 %2B 4 %2B 33 - 6 - 7 * 3 %2B 12 / 4)"
        }
    ]'
    echo "";
  done ;
