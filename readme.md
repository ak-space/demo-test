
curl --location --request POST 'localhost:8080/api/records' \
--header 'Content-Type: application/json' \
--data-raw '{
"eventType":"SPEED",
"limit":"30",
"speed":"70",
"licensePlate": "ABC-123",
"unity": "km/h"
}'

curl --location --request GET 'localhost:8080/api/records'

curl --location --request GET 'localhost:8080/api/violations'

curl --location --request GET 'localhost:8080/api/violations/summary'

curl --location --request GET 'localhost:8080/api/violations/a7914e0d-5852-41f7-927d-a20666277c45'

curl --location --request PUT 'localhost:8080/api/violations/a7914e0d-5852-41f7-927d-a20666277c45/pay'
