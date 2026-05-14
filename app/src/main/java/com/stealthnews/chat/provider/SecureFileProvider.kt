package com.stealthnews.chat.provider

import androidx.core.content.FileProvider

class SecureFileProvider : FileProvider() {
    // FileProvider 用于安全地分享文件
    // 配置已在 @xml/file_paths 中定义
}
