rm -vf Key-Manager-Grpc.zip
zip -r Key-Manager-Grpc.zip *
aws s3 cp Key-Manager-Grpc.zip s3://orangetalents/