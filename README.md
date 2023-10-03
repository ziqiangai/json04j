# json04j

This is the java implementation of [json0](https://github.com/ottypes/json0) based on Jackson.


## How to use

import in your `pom.xml`

```xml
<dependency>
  <groupId>io.github.ziqiangai</groupId>
  <artifactId>json0</artifactId>
  <version>1.0.2</version>
</dependency>
```

You can find how to use it in unit tests:

https://github.com/ziqiangai/json04j/blob/bcb1fdb4a0eb385237e38d3afab7bdd25cf62130/src/test/java/json0/Json0Test.java#L255-L265


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

https://github.com/Exclamation-mark/json04j/blob/d8dd8c0cc30e0a788d53aa7daff5eacc1dadd5bf/src/test/java/json0/Const.java#L5


