# if test.sh gives <error> permission denied, run the commented line first in the terminal, then run the test.sh file
# $ chmod +x test.sh
docker build -t democracia_test . -f test.dockerfile
docker run democracia_test
