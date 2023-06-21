const mongoose = require('mongoose');

const mongoConnection = async () => {
    mongoose.connect("mongodb+srv://AmitRaj:AmitRaj123@cluster0.yog8h5r.mongodb.net/test", { useNewUrlParser: true, useUnifiedTopology: true }).then(() => {
        console.log("Successfully");
    });
}

const mongoCloseConnection = async () => {
    mongoose.connection.close();
};

module.exports = { mongoConnection, mongoCloseConnection };

