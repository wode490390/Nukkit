#!/bin/sh
if [ -d "target" ]; then
	echo "清理 target 目录"
	rm -rf target
fi
echo "生成中..."
mkdir target
javadoc -locale zh_CN -charset utf-8 -encoding utf-8 -docencoding utf-8 -sourcepath "src/main/java" -d target -overview "src/main/javadoc/overview.html" -docfilessubdirs -use -doctitle "Nukkit 中文文档" -linkoffline -windowtitle "Nukkit 中文文档" -subpackages cn.nukkit
echo "生成结束"
