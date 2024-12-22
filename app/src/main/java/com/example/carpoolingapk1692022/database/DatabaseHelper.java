package com.example.carpoolingapk1692022.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.carpoolingapk1692022.models.Driver;
import com.example.carpoolingapk1692022.models.Ride;
import com.example.carpoolingapk1692022.models.User;
import com.example.carpoolingapk1692022.models.Review;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CarPoolDB";
    private static final int DATABASE_VERSION = 3;

    // Табела за корисници
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_NUM_RATINGS = "number_of_ratings";
    private static final String COLUMN_IS_DRIVER = "is_driver";

    // Табела за возачи
    private static final String TABLE_DRIVERS = "drivers";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CAR_MODEL = "car_model";
    private static final String COLUMN_CAR_PLATE = "car_plate";
    private static final String COLUMN_CAR_YEAR = "car_year";
    private static final String COLUMN_SEATS = "seats";
    private static final String COLUMN_PRICE_PER_KM = "price_per_km";
    private static final String COLUMN_IS_ACTIVE = "is_active";
    private static final String COLUMN_ACTIVE_TIME_START = "active_time_start";
    private static final String COLUMN_ACTIVE_TIME_END = "active_time_end";

    // Табела за возења
    private static final String TABLE_RIDES = "rides";
    private static final String COLUMN_RIDE_ID = "id";
    private static final String COLUMN_PASSENGER_ID = "passenger_id";
    private static final String COLUMN_DRIVER_ID = "driver_id";
    private static final String COLUMN_FROM_LOCATION = "from_location";
    private static final String COLUMN_TO_LOCATION = "to_location";
    private static final String COLUMN_FROM_LAT = "from_lat";
    private static final String COLUMN_FROM_LNG = "from_lng";
    private static final String COLUMN_TO_LAT = "to_lat";
    private static final String COLUMN_TO_LNG = "to_lng";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Табела за барања за возење
    private static final String TABLE_RIDE_REQUESTS = "ride_requests";
    private static final String COLUMN_REQUEST_ID = "id";
    private static final String COLUMN_REQUEST_STATUS = "status";

    // Табела за рејтинзи
    private static final String TABLE_REVIEWS = "reviews";
    private static final String COLUMN_REVIEW_ID = "id";
    private static final String COLUMN_REVIEW_TEXT = "review_text";
    private static final String COLUMN_REVIEWER_ID = "reviewer_id";
    private static final String COLUMN_REVIEWED_ID = "reviewed_id";
    private static final String COLUMN_REVIEW_RATING = "rating";
    private static final String COLUMN_REVIEW_DATE = "review_date";

    // Додадете променлива за тековен корисник
    private static int currentUserId = -1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Креирање на табел за корисници
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SURNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_RATING + " REAL DEFAULT 0,"
                + COLUMN_NUM_RATINGS + " INTEGER DEFAULT 0,"
                + COLUMN_IS_DRIVER + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Креирање на табела за возачи
        String CREATE_DRIVERS_TABLE = "CREATE TABLE " + TABLE_DRIVERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CAR_MODEL + " TEXT,"
                + COLUMN_CAR_PLATE + " TEXT UNIQUE,"
                + COLUMN_CAR_YEAR + " INTEGER,"
                + COLUMN_SEATS + " INTEGER,"
                + COLUMN_PRICE_PER_KM + " REAL,"
                + COLUMN_IS_ACTIVE + " INTEGER DEFAULT 0,"
                + COLUMN_ACTIVE_TIME_START + " TEXT,"
                + COLUMN_ACTIVE_TIME_END + " TEXT,"
                + COLUMN_RATING + " REAL DEFAULT 0,"
                + COLUMN_NUM_RATINGS + " INTEGER DEFAULT 0,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " 
                + TABLE_USERS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_DRIVERS_TABLE);

        // Креирање на табела за возења
        String CREATE_RIDES_TABLE = "CREATE TABLE " + TABLE_RIDES + "("
                + COLUMN_RIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PASSENGER_ID + " INTEGER,"
                + COLUMN_DRIVER_ID + " INTEGER,"
                + COLUMN_FROM_LOCATION + " TEXT,"
                + COLUMN_TO_LOCATION + " TEXT,"
                + COLUMN_FROM_LAT + " REAL,"
                + COLUMN_FROM_LNG + " REAL,"
                + COLUMN_TO_LAT + " REAL,"
                + COLUMN_TO_LNG + " REAL,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_PASSENGER_ID + ") REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(" + COLUMN_DRIVER_ID + ") REFERENCES " + TABLE_USERS + "(id)"
                + ")";
        db.execSQL(CREATE_RIDES_TABLE);

        // Кре��рање на табела за барања за возење
        String CREATE_RIDE_REQUESTS_TABLE = "CREATE TABLE " + TABLE_RIDE_REQUESTS + "("
                + COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PASSENGER_ID + " INTEGER,"
                + COLUMN_DRIVER_ID + " INTEGER,"
                + COLUMN_FROM_LOCATION + " TEXT,"
                + COLUMN_TO_LOCATION + " TEXT,"
                + COLUMN_FROM_LAT + " REAL,"
                + COLUMN_FROM_LNG + " REAL,"
                + COLUMN_TO_LAT + " REAL,"
                + COLUMN_TO_LNG + " REAL,"
                + COLUMN_REQUEST_STATUS + " TEXT,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_PASSENGER_ID + ") REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(" + COLUMN_DRIVER_ID + ") REFERENCES " + TABLE_USERS + "(id)"
                + ")";
        db.execSQL(CREATE_RIDE_REQUESTS_TABLE);

        // Креирање на табела за рејтинзи
        String CREATE_REVIEWS_TABLE = "CREATE TABLE " + TABLE_REVIEWS + "("
                + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REVIEW_TEXT + " TEXT,"
                + COLUMN_REVIEWER_ID + " INTEGER,"
                + COLUMN_REVIEWED_ID + " INTEGER,"
                + COLUMN_REVIEW_RATING + " REAL,"
                + COLUMN_REVIEW_DATE + " INTEGER,"
                + COLUMN_RIDE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_REVIEWER_ID + ") REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(" + COLUMN_REVIEWED_ID + ") REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(" + COLUMN_RIDE_ID + ") REFERENCES " + TABLE_RIDES + "(id)"
                + ")";
        db.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Бришење на постоечките табели
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Креирање на нови табели
        onCreate(db);
    }

    // Метод за додавање нов корисник
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_SURNAME, user.getSurname());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_IS_DRIVER, user.isDriver() ? 1 : 0);

        long userId = db.insert(TABLE_USERS, null, values);
        db.close();

        return userId;
    }

    // Метод за додавање нов возач
    public boolean addDriver(Driver driver, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CAR_MODEL, driver.getCarModel());
        values.put(COLUMN_CAR_PLATE, driver.getCarPlate());
        values.put(COLUMN_CAR_YEAR, driver.getCarYear());
        values.put(COLUMN_SEATS, driver.getSeats());
        values.put(COLUMN_PRICE_PER_KM, driver.getPricePerKm());

        long result = db.insert(TABLE_DRIVERS, null, values);
        db.close();

        return result != -1;
    }

    // Метод за проверка на најава
    public User checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] columns = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_SURNAME,
            COLUMN_EMAIL,
            COLUMN_IS_DRIVER,
            COLUMN_RATING,
            COLUMN_NUM_RATINGS
        };

        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setDriver(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DRIVER)) == 1);
            
            if (user.isDriver()) {
                // Ако е возач, земи ги и деталите за возилот
                user = getDriverDetails(user.getId());
            }
        }

        cursor.close();
        db.close();
        return user;
    }

    // Помошен метод за земање детали за возач
    private Driver getDriverDetails(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Driver driver = null;

        String query = "SELECT u.*, d.* FROM " + TABLE_USERS + " u "
                + "JOIN " + TABLE_DRIVERS + " d ON u.id = d.user_id "
                + "WHERE u.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            driver = new Driver();
            driver.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            driver.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            driver.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)));
            driver.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            driver.setCarModel(cursor.getString(cursor.getColumnIndex(COLUMN_CAR_MODEL)));
            driver.setCarPlate(cursor.getString(cursor.getColumnIndex(COLUMN_CAR_PLATE)));
            driver.setCarYear(cursor.getInt(cursor.getColumnIndex(COLUMN_CAR_YEAR)));
            driver.setSeats(cursor.getInt(cursor.getColumnIndex(COLUMN_SEATS)));
            driver.setPricePerKm(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PER_KM)));
            driver.setActive(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ACTIVE)) == 1);
        }

        cursor.close();
        return driver;
    }

    // Метод за земање на корисник по ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] columns = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_SURNAME,
            COLUMN_EMAIL,
            COLUMN_IS_DRIVER,
            COLUMN_RATING,
            COLUMN_NUM_RATINGS
        };

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setDriver(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DRIVER)) == 1);
            
            if (user.isDriver()) {
                user = getDriverDetails(user.getId());
            }
        }

        cursor.close();
        db.close();
        return user;
    }

    // Метод за земање на возач по ID
    public Driver getDriverById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Driver driver = null;

        String query = "SELECT u.*, d.* FROM " + TABLE_USERS + " u "
                + "JOIN " + TABLE_DRIVERS + " d ON u.id = d.user_id "
                + "WHERE u.id = ? AND u.is_driver = 1";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            driver = new Driver();
            driver.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            driver.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            driver.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)));
            driver.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            driver.setCarModel(cursor.getString(cursor.getColumnIndex(COLUMN_CAR_MODEL)));
            driver.setCarPlate(cursor.getString(cursor.getColumnIndex(COLUMN_CAR_PLATE)));
            driver.setCarYear(cursor.getInt(cursor.getColumnIndex(COLUMN_CAR_YEAR)));
            driver.setSeats(cursor.getInt(cursor.getColumnIndex(COLUMN_SEATS)));
            driver.setPricePerKm(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PER_KM)));
            driver.setActive(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ACTIVE)) == 1);
            
            // Додадете ги и овие полиња
            double rating = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING));
            int numRatings = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_RATINGS));
            driver.setRating(rating);
            driver.setNumberOfRatings(numRatings);
            
            cursor.close();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return driver;
    }

    // Метод за листање на сите активни возачи
    public List<Driver> getActiveDrivers() {
        List<Driver> activeDrivers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u.*, d.* FROM " + TABLE_USERS + " u "
                + "JOIN " + TABLE_DRIVERS + " d ON u.id = d.user_id "
                + "WHERE u.is_driver = 1 AND d.is_active = 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Driver driver = new Driver();
                driver.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                driver.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                driver.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)));
                driver.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                driver.setCarModel(cursor.getString(cursor.getColumnIndex(COLUMN_CAR_MODEL)));
                driver.setCarPlate(cursor.getString(cursor.getColumnIndex(COLUMN_CAR_PLATE)));
                driver.setCarYear(cursor.getInt(cursor.getColumnIndex(COLUMN_CAR_YEAR)));
                driver.setSeats(cursor.getInt(cursor.getColumnIndex(COLUMN_SEATS)));
                driver.setPricePerKm(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PER_KM)));
                driver.setActive(true);
                activeDrivers.add(driver);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return activeDrivers;
    }

    // Метод за ажурирање на активен статус на возач
    public boolean updateDriverActiveStatus(int userId, boolean isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_ACTIVE, isActive ? 1 : 0);

        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};

        int result = db.update(TABLE_DRIVERS, values, whereClause, whereArgs);
        db.close();

        return result > 0;
    }

    // Метод за додавање рејтинг
    public boolean addRating(int userId, double rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Прво проверуваме дали корисникот е возач
        String[] columns = {COLUMN_IS_DRIVER};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null);
        
        boolean isDriver = false;
        if (cursor != null && cursor.moveToFirst()) {
            isDriver = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DRIVER)) == 1;
            cursor.close();
        }

        // Земи ги тековните вредности за рејтинг
        columns = new String[]{COLUMN_RATING, COLUMN_NUM_RATINGS};
        cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            double currentRating = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING));
            int numRatings = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_RATINGS));
            cursor.close();
            
            // Пресметај нов рејтинг
            double newRating = ((currentRating * numRatings) + rating) / (numRatings + 1);
            
            ContentValues values = new ContentValues();
            values.put(COLUMN_RATING, newRating);
            values.put(COLUMN_NUM_RATINGS, numRatings + 1);
            
            // Ажурирај users табела
            int userResult = db.update(TABLE_USERS, values, selection, selectionArgs);
            
            // Ако е возач, ажурирај и drivers табела
            int driverResult = 1;
            if (isDriver) {
            String driverSelection = COLUMN_USER_ID + " = ?";
                driverResult = db.update(TABLE_DRIVERS, values, driverSelection, selectionArgs);
            }
            
            db.close();
            return userResult > 0 && driverResult > 0;
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    // Метод за додавање возење
    public long addRide(Ride ride) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PASSENGER_ID, ride.getPassengerId());
        values.put(COLUMN_DRIVER_ID, ride.getDriverId());
        values.put(COLUMN_FROM_LOCATION, ride.getFromLocation());
        values.put(COLUMN_TO_LOCATION, ride.getToLocation());
        values.put(COLUMN_FROM_LAT, ride.getFromLat());
        values.put(COLUMN_FROM_LNG, ride.getFromLng());
        values.put(COLUMN_TO_LAT, ride.getToLat());
        values.put(COLUMN_TO_LNG, ride.getToLng());
        values.put(COLUMN_PRICE, ride.getPrice());
        values.put(COLUMN_STATUS, ride.getStatus());
        values.put(COLUMN_TIMESTAMP, ride.getTimestamp());

        long id = db.insert(TABLE_RIDES, null, values);
        db.close();
        return id;
    }

    // Метод за ажурирање на статус на возење
    public boolean updateRideStatus(int rideId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        int result = db.update(TABLE_RIDES, values, 
            COLUMN_RIDE_ID + " = ?", 
            new String[]{String.valueOf(rideId)});
        db.close();
        return result > 0;
    }

    // Метод за земање на возења за возач
    public List<Ride> getDriverRides(int driverId) {
        List<Ride> rides = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_RIDES + 
            " WHERE " + COLUMN_DRIVER_ID + " = ? ORDER BY " + 
            COLUMN_TIMESTAMP + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, 
            new String[]{String.valueOf(driverId)});

        if (cursor.moveToFirst()) {
            do {
                Ride ride = new Ride();
                ride.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_RIDE_ID)));
                ride.setPassengerId(cursor.getInt(cursor.getColumnIndex(COLUMN_PASSENGER_ID)));
                ride.setDriverId(cursor.getInt(cursor.getColumnIndex(COLUMN_DRIVER_ID)));
                ride.setFromLocation(cursor.getString(cursor.getColumnIndex(COLUMN_FROM_LOCATION)));
                ride.setToLocation(cursor.getString(cursor.getColumnIndex(COLUMN_TO_LOCATION)));
                ride.setFromLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_FROM_LAT)));
                ride.setFromLng(cursor.getDouble(cursor.getColumnIndex(COLUMN_FROM_LNG)));
                ride.setToLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_TO_LAT)));
                ride.setToLng(cursor.getDouble(cursor.getColumnIndex(COLUMN_TO_LNG)));
                ride.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
                ride.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                ride.setTimestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                rides.add(ride);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rides;
    }

    // Метод за земање на возења за патник
    public List<Ride> getPassengerRides(int passengerId) {
        List<Ride> rides = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_RIDES + 
            " WHERE " + COLUMN_PASSENGER_ID + " = ? ORDER BY " + 
            COLUMN_TIMESTAMP + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, 
            new String[]{String.valueOf(passengerId)});

        if (cursor.moveToFirst()) {
            do {
                Ride ride = new Ride();
                ride.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_RIDE_ID)));
                ride.setPassengerId(cursor.getInt(cursor.getColumnIndex(COLUMN_PASSENGER_ID)));
                ride.setDriverId(cursor.getInt(cursor.getColumnIndex(COLUMN_DRIVER_ID)));
                ride.setFromLocation(cursor.getString(cursor.getColumnIndex(COLUMN_FROM_LOCATION)));
                ride.setToLocation(cursor.getString(cursor.getColumnIndex(COLUMN_TO_LOCATION)));
                ride.setFromLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_FROM_LAT)));
                ride.setFromLng(cursor.getDouble(cursor.getColumnIndex(COLUMN_FROM_LNG)));
                ride.setToLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_TO_LAT)));
                ride.setToLng(cursor.getDouble(cursor.getColumnIndex(COLUMN_TO_LNG)));
                ride.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
                ride.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                ride.setTimestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                rides.add(ride);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rides;
    }

    // Метод за додавање барање за возење
    public long addRideRequest(int passengerId, int driverId, String fromLocation, String toLocation,
                         double fromLat, double fromLng, double toLat, double toLng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PASSENGER_ID, passengerId);
        values.put(COLUMN_DRIVER_ID, driverId);
        values.put(COLUMN_FROM_LOCATION, fromLocation);
        values.put(COLUMN_TO_LOCATION, toLocation);
        values.put(COLUMN_FROM_LAT, fromLat);
        values.put(COLUMN_FROM_LNG, fromLng);
        values.put(COLUMN_TO_LAT, toLat);
        values.put(COLUMN_TO_LNG, toLng);
        values.put(COLUMN_REQUEST_STATUS, "PENDING");
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());

        long id = db.insert(TABLE_RIDE_REQUESTS, null, values);
        db.close();
        return id;
    }

    // Метод за земање на барања за возење за возач
    public List<Ride> getDriverRequests(int driverId) {
        List<Ride> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_RIDE_REQUESTS + 
            " WHERE " + COLUMN_DRIVER_ID + " = ? AND " + 
            COLUMN_REQUEST_STATUS + " = 'PENDING' ORDER BY " + 
            COLUMN_TIMESTAMP + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(driverId)});

        if (cursor.moveToFirst()) {
            do {
                Ride request = new Ride();
                request.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_REQUEST_ID)));
                request.setPassengerId(cursor.getInt(cursor.getColumnIndex(COLUMN_PASSENGER_ID)));
                request.setDriverId(cursor.getInt(cursor.getColumnIndex(COLUMN_DRIVER_ID)));
                request.setFromLocation(cursor.getString(cursor.getColumnIndex(COLUMN_FROM_LOCATION)));
                request.setToLocation(cursor.getString(cursor.getColumnIndex(COLUMN_TO_LOCATION)));
                request.setFromLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_FROM_LAT)));
                request.setFromLng(cursor.getDouble(cursor.getColumnIndex(COLUMN_FROM_LNG)));
                request.setToLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_TO_LAT)));
                request.setToLng(cursor.getDouble(cursor.getColumnIndex(COLUMN_TO_LNG)));
                request.setStatus("PENDING");
                request.setTimestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                requests.add(request);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return requests;
    }

    // Метод за ажурирање на статус на барање
    public boolean updateRequestStatus(int requestId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REQUEST_STATUS, status);

        int result = db.update(TABLE_RIDE_REQUESTS, values,
            COLUMN_REQUEST_ID + " = ?",
            new String[]{String.valueOf(requestId)});
        db.close();
        return result > 0;
    }

    // Метод за проверка на статус на барање
    public String getRequestStatus(int requestId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String status = null;

        Cursor cursor = db.query(TABLE_RIDE_REQUESTS,
            new String[]{COLUMN_REQUEST_STATUS},
            COLUMN_REQUEST_ID + " = ?",
            new String[]{String.valueOf(requestId)},
            null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex(COLUMN_REQUEST_STATUS));
            cursor.close();
        }
        db.close();
        return status;
    }

    // Метод за ажурирање на активно време
    public boolean updateDriverActiveTime(int userId, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVE_TIME_START, startTime);
        values.put(COLUMN_ACTIVE_TIME_END, endTime);

        int result = db.update(TABLE_DRIVERS, values,
            COLUMN_USER_ID + " = ?",
            new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    // Метод за рејтинг на патник
    public boolean ratePassenger(int passengerId, int rideId, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Прво проверете дали возењето постои и е завршено
        String[] columns = {COLUMN_STATUS};
        String selection = COLUMN_RIDE_ID + " = ? AND " + COLUMN_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(rideId), "COMPLETED"};
        
        Cursor cursor = db.query(TABLE_RIDES, columns, selection, selectionArgs,
                null, null, null);
                
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return addRating(passengerId, rating);
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    public List<Ride> getPassengerActiveRides(int passengerId) {
        List<Ride> rides = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM rides WHERE passenger_id = ? AND status IN ('PENDING', 'ACCEPTED') ORDER BY timestamp DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(passengerId)});
        
        if (cursor.moveToFirst()) {
            do {
                Ride ride = getRideFromCursor(cursor);
                rides.add(ride);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rides;
    }

    public boolean rateDriver(int driverId, int rideId, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Прво проверете дали возењето постои и е завршено
        String[] columns = {COLUMN_STATUS};
        String selection = COLUMN_RIDE_ID + " = ? AND " + COLUMN_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(rideId), "COMPLETED"};
        
        Cursor cursor = db.query(TABLE_RIDES, columns, selection, selectionArgs,
                null, null, null);
                
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return addRating(driverId, rating);
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    public boolean updateRide(Ride ride) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_PRICE, ride.getPrice());
        values.put(COLUMN_STATUS, ride.getStatus());

        int result = db.update(TABLE_RIDES, values,
            COLUMN_RIDE_ID + " = ?",
            new String[]{String.valueOf(ride.getId())});
        db.close();
        return result > 0;
    }

    // Метод за додавање review
    public boolean addReview(int reviewerId, int reviewedId, float rating, String comment, int rideId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_REVIEW_TEXT, comment);
        values.put(COLUMN_REVIEWER_ID, reviewerId);
        values.put(COLUMN_REVIEWED_ID, reviewedId);
        values.put(COLUMN_REVIEW_RATING, rating);
        values.put(COLUMN_REVIEW_DATE, System.currentTimeMillis());
        values.put(COLUMN_RIDE_ID, rideId);
        
        long reviewId = db.insert(TABLE_REVIEWS, null, values);
        boolean ratingAdded = addRating(reviewedId, rating);
        
        db.close();
        return reviewId != -1 && ratingAdded;
    }

    // Метод за земање на reviews за возач
    public List<Review> getDriverReviews(int driverId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT r.*, u.name, u.surname FROM " + TABLE_REVIEWS + " r "
                + "JOIN " + TABLE_USERS + " u ON r." + COLUMN_REVIEWER_ID + " = u." + COLUMN_ID
                + " WHERE " + COLUMN_REVIEWED_ID + " = ? ORDER BY " + COLUMN_REVIEW_DATE + " DESC";
                
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(driverId)});
        
        if (cursor.moveToFirst()) {
            do {
                Review review = new Review();
                review.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW_ID)));
                review.setReviewText(cursor.getString(cursor.getColumnIndex(COLUMN_REVIEW_TEXT)));
                review.setRating(cursor.getFloat(cursor.getColumnIndex(COLUMN_REVIEW_RATING)));
                review.setReviewDate(cursor.getLong(cursor.getColumnIndex(COLUMN_REVIEW_DATE)));
                review.setReviewerName(cursor.getString(cursor.getColumnIndex("name")) + " " 
                    + cursor.getString(cursor.getColumnIndex("surname")));
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return reviews;
    }

    // Додадете го овој метод
    public boolean addRatingWithReview(int userId, double rating, String comment) {
        if (currentUserId == -1) {
            return false; // Нема најавен корисник
        }

        SQLiteDatabase db = this.getWritableDatabase();
        
        // Додади го рејтингот
        boolean ratingAdded = addRating(userId, rating);
        
        // Додади го коментарот во reviews табелата
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW_TEXT, comment);
        values.put(COLUMN_REVIEWER_ID, currentUserId);
        values.put(COLUMN_REVIEWED_ID, userId);
        values.put(COLUMN_REVIEW_RATING, rating);
        values.put(COLUMN_REVIEW_DATE, System.currentTimeMillis());
        
        long reviewId = db.insert(TABLE_REVIEWS, null, values);
        db.close();
        
        return ratingAdded && reviewId != -1;
    }

    // Додадете ги овие методи
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public Review getRideReview(int rideId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Review review = null;

        String query = "SELECT r.*, u.name, u.surname FROM " + TABLE_REVIEWS + " r "
                + "JOIN " + TABLE_USERS + " u ON r." + COLUMN_REVIEWER_ID + " = u." + COLUMN_ID
                + " WHERE r." + COLUMN_RIDE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(rideId)});

        if (cursor != null && cursor.moveToFirst()) {
            review = new Review();
            review.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW_ID)));
            review.setReviewText(cursor.getString(cursor.getColumnIndex(COLUMN_REVIEW_TEXT)));
            review.setRating(cursor.getFloat(cursor.getColumnIndex(COLUMN_REVIEW_RATING)));
            review.setReviewDate(cursor.getLong(cursor.getColumnIndex(COLUMN_REVIEW_DATE)));
            review.setReviewerName(cursor.getString(cursor.getColumnIndex("name")) + " " 
                + cursor.getString(cursor.getColumnIndex("surname")));
            cursor.close();
        }

        db.close();
        return review;
    }

    // Метод за земање само завршени возења
    public List<Ride> getPassengerCompletedRides(int passengerId) {
        List<Ride> rides = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM rides WHERE passenger_id = ? AND status = 'COMPLETED' ORDER BY timestamp DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(passengerId)});
        
        if (cursor.moveToFirst()) {
            do {
                Ride ride = getRideFromCursor(cursor);
                rides.add(ride);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rides;
    }

    private Ride getRideFromCursor(Cursor cursor) {
        Ride ride = new Ride();
        ride.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_RIDE_ID)));
        ride.setPassengerId(cursor.getInt(cursor.getColumnIndex(COLUMN_PASSENGER_ID)));
        ride.setDriverId(cursor.getInt(cursor.getColumnIndex(COLUMN_DRIVER_ID)));
        ride.setFromLocation(cursor.getString(cursor.getColumnIndex(COLUMN_FROM_LOCATION)));
        ride.setToLocation(cursor.getString(cursor.getColumnIndex(COLUMN_TO_LOCATION)));
        ride.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
        ride.setTimestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
        ride.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
        return ride;
    }
} 