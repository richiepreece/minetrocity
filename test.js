var test = { value : 5 };
var test2 = test;
test2.value = 6;

console.log(test.value + ' ' + test2.value);

var test3 = JSON.parse(JSON.stringify(test2));
test3.value = 7;

console.log(test2.value + ' ' + test3.value);