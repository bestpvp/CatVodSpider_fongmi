rm -rf ./custom_spider.jar

rm -rf ./Smali_classes

java -jar ./3rd/baksmali-2.5.2.jar d ../app/build/intermediates/dex/release/minifyReleaseWithR8/classes.dex -o ./Smali_classes

rm -rf  ./spider.jar/smali/com/github/catvod/spider
rm -rf  ./spider.jar/smali/com/github/catvod/parser
rm -rf  ./spider.jar/smali/com/github/catvod/js

if [ ! -d ./spider.jar/smali/com/github/catvod ]; then
  mkdir -p ./spider.jar/smali/com/github/catvod
fi

#if [ "$1" == "ec" ]; then
#  java -Dfile.encoding=utf-8 -jar ./3rd/oss.jar ./Smali_classes
#fi

mv ./Smali_classes/com/github/catvod/spider ./spider.jar/smali/com/github/catvod/
mv ./Smali_classes/com/github/catvod/parser ./spider.jar/smali/com/github/catvod/
mv ./Smali_classes/com/github/catvod/js ./spider.jar/smali/com/github/catvod/

rm -rf ./Smali_classes

java -jar ./3rd/apktool_2.4.1.jar b ./spider.jar -c

timestamp=$(date +%Y%m%d-%H%M%S)

mv ./spider.jar/dist/dex.jar ./custom_spider

md5 -q ./custom_spider > ./custom_spider.md5

# 删除 spider.jar 中的 com/github/catvod/spider 和 com/github/catvod/parser 目录
rm -rf ./spider.jar/smali/com/github/catvod/spider
rm -rf ./spider.jar/smali/com/github/catvod/parser

# 删除 spider.jar 中的 build 和 dist 目录
rm -rf ./spider.jar/build
rm -rf ./spider.jar/dist
