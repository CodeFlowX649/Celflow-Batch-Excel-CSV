# Celflow - 批量处理 Excel / CSV 数据的高效工具

🚀 **Celflow** 是一个高效、稳定的 **CLI（命令行）工具**，用于批量处理 Excel 和 CSV 数据。适用于开发者、数据分析师和运营人员，极大提升数据整理效率。

---

## ✨ 主要功能
- ✅ **批量处理 CSV/Excel**：支持多个文件同时处理
- ✅ **格式转换**：Excel（`.xlsx`） ⇄ CSV（`.csv`）
- ✅ **数据去重**：自动去除重复行
- ✅ **字段筛选**：按指定列筛选数据
- ✅ **数据合并**：多个 CSV / Excel 文件合并
- ✅ **自定义规则处理**（开发中 🚀）
- ✅ **高效 & 稳定**：支持**大文件**处理，优化性能

---

## 📌 安装

### 方式 1：直接下载源码文件（推荐）
📥 [点击下载最新版本](https://github.com/CodeFlowX649/Celflow-Batch-Excel-CSV/tree/master)

### 方式 2：使用 JAR 包（需安装 Java）
📥 [点击下载最新版本](https://github.com/CodeFlowX649/Celflow-Batch-Excel-CSV/tree/main)


# 运行工具(命令查看)
```bash
java -jar celflow.jar --help

```


### 方式 3：使用源码运行（适合开发者）
```bash
java -jar target/celflow.jar --help
```

---

## 🚀 快速开始

```bash
java -jar Celflow.jar -csv_merge                                                --合并csv
java -jar Celflow.jar -csv_removal                                              --csv 相同数据行去重/删除全是空值的行
java -jar Celflow.jar -csv_format yyyy-MM-dd yyyy/MM/dd Workmonth               --csv 相同数据行去重/删除全是空值的行
java -jar Celflow.jar -csv_filter Memo                                          --csv 按某一列的值拆分成多个 CSV 文件（比如按“部门”拆分）
java -jar Celflow.jar -csv_columncounter Memo                                   --csv 统计某列的唯一值个数（如不同的“城市”数量）
java -jar Celflow.jar -csv_to_execl                                             --csv转execl
java -jar Celflow.jar -excel_to_csv                                             --execl转csv
```

---

## 📦 运行示例
### **输入（data.csv）**
| ID  | Name  | Age |
|-----|------|-----|
| 1   | 张三 | 25  |
| 2   | 李四 | 30  |
| 3   | 张三 | 25  |

### **去重后（cleaned.csv）**
| ID  | Name  | Age |
|-----|------|-----|
| 1   | 张三 | 25  |
| 2   | 李四 | 30  |

---

## 🛠️ 未来计划
- [ ] **GUI 版**（提供可视化界面）
- [ ] **API 版**（可用于 SaaS 服务）
- [ ] **数据分析功能**（统计、可视化）

---

## 🤝 贡献
欢迎 Star ⭐ 和 PR 贡献代码！

```bash
git clone https://github.com/CodeFlowX649/Celflow.git
```

如有问题，请在 [Issues](https://github.com/CodeFlowX649/Celflow-Batch-Excel-CSV/issues) 反馈！

---

## 📄 License
**Celflow** 遵循 `Apache 2.0` 许可证，免费供个人和企业使用。
