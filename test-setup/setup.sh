kind create cluster \
  --name chart-testing \
  --config=./kind-config.yaml

./setup-image-registry.sh
./setup-nginx-ingress-controller.sh