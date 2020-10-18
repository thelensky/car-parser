
git colone https://github.com/thelensky/car-parser/
cd car-parser
sbt run

#Add Car to db POST /api/car
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"gosNumber": "A000AA", "mark":"Reno", "year": 2020, "color":"yellow"}' \
  http://localhost:9000/api/car

#List all cars GET /api/cars
curl http://localhost:9000/api/cars

#Static data about db GET /api/statistics
curl http://localhost:9000/api/statistics

#Remove Car from db DELETE /api/car/:id
curl -X DELETE http://localhost:9000/api/car/1
