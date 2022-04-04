API_URL = http://localhost:8080

default:
	@curl --verbose \
		--location \
		--header 'Accept: application/json' \
		--header 'Content-Type: application/json' \
		--data '{"firstName": "John", "lastName":"Smith"}' \
		$(API_URL)/students

