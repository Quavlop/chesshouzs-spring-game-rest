generate-go-rpc:
	protoc --proto_path=./src/main/java/com/chesshouzs/server/service/rpc/proto \
		--java_out=./src/main/java \
		--plugin=protoc-gen-grpc-java=/usr/local/bin/protoc-gen-grpc-java \
		--grpc-java_out=./src/main/java \
		./src/main/java/com/chesshouzs/server/service/rpc/proto/match_service.proto
	protoc --proto_path=./src/main/java/com/chesshouzs/server/service/rpc/proto \
		--java_out=./src/main/java \
		--plugin=protoc-gen-grpc-java=/usr/local/bin/protoc-gen-grpc-java \
		--grpc-java_out=./src/main/java \
		./src/main/java/com/chesshouzs/server/service/rpc/proto/match_service_model.proto
