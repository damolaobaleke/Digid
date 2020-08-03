package com.eazifunds.efid
/**Written by Oyindamola Obaleke
 * 1/14/2020
 * All rights reserved Softroniiks inc*/
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates

class AdminLogin : AppCompatActivity() {

    lateinit var db: DatabaseHelper
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var signUp: Button
    lateinit var logIn: Button
    lateinit var cursor: Cursor
    var nameIndex by Delegates.notNull<Int>()
    var passwordIndex by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        title = "Admin"

        db = DatabaseHelper(this)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        val EFID : ImageView = findViewById(R.id.imageView)
        EFID.translationY = -1000f
        EFID.animate().translationYBy(1000f).setDuration(1000).startDelay = 1000; EFID.animate().start()

        signUp = findViewById(R.id.signUp)
        logIn =findViewById(R.id.Login)

        signUp.setOnClickListener { signUpAuth() }
        logIn.setOnClickListener { Login() }


    }

    fun signUp() {
        val check: Boolean = db.auth(username.text.toString(), password.text.toString())
        if(check == true) {
            val callback: Boolean = db.signUp(username.text.toString(), password.text.toString())
            if (callback == true) {
                Toast.makeText(this, "Signed Up", LENGTH_SHORT).show()
            } else {
                Log.i("DB","Didn't Sign Up")
                Toast.makeText(this, "Didn't Sign Up", LENGTH_SHORT).show()
            }
        }else{
            //dont add
        }
    }

    fun signUpAuth() {
        val cursor: Cursor = db.getData()
        if (cursor.getCount() == 0) {
            Log.i("Database", "No data found")

        } else {
            while (cursor.moveToNext()) {
                nameIndex = cursor.getColumnIndex("username")
                passwordIndex = cursor.getColumnIndex("password")

                if (cursor.getString(nameIndex) == username.text.toString() && cursor.getString(passwordIndex) == password.text.toString()) {
                    Toast.makeText(this, "Username & Password already exists", LENGTH_SHORT).show()

                } else if (cursor.getString(nameIndex) == username.text.toString() || password.text.toString() == cursor.getString(passwordIndex) ) {
                    Toast.makeText(this, "Username or Password already exists", LENGTH_SHORT).show()
                    //signUp()

                }else if(password.text.toString() == cursor.getString(passwordIndex)){
                    Toast.makeText(this, "Password already exists", LENGTH_SHORT).show()

                }else if(password.text.toString() == "" || username.text.toString() == ""){
                    Toast.makeText(this, "Fields are empty", LENGTH_SHORT).show()
                }else{
                    signUp()
                    Log.i("Database", "")
                }
            }
            Log.i("Database", "Data available")
            //signUp()
        }

        try {
        HideKeyboard()}catch (e: NullPointerException){
        }
    }

    fun Login() {
        cursor = db.getData()
        if (cursor.getCount() == 0) {
            Log.i("Database", "No data found")

        } else {
            while (cursor.moveToNext()) {
                nameIndex = cursor.getColumnIndex("username")
                passwordIndex = cursor.getColumnIndex("password")
                if (cursor.getString(nameIndex) == allowed(username.text.toString()) && cursor.getString(passwordIndex) == password.text.toString()) {
                    List()

                } else if (cursor.getString(nameIndex) == username.text.toString() && password.text.toString() ==""){
                    Toast.makeText(this, "Enter Password", LENGTH_SHORT).show();

                } else if (cursor.getString(passwordIndex) == password.text.toString() && username.text.toString() ==""){
                    Toast.makeText(this, "Enter Username", LENGTH_SHORT).show();

                }else if(username.text.toString() != cursor.getString(nameIndex) || allowed(username.text.toString()) != cursor.getString(nameIndex) && password.text.toString() != cursor.getString(passwordIndex) || password.text.toString() != cursor.getString(passwordIndex)){
                    //Toast.makeText(this, "Wrong Username or Password", LENGTH_SHORT).show();
                    check()
                }
                else {
                    return
                }
            }
        }
        try{
        HideKeyboard()}catch (error : NullPointerException){
            Toast.makeText(this, "Fill in fields", LENGTH_LONG).show();
        }
    }


    fun HideKeyboard() {
        try {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            Log.e("Keyboard", e.localizedMessage)
        }
    }

    fun allowed(arg1: String): String{
        if (username.text.toString() == "Damola" && password.text.toString() == cursor.getString(passwordIndex)){
            List()

        }else if(username.text.toString() == "Damola" && password.text.toString() == ""){
            Toast.makeText(this, "Enter Password", LENGTH_SHORT).show()

        }else if(username.text.toString() == "Damola" && password.text.toString() != "obaleke") {
            Toast.makeText(this, "Enter Password", LENGTH_SHORT).show()

        }else if(username.text.toString() == cursor.getColumnName(nameIndex) && password.text.toString() !=  "obaleke") {
            Toast.makeText(this,"Not Allowed", LENGTH_SHORT).show()
        }
        else if(username.text.toString() == "Damola" && password.text.toString() ==  "obaleke") {
            Toast.makeText(this, "Admin Allowed", LENGTH_SHORT).show()
        }
        else if(username.text.toString() == "Busayo" &&  password.text.toString() == cursor.getString(passwordIndex)){
            List()

        }else if(username.text.toString() == "Busayo" && password.text.toString() == ""){
            Toast.makeText(this, "Enter Password", LENGTH_SHORT).show()
        }
        else if(username.text.toString() == "Busayo" && password.text.toString() !=  "okusi") {
            Toast.makeText(this, "Enter Password", LENGTH_SHORT).show()

        }else if(username.text.toString() == "Busayo" && password.text.toString() ==  "okusi") {
            Toast.makeText(this, "Admin Allowed", LENGTH_SHORT).show()
        }
        else if(username.text.toString() == cursor.getColumnName(nameIndex) && password.text.toString() !=  "okusi") {
            Toast.makeText(this,"Not Allowed", LENGTH_SHORT).show()
        }
        else if(username.text.toString() == "" && password.text.toString() == "" || username.text.toString() == ""){
            Toast.makeText(this, "Fill in fields", LENGTH_LONG).show()
            check()

        }else if(username.text.toString() != "Damola" || username.text.toString() != "Busayo"){
            Toast.makeText(this, "Not allowed", LENGTH_SHORT).show();
        }
        else{
            Log.i("Database", "Not allowed")
        }
        return arg1;
    }

    fun List(){
        val intent = Intent(this, ExecList::class.java)
        startActivity(intent)
        val text = "Logged In"

        val toast = Toast.makeText(this,text, LENGTH_SHORT)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custom_toast,null)
        val logintext = view.findViewById<TextView>(R.id.textlogin)
        logintext.setText(text)
        toast.view = view
        /*val view = toast.view
        view.setBackgroundColor(ContextCompat.getColor(this,R.color.eazifundsgreen))*/
        toast.show()
    }

    fun check(){
        val run=Runnable() {
            val check: TextView = findViewById(R.id.check)
            check.text = "Have you signed up ?"
        }
        val handler= Handler()
        handler.postAtTime(run, 2000)
        Handler().postDelayed({ val check: TextView = findViewById(R.id.check);check.visibility =GONE },3000)
    }
}