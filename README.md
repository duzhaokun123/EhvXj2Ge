# EhvXj2Ge

EhViewer 导出 db 文件 xiaojie 版本转通用版本

其实就是把版本改成 4, 因为 xiaojie 没改通用部分所以这样做其他 EhViewer 能接受

## 很明显这种简单的操作浏览器就可以完成

将xiaojie版导出的数据导入到其他版本

1. 在 xiaojie 版的设置-高级中导出数据，记住文件名`2022-xxxx.db`

2. 访问 https://sql.js.org/examples/GUI/

3. 点击 Load an SQLite database file, 选择刚刚导出的 db 文件

4. 在上方输入`PRAGMA user_version = 4`，点击 Execute

5. 点击 Save the db, 保存修改后的db文件

6. 在其他版本的设置-高级中导入数据

来源 https://t.me/ehviewer/2126046
