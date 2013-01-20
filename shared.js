var kv = {};

exports.get = function (index) {
  return kv[index];
};

exports.set = function (index, value) {
  kv[index] = value;
};
