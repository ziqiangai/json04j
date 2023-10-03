const {type, text0T} = require('./index');
const fs = require('fs');
const path = require('path');

function readAllJsonFiles(data, getArg, execute, finished) {
    const directory = `E:\\code\\json04j\\src\\test\\resources\\${data.module}\\${data.method}`;
    // 读取目录中的所有文件
    const files = fs.readdirSync(directory);

    // 过滤出JSON文件
    const jsonFiles = files.filter(file => path.extname(file) === '.json');

    jsonFiles.forEach(file => {
        const filePath = path.join(directory, file);
        const fileContent = fs.readFileSync(filePath, 'utf8');
        const fileData = JSON.parse(fileContent);
        fileData.forEach(f => {
            const args = getArg(f);
            if (args) {
                // 获取当前时间
                const startTime = new Date();
                execute(args);
                data.testCount++;
                // 获取当前时间
                const endTime = new Date();
                const executionTime = endTime - startTime;
                data.take += executionTime;
            }
        })
    });

    finished(data);
}


const onFinished = (data) => {
    console.log(`| ${data.name}  | ${data.method}             | ${data.testCount}        | ${data.take}     ms  |`)
};

console.log(`
| Module | Function           | Test Count | Test Time |
|--------|--------------------|------------|----------|
`);
// 读取所有JSON文件
readAllJsonFiles({
    module: 'json0',
    name: 'json0',
    method: 'invert',
    testCount: 0,
    take: 0,
}, (d) => {
    const op = d.op;
    return [op];
}, (args) => {
    type.invert(args[0]);

}, onFinished);
readAllJsonFiles({
    module: 'json0',
    name: 'json0',
    method: 'compose',
    testCount: 0,
    take: 0,
}, (d) => {
    const op1 = d.op1;
    const op2 = d.op2;
    return [op1, op2];
}, (args) => {
    type.compose(args[0], args[1]);

}, onFinished);
readAllJsonFiles({
    module: 'json0',
    name: 'json0',
    method: 'apply',
    testCount: 0,
    take: 0,
}, (d) => {
    const snapshot = d.snapshot;
    const op = d.op;
    return [snapshot, op];
}, (args) => {
    type.apply(args[0], args[1]);

}, onFinished);

readAllJsonFiles({
    module: 'json0',
    name: 'json0',
    method: 'transformComponent',
    testCount: 0,
    take: 0,
}, (d) => {
    const destOrigin = d.destOrigin;
    const c = d.c;
    const otherC = d.otherC;
    const type = d.type;
    return [destOrigin, c, otherC, type];
}, (args) => {
    type.transformComponent(args[0], args[1], args[2], args[3]);
}, onFinished);

readAllJsonFiles({
    module: 'json0',
    name: 'json0',
    method: 'transformX',
    testCount: 0,
    take: 0,
}, (d) => {
    const leftOp = d.leftOp;
    const rightOp = d.rightOp;
    const otherC = d.otherC;
    const type = d.type;
    if (typeof leftOp[0]?.p == 'number' || typeof rightOp[0]?.p == 'number') {
        return null;
    }
    return [leftOp, rightOp];
}, (args) => {
    if (args) {
        type.transformX(args[0], args[1]);
    }

}, onFinished);
readAllJsonFiles({
    module: 'json0',
    name: 'text0',
    method: 'transformX',
    testCount: 0,
    take: 0,
}, (d) => {
    const leftOp = d.leftOp;
    const rightOp = d.rightOp;
    const otherC = d.otherC;
    const type = d.type;
    if (typeof leftOp[0]?.p == 'number' || typeof rightOp[0]?.p == 'number') {
        return [leftOp, rightOp];
    }
    return null;
}, (args) => {
    if (args) {
        text0T.transformX(args[0], args[1]);
    }

}, onFinished);
readAllJsonFiles({
    module: 'text0',
    name: 'text0',
    method: 'invert',
    testCount: 0,
    take: 0,
}, (d) => {
    const opOrigin = d.opOrigin;
    return [opOrigin];
}, (args) => {
    if (args) {
        text0T.invert(args[0]);
    }

}, onFinished);

readAllJsonFiles({
    module: 'text0',
    name: 'text0',
    method: 'compose',
    testCount: 0,
    take: 0,
}, (d) => {
    const op1 = d.op1;
    const op2 = d.op2;
    return [op1, op2];
}, (args) => {
    if (args) {
        text0T.compose(args[0], args[1]);
    }

}, onFinished);

readAllJsonFiles({
    module: 'text0',
    name: 'text0',
    method: 'transformComponent',
    testCount: 0,
    take: 0,
}, (d) => {
    const destOrigin = d.destOrigin;
    const c = d.c;
    const otherC = d.otherC;
    const side = d.side;
    return [destOrigin, c, otherC, side];
}, (args) => {
    if (args) {
        text0T._tc(args[0], args[1], args[2], args[3]);
    }

}, onFinished);


readAllJsonFiles({
    module: 'text0',
    name: 'text0',
    method: 'apply',
    testCount: 0,
    take: 0,
}, (d) => {
    const snapshotOrigin = d.snapshotOrigin;
    const op = d.op;
    return [snapshotOrigin, op];
}, (args) => {
    if (args) {
        text0T.apply(args[0], args[1]);
    }

}, onFinished);

