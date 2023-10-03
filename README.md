# json04j

This is the java implementation of [json0](https://github.com/ottypes/json0).

There are complete tests.(No statistics on serialization time)

| Module | Function           | Test Count | Test Time |
|--------|--------------------|------------|-----------|
| json0  | invert             | 32979      | 14   ms   |
| json0  | compose            | 14127      | 27   ms   |
| json0  | apply              | 136860     | 113  ms   |
| json0  | transformComponent | 58569      | 84   ms   |
| json0  | transformX         | 26918      | 95   ms   |
| text0  | transformX         | 986        | 0    ms   |
| text0  | invert             | 39203      | 5    ms   |
| text0  | compose            | 22907      | 2    ms   |
| text0  | transformComponent | 251335     | 12   ms   |
| text0  | apply              | 125208     | 592  ms   |


Test results of js under the same amount of data:


| Module | Function           | Test Count | Test Time |
|--------|--------------------|------------|-----------|
| json0  | invert             | 32979      | 37   ms   |
| json0  | compose            | 14127      | 239   ms  |
| json0  | apply              | 136860     | 711  ms   |
| json0  | transformComponent | 58569      | 821  ms   |
| json0  | transformX         | 26918      | 1009  ms  |
| text0  | transformX         | 986        | 2    ms   |
| text0  | invert             | 39203      | 16   ms   |
| text0  | compose            | 22907      | 18   ms   |
| text0  | transformComponent | 251335     | 118  ms   |
| text0  | apply              | 125208     | 254  ms   |
