print('@@@@@@@@@ MongoDB initialization started @@@@@@@@@');

// db = db.getSiblingDB('inventory_db');
// db.createUser(
//     {
//         user: 'test',
//         pwd: 'test',
//         roles: [{ role: 'readWrite', db: 'inventory_db' }],
//     },
// );
// db.createCollection('products');

// db = db.getSiblingDB('users_db');
// db.createUser(
//     {
//         user: 'test',
//         pwd: 'test',
//         roles: [{ role: 'readWrite', db: 'users_db' }],
//     },
// );
// db.createCollection('users');

/*************/
/* ORDERS-DB */
/*************/

// Create the Database
db = db.getSiblingDB('orders-db');
// Create the Applicative User
db.createUser(
    {
        user: 'test',
        pwd: 'test',
        roles: [{ role: 'readWrite', db: 'orders-db' }],
    },
);
// Create the collection ORDERS-EVENTS-STORE
db.createCollection('order-events');
// Create a Unique Index on the EVENT_TYPE-LRA_ID pair -- To avoid SAGA's events duplicates (enforce idempotency)
db['order-events'].createIndex( { eventType: 1, "eventBody.lraId": 1 }, { unique: true } )

print('@@@@@@@@@ MongoDB initialization ended @@@@@@@@@');