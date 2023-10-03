# json04j

This is the java implementation of [json0](https://github.com/ottypes/json0) based on Jackson.

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


## How to test?

Collect the test data of the original [json0](https://github.com/ottypes/json0) lib for testing.
Because the file to be tested is large,
you can click here to [download it](https://github.com/ziqiangai/json04j/releases/download/v1.1.0-beta/resources.zip).
Modify the parameter values after decompressing it locally.

https://github.com/ziqiangai/json04j/blob/9a42148dbc6c7434f6c40b8bf86336ce01e8a147/src/test/java/json0/Const.java#L5


