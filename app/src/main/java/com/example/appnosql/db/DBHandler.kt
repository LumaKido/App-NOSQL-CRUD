import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.appnosql.model.CourseModel

class DBHandler // creating a constructor for our database handler.
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    // below method is for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating an sqlite query and we are
        // setting our column names along with their data types.
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DURATION_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + TRACKS_COL + " TEXT)")

        // at last we are calling a exec sql method to execute above sql query
        db.execSQL(query)
    }

    // this method is use to add new course to our sqlite database.
    fun addNewCourse(
        courseName: String?,
        courseDuration: String?,
        courseDescription: String?,
        courseTracks: String?
    ) {
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        val db = this.writableDatabase
        // on below line we are creating a
        // variable for content values.
        val values = ContentValues()
        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, courseName)
        values.put(DURATION_COL, courseDuration)
        values.put(DESCRIPTION_COL, courseDescription)
        values.put(TRACKS_COL, courseTracks)
        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values)
        // at last we are closing our
        // database after adding database.
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "coursedb"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        private const val TABLE_NAME = "mycourses"

        // below variable is for our id column.
        private const val ID_COL = "id"

        // below variable is for our course name column
        private const val NAME_COL = "name"

        // below variable id for our course duration column.
        private const val DURATION_COL = "duration"

        // below variable for our course description column.
        private const val DESCRIPTION_COL = "description"

        // below variable is for our course tracks column.
        private const val TRACKS_COL = "tracks"
    }

    // we have created a new method for reading all the courses.
    fun readCourses(): ArrayList<CourseModel>? {
        // on below line we are creating a database for reading our database.
        val db = this.readableDatabase

        // on below line we are creating a cursor with query to read data from database.
        val cursorCourses: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        // on below line we are creating a new array list.
        val courseModelArrayList: ArrayList<CourseModel> = ArrayList()

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModelArrayList.add(
                    CourseModel(
                        cursorCourses.getString(1),
                        cursorCourses.getString(4),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3)
                    )
                )
            } while (cursorCourses.moveToNext())
            // moving our cursor to next.
        }
        // at last closing our cursor and returning our array list.
        cursorCourses.close()
        return courseModelArrayList
    }
    fun deleteCourse(courseName: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$NAME_COL = ?"
        val whereArgs = arrayOf(courseName)
        val result = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        return result != -1
    }

    fun updateCourse(
        courseNameToUpdate: String,
        newDuration: String,
        newDescription: String,
        newTracks: String
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(DURATION_COL, newDuration)
        values.put(DESCRIPTION_COL, newDescription)
        values.put(TRACKS_COL, newTracks)

        val whereClause = "$NAME_COL = ?"
        val whereArgs = arrayOf(courseNameToUpdate)

        val result = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()

        return result != -1
    }

    fun getCourseDetailsByName(courseName: String): CourseModel? {
        val db = this.readableDatabase
        val selection = "$NAME_COL = ?"
        val selectionArgs = arrayOf(courseName)

        val cursor = db.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val course: CourseModel?
        if (cursor != null && cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(NAME_COL)
            val tracksIndex = cursor.getColumnIndex(TRACKS_COL)
            val durationIndex = cursor.getColumnIndex(DURATION_COL)
            val descriptionIndex = cursor.getColumnIndex(DESCRIPTION_COL)

            if (nameIndex >= 0) {
                course = CourseModel(
                    cursor.getString(nameIndex),
                    if (tracksIndex >= 0) cursor.getString(tracksIndex) else "",
                    if (durationIndex >= 0) cursor.getString(durationIndex) else "",
                    if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else ""
                )
            } else {
                course = null
            }

            cursor.close()
        } else {
            course = null
        }

        db.close()
        return course
    }


    }
