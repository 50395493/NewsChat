#!/bin/bash
# AppCenter Pre-Build Script

echo "=== 开始构建伪装新闻聊天APK ==="

# 设置环境变量
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export ANDROID_HOME=/usr/local/lib/android/sdk

# 检查环境
echo "Java版本:"
java -version
echo "Gradle版本:"
./gradlew --version

# 清理构建缓存
echo "清理构建缓存..."
./gradlew clean

echo "预构建脚本执行完成"