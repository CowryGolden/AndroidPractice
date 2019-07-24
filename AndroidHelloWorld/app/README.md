# app目录结构

```
● build：构建目录，相当于Eclipse的bin目录
● src：
    ● androidTest：安卓单元测试的目录
    ● main：
        ● java：写Java代码的地方
        ● res：资源文件
            ● drawable：图像资源
            ● layout：布局资源
            ● menu：菜单资源
            ● mipmap*：与drawable一样都是图片资源，不同尺寸设备系统会在缩放上提供一定的性能优化
            ● values：
                ● arrays.xml：定义数组资源
                ● attrs.xml：自定义控件的属性（自定义控件时用的较多）
                ● colors.xml：定义颜色资源
                ● demens.xml：定义尺寸资源
                ● strings.xml：定义字符串资源
                ● styles.xml：定义样式资源
        ● AndroidManifest.xml：系统配置文件
    ● build.gradle：Gradle构建脚本        
```