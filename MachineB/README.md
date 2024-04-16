# MachineB Task Scheduler

## Description

This program, `MachineB.java`, implements a greedy algorithm to schedule jobs on a machine. The algorithm schedules tasks in chronological order, starting from the first time slot. It prioritizes tasks based on their start time, scheduling the shortest task if multiple tasks start at the same time slot.

## Optimization
The greedy algorithm implemented in [MachineA.java](https://github.com/luigi-dv/ad-machinea/blob/master/src/MachineA.java) may not always schedule the maximum number of tasks. To address this issue, we can design an algorithm which
is guaranteed to always schedule the maximum possible number of jobs. The input will be in the same format as before, but now you should out- put only a single integer m, the maximum number of jobs that can be scheduled.

## Algorithm Overview

The greedy algorithm follows these steps:
- Start from the first time slot and proceed chronologically.
- If the machine is unused at a time slot, schedule a task from the set of tasks if one exists.
- If multiple tasks start at the same time slot, schedule the shortest task.

## Input Format

The program accepts a set of tasks in the following format:
- The first line of input contains an integer `k` (< 10^7), representing the number of tasks.
- Each of the following `k` lines contains a pair of integers `a` and `b`, where `a ≤ b ≤ 10^7`, representing the start and finish times of each task.

## Output Format

The output of the program consists of a sequence of lines, each containing a pair of integers `a` and `b`, representing the start and finish times of a scheduled task. The scheduled tasks are listed in chronological order.

## Usage

To run the program, compile `MachineA.java` and execute the compiled class file.

```
javac MachineA.java
java MachineA
```

Enter the filename containing the tasks when prompted. The program will output the scheduled tasks based on the described algorithm.

## Example

Input:
```
4
1 3
2 5
4 7
6 9
```

Output:
```
1 3
4 7
```
