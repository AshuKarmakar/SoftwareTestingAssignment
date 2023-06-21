const mongoose = require('mongoose');
const { Schema, model } = mongoose;

const entitySchema = new Schema({
    type: {
        required: true,
        type: String
    },
    data: {
        required: true,
        type: String
    }
});

const Entity = model('Entity', entitySchema);
module.exports = Entity;