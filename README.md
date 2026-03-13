# Toy Language Interpreter

A fully functional interpreter for a custom toy programming language, built in Java with a JavaFX GUI. Developed as part of the Advanced Programming Methods course at Babeș-Bolyai University.

## Overview

This project implements an interpreter that executes programs written in a custom toy language. It supports a rich set of language features including heap memory management, multi-threaded execution via fork statements, file I/O, and synchronization primitives — all visualized step-by-step through a JavaFX GUI.

## Features

- **Multi-threaded execution** — programs can spawn new threads via `fork`, each with its own execution stack and symbol table
- **Heap memory** with garbage collection (shared across all threads)
- **Type checking** before execution
- **Lock/Unlock synchronization** primitives for thread safety
- **File I/O** — open, read, and close files from within the toy language
- **Step-by-step GUI** — observe execution stack, symbol table, heap, output, and lock table in real time
- **18 statement types** — assignment, if, while, for, fork, heap read/write, file ops, and more
- **Program selector** with multiple built-in example programs

## Language Features

| Category    | Supported                                                                                        |
| ----------- | ------------------------------------------------------------------------------------------------ |
| Types       | `int`, `bool`, `string`, `Ref<T>`                                                                |
| Expressions | Arithmetic, logical, relational, heap read, variable                                             |
| Statements  | Variable declaration, assignment, if, while, for, fork, print, file I/O, heap write, lock/unlock |
| Memory      | Stack-allocated variables, heap-allocated references                                             |
| Concurrency | Fork (thread spawn), NewLock, Lock, Unlock                                                       |

## Example Program

```
int v;
Ref<int> a;
v = 10;
new(a, 20);
fork(
    writeHeap(a, 30);
    print(v);
    print(readHeap(a))
);
print(v);
print(readHeap(a))
```

## Architecture

The project follows a clean **MVC architecture**:

```
src/
├── controller/       # Controller — drives execution, manages thread pool
├── model/
│   ├── expression/   # Arithmetic, logical, relational, heap, variable expressions
│   ├── statement/    # All 18 statement types
│   ├── state/        # ProgramState, SymbolTable, HeapTable, LockTable, etc.
│   ├── type/         # Type system (int, bool, string, ref)
│   ├── value/        # Value wrappers
│   └── gui/          # JavaFX controllers and FXML views
├── repo/             # Repository — persists program states and logs
├── exception/        # Custom exceptions
├── data/             # Built-in example programs
└── view/             # Text UI (legacy)
```

## Tech Stack

- **Java 25**
- **JavaFX** — GUI
- **IntelliJ IDEA** — development environment

## Getting Started

### Prerequisites

- Java 25
- JavaFX SDK

### Running

1. Clone the repository
   ```bash
   git clone git@github.com:Gaboruuu/Interpreter.git
   ```
2. Open the project in IntelliJ IDEA
3. Add the JavaFX SDK to the project libraries
4. Run `Interpreter.java`

## License

This project was developed for educational purposes at Babeș-Bolyai University.
