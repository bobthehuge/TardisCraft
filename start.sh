ROOT=$(git rev-parse --show-toplevel)

cd ${ROOT}/server/
java -jar -Xmx4G paper-1.19.4-550.jar -nogui