## Monte Carlo Tree Search (MCTS)

This project implements the **Monte Carlo Tree Search (MCTS)** algorithm in Java for two games:
- **Tic-Tac-Toe**
- **2048**

The goal is to demonstrate how MCTS can be applied to both games through simulation-based decision making.

---

## 🚀 Getting Started
Make sure you have [Maven](https://maven.apache.org/) and Java installed.

### 🔧 Build the project
```bash
cd MCTS
mvn compile
```

### Run Tic-Tac-Toe
```bash
mvn exec:java -Dexec.mainClass="com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.GameLauncher"
```


### Run 2048
```bash
mvn exec:java -Dexec.mainClass="com.phasmidsoftware.dsaipg.projects.mcts.game2048.GameLauncher"
```

### Report & Analysis
For detailed discussion, benchmarking results, and simulation analysis,
please refer to the report file in this repository.
